<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page import="org.springframework.security.core.Authentication"%>
<%@ page import="org.springframework.security.core.userdetails.UserDetails"%>
<%@ page import="org.springframework.security.core.userdetails.UserDetailsService"%>

<!DOCTYPE html>
<html>
<head>
	
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>차단 안내</title>
	
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
			차단된 계정입니다.<br />
			</p>
			<p>
				에러 메시지: ${securityexceptionmsg}<br />
				<c:if test="${not empty username}">
					계정 일련 번호: ${username}<br />
				</c:if>
				<c:if test="${not empty id}">
					계정 아이디: ${id}<br />
				</c:if> 
				<c:if test="${not empty name}">
					계정 닉네임: ${name}<br />
				</c:if>
				<c:if test="${not empty blockinfo}">
					<br/>[차단 정보]<br />
					<c:forEach items="${blockinfo}" var="vo" varStatus="status">
						<jsp:useBean id="blockStartDate" class="java.util.Date" />
						<c:set target="${blockStartDate}" property="time" value="${vo.blockStartDate}" />
						
						<jsp:useBean id="blockExpireDate" class="java.util.Date" />
						<c:set target="${blockExpireDate}" property="time" value="${vo.blockExpireDate}" />
						
						<p>
							<b>${status.count}</b><br />
							차단 사유: ${vo.blockCause}<br />
							차단 기간: [<fmt:formatDate value="${blockStartDate}" pattern="yyyy/MM/dd HH:mm"/> ~ <fmt:formatDate value="${blockExpireDate}" pattern="yyyy/MM/dd HH:mm"/>]<br />
						</p>
					</c:forEach>						
				</c:if>
				
				<%-- <c:if test="${not empty blockinfo.blockCause}">
					차단 사유: ${blockinfo.blockCause}<br />
				</c:if>
				<c:if test="${not empty blockinfo.blockStartDate}">
					차단 시작 일자: ${blockinfo.blockStartDate}<br />
				</c:if>
				<c:if test="${not empty blockinfo.blockExpireDate}">
					차단 만료 일자: ${blockinfo.blockExpireDate}<br />
				</c:if> --%>
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








