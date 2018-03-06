<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page import="org.springframework.security.core.Authentication"%>
<%@ page import="org.springframework.security.core.userdetails.UserDetails"%>
<%@ page import="org.springframework.security.core.userdetails.UserDetailsService"%>

<!DOCTYPE html>
<html>
<head>
	
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>오류 페이지</title>
	
	<style type="text/css">
		table{
			width: 800px;
		}
		
		table, th, td{
			border-collapse: collapse;
			border: 1px solid gray;
		}
	</style>
	
	<script type="text/javascript">
		$(function(){
			
		});
	</script>
</head>
<body>
	
	<div style="display: inline-block">
		<%-- <jsp:include page="/WRB-INF/views/leftmenu.jsp"></jsp:include> --%>
		<div style="float:right;">
			<p>
			접근권한이 없습니다.<br />
			담당자에게 문의하여 주시기 바랍니다.<br />
			</p>
			<p>
				에러 메시지: ${errormsg}<br />
				<c:if test="${not empty username}">
					계정 일련 번호: ${username}<br />
				</c:if>
				<c:if test="${not empty id}">
					계정 아이디: ${id}<br />
				</c:if>
				<c:if test="${not empty name}">
					계정 닉네임: ${name}<br />
				</c:if>
				<c:if test="${not empty authorities}">
					계정 보유 권한: ${authorities}<br />
				</c:if>
			</p>
			
			
			
			<%-- ${SPRING_SECURITY_403_EXCEPTION} // 디폴트 AccessDeniedHandlerImpl이었다면 이런 key를 사용
			<br />
			<%
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				Object principal = auth.getPrincipal();
				if(principal instanceof UserDetails){
					String username = ((UserDetails)principal).getUsername();
					String password = ((UserDetails)principal).getPassword();
					out.println("Account : " + username.toString() + "<br>");
				}
			%> --%>
			<a href="<c:url value='/main'/>">확인</a>
		</div>
	</div>
	
</body>
</html>








