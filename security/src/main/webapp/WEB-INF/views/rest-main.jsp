<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="com.suph.security.core.userdetails.MemberInfo" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%	
	// 로그인 정보를 가져오기 위해 스프링 시큐리티에서 제공하는 방법
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Object principal = auth.getPrincipal();
	String name = "";
	if(principal != null && principal instanceof MemberInfo){
		name = ((MemberInfo)principal).getName();
		// 미인증 상태에선 "anonymousUser" 가 반환된다.
	}
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="shorcut icon" href="./resources/favicon.ico" type="image/x-icon" />
	<sec:csrfMetaTags />
	<title>메인화면</title>
</head>
<body>
	<h3>Rest 메인화면 입니다.</h3>
	<div style="width:200px; float:left;">
		<sec:authorize access="isAnonymous()">
			<form id="loginfrm" name="loginfrm">
				<table>
					<tr>
						<td style="width:50px;">id</td>
						<td style="width:150px;">
							<input style="width:145px;" type="text" id="loginid" name="loginid" value=""/>
						</td>
					</tr>
					<tr>
						<td>pwd</td>
						<td>
							<input style="width:145px;" type="password" id="loginpwd" name="loginpwd" value=""/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="button" id="loginbtn" value="로그인" onclick="javascript:login()"/>
							<sec:csrfInput />
						</td>
					</tr>
				</table>
				
				<!-- 로그인 성공시 이동할 경로 지정 -->
				<input type="hidden" id="loginRedirect" name="loginRedirect" value="${loginRedirect}"/>
			</form>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<%=name%>님 반갑습니다<br/>
			<c:url var="logoutUrl" value="/logout"/>
			<form action="${logoutUrl}" method="post">
				<input type="submit" value="로그아웃" />
				<sec:csrfInput />
			</form>
			
			
		</sec:authorize>
		<ul>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<li>관리자 화면</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
				<li>매니저 화면</li>
			</sec:authorize>
			<sec:authorize access="permitAll">
				<li>비회원 게시판</li>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<li>준회원 게시판</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER')">
				<li>정회원 게시판</li>
			</sec:authorize>
		</ul>
	</div>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script type="text/javascript">
		function login(){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
						
			$.ajax({
				//dataType: "json",	// 받아서 해석할 타입. 이 값이 서버에서 응답한 형식과 다르면 error코드부로 넘어간다. "error 200(success)"을 볼 수도 있다는 의미.
				//contentType: "application/json;charset=UTF-8",	// 서버에 보낼 타입
				data: $("#loginfrm").serialize(),
				type: "POST",
				url: "${pageContext.request.contextPath}/login_check",
				beforeSend: function(xhr){
					xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
					xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
				},
				success: function(responseJSON, statusText, xhr){
					// HTTP STATUS 200 반환
					location.href="./main";
				},
				error: function(xhr, status, error){
					var httpStatusCode = xhr.status;	// 상태코드
					var httpStatusMessage = xhr.statusTest;	// 상태 메시지
					
					console.log("상태코드: ", httpStatusCode);
					console.log("상태메시지: ", httpStatusMessage);
					
					if(httpStatusCode == "401"){
						//location.href="./main";
						alert("ajax 로그인에 실패했습니다.");
					}
				}
			});
		}
	</script>
</body>
</html>