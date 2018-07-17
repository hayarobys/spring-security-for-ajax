/** RESOURCE가 저장될 jqxGrid id */
var resourceGridId = "#data_resource";
/** 등록할 RESOURCE 정보가 있는 form id */
var resourceFormId = "#resourceForm";
/** 등록폼의 HTTP METHOD select 태그 ID */
var httpMethodSelectTagId = "#httpMethod";
/** 서버로부터 최초 한 번 받아올 전체 HTTP METHOD 목록. 등록폼의 select 태그를 생성하는데 사용 */
var httpMethodList = null;

$(document).ready(function(){	
	// HTTP METHOD 등록 select 태그 초기화
	initHttpMethodSelectBox();
});

/**
 * 서버로부터 HTTP METHOD 목록을 조회해 등록폼의 select 태그를 생성합니다.
 * @returns
 */
function initHttpMethodSelectBox(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: CONTEXT_PATH + "/http-method",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			console.log('data', data);	// response body
			//console.log('statusText', statusText);	// "success" or ?
			//console.log('xhr', xhr);	// header
			if(data.result == "success"){
				httpMethodList = data.list;
				replaceHttpMethodSelectBox(httpMethodList)
				
				// 그리드 생성
				initResourceGrid();
				
				// RESOURCE 그리드 갱신
				//reloadResourceGrid();
			}else{
				console.log("HTTP METHOD 목록 조회를 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
 * 등록 폼의 HTTP METHOD SELECT 태그를 주어진 데이터로 변경합니다.
 * @param httpMethodList
 * @returns
 */
function replaceHttpMethodSelectBox(httpMethodList){
	$(httpMethodSelectTagId).html('');
	
	$.each(httpMethodList, function(index, value){
		$(httpMethodSelectTagId).append($('<option />',{
			label: value,	// 사용자에게 보여줄 값
			value: value	// 서버 전송에 사용할 값
		}));
	})
}

/**
* 리소스 그리드를 초기화 합니다.
* (리소스 그리드를 생성)
*/
function initResourceGrid(){
	//console.log('httpMethodList',httpMethodList);
	var httpMethodSource = {
			localdata: httpMethodList,
			datatype: "array"
	};
	
	var httpMethodAdapter = new $.jqx.dataAdapter(httpMethodSource);
	
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
			{name: 'resourceNo', type: 'int'},
			{name: 'httpMethod', type: 'string'},
			{name: 'resourceNm', type: 'string'},
			{name: 'resourcePattern', type: 'string'},
			{name: 'resourceType', type: 'string'},
			{name: 'sortOrder', type: 'int'}
		],
		url: CONTEXT_PATH + '/resource',
		root: 'rows',
		cache: false,
		beforeprocessing: function(data){
			//console.log("데이타:",data);
			source.totalrecords = data.list.totalRows;
		}
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source);
	
	$(resourceGridId).jqxGrid({
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
		selectionmode: 'multiplerows',
		pagerButtonsCount: 8,
		editable: true,
		editmode: 'dblclick',
		columns: [
			{
				text: '일련 번호',
				dataField: 'resourceNo',
				cellsalign: 'center',
				align: 'center',
				editable: false,
				width: '10%'
			},
			{text: '이름', dataField: 'resourceNm', cellsalign: 'center', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '33%'},
			{
				text: 'HTTP 메소드',	// 보여질 이름
				dataField: 'httpMethod',	// 소스로부터 사용할 데이터의 키명
				displayField: 'httpMethod',	// 해당 컬럼의 내부 명칭?
				columntype: 'dropdownlist',
				cellsalign: 'center',
				align: 'center',
				editable: true,
				cellvaluechanging: cellValueChanging,
				width: '10%',
				
				createeditor: function(row, cellvalue, editor, cellText, width, height){
					// 에디트 모드가 활성화 되면 해당 칸에 커스텀 에디터를 생성합니다.
					editor.jqxDropDownList({
						autoDropDownHeight: true,
						width: width,
						height: height,
						source: httpMethodAdapter,
						displayMember: "httpMethod",	// 사용자에게 보여줄 값
						valueMember: "httpMethod"	// 서버 전송에 사용할 값
					});
				}
			},
			{text: '패턴', dataField: 'resourcePattern', cellsalign: 'left', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '27%'},
			{text: '타입', dataField: 'resourceType', cellsalign: 'center', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '10%'},
			{text: '순서', dataField: 'sortOrder', cellsalign: 'center', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '10%'}
		]
	});
	
	// Cell Begin Edit
	$(resourceGridId).on('cellbeginedit', function(event){
		$(resourceGridId).jqxGrid("clearselection"); // RESOURCE 그리드의 선택 효과 제거
		$(resourceGridId).jqxGrid('selectrow', event.args.rowindex);	// 편집에 들어간 행에 선택 효과 부여
		
	});
	
	// Cell End Edit
	$(resourceGridId).on('cellendedit', function(event){
		
		/** 편집한 행 번호 */
		var rowIndex = event.args.rowindex;
		/** 편집한 리소스 일련 번호 */
		var resourceNo = event.args.row.resourceNo;
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
		console.log("전송할 json 데이터", resourceNo, jsonData);
		
		// 수정 요청 전송
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			type: "PATCH",
			url: CONTEXT_PATH + "/resource/" + Number(resourceNo),
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
					//reloadResourceGrid();
				}else{
					console.log("RESOURCE 수정에 실패했습니다.");
					console.log(data.result);
					
					// 수정 전 값으로 복원
					$(resourceGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
				}
			},
			error: function(xhr){
				console.log("error", xhr);
				
				// 수정 전 값으로 복원
				$(resourceGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
			}
		});
	});
}

/**
* 서버로부터 resource목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadResourceGrid(){
	$(resourceGridId).jqxGrid("clearselection"); // MEMBER 그리드의 선택 효과 제거
	$(resourceGridId).jqxGrid("updatebounddata");
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
 * 폼에 입력된 RESOURCE를 등록합니다.
 * @returns
 */
function insertResource(){
	// 전송할 json 데이터 생성
	var serializeArrayForm = $(resourceFormId).serializeArray();
	var objectForm = objectifyForm(serializeArrayForm);
	var jsonForm = JSON.stringify(objectForm);
	
	// 출력
	console.log('전송할 json 데이터', jsonForm);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "POST",
		url: CONTEXT_PATH + "/resource/",
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
				reloadResourceGrid();
				$(resourceFormId)[0].reset();
			}else{
				console.log("RESOURCE 등록에 실패했습니다.");
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
 * RESOURCE jqxGrid에서 선택된 항목을 제거합니다.
 * @returns
 */
function deleteSelectedResources(){
	// 현재 선택한 리소스와 권한의 일련 번호 구하기
	var selectedResourceNoArray = String(getSelectedNoArray(resourceGridId, 'resourceNo'));
	
	// 선택한 행이 없으면 이벤트 취소
	if(selectedResourceNoArray.length <= 0){
		console.log('제거할 행을 선택해 주세요.');
		return;
	}
	
	// 출력
	console.log('전송할 데이터', selectedResourceNoArray);
	
	// 제거 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "DELETE",
		url: CONTEXT_PATH + "/resource/" + selectedResourceNoArray,
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadResourceGrid();
			}else{
				console.log("다음의 RESOURCE 제거에 실패했습니다.");
				console.log(data.result);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}














