<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>RESOURCE 관리</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/resource/resource.css'/>" />
	
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="<c:url value='/resources/scripts/ui/resource/resource.js'/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	
	<div id="contents">
		<jsp:include page="/WEB-INF/views/common/nav.jsp"></jsp:include>
		
		<section id="box" class="box">
			<div class="data_box">
				<header class="title">
					<span class="title_font">RESOURCE</span>
				</header>
				
				<section id="data_resource" class="data_body">
					
				</section>
				
				<input type="button" value="새로고침" onclick="javascript:reloadResourceGrid();" />
				<input type="button" value="선택 항목 제거" onclick="javascript:deleteSelectedResources();" />
			</div>
			
			<div class="data_box">
				<form method="post" id="resourceForm">
					<table id="resourceFormTable">
						<tr>
							<th>
								<label for="resourceNm">이름</label>
							</th>
							<td>
								<input id="resourceNm" name="resourceNm" type="text" placeholder="OO 게시판" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="httpMethod">HTTP METHOD</label>
							</th>
							<td>
								<select id="httpMethod" name="httpMethod">
									<option value="" label="선택" />
								</select>
							</td>
						</tr>
						<tr>
							<th>
								<label for="resourcePattern">패턴</label>
							</th>
							<td>
								<input id="resourcePattern" name="resourcePattern" type="text" placeholder="/info/item/**" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="resourceType">타입</label>
							</th>
							<td>
								<input id="resourceType" name="resourceType" type="text" placeholder="url, method, class..." value="url" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="sortOrder">우선순위</label>
							</th>
							<td>
								<input id="sortOrder" name="sortOrder" type="number" min="0" max="99999999999" placeholder="50100" />
							</td>
						</tr>
					</table>
					<input type="button" value="리소스 등록" onclick="javascript:insertResource();">
				</form>
			</div>
		</section>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>









