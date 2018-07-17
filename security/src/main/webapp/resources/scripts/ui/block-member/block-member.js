/** BLOCK_MEMBER가 저장될 jqxGrid id */
var blockMemberGridId = "#data_block_member";
/** 등록할 BLOCK_MEMBER 정보가 있는 form id */
var blockMemberFormId = "#blockMemberForm";
/** 차단 계정 닉네임(아이디/일련번호) 입력 폼의 id */
var memInfoInputFormId = "#memInfo";
/** 차단 계정 일련 번호 입력 폼의 id */
var memNoInputFormId = "#memNo";
/** 차단 계정 검색 버튼의 id */
var popupSearchMemNoButtonId = "#popupSearchMemNoButton"
	
$(document).ready(function(){
	// 차단 시작,만료 일 입력 폼 생성
	initDateTimeForm();
	
	// 그리드 생성
	initBlockMemberGrid();
	
	// BLOCK_MEMBER 그리드 갱신
	reloadBlockMemberGrid();
	
});

/**
 * 차단 시작/만료 일자 입력 폼을 생성합니다.
 * 기간 검색 폼을 생성합니다.
 * @returns
 */
function initDateTimeForm(){
	var option = {
		formatString: 'yyyy/MM/dd HH:mm:ss',
		showTimeButton: true,
		showCalendarButton: true
	};
	
	$("#jqxdatetimeinputStart").jqxDateTimeInput(option);
	$("#jqxdatetimeinputExpire").jqxDateTimeInput(option);
	$(blockMemberFormId)[0].reset();
	
	option.width = '30%';
	option.value = null;
	$("#search_block_start_date").jqxDateTimeInput(option);
	//$("#search_block_start_date").val(null);
	
	$("#search_block_expire_date").jqxDateTimeInput(option);
	//$("#search_block_expire_date").val(null);
}

/**
* 차단 계정(BLOCK_MEMBER) 그리드를 초기화 합니다.
* (차단 계정 그리드를 생성)
*/
function initBlockMemberGrid(){
	
	function cellValueChanging(row, column, columntype, oldvalue, newvalue){
		// new value가 공백이라면 old value로 재설정
		if(newvalue == ""){
			console.log('공백이 입력되었으므로, 기존값으로 복원합니다.');
			return oldvalue;
		}
	}
	
	$(blockMemberGridId).jqxGrid({
		width: '100%',
		height: '400px',              
		pageable: true,
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
				dataField: 'blockNo',
				displayField: 'blockNo',
				cellsalign: 'center',
				align: 'center',
				editable: false,
				width: '8%'
			},
			{
				text: '아이디/닉네임/계정 번호',
				dataField: 'memNo',
				displayField: 'memInfo',
				cellsalign: 'center',
				align: 'center',
				editable: false,
				width: '20%'
			},
			{
				text: '차단 시작 일자',
				dataField: 'blockStartDate',
				columntype: 'datetimeinput',
				cellsformat: 'yyyy/MM/dd HH:mm:ss',
				cellsalign: 'center',
				align: 'center',
				editable: true,
				cellvaluechanging: cellValueChanging,
				width: '22%',
				createeditor: function(row, cellvalue, editor, cellText, width, height){
					// 에디트 모드가 활성화 되면 해당 칸에 커스텀 에디터를 생성합니다.
					editor.jqxDateTimeInput({
						width: width,
						height: height,
						formatString: 'yyyy/MM/dd HH:mm:ss',
						showTimeButton: true,
						showCalendarButton: true
					});
				},
				initeditor: function(row, cellvalue, editor, celltext, pressedkey){
					// 커스텀 에디터의 초기값을 설정합니다. 에디터가 보여질때마다 콜백함수가 호출됩니다.
					editor.jqxDateTimeInput('val', cellvalue);
				},
				geteditorvalue: function(row, cellvalue, editor){
					// 작성을 마친 후 커스텀 에디터의 값을 jqxGrid에게 반환 합니다.
					return editor.jqxDateTimeInput('getDate');
				}
			},
			{
				text: '차단 종료 일자',
				dataField: 'blockExpireDate',
				columntype: 'custom',
				cellsformat: 'yyyy/MM/dd HH:mm:ss',
				cellsalign: 'center',
				align: 'center',
				editable: true,
				cellvaluechanging: cellValueChanging,
				width: '22%',
				createeditor: function(row, cellvalue, editor, cellText, width, height){
					// 에디트 모드가 활성화 되면 해당 칸에 커스텀 에디터를 생성합니다.
					editor.jqxDateTimeInput({
						width: width,
						height: height,
						formatString: 'yyyy/MM/dd HH:mm:ss',
						showTimeButton: true,
						showCalendarButton: true
					});
				},
				initeditor: function(row, cellvalue, editor, celltext, pressedkey){
					// 커스텀 에디터의 초기값을 설정합니다. 에디터가 보여질때마다 콜백함수가 호출됩니다.
					editor.jqxDateTimeInput('val', cellvalue);
				},
				geteditorvalue: function(row, cellvalue, editor){
					// 작성을 마친 후 커스텀 에디터의 값을 jqxGrid에게 반환 합니다.
					return editor.jqxDateTimeInput('getDate');
				}
			},
			{
				text: '차단 사유',
				dataField: 'blockCause',
				cellsalign: 'left',
				align: 'center',
				editable: true,
				cellvaluechanging: cellValueChanging,
				width: '28%'
			}
		]
	});
	
	// Cell Begin Edit
	$(blockMemberGridId).on('cellbeginedit', function(event){
		
		$(blockMemberGridId).jqxGrid("clearselection"); // BLOCK_MEMBER 그리드의 선택 효과 제거
		$(blockMemberGridId).jqxGrid('selectrow', event.args.rowindex);	// 편집에 들어간 행에 선택 효과 부여
		
	});
	
	// Cell End Edit
	$(blockMemberGridId).on('cellendedit', function(event){
		
		/** 편집한 행 번호 */
		var rowIndex = event.args.rowindex;
		/** 편집한 권한 일련 번호 */
		var blockNo = event.args.row.blockNo;
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
		
		console.log("oldValue", oldValue);
		console.log("newValue", newValue);
		
		// 변경되지 않았다면 이벤트 종료
		var isOldDateType = (typeof oldValue === "object") && (oldValue instanceof Date);
		var isNewDateType = (typeof newValue === "object") && (newValue instanceof Date);
		if(		isOldDateType == true
			&&	isNewDateType == true
		){
			if(oldValue.getTime() == newValue.getTime()){
				return;
			}
		}else{
			if(oldValue == newValue){
				return;
			}
		}
		
		// 전송할 json 데이터 생성
		var isDateType = (typeof newValue === "object") && (newValue instanceof Date);
		var data = {};
		eval(
				"data."
				+ dataField
				+ " = "
				+ ( isDateType ? newValue.getTime() : "'" + newValue + "'" )
		);
		var jsonData = JSON.stringify(data);
		
		// 출력
		console.log("전송할 json 데이터", blockNo, jsonData);
				
		// 수정 요청 전송
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			type: "PATCH",
			url: CONTEXT_PATH + "/block-member/" + Number(blockNo),
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
					console.log("BLOCK_MEMBER 수정에 실패했습니다.");
					console.log(data.result);
					console.log("message", data.message);
					
					// 수정 전 값으로 복원
					$(blockMemberGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
				}
			},
			error: function(xhr){
				console.log("error", xhr);
				// 수정 전 값으로 복원
				$(blockMemberGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
			}
		});
	});
}

/**
* 서버로부터 block-member목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadBlockMemberGrid(){
	search_block_member();
}

/**
* json배열 형식의 권한 목록을 gridId에 교체 합니다.
*/
function changeBlockMemberGrid(listData){
	$(blockMemberGridId).jqxGrid("clearselection"); // BLOCK_MEMBER 그리드의 선택 효과 제거
	$(blockMemberGridId).jqxGrid("clear"); // BLOCK_MEMBER 그리드의 데이터 제거
	
	var source = {
		localdata: listData,
		datatype: "array",
		datafields: [
			{name: 'memInfo', type: 'string'},
			{name: 'blockNo', type: 'int'},
			{name: 'memNo', type: 'int'},
			{name: 'blockStartDate', type: 'date'},
			{name: 'blockExpireDate', type: 'date'},
			{name: 'blockCause', type: 'string'}
		]
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source, {
		downloadComplete: function (data, status, xhr) { },
		loadComplete: function (data) { },
		loadError: function (xhr, status, error) { }
	});
	
	// BLOCK_MEMBER 그리드에 새로운 데이터 삽입
	$(blockMemberGridId).jqxGrid({
		source: dataAdapter
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

/**
 * 폼에 입력된 BLOCK_MEMBER를 등록합니다.
 * @returns
 */
function insertBlockMember(){	
	// 전송할 json 데이터 생성
	var formData = {};
	formData.memNo = $(memNoInputFormId).val();
	formData.setStartDate = $("#jqxdatetimeinputStart").val();
	formData.setExpireDate = $("#jqxdatetimeinputExpire").val();
	formData.blockCause = $("#blockCause").val();
	
	var jsonForm = JSON.stringify(formData);
	
	// 출력
	console.log('전송할 json 데이터', jsonForm);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "POST",
		url: CONTEXT_PATH + "/block-member",
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
				reloadBlockMemberGrid();
				$(blockMemberFormId)[0].reset();
			}else{
				console.log("BLOCK_MEMBER 등록에 실패했습니다.");
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
 * BLOCK_MEMBER jqxGrid에서 선택된 항목을 제거합니다.
 * @returns
 */
function deleteSelectedBlockMember(){
	// 현재 선택한 권한의 일련 번호 구하기
	var selectedBlockMemberNoArray = String(getSelectedNoArray(blockMemberGridId, 'blockNo'));
	
	// 선택한 행이 없으면 이벤트 취소
	if(selectedBlockMemberNoArray.length <= 0){
		console.log('제거할 행을 선택해 주세요.');
		return;
	}
	
	// 출력
	console.log('전송할 데이터', selectedBlockMemberNoArray);
	
	// 제거 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "DELETE",
		url: CONTEXT_PATH + "/block-member/" + selectedBlockMemberNoArray,
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadBlockMemberGrid();
			}else{
				console.log("다음의 BLOCK_MEMBER 제거에 실패했습니다.");
				console.log(data.result);
				console.log("message", data.message);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

function search_block_member(){
	// 전송할 json 데이터 생성
	var searchData = {};
	searchData.searchType = $("#searchType").val();
	searchData.searchKeyword = $("#searchKeyword").val();
	searchData.searchStartDate = $("#search_block_start_date").val();
	searchData.searchExpireDate = $("#search_block_expire_date").val();
	searchData.searchTime = $("input[name='search_time']:checked").map(function(){
		return $(this).val();
	}).get();
	
	// 출력
	console.log('검색조건', searchData);
	console.log('전송할 데이터', decodeURIComponent($.param(searchData)));
	/**
	 * blockStartDate=2018/03/01 00:00:00&blockExpireDate=2018/03/30 00:00:00&searchTime[]=PRESENT&searchTime[]=FUTURE
	 */
	//return;
	
	// 제거 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: CONTEXT_PATH + "/block-member?" + $.param(searchData),
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				changeBlockMemberGrid(data.list);
				//reloadBlockMemberGrid();
			}else{
				console.log("BLOCK_MEMBER 조회에 실패했습니다.");
				console.log(data.result);
				console.log("message", data.message);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
	
}

/**
 * 계정 검색 팝업창의 특정 변수(var chooseMemberInfo) 속 데이터를 꺼냈을때 호출되는 함수
 * @param target
 * @param prop
 * @returns
 */
function getCallback(target, prop){
	//console.log({type: 'get', target, prop});
	return Reflect.get(target, prop);
}

/**
 * 계정 검색 팝업창의 특정 변수(var chooseMemberInfo)에 데이터가 저장될때 호출되는 함수
 * @param target
 * @param prop
 * @param value
 * @returns
 */
function setCallback(target, prop, value){
	//console.log({type: 'set', target, prop, value});
	$(memInfoInputFormId).val(value);
	$(memNoInputFormId).val(value);
	return Reflect.set(target, prop, value);
}



