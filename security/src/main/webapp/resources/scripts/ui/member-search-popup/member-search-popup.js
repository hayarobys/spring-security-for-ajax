/** 계정 검색 팝업창의 최상위 아이디 */
let layerMemSearchId = "#layer-mem-search";
/** MEMBER가 저장될 jqxGrid id */
let memberGridId = "#data_member";
/** 선택한 MEMBER 정보가 저장될 input 태그의 아이디 */
let chooseMemberInfo = {};

// 특정 변수에 대한 감시 수행
let proxied = new Proxy(chooseMemberInfo, {
	get: getCallback,
	set: setCallback
});


$(document).ready(function(){
	// 그리드 생성
	initMemberGrid();
	
	// MEMBER 그리드 갱신
	reloadMemberGrid();
	
	// 계정 검색 용 레이어 팝업에 마우스 드래그 기능 부여
	$("#layer-mem-search").draggable();
	
	// 검색 버튼에 이벤트 바인딩
	$('#memberSearchButton').on('click', function(){
		
		console.log("검색 클릭");
	});
});

/*
 * 모든 DOM 로딩 완료 후, 검색 버튼에 이벤트 바인딩
 */
/*window.onload = function(){
	var searchDom = document.getElementById("memberSearchButton")
	searchDom.onclick = selectMember;
}
*/
/**
 * 계정 검색 팝업 창을 띄웁니다.
 * @returns
 */
function popupSearchMemNo(){
	let $layer = $(layerMemSearchId);
	$(layerMemSearchId).fadeIn();
	
	/* 가운데 정렬 */
	let layerWidth = Math.floor($layer.outerWidth());	// 팝업창의 border, padding을 포함한 너비값 산출
	let layerHeight = Math.floor($layer.outerHeight());	// 팝업창의 border, padding을 포함한 높이값 산출
	let docWidth = $(document).width();		// 브라우저의 padding 안쪽 너비(padding 값 제외)
	let docHeight = $(document).height();	// 브라우저의 padding 안쪽 높이(padding 값 제외)

	// 화면의 중앙에 레이어를 띄운다.
	if (layerHeight < docHeight || layerWidth < docWidth) {
		$layer.css({
			marginTop: -layerHeight /2,
			marginLeft: -layerWidth/2
		})
	} else {
		$layer.css({top: 0, left: 0});
	}
	
	$(layerMemSearchId).find(".btn-layer-close").click(function(){
		popupClose();
		return false;	// stop event bubbling
	});
}

/**
 * 계정 검색 팝업창을 닫습니다.
 * @returns
 */
function popupClose(){
	$(layerMemSearchId).fadeOut();
}

/**
* 계정(MEMBER) 그리드를 초기화 합니다.
* (계정 그리드를 생성)
*/
function initMemberGrid(){
	
	$(memberGridId).jqxGrid({
		width: '100%',
		height: '380px',              
		pageable: true,
		autoheight: false,
		sortable: true,
		altrows: true,
		enabletooltips: true,
		selectionmode: 'singlerow',
		pagerButtonsCount: 8,
		editable: false,
		columns: [
			{text: '일련 번호', dataField: 'memNo', cellsalign: 'center', align: 'center', width: '24%'},
			{text: '아이디', dataField: 'memId', cellsalign: 'center', align: 'center', width: '38%'},
			{text: '닉네임', dataField: 'memNicknm', cellsalign: 'center', align: 'center', width: '38%'}
		],
	});
	
	// 행 더블 클릭 시 동작 정의
	$(memberGridId).on('rowdoubleclick', function(event){
		selectMember();
	});
}

/**
* 서버로부터 member목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadMemberGrid(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	var formData = {};
	formData.memId = $("#memId").val();
	formData.memNicknm = $("#memNicknm").val();
	
	$.ajax({
		type: "GET",
		url: "/security/member",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		data: JSON.stringify(formData),
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == "success"){console.log("멤버: ", data.list);
				changeMemberGrid(data.list)
			}else{
				console.log("MEMBER 목록 조회를 실패했습니다.");
				console.log("message", data.message);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
* json배열 형식의 권한 목록을 gridId에 추가 합니다.
*/
function changeMemberGrid(listData){
	$(memberGridId).jqxGrid("clearselection"); // MEMBER 그리드의 선택 효과 제거
	$(memberGridId).jqxGrid("clear"); // MEMBER 그리드의 데이터 제거
	
	var source = {
		localdata: listData,
		datatype: "array",
		datafields: [
			{name: 'memNo', type: 'int'},
			{name: 'memId', type: 'string'},
			{name: 'memNicknm', type: 'string'},
			{name: 'lastLoginDate', type: 'date'}
		]
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source, {
		downloadComplete: function (data, status, xhr) { },
		loadComplete: function (data) { },
		loadError: function (xhr, status, error) { }
	});
	
	// MEMBER 그리드에 새로운 데이터 삽입
	$(memberGridId).jqxGrid({
		source: dataAdapter
	});
}

/**
 * 특정 jqxGrid로부터 현재 선택된 행의 특정 column value 목록을 반환합니다.
 * 이때 검색할 column의 value는 반드시 Number 타입이어야 합니다.
 * @param jqxGridId 검색할 jqxGrid 셀렉터
 * @param returnColumnStr 검색할 column 명(dataField명?)
 * @returns 현재 선택 상태인 row의 column value 목록
 */
function getSelectedNoArray(jqxGridId, returnColumnStr){
	var selectedRowIndexes = $(jqxGridId).jqxGrid('getselectedrowindexes');
	var selectedRowData = [];
	
	for(var i=0; i<selectedRowIndexes.length; i++){
		selectedRowData[i] = Number($(jqxGridId).jqxGrid('getcellvalue', selectedRowIndexes[i], returnColumnStr));
	}
	
	return selectedRowData;
}

/**
 * 계정을 더블 클릭했을때의 동작을 정의합니다.
 * 1. 더블 클릭한 계정의 일련 번호가 특정 변수(var chooseMemberInfo = {})에 저장됩니다.
 * 2. 계정 검색 팝업이 닫힙니다.
 * @returns
 */
function selectMember(){
	var chooseValue = getSelectedNoArray(memberGridId, 'memNo');
	console.log('chooseValue: ', chooseValue);
	proxied.info = chooseValue;
	popupClose();
}

// IIFE(즉시 실행 함수)
//(function(x){
	/*
	 * 주의: jQuery를 사용하지 마십시오.
	 * 이 소스파일은 import용도로 사용되며, jQuery 사용시 대상 파일의 jQuery와 충돌 할 수 있습니다.
	 */
	
	//var httpRequest;
	/*
	window.onload = function(){
		var searchDom = document.getElementById("memberSearchButton")
		//searchDom.onclick = function(){ makeRequest('https://localhost:8443/security/test/echo/good'); }
		searchDom.onclick = selectMember;
	}
	*/
	/*
	 * 특정 url로 요청하고 응답값을 반환합니다.
	 */
	/*function makeRequest(url){
		if(window.XMLHttpRequest){	// 모질라, 사파리
			httpRequest = new XMLHttpRequest();
			
		}else if(window.ActiveXObject){	// IE 8 이상
			try{ httpRequest = new ActiveXObject("Msxml2.XMLHTTP"); }catch(e){
				try{ httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); }catch(e){}
			}
		}
		
		if(!httpRequest){
			alert('Give up :( Cannot create an XMLHTTP instance');
			return false;
		}
		httpRequest.onreadystatechange = alertContents;
		httpRequest.open('GET', url);
		httpRequest.send(null);
	}*/
	
	/**
	 * 응답을 받았고, 응답코드가200 이라면(서버에서 문제없다고 답해주었다면) 해당 메시지를 띄웁니다.
	 * @returns
	 */
	/*function alertContents(){
		if(httpRequest.readyState === 4){
			if(httpRequest.status === 200){
				alert(httpRequest.responseText);
			}else{
				alert('There was a problem with the request.');
			}
		}
	}*/
	
//})('입력');

/*
// 응답 받았을시 호출할 콜백 함수 등록
httpRequest.onreadystatechange = function(){
	
	 //readyState가 가질 수 있는 모든 값의 목록은 아래와 같습니다.
	 //* 0 (uninitialized)
	 //* 1 (loading)
	 //* 2 (loaded)
	 //* 3 (interactive)
	 //* 4 (complete)
	 
	if(httpRequest.readyState == 4){
		// 이상 없음, 응답 받았음
		
		if(httpRequest.status == 200){
			// 이상 없음!
			
			// 서버의 응답을 텍스트 문자열로 반환합니다.
			var responseText = httpRequest.responseText;
			
			// 서버의 응답을 XMLDocument 객체로 반환하며, 자바스크립트의 DOM 함수들을 통해 이 객체를 다룰 수 있습니다.
			var xmlDocument = httpRequest.responseXML;
			
		}else{
			// 요구를 처리하는 과정에서 문제가 발생되었음(항상 그런건 아닐텐데?)
			// 예를 들어 응답 상태 코드는 404(Not Found) 이거나
			// 혹은 500(Internal Server Error)이 될 수 있음
		}
		
	}else{
		// 아직 준비되지 않음
	}
	
}
*/

/* open() 메소드
 * 
 * 첫 파라미터 HTTP Method는 HTTP표준에 따라 모두 대문자로 표기합니다.
 * 그렇지 않으면 파이어폭스와 같은 특정 브라우저는 이 요구를 처리하지 않을 수도 있습니다.
 * 
 * 두번째 파라미터 URL은 보안을 위해 타도메인을 호출할 수 없습니다. 도메인명을 잘못 입력했을 시,
 * 'permission denied'에러가 발생할 수 있습니다.
 * 
 * 세번째 파라미터 isAsync는 이 요구가 비동기로 수행될 지를 결정합니다.
 * TRUE로 설정한 경우 자바스크립트는 응답을 받기 전에도 계속 진행됩니다.
 */
//httpRequest.open('GET', 'https://localhost:8443/security', true);

/* send() 메소드
 * 
 * POST방식으로 요구한 경우 서버로 보내고 싶은 어떠한 데이터라도 파라미터로 넣을 수 있습니다.
 * 데이터는 서버에서 쉽게 parse 할 수 있는 형식(format)이어야 합니다.
 * 
 * 예를 들자면 아래와 같습니다.
 * "name=value&anothername=" + encodeURIComponent(myVar) + "&so=on"
 * 
 * 또는 JSON, SOAP등과 같은 다른 형식으로도 가능합니다.
 * 
 * 만약, POST type을 보내려 한다면, 요청(request)에 MIME type을 설정 해야 합니다.
 * 예를 들자면 send()를 호출하기 전에 아래와 같은 형태로 send()로 보낼 쿼리를 이용해야 합니다.
 * http_request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
 */
//httpRequest.send(null);












