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
	
	/* 
	// 로그인 정보를 가져오기 위해 Servlet Spec에서 제공하는 방법
	Authentication auth = (Authentication)request.getUserPrincipal();
	String name = "";
	if(auth == null){
		
	}else{
		Object principal = auth.getPrincipal();
		if(principal != null && principal instanceof MemberInfo){
			name = ((MemberInfo)principal).getName();
		}
	}
	 */
	// 로그인 유저라면 양쪽 모두 UsernamePasswordAuthenticationToken 객체를 반환한다.
	// 미로그인 유저라면 스프링은 AnonymousAuthenticationToken 반환, 서블릿은 null을 반환한다.
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	
	<!-- Ajax, Json에서 사용하기 위한 CSRF 메타 태그 -->
	<sec:csrfMetaTags /> <!-- 로 대체 가능 -->
	<%-- <meta name="_csrf" content="${_csrf.token}" /> --%>
	<!-- default header name is X_CSRF_TOKEN -->
	<%-- <meta name="_csrf_header" content="${_csrf.headerName}" /> --%>
	
	<title>메인화면</title>
</head>
<body>
	<h3>메인화면 입니다.</h3>
	<div style="width:200px; float:left;">
		<sec:authorize access="isAnonymous()">
			<!-- 스프링 시큐리티 4.x 버전부터는 action 경로가 login으로, name들은 더 명확한걸로 변경되었음에 주의. -->
			<form id="loginfrm" name="loginfrm" method="POST" action="./login_check">
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
							<input type="submit" id="loginbtn" value="로그인"/>
							<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
							<sec:csrfInput /> <!-- 을 사용하는 방법도 존재 -->
						</td>
					</tr>
				</table>
				
				<!-- 로그인 성공시 이동할 경로 지정 -->
				<!-- loginRedirect 값은 CustomAuthenticationFailureHandler에서 다룸 -->
				<input type="hidden" name="loginRedirect" value="${loginRedirect}"/>
			</form>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<%=name%>님 반갑습니다<br/>
			<%-- <a href="<%=request.getContextPath()%>/j_spring_security_logout">로그아웃</a> --%>
			
			<%-- <c:url var="logoutUrl" value="/j_spring_security_logout"/> --%>
			<c:url var="logoutUrl" value="/logout"/>
			<form action="${logoutUrl}" method="post">
				<input type="submit" value="로그아웃" />
				<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
				<sec:csrfInput /> <!-- 을 사용하는 방법도 존재 -->
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
		<p>
			메인 컨텐츠1
			<input type="button" value="ajax요청(반가워! 전송)" onclick="javascript:getHelloMessage()"/>
			<article id="message">
			
			</article>
		</p>
		<p>
			메인 컨텐츠2
		</p>
	</div>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script type="text/javascript">
		function getHelloMessage(){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			
			$.ajax({
				dataType: "json",
				contentType: "application/json;charset=UTF-8",
				type: "GET",
				// 모든 종류의 HTTP 요청시 쿠키에 저장된 csrf토큰값이 변경되지만,
				// ajax요청일 경우 헤더 메타 태그에 넣어둔 csrf토큰 값은 그대로 있기에 다음 요청에서 자칫 쿠키와 헤더 csrf의 불일치 오류가 발생할 수 있다.
				// 또한 GET으로 보낸 경우라도 CsrfFilter에서 검사하지 않을 뿐 쿠키Csrf토큰 값은 계속 새로 발급되는 것에 주의하자.
				url: "${pageContext.request.contextPath}/hello-message",
				beforeSend: function(xhr){
					xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
					xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
				},
				success: function(responseJSON, statusText, xhr){
					if(statusText == "success"){
						console.log("안녕 메시지 요청 성공", responseJSON.message);
						$("#message").append(responseJSON.message + "<br />");
					}else{
						console.log("요청 실패");
					}
				},
				error: function(xhr, status, error){
					var httpStatusCode = xhr.status;	// 상태코드
					var httpStatusMessage = xhr.statusTest;	// 상태 메시지
					
					console.log("상태코드: ", httpStatusCode);
					console.log("상태메시지: ", httpStatusMessage);
				}
			});
		}
	</script>
</body>
</html>