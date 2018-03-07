<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>로그인 화면</title>
</head>
<body>
	<form id="loginfrm" name="loginfrm" method="POST" action="${pageContext.request.contextPath}/login_check">
		<table>
			<tr>
				<td style="width:50px;">id</td>
				<td style="width:150px;">
					<input style="width:145px;" type="text" id="loginid" name="loginid" value="${loginid}"/>
				</td>
			</tr>
			<tr>
				<td>pwd</td>
				<td>
					<input style="width:145px;" type="password" id="loginpwd" name="loginpwd" value="${loginpwd}"/>
				</td>
			</tr>
		<%--
		<c:if test="${not empty param.fail}">
		<!-- url parameter에 fail값이 존재한다면 현재 구문 실행 -->
			<tr>
				<td colspan="2">
					<font color="red">
						<p>
							Your login attempt was not successful, try again.
						</p>
						<p>	<!-- 세션에서 예외 메시지를 가져오는 부분 -->
							Reason: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
						</p>
					</font>
					<c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>
				</td>
			</tr>
		</c:if>
		--%>
		
		<c:if test="${not empty securityexceptionmsg}">
			<tr>
				<td colspan="2">
					<font color="red">
						<p>Your login atttemt was not successful, try again.</p>
						<!-- 더 이상 세션을 이용하지 않고, request영역에서 예외 메시지를 가져오는 부분 -->
						<p>${securityexceptionmsg}</p>
					</font>
				</td>
			</tr>
		</c:if>
			<tr>
				<td colspan="2">
					<input type="submit" id="loginbtn" value="로그인"/>
					<sec:csrfInput/>
				</td>
			</tr>
		</table>
		
		<%-- 인증 성공 후, 이동할 경로 저장 방식으로 세션을 썼을때의 코드 --%>
		<%-- <input type="hidden" name="loginRedirect" value="${loginRedirect}"/> --%>
		
		<%-- 인증 성공 후, 이동할 경로 저장 방식으로 URL 파라미터를 썼을때의 코드. --%>
		<%-- forward를 통해 이 페이지로 이동해 왔고, request에 loginRedirect값이 존재한다면, EL을 통해 그대로 불러쓸 수 있다. --%>
		<%-- forward로 왔을 경우, 직전의 요청 URL인 form action값이 주소값으로 대체되어 있을 것이다. --%>
		<input type="hidden" name="loginRedirect" value="${param.loginRedirect}"/>
	</form>
</body>
</html>











