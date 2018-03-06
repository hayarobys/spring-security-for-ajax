
/**
* jwt의 payload를 파싱 합니다.
* signature 검사를 하지 않습니다.
*/
function parseJwt(token){
	var base64Url = token.split('.')[1];	// 점 단위로 잘라서(header, payload, signature) payload부를 얻습니다.
	var base64 = base64Url.replace('-', '+').replace('_', '/');	// 대쉬를 플러스로 바꾸고 언더바를 슬래시로 바꿉니다.
	return JSON.parse(window.atob(base64));	// base64타입으로 인코딩된 payload를 javascript의 window.atob()함수로 디코드 한 후, JSON 파싱 합니다.
}

/**
* 특정 쿠키를 불러옵니다.
*/
function getCookie(cname){
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for(var i = 0; i < ca.length; i++){
		var c = ca[i];
		while(c.charAt(0) == ' '){
			c = c.substring(1);
		}
		if(c.indexOf(name) == 0){
			return c.substring(name.length, c.length);
		}
	}
}

/**
* 닉네임 쿠키로부터 jwt를 읽어 파싱한 뒤에 읽어들인 닉네임을 #nickname에 추가합니다.
*/
function getNickname(){
	var jwtNameToken = getCookie("info");
	var payload = parseJwt(jwtNameToken);
	$("#nickname").append(decodeURI(payload.name) + "<br />");
	console.log("쿠키내용",payload);
}

/**
* 서버로부터 Ajax get 방식으로 메시지를 받아옵니다.
* 만약 post요청이라면 쿠키속 csrf와 meta로부터 읽어 수동으로 헤더에 담은 csrf값을 서버에서 비교해 csrf공격을 방어합니다.
* get으로 보내더라도 쿠키속 csrf값은 재발급 되므로 헤더에 담을 meta데이터 속 csrf값과 달라짐에 유의해야 합니다.
*/
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
		url: "/security/hello-message",
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(responseJSON, statusText, xhr){
			var csrfToken = xhr.getResponseHeader("X-XSRF-TOKEN");
			console.log("token", csrfToken);
			
			if(statusText == "success"){
				//console.log("안녕 메시지 요청 성공", responseJSON.message);
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
		},
		complete: function(xhr){
			
		}
	});
}