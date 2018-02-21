package com.suph.security.core.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController{
	/**
	 * 메인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String main() {
		return "main/main";
	}
	
	/**
	 * Ajax 로그인 예제가 있는
	 * Rest 메인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/rest-main", method=RequestMethod.GET)
	public String restMain() {
		return "main/rest-main";
	}
	
	/**
	 * Json 응답을 전송합니다.
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hello-message", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Map<String, Object> ajaxGetHelloMessage(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("result", "success");
		result.put("message", "<p>나도 반가워 ~ " + request.getMethod()
				+ "<br />ajax get 요청시 CsrfFilter 검사 없이 통과하지만, 쿠키값은 새롭게 갱신됩니다."
				+ "<br />따라서 현재 페이지의 헤더 속 meta 태그에 저장된 csrf값과 쿠키 속 csrf값이 불일치 상태가 되었습니다."
				+ "<br />이 상태에서 로그아웃 등의 post요청시 접근 거부 당할 수 있음에 주의하세요.</p>");
		
		return result;
	}
}
