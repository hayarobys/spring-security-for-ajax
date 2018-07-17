<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>차단 계정 관리</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/block-member/block-member.css'/>" />
	
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>	<!-- 레이어 팝업에 드래그 기능을 부여하기 위한 참조 -->
	<script charset="utf-8" src="<c:url value='/resources/scripts/ui/block-member/block-member.js'/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	
	<div id="contents">
		<jsp:include page="/WEB-INF/views/common/nav.jsp"></jsp:include>
		
		<section id="box" class="box">
			<div>
				<header class="title">
					<span class="title_font">차단 계정 관리</span>
				</header>
				
				<section id="data_block_member" class="data_body">
					
				</section>
				
				<input type="button" value="새로고침" onclick="javascript:reloadBlockMemberGrid();" />
				<input type="button" value="선택 항목 제거" onclick="javascript:deleteSelectedBlockMember();" />
			</div>
			<hr />
			<div class="search_box">
				<form method="GET" id="searchForm">
					<div id="search_member">
						<select id="searchType" name="searchType">
							<option value="ID" label="아이디" />
							<option value="NICKNM" label="닉네임" />
							<option value="NO" label="계정 일련 번호" />
						</select>(으)로 검색: 
						<input type="text" id="searchKeyword" name="searchKeyword" value="" />
					</div>
					
					<div id="search_block_start_date" name="searchStartDate" class="datetime"><!-- javascript로 jqxdatimeinput 생성 영역 --></div>
					<div class="datetime">&nbsp;&nbsp;~&nbsp;&nbsp;</div>
					<div id="search_block_expire_date" name="searchExpireDate" class="datetime"><!-- javascript로 jqxdatimeinput 생성 영역 --></div>
					
					<input type="checkbox" id="checkbox_search_past" name="search_time" value="PAST" />
					<label for="checkbox_search_past">과거</label>&nbsp;&nbsp;|
					
					<input type="checkbox" id="checkbox_search_present" name="search_time" value="PRESENT" checked="checked" />
					<label for="checkbox_search_present">현재</label>&nbsp;&nbsp;|
					
					<input type="checkbox" id="checkbox_search_future" name="search_time" value="FUTURE" />
					<label for="checkbox_search_future">미래</label>
					
					<div id="search_button_box">
						<button type="button" id="search_button" onclick="javascript:search_block_member();">검색</button>
					</div>
				</form>
			</div>
			<hr />
			<div>
				<form method="post" id="blockMemberForm" >
					<table id="blockMemberFormTable">
						<tr>
							<th>
								<label for="popupSearchMemNoButton">닉네임<br />(아이디/일련번호)</label>
							</th>
							<td>
								<label for="popupSearchMemNoButton">
									<input type="text" id="memInfo" name="memInfo" readonly="readonly" />
									<input type="hidden" id="memNo" name="memNo" />
								</label>
								<button id="popupSearchMemNoButton" type="button" onclick="javascript:popupSearchMemNo();" >계정 검색</button>
							</td>
						</tr>
						<tr>
							<th>
								<label for="blockStartDate">시작 일</label>
							</th>
							<td id="jqxdatetimeinputStart">
								<%-- jqxdatetimeinput script --%>
							</td>
						</tr>
						<tr>
							<th>
								<label for="blockExpireDate">만료 일</label>
							</th>
							<td id="jqxdatetimeinputExpire">
								<%-- jqxdatetimeinput script --%>
							</td>
						</tr>
						<tr>
							<th>
								<label for="blockCause">사유</label>
							</th>
							<td>
								<input id="blockCause" name="blockCause" type="text" placeholder="차단 사유를 적어주세요." />
							</td>
						</tr>
					</table>
					<input type="button" value="차단 계정 등록" onclick="javascript:insertBlockMember();">
				</form>
			</div>
		</section>
	
		<%@ include file="../member-search-popup/member-search-popup.jsp" %>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>