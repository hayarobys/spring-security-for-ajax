<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>AUTH 관리</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/auth/auth.css'/>" />
	
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="<c:url value='/resources/scripts/ui/auth/auth.js'/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	
	<div id="contents">
		<jsp:include page="/WEB-INF/views/common/nav.jsp"></jsp:include>
		
		<section id="box" class="box">
			<div class="data_box">
				<header class="title">
					<span class="title_font">AUTH</span>
				</header>
				
				<section id="data_auth" class="data_body">
					
				</section>
				
				<input type="button" value="새로고침" onclick="javascript:reloadAuthGrid();" />
				<input type="button" value="선택 항목 제거" onclick="javascript:deleteSelectedAuth();" />
			</div>
			
			<div class="data_box">
				<form method="post" id="authForm" >
					<table id="authFormTable">
						<tr>
							<th>
								<label for="authNm">권한 명</label>
							</th>
							<td>
								<input id="authNm" name="authNm" type="text" placeholder="ROLE_권한명" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="authExplanation">권한 설명</label>
							</th>
							<td>
								<input id="authExplanation" name="authExplanation" type="text" placeholder="권한에 대한 설명을 적어주세요." />
							</td>
						</tr>
					</table>
					<input type="button" value="리소스 등록" onclick="javascript:insertAuth();">
				</form>
			</div>
		</section>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>









