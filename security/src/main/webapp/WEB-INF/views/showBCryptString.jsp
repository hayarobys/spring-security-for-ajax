<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>BCrypt 변환 보기</title>
	
</head>
<body>
	<h3>사용법</h3>
	<p>
		url에 http://localhost:8090/security/passwordEncoder?targetStr=mypassword 를 입력하세요.
		결과값이 이 페이지 하단에 나타납니다.
	</p>
	<h3>결과</h3>
	<p>
		<table>
			<tr>
				<th>원본 문자</th>
				<td>${targetStr}</td>
			</tr>
			<tr>
				<th>변환 문자</th>
				<td>${bCryptString}</td>
			</tr>
		</table>
	</p>
	
</body>
</html>