
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
	if(jwtNameToken === undefined){
		$("#nickname").append("미로그인 상태 입니다.<br />");
		return;
	}
	var payload = parseJwt(jwtNameToken);
	$("#nickname").append(decodeURI(payload.name) + "<br />");
	console.log("쿠키내용",payload);
}
