<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>MEMBER 관리</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/member/member.css'/>" />
	
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="<c:url value='/resources/scripts/ui/member/member.js'/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	
	<div id="contents">
		<jsp:include page="/WEB-INF/views/common/nav.jsp"></jsp:include>
		
		<section id="box" class="box">
			<div class="data_box">
				<header class="title">
					<span class="title_font">MEMBER</span>
				</header>
				
				<section id="data_member" class="data_body">
					
				</section>
				
				<input type="button" value="새로고침" onclick="javascript:reloadMemberGrid();" />
				<input type="button" value="선택 항목 제거" onclick="javascript:deleteSelectedMember();" />
			</div>
			
			<div class="data_box">
				<form method="post" id="memberForm">
					<table id="memberFormTable">
						<tr>
							<th>
								<label for="memId">아이디</label>
							</th>
							<td>
								<input id="memId" name="memId" type="text" placeholder="영문 50Byte" />
								<button id="memIdDuplicateCheckButton" type="button" onclick="javascript:memIdDuplicateCheck()" >아이디 중복 검사</button>
							</td>
						</tr>
						<tr>
							<th>
								<label for="memPassword">비밀번호</label>
							</th>
							<td>
								<input id="memPassword" name="memPassword" type="text" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="memNicknm">닉네임</label>
							</th>
							<td>
								<input id="memNicknm" name="memNicknm" type="text" placeholder="30Byte" />
							</td>
						</tr>
					</table>
					<input type="button" value="계정 등록" onclick="javascript:insertMember();">
				</form>
			</div>
		</section>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
	
	
</body>
</html>









