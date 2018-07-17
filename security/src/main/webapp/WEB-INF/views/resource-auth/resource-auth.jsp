<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>RESOURCE-AUTH 관리</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/resource-auth/resource-auth.css'/>" />
	
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="<c:url value='/resources/scripts/ui/resource-auth/resource-auth.js'/>"></script>
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
				
				<input type="button" value="reload" onclick="javascript:reloadResourceGrid();" />
				<input type="button" id="real_time_reflection" value="realtime reflection" onclick="javascript:realTimeReflection();" />
			</div>
			
			<div class="data_box">
				<header class="title">
					<span class="title_font">AUTH</span>
				</header>
				
				<section id="data_auth" class="data_body">
					
				</section>
				
				<input type="button" value="save" onclick="javascipt:save();">
				<br/>
				<span>
					※ 권한 미 선택시, 해당 URL은 패턴 "/**"의 권한 설정을 참조합니다.
				</span>
			</div>
		</section>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>









