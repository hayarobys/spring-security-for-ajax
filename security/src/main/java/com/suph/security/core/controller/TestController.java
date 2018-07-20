package com.suph.security.core.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController{
	private Logger logger = LoggerFactory.getLogger(TestController.class);
	
	/**
	 * 레이어 팝업 예제 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/test/layer-popup", method=RequestMethod.GET)
	public String admin(){
		return "test/layer-popup";
	}
	
	@RequestMapping(value="/test/echo/{message}", method=RequestMethod.GET)
	private @ResponseBody Map<String, Object> echoMessage(@PathVariable(required=true) String message){
		logger.debug("message: {}", message);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result", message);
		return returnMap;
	}
}
