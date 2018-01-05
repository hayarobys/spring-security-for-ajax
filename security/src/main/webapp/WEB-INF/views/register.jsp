<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
				</td>
			</tr>
		</table>
	
	</form>
</body>
</html>