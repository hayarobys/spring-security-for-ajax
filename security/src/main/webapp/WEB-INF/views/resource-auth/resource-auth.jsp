<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="_csrf_header" content="${_csrf.headerName}" />
	<meta name="_csrf" content="${_csrf.token}" />
	
	<title>RESOURCE-AUTH 관리</title>
	
	<link rel="shorcut icon" href="<c:url value='/resources/favicon.ico'/>" type="image/x-icon" />
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/resource-auth/resource-auth.css'/>" />
	
	<script src="<c:url value='https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js'/>"></script>
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="<c:url value='/resources/scripts/ui/resource-auth/resource-auth.js'/>"></script>
</head>
<body>
	<section id="box" class="box">
		<div class="data_box">
			<header class="title">
				<span class="title_font">RESOURCE</span>
			</header>
			
			<section id="data_resource" class="data_body">
				
			</section>
			
			<input type="button" value="reload" onclick="javascript:reloadResourceGrid();" />
		</div>
		
		<div class="data_box">
			<header class="title">
				<span class="title_font">AUTH</span>
			</header>
			
			<section id="data_auth" class="data_body">
				
			</section>
			
			<input type="button" value="save" onclick="javascipt:save();">
		</div>
	</section>
	
</body>
</html>









