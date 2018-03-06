<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>회원 등록</title>	
</head>
<body>
	<form action="./register" method="post">
		<table>
			<tr>
				<td style="width:50px;">id</td>
				<td style="width:150px;">
					<input style="width:145px;" type="text" id="id" name="id" value=""/>
				</td>
			</tr>
			<tr>
				<td>password</td>
				<td>
					<input style="width:145px;" type="text" id="password" name="password" value=""/>
				</td>
			</tr>
			<tr>
				<td>nickname</td>
				<td>
					<input style="width:145px;" type="text" id="name" name="name" value=""/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="등록"/>
					<sec:csrfInput />
				</td>
			</tr>
		</table>
	
	</form>
</body>
</html>