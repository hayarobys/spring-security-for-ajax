/** MEMBER가 저장될 jqxGrid id */
var memberGridId = "#data_member";
/** 등록할 MEMBER 정보가 있는 form id */
var memberFormId = "#memberForm";
/** 아이디 입력 폼의 id */
var memIdInputFormId = "#memId";
/** 아이디 중복 검사 버튼의 id */
var memIdDuplicateCheckButtonId = "#memIdDuplicateCheckButton"
/** 아이디 중복 검사 수행(통과) 여부 */
var isPassedMemIdDuplicateCheck = false;
/** 사용 가능/불가에 따라 아이디 중복 검사 버튼에 노출될 메시지 */
var memIdDuplicateCheckMessage ={
	"process":"기다려 주세요",
	"success":"사용 가능한 아이디",
	"before":"아이디 중복 검사",
	"fail":"중복 검사 (다시 시도해 주세요)"
};

$(document).ready(function(){
	// 아이디 입력 폼의 값 변경 시, 아이디 중복 검사 버튼 재활성화.
	$(memIdInputFormId).change(function(){
		isPassedMemIdDuplicateCheck = false;
		$(memIdDuplicateCheckButtonId).removeAttr("disabled").text(memIdDuplicateCheckMessage.before);
	});
	
	// 그리드 생성
	initMemberGrid();
	
	// MEMBER 그리드 갱신
	//reloadMemberGrid();
});

/**
* 계정(MEMBER) 그리드를 초기화 합니다.
* (계정 그리드를 생성)
*/
function initMemberGrid(){
	
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
			{name: 'memNo', type: 'int'},
			{name: 'memId', type: 'string'},
			{name: 'memNicknm', type: 'string'},
			{name: 'lastLoginDate', type: 'date'}
		],
		url: CONTEXT_PATH + '/member',
		root: 'rows',
		cache: false,
		beforeprocessing: function(data){
			//console.log("데이타:",data);
			source.totalrecords = data.list.totalRows;
		}
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source);
	
	$(memberGridId).jqxGrid({
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
				dataField: 'memNo',
				cellsalign: 'center',
				align: 'center',
				editable: false,
				width: '10%'
			},
			{text: '아이디', dataField: 'memId', cellsalign: 'center', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '30%'},
			{text: '이름', dataField: 'memNicknm', cellsalign: 'center', align: 'center', editable: true, cellvaluechanging: cellValueChanging, width: '20%'},
			{
				text: '마지막 로그인 일자',
				dataField: 'lastLoginDate',
				columntype: 'datetimeinput',
				cellsformat: 'yyyy/MM/dd HH:mm:ss',
				cellsalign: 'center',
				align: 'center',
				editable: true,
				cellvaluechanging: cellValueChanging,
				width: '40%'
			}
		]
	});
	
	// Cell Begin Edit
	$(memberGridId).on('cellbeginedit', function(event){
		
		$(memberGridId).jqxGrid("clearselection"); // MEMBER 그리드의 선택 효과 제거
		$(memberGridId).jqxGrid('selectrow', event.args.rowindex);	// 편집에 들어간 행에 선택 효과 부여
		
	});
	
	// Cell End Edit
	$(memberGridId).on('cellendedit', function(event){
		
		/** 편집한 행 번호 */
		var rowIndex = event.args.rowindex;
		/** 편집한 권한 일련 번호 */
		var memNo = event.args.row.memNo;
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
		
		//console.log("oldValue", oldValue);
		//console.log("newValue", newValue);
		
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
		console.log("전송할 json 데이터", memNo, jsonData);
				
		// 수정 요청 전송
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			type: "PATCH",
			url: CONTEXT_PATH + "/member/" + Number(memNo),
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
					console.log("MEMBER 수정에 실패했습니다.");
					console.log(data.result);
					console.log("message", data.message);
					
					// 수정 전 값으로 복원
					$(memberGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
				}
			},
			error: function(xhr){
				console.log("error", xhr);
				// 수정 전 값으로 복원
				$(memberGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
			}
		});
	});
}

/**
* 서버로부터 member목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadMemberGrid(){
	$(memberGridId).jqxGrid("clearselection"); // MEMBER 그리드의 선택 효과 제거
	//$(memberGridId).jqxGrid("clear"); // MEMBER 그리드의 데이터 제거
	
	$(memberGridId).jqxGrid("updatebounddata");
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
 * 폼에 입력된 MEMBER를 등록합니다.
 * @returns
 */
function insertMember(){
	if(isPassedMemIdDuplicateCheck == false){
		alert(memIdDuplicateCheckMessage.before + '를 수행해 주세요.');
		return;
	}
	
	// 전송할 json 데이터 생성
	var serializeArrayForm = $(memberFormId).serializeArray();
	var objectForm = objectifyForm(serializeArrayForm);
	var jsonForm = JSON.stringify(objectForm);
	
	// 출력
	console.log('전송할 json 데이터', jsonForm);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "POST",
		url: CONTEXT_PATH + "/member",
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
				reloadMemberGrid();
				$(memberFormId)[0].reset();
			}else{
				console.log("MEMBER 등록에 실패했습니다.");
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
 * MEMBER jqxGrid에서 선택된 항목을 제거합니다.
 * @returns
 */
function deleteSelectedMember(){
	// 현재 선택한 권한의 일련 번호 구하기
	var selectedMemberNoArray = String(getSelectedNoArray(memberGridId, 'memNo'));
	
	// 선택한 행이 없으면 이벤트 취소
	if(selectedMemberNoArray.length <= 0){
		console.log('제거할 행을 선택해 주세요.');
		return;
	}
	
	// 출력
	console.log('전송할 데이터', selectedMemberNoArray);
	
	// 제거 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "DELETE",
		url: CONTEXT_PATH + "/member/" + selectedMemberNoArray,
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadMemberGrid();
			}else{
				console.log("다음의 MEMBER 제거에 실패했습니다.");
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
 * 아이디 중복 검사를 수행합니다.
 */
function memIdDuplicateCheck(){
	var requestId = {};
	requestId.memId = $(memIdInputFormId).val().trim();
	
	// 수정 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "POST",
		url: CONTEXT_PATH + "/check/member/id",
		data: JSON.stringify(requestId),
		contentType: 'application/json',
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			// ID 중복 검사 버튼 비활성화
			isPassedMemIdDuplicateCheck = false;
			$(memIdDuplicateCheckButtonId).attr("disabled", "disabled").text(memIdDuplicateCheckMessage.process);
			
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			console.log("data", data);
			switch(data.result){
			case 'success':
				isPassedMemIdDuplicateCheck = true;
				$(memIdDuplicateCheckButtonId).attr("disabled", "disabled").text(memIdDuplicateCheckMessage.success);
				alert(data.message);
				break;
			case 'empty':
				isPassedMemIdDuplicateCheck = false;
				$(memIdDuplicateCheckButtonId).removeAttr("disabled").text(memIdDuplicateCheckMessage.before);
				alert(data.message);
				break;
			case 'duplicate':
				isPassedMemIdDuplicateCheck = false;
				$(memIdDuplicateCheckButtonId).removeAttr("disabled").text(memIdDuplicateCheckMessage.fail);
				alert(data.message);
				break;
			case 'fail':
				isPassedMemIdDuplicateCheck = false;
				$(memIdDuplicateCheckButtonId).removeAttr("disabled").text(memIdDuplicateCheckMessage.fail);
				alert(data.message);
				break;
			default:
				isPassedMemIdDuplicateCheck = false;
				$(memIdDuplicateCheckButtonId).removeAttr("disabled").text(memIdDuplicateCheckMessage.fail);
				console.log("message",data.message);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
			isPassedMemIdDuplicateCheck = false;
			$(memIdDuplicateCheckButtonId).removeAttr("disabled").text(memIdDuplicateCheckMessage.fail);
		}
	});
}












