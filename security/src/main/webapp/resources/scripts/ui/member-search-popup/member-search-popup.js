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
	
	// 계정 검색 용 레이어 팝업에 마우스 드래그 기능 부여
	$("#layer-mem-search").draggable();
	
	// 검색 버튼에 MEMBER 그리드 갱신 이벤트 부여
	$("#memberSearchForm").on('submit', function(event){
		event.preventDefault();
		reloadMemberGrid();
	});
});

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
 * 전송할 파라미터와 받았을때의 컬럼을 정의하고 dataAdapter로 변환해 반환합니다.
 * @returns
 */
function getDataAdapter(){
	var source = {
		data: {
			memId: $("#memId").val(),
			memNicknm: $("#memNicknm").val()
		},
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
			console.log("데이타:",data);
			source.totalrecords = data.list.totalRows;
		}
	};
	
	return new $.jqx.dataAdapter(source);
}

/**
* 계정(MEMBER) 그리드를 초기화 합니다.
* (계정 그리드를 생성)
*/
function initMemberGrid(){
	$(memberGridId).jqxGrid({
		width: '100%',
		height: '380px',  
		
		source: getDataAdapter(),
		pageable: true,
		virtualmode: true,
		rendergridrows: function(obj)
		{
			  return obj.data;     
		},
		
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
	$(memberGridId).jqxGrid("clearselection"); // MEMBER 그리드의 선택 효과 제거
	$(memberGridId).jqxGrid({source: getDataAdapter()});
	//$(memberGridId).jqxGrid("updatebounddata");
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










