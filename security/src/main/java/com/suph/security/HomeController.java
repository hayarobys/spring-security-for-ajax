package com.suph.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private MemberDAO memberDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/admin/hello")
	public String hello(){
		logger.debug("hello 페이지");
		return "hello";
	}
	
	@RequestMapping("/admin")
	public String admin(){
		logger.debug("admin 페이지");
		return "admin";
	}
	
	@RequestMapping("/main")
	public String main() {
		logger.debug("메인 페이지");
		return "main";
	}
	
	@RequestMapping("/login")
	public String login() {
		logger.debug("로그인 페이지");
		return "login";
	}
	
	@RequestMapping(value="/passwordEncoder", method=RequestMethod.GET)
	public String passwordEncoder(@RequestParam(value="targetStr", required=false, defaultValue="") String targetStr, Model model){
		logger.debug("암호화 예시 페이지");
		if(StringUtils.hasText(targetStr)){
			// 암호화 작업
			String bCryptString = passwordEncoder.encode(targetStr);
			model.addAttribute("targetStr", targetStr);
			model.addAttribute("bCryptString", bCryptString);
		}
		
		return "/showBCryptString";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(){
		logger.debug("회원등록 페이지");
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(MemberInfo vo){
		logger.debug("회원등록 요청 / MemberInfo {}", vo);
		
		vo.setPassword(
				passwordEncoder.encode( vo.getPassword() )
		);
		
		int result = memberDAO.insertMemverVO(vo);
		System.out.println(result > 0 ? "등록 성공" : "등록 실패");
		
		return "redirect:/main";
	}
	
	@RequestMapping("/access_denied")
	public String accessDenied(){
		logger.debug("접근 거부 페이지");
		return "access_denied";
	}
}
