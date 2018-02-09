<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="shorcut icon" href="./resources/favicon.ico" type="image/x-icon" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
	<meta name="_csrf" content="${_csrf.token}" />
	<title>제목</title>
	
	<link rel="stylesheet" href="./resources/scripts/jqwidgets/styles/jqx.base.css" />
	<style type="text/css">
		.data_box{
			display: inline-block;
			float: left;
			width: 45%;
		}
		
		.space{
			display: inline-block;
			float: left;
			width: 15px;
			height: 10px;
		}
		
		.title{
			background-color: #04BCD4;
			text-align: center;
			border-radius: 8px;
			padding: 5px;
			margin-bottom: 10px;
		}
		
		.title_font{
			color: white;
			font-size: 1.4em;
			font-weight: bold;
		}
	</style>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="./resources/scripts/jqwidgets/jqx-all.js"></script> 
</head>
<body>
	
	<div id="box" class="box">
		<div class="data_box">
			<div class="title">
				<span class="title_font">RESOURCE</span>
			</div>
			<div id="data_resource" class="data_body">
				
			</div>
		</div>
		<div class="space"></div>
		<div class="data_box">
			<div class="title">
				<span class="title_font">AUTH</span>
			</div>
			<div id="data_auth" class="data_body">
				
			</div>
		</div>
		<div style="clear: left;">
			<input type="button" value="reload" onclick="javascript:reloadResourceGrid();" />
		</div>
	</div>
	
	<script type="text/javascript">
		var resourceGridId = "#data_resource";
		var authGridId = "#data_auth";
		var everyAuthList = null;
		
		$(document).ready(function(){
			initResourceGrid();
			initAuthGrid();
			
			getEveryAuthList();
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
					{text: '일련 번호', dataField: 'no', cellsalign: 'left', align: 'center', editable: false, width: '10%'},
					{text: '권한 명', dataField: 'name', cellsalign: 'left', align: 'center', editable: false, width: '30%'},
					{text: '권한 설명', dataField: 'explanation', cellsalign: 'left', align: 'center', editable: false, width: '60%'}
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
				url: "/security/auth-list",
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
				url: "/security/resource-list",
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
				url: "/security/auth-list/" + resourceNo,
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
			$(resourceGridId).jqxGrid("clearselection"); // 선택 효과 제거
			$(authGridId).jqxGrid("clearselection"); // 선택 효과 제거
			$(resourceGridId).jqxGrid("clear");
			$(authGridId).jqxGrid("clear");
			
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
	</script>
</body>
</html>













