/** AUTH가 저장될 jqxGrid id */
var authGridId = "#data_auth";
/** 등록할 AUTH 정보가 있는 form id */
var authFormId = "#authForm";

$(document).ready(function(){	
	// 그리드 생성
	initAuthGrid();
});

/**
* 권한(AUTH) 그리드를 초기화 합니다.
* (권한 그리드를 생성)
*/
function initAuthGrid(){
	
	function cellValueChanging(row, column, columntype, oldvalue, newvalue){
		// new value가 공백이라면 old value로 재설정
		if(newvalue == ""){
			console.log('공백이 입력되었으므로, 기존값으로 복원합니다.');
			return oldvalue;
		}
	}
	
	var source = {
		datatype: "json",
		datafields: [
			{name: 'authNo', type: 'int'},
			{name: 'authNm', type: 'string'},
			{name: 'authExplanation', type: 'string'}
		],
		url: CONTEXT_PATH + '/auth',
		root: 'rows',
		cache: false,
		beforeprocessing: function(data){
			console.log("데이타:",data);
			source.totalrecords = data.list.totalRows;
		}
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source);
	
	$(authGridId).jqxGrid({
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
		selectionmode: 'singlerow',
		pagerButtonsCount: 8,
		editable: true,
		editmode: 'dblclick',
		columns: [
			{
				text: '일련 번호',
				dataField: 'authNo',
				cellsalign: 'center',
				align: 'center',
				editable: false,
				width: '10%'
			},
			{text: '권한 명', dataField: 'authNm', cellsalign: 'center', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '45%'},
			{text: '권한 설명', dataField: 'authExplanation', cellsalign: 'left', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '45%'}
		]
	});
	
	// Cell Begin Edit
	$(authGridId).on('cellbeginedit', function(event){
		
		$(authGridId).jqxGrid("clearselection"); // AUTH 그리드의 선택 효과 제거
		$(authGridId).jqxGrid('selectrow', event.args.rowindex);	// 편집에 들어간 행에 선택 효과 부여
		
	});
	
	// Cell End Edit
	$(authGridId).on('cellendedit', function(event){
		
		/** 편집한 행 번호 */
		var rowIndex = event.args.rowindex;
		/** 편집한 권한 일련 번호 */
		var authNo = event.args.row.authNo;
		/** 편집한 컬럼명 */
		var dataField = event.args.datafield;
		
		/** 기존 값 */
		var oldValue = event.args.oldvalue;
		if(		(typeof oldValue) != "number"
			&&	(typeof oldValue) == "string"
		){
			oldValue = oldValue.trim();
		}
		
		/** 변경 값 */
		var newValue = event.args.value;
		if(		(typeof newValue) != "number"
			&&	(typeof newValue) == "string"
		){
			newValue = newValue.trim();
		}
		
		// 변경되지 않았다면 이벤트 종료
		if(oldValue == newValue){
			return;
		}
		
		// 전송할 json 데이터 생성
		var data = {};
		eval("data." + dataField + " = '" + newValue + "'");
		var jsonData = JSON.stringify(data);
		
		// 출력
		console.log("전송할 json 데이터", authNo, jsonData);
		
		// 수정 요청 전송
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			type: "PATCH",
			url: CONTEXT_PATH + "/auth/" + Number(authNo),
			data: jsonData,
			contentType: 'application/json',
			dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
			beforeSend: function(xhr){
				xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
				xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
			},
			success: function(data, statusText, xhr){
				if(data.result == 'success'){
					console.log("data", data);
				}else{
					console.log("AUTH 수정에 실패했습니다.");
					console.log(data.result);
					console.log("message", data.message);
					
					// 수정 전 값으로 복원
					$(authGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
				}
			},
			error: function(xhr){
				console.log("error", xhr);
				// 수정 전 값으로 복원
				$(authGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
			}
		});
	});
}

/**
* 서버로부터 auth목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadAuthGrid(){
	$(authGridId).jqxGrid("clearselection"); // AUTH 그리드의 선택 효과 제거
	$(authGridId).jqxGrid("updatebounddata");
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

/**
 * 폼에 입력된 AUTH를 등록합니다.
 * @returns
 */
function insertAuth(){
	// 전송할 json 데이터 생성
	var serializeArrayForm = $(authFormId).serializeArray();
	var objectForm = objectifyForm(serializeArrayForm);
	var jsonForm = JSON.stringify(objectForm);
	
	// 출력
	console.log('전송할 json 데이터', jsonForm);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "POST",
		url: CONTEXT_PATH + "/auth",
		data: jsonForm,
		contentType: 'application/json',
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadAuthGrid();
				$(authFormId)[0].reset();
			}else{
				console.log("AUTH 등록에 실패했습니다.");
				console.log("message", data.message);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
 * $(form selector).serializeArray()로 전달된 객체를 오브젝트로 변환합니다.
 * 이렇게 변환한 객체를 JSON.stringify()에 사용할 수 있습니다.
 */
function objectifyForm(formArray){
	var returnArray = {};
	for(var i=0; i<formArray.length; i++){
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

/**
 * AUTH jqxGrid에서 선택된 항목을 제거합니다.
 * @returns
 */
function deleteSelectedAuth(){
	// 현재 선택한 권한의 일련 번호 구하기
	var selectedAuthNoArray = String(getSelectedNoArray(authGridId, 'authNo'));
	
	// 선택한 행이 없으면 이벤트 취소
	if(selectedAuthNoArray.length <= 0){
		console.log('제거할 행을 선택해 주세요.');
		return;
	}
	
	// 출력
	console.log('전송할 데이터', selectedAuthNoArray);
	
	// 제거 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "DELETE",
		url: CONTEXT_PATH + "/auth/" + selectedAuthNoArray,
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadAuthGrid();
			}else{
				console.log("다음의 AUTH 제거에 실패했습니다.");
				console.log(data.result);
				console.log("message", data.message);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}














