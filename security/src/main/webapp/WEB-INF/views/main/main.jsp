<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="com.suph.security.core.userdetails.MemberInfo" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>메인화면</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/main/main.css'/>" />
	<script src="<c:url value='/resources/scripts/ui/main/main.js'/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	
	<div id="contents">
		<jsp:include page="/WEB-INF/views/common/nav.jsp"></jsp:include>
		
		<section id="mainContents">
			<article>
				메인 컨텐츠<br />
				<input type="button" value="현재 닉네임 읽기" onclick="javascript:getNickname()"/>
				<div id="nickname">
				
				</div>
			</article>
		</section>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>