<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<meta charset="UTF-8">
	
	<!-- Ajax, Json에서 사용하기 위한 CSRF 메타 태그 -->
	<!-- default header name is X-XSRF-TOKEN -->
	<%-- <meta name="_csrf_header" content="${_csrf.headerName}" /> --%>
	<%-- <meta name="_csrf" content="${_csrf.token}" /> --%>
	<sec:csrfMetaTags /> <!-- 로 대체 가능 -->
	
	<link rel="shorcut icon" href="<c:url value='/resources/favicon.ico'/>" type="image/x-icon" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script charset="utf-8" src="<c:url value='/resources/scripts/ui/common/common.js'/>"></script>