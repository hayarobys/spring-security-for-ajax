<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>로그인 화면</title>
</head>
<body>
	<form id="loginfrm" name="loginfrm" method="POST" action="./j_spring_security_check">
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
					<input style="width:145px;" type="text" id="loginpwd" name="loginpwd" value="${loginpwd}"/>
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
				</td>
			</tr>
		</table>
		
		<input type="hidden" name="loginRedirect" value="${loginRedirect}"/>
	</form>
</body>
</html>











