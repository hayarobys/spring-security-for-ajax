package com.suph.security.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.userdetails.jdbc.dao.MemberDAO;
import com.suph.security.core.userdetails.jdbc.vo.MemberVO;

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
