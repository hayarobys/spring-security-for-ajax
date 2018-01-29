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
 * 스프링 시큐리티에 관련된 동작을 처리하는 컨트롤러 입니다.
 * @author NB-0267
 *
 */
@Controller
public class SecurityController {
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private MemberDAO memberDAO;
	
	/**
	 * 관리자 전용 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping("/admin/hello")
	public String hello(){
		return "hello";
	}
	
	/**
	 * 관리자 전용 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping("/admin")
	public String admin(){
		return "admin";
	}
	
	/**
	 * 메인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping("/main")
	public String main() {
		return "main";
	}
	
	/**
	 * 로그인 페이지로 이동합니다.
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * 평문 비밀번호를 BCrypt로 암호화하여 보여주는 페이지로 이동합니다.
	 * @param targetStr
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/passwordEncoder", method=RequestMethod.GET)
	public String passwordEncoder(@RequestParam(value="targetStr", required=false, defaultValue="") String targetStr, Model model){
		if(StringUtils.hasText(targetStr)){
			// 암호화 작업
			String bCryptString = passwordEncoder.encode(targetStr);
			model.addAttribute("targetStr", targetStr);
			model.addAttribute("bCryptString", bCryptString);
		}
		
		return "/showBCryptString";
	}
	
	/**
	 * 회원가입 폼으로 이동합니다.
	 * @return
	 */
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(){
		return "register";
	}
	
	/**
	 * 회원가입 동작을 수행하고 메인페이지로 이동합니다.
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(MemberVO vo){
		
		vo.setPassword(
				passwordEncoder.encode( vo.getPassword() )
		);
		
		int result = memberDAO.insertMemberVO(vo);
		System.out.println(result > 0 ? "등록 성공" : "등록 실패");
		
		return "redirect:/main";
	}
	
	/**
	 * 접근 거부 화면으로 이동합니다.
	 * @return
	 */
	@RequestMapping("/access_denied")
	public String accessDenied(){
		return "access_denied";
	}
}
