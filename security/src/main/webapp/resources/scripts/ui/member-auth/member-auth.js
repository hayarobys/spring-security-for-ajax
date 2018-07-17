/** MEMBER가 저장될 jqxGrid id */
var memberGridId = "#data_member";
/** AUTH가 저장될 jqxGrid id */
var authGridId = "#data_auth";
/** 서버로부터 최초 한 번 받아올 전체 권한 목록. 이 권한 목록과 특정 MEMBER의 권한 목록을 비교하여 일치하는 경우, jqxGrid의 해당 row를 select처리 하는데 사용 */
var everyAuthList = null;

$(document).ready(function(){
	// 그리드 생성
	initMemberGrid();
	initAuthGrid();
	
	// 변수 everyAuthList 갱신
	getEveryAuthList();
	// MEMBER 그리드 갱신
	//reloadMemberGrid();
});

/**
* 리소스 그리드를 초기화 합니다.
* (리소스 그리드를 생성 & 클릭 이벤트 부여)
*/
function initMemberGrid(){
	
	var source = {
		datatype: "json",
		datafields: [
			{name: 'memNo', type: 'int'},
			{name: 'memId', type: 'string'},
			{name: 'memNicknm', type: 'string'}
			
		],
		url: CONTEXT_PATH + '/member',
		root: 'rows',
		cache: false,
		beforeprocessing: function(data){
			console.log("데이타:",data);
			source.totalrecords = data.list.totalRows;
		}
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source);
		
	$("#data_member").jqxGrid({
		width: '100%',
		height: '400px',
		source: dataAdapter,
		pageable: true,
		virtualmode: true,
		rendergridrows: function(obj)
		{
			  return obj.data;     
		},
		autoheight: false,
		sortable: false,
		altrows: true,
		enabletooltips: true,
		editable: false,
		selectionmode: 'singlerow',
		handlekeyboardnavigation: function(event){
			var key = event.charCode ? event.charCode : event.keyCode ? event.keyCode : 0;
			if(key == 13){	// key 13 = enter
				var row = $(memberGridId).jqxGrid('getselectedrowindex');
				var rowData = $(memberGridId).jqxGrid("getrowdata", row);
				reloadAuthGridByNo(rowData.memNo);
				return true;
			}
		},
		columns: [
			{text: '일련 번호', dataField: 'memNo', cellsalign: 'center', align: 'center', width: '20%'},
			{text: '아이디', dataField: 'memId', cellsalign: 'center', align: 'center', width: '40%'},
			{text: '이름', dataField: 'memNicknm', cellsalign: 'center', align: 'center', width: '40%'}
		]
	});
	
	$(memberGridId).on("rowclick", function(event){
		var row = event.args.rowindex;
		var rowData = $(memberGridId).jqxGrid("getrowdata", row);
		reloadAuthGridByNo(rowData.memNo);
	});
}

/**
* 권한 그리드를 초기화 합니다.
* (권한 그리드 박스 생성)
*/
function initAuthGrid(){
	$(authGridId).jqxGrid({
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
			{text: '일련 번호', dataField: 'authNo', cellsalign: 'center', align: 'center', editable: false, width: '15%'},
			{text: '권한 명', dataField: 'authNm', cellsalign: 'left', align: 'center', editable: false, width: '30%'},
			{text: '권한 설명', dataField: 'authExplanation', cellsalign: 'left', align: 'center', editable: false, width: '55%'}
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
		url: CONTEXT_PATH + "/auth?pagesize=99999",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == "success"){
				everyAuthList = data.list.rows;
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
* 서버로부터 member목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadMemberGrid(){
	$(memberGridId).jqxGrid("clearselection"); // MEMBER 그리드의 선택 효과 제거
	$(authGridId).jqxGrid("clearselection"); // AUTH 그리드의 선택 효과 제거
	$(authGridId).jqxGrid("clear"); // AUTH 그리드의 데이터 제거
	$(memberGridId).jqxGrid("updatebounddata");
}

/**
* 서버로부터 특정 URL패턴의 auth목록을 불러와 jqxGrid를 갱신 합니다.
*/
function reloadAuthGridByNo(memNo){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: CONTEXT_PATH + "/member/" + memNo + "/auth",
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
			{name: 'authNo', type: 'int'},
			{name: 'authNm', type: 'string'},
			{name: 'authExplanation', type: 'string'}
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
		var value = $(jqxGridSelector).jqxGrid('getcellvalue', i, 'authNo');
		
		for(var k = 0; k < searchValueCount; k++){
			if(value == searchValueList[k].authNo){
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
	var selectedMemberNoArray = getSelectedNoArray(memberGridId, 'memNo');
	var selectedAuthNoArray = getSelectedNoArray(authGridId, 'authNo');
	
	// 전송할 json 데이터 생성
	var data = {};
	//data.memberNo = Number(selectedMemberNoArray[0]);	// String to Number
	data.authNoList = selectedAuthNoArray;
	data = JSON.stringify(data);
	
	// 출력
	console.log('전송할 json 데이터', data);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "PATCH",
		url: CONTEXT_PATH + "/member/" + Number(selectedMemberNoArray[0]) + "/auth",
		data: data,
		contentType: 'application/json',
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			console.log("result", data.result);
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
 * 특정 jqxGrid로부터 현재 선택된 행의 특정 column value 목록을 반환합니다.
 * 이때 검색할 column의 value는 반드시 Number 타입이어야 합니다.
 * @param jqxGridId 검색할 jqxGrid 셀렉터
 * @param returnColumnStr 검색할 column 명
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