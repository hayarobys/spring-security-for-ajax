/** RESOURCE가 저장될 jqxGrid id */
var resourceGridId = "#data_resource";
/** AUTH가 저장될 jqxGrid id */
var authGridId = "#data_auth";
/** 서버로부터 최초 한 번 받아올 전체 권한 목록. 이 권한 목록과 특정 RESOURCE의 권한 목록을 비교하여 일치하는 경우, jqxGrid의 해당 row를 select처리 하는데 사용 */
var everyAuthList = null;

$(document).ready(function(){
	// 그리드 생성
	initResourceGrid();
	initAuthGrid();
	
	// 변수 everyAuthList 갱신
	getEveryAuthList();
	// RESOURCE 그리드 갱신
	reloadResourceGrid();
});

/**
* 리소스 그리드를 초기화 합니다.
* (리소스 그리드를 생성 & 클릭 이벤트 부여)
*/
function initResourceGrid(){
	$("#data_resource").jqxGrid({
		width: '100%',
		height: '400px',              
		pageable: false,
		autoheight: false,
		sortable: false,
		altrows: true,
		enabletooltips: true,
		editable: false,
		selectionmode: 'singlerow',
		columns: [
			{text: '일련 번호', dataField: 'resourceNo', cellsalign: 'left', align: 'center', editable: false, width: '15%'},
			{text: '패턴', dataField: 'resourcePattern', cellsalign: 'left', align: 'center', editable: false, width: '60%'},
			{text: '정렬 순서', dataField: 'sortOrder', cellsalign: 'left', align: 'center', editable: false, width: '25%'}
		]
	});
	
	$("#data_resource").on("rowclick", function(event){
		var row = event.args.rowindex;
		var rowData = $("#data_resource").jqxGrid("getrowdata", row);
		reloadAuthGridByNo(rowData.resourceNo);
	});
}

/**
* 권한 그리드를 초기화 합니다.
* (권한 그리드 박스 생성)
*/
function initAuthGrid(){
	$("#data_auth").jqxGrid({
		width: '100%',
		height: '400px',              
		pageable: false,
		autoheight: false,
		sortable: false,
		altrows: true,
		enabletooltips: true,
		editable: false,
		selectionmode: 'multiplerows',
		columns: [
			{text: '일련 번호', dataField: 'no', cellsalign: 'left', align: 'center', editable: false, width: '15%'},
			{text: '권한 명', dataField: 'name', cellsalign: 'left', align: 'center', editable: false, width: '30%'},
			{text: '권한 설명', dataField: 'explanation', cellsalign: 'left', align: 'center', editable: false, width: '55%'}
		]
	});
}

/**
* 전체 Auth 목록을 조회 합니다.
*/
function getEveryAuthList(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: "/security/auth",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == "success"){
				everyAuthList = data.list;
				console.log("모든 권한 목록", everyAuthList);
			}else{
				console.log("AUTH 목록 조회를 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
* 서버로부터 resource목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadResourceGrid(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: "/security/resource",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			//console.log('data', data);	// response body
			//console.log('statusText', statusText);	// "success" or ?
			//console.log('xhr', xhr);	// header
			if(data.result == "success"){
				changeResourceGrid(data.list)
			}else{
				console.log("RESOURCE 목록 조회를 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
* 서버로부터 특정 URL패턴의 auth목록을 불러와 jqxGrid를 갱신 합니다.
*/
function reloadAuthGridByNo(resourceNo){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: "/security/resource/" + resourceNo + "/auth",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == "success"){
				changeAuthGrid(data.list);
			}else{
				console.log("AUTH 목록 조회를 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
* json배열 형식의 리소스 목록을 gridId에 추가 합니다.
*/
function changeResourceGrid(listData){
	$(resourceGridId).jqxGrid("clearselection"); // RESOURCE 그리드의 선택 효과 제거
	$(authGridId).jqxGrid("clearselection"); // AUTH 그리드의 선택 효과 제거
	$(resourceGridId).jqxGrid("clear"); // RESOURCE 그리드의 데이터 제거
	$(authGridId).jqxGrid("clear"); // AUTH 그리드의 데이터 제거
	
	var source = {
		localdata: listData,
		datatype: "array",
		datafields: [
			{name: 'resourceNo', type: 'int'},
			{name: 'sortOrder', type: 'int'},
			{name: 'resourceType', type: 'String'},
			{name: 'resourcePattern', type: 'String'},
			{name: 'resourceNm', type: 'String'}
		]
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source, {
		downloadComplete: function (data, status, xhr) { },
		loadComplete: function (data) { },
		loadError: function (xhr, status, error) { }
	});
	
	// RESOURCE 그리드에 새로운 데이터 삽입
	$(resourceGridId).jqxGrid({
		source: dataAdapter
	});
}

/**
* json배열 형식의 권한목록을 gridId에 반영 합니다. ( 인자로 들어온 데이터와 일치하는 row들을 select상태로 변경 )
*/
function changeAuthGrid(listData){
	$(authGridId).jqxGrid("clearselection"); // 선택 효과 제거
	$(authGridId).jqxGrid("clear"); // row제거
	
	var source = {
		//localdata: listData,
		localdata: everyAuthList,
		datatype: "array",
		datafields: [
			{name: 'no', type: 'int'},
			{name: 'name', type: 'String'},
			{name: 'explanation', type: 'String'}
		]
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source, {
		downloadComplete: function (data, status, xhr) { },
		loadComplete: function (data) { },
		loadError: function (xhr, status, error) { }
	});
	
	$(authGridId).jqxGrid({
		source: dataAdapter
	});
	
	selectRowByValueList(authGridId, listData);
}

/**
* 해당 jqxGrid의 no 열 에서 value값이 일치하는 row를 select상태로 만듭니다.
* @param jqxGridSelector jqxGrid 선택자
* @param searchValue 선택할 값 들의 목록
*/
function selectRowByValueList(jqxGridSelector, searchValueList){
	var rows = $(jqxGridSelector).jqxGrid('getrows');
	var rowsCount = rows.length;
	var searchValueCount = searchValueList.length;
	
	for(var i = 0; i < rowsCount; i++){
		var value = $(jqxGridSelector).jqxGrid('getcellvalue', i, 'no');
		
		for(var k = 0; k < searchValueCount; k++){
			if(value == searchValueList[k].no){
				$(jqxGridSelector).jqxGrid('selectrow', i);
			}
		}
	}
}

/**
 * 현재 선택한 대로 리소스에 연결된 권한을 저장/변경 합니다.
 * @returns
 */
function save(){
	// 현재 선택한 리소스와 권한의 일련 번호 구하기
	var selectedResourceNoArray = getSelectedNoArray(resourceGridId, 'resourceNo');
	var selectedAuthNoArray = getSelectedNoArray(authGridId, 'no');
	
	// 전송할 json 데이터 생성
	var data = {};
	data.resourceNo = selectedResourceNoArray[0];
	data.authNo = selectedAuthNoArray;
	data = JSON.stringify(data);
	
	// 출력
	console.log('현재 선택한 RESOURCE 일련 번호',selectedResourceNoArray);
	console.log('현재 선택한 AUTH 일련 번호',selectedAuthNoArray);
	console.log('전송할 json 데이터', data);
	console.log('JSON.parse(data).resourceNo 출력', JSON.parse(data).resourceNo);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "PATCH",
		url: "/security/resource/" + selectedResourceNoArray[0] + "/auth",
		data: data,
		contentType: 'application/json',
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == "success"){
				changeAuthGrid(data.list);
			}else{
				console.log("해당 RESOURCE의 AUTH 변경을 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
 * 특정 jqxGrid로부터 현재 선택된 행의 특정 column value 목록을 반환합니다.
 * @param jqxGridId 검색할 jqxGrid 셀렉터
 * @param returnColumnStr 검색할 column 명
 * @returns 현재 선택 상태인 row의 column value 목록
 */
function getSelectedNoArray(jqxGridId, returnColumnStr){
	var selectedRowIndexes = $(jqxGridId).jqxGrid('getselectedrowindexes');
	var selectedRowData = [];
	
	for(var i=0; i<selectedRowIndexes.length; i++){
		selectedRowData[i] = $(jqxGridId).jqxGrid('getcellvalue', selectedRowIndexes[i], returnColumnStr);
	}
	
	return selectedRowData;
}