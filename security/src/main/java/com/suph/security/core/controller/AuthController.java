package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dto.AuthDTO;
import com.suph.security.core.dto.PaginationRequest;
import com.suph.security.core.service.AuthService;

@Controller
public class AuthController{
	@Autowired
	private AuthService authService;
	
	/**
	 * 권한 관리 페이지로 이동
	 */
	@RequestMapping(value="/auth/edit", method=RequestMethod.GET)
	public String getAuthEdit(){
		return "auth/auth";
	}
	
	/**
	 * 모든 권한 목록을 조회합니다.
	 * @return
	 */
	@RequestMapping(value="/auth", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAuth(
			@RequestParam(name="pagenum", required=false, defaultValue="0") int pagenum,
			@RequestParam(name="pagesize", required=false, defaultValue="20") int pagesize
	){
		PaginationRequest paginationRequest = new PaginationRequest();
		paginationRequest.setPagenum(pagenum);
		paginationRequest.setPagesize(pagesize);
		
		return authService.getAuthList(paginationRequest);
	}
	
	/**
	 * 권한을 등록합니다.
	 * @return
	 */
	@RequestMapping(value="/auth", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> postAuth(@RequestBody AuthDTO authDTO){
		return authService.postAuth(authDTO);
	}
	
	/**
	 * 특정 권한을 수정합니다.
	 * @param authNo
	 * @param authDTO
	 * @return
	 */
	@RequestMapping(value="/auth/{authNo}", method=RequestMethod.PATCH)
	public @ResponseBody Map<String, Object> patchAuthByAuthNo(
			@PathVariable(required=true) Integer authNo,
			@RequestBody AuthDTO authDTO
	){
		return authService.patchAuthByAuthNo(authNo, authDTO);
	}
	
	/**
	 * 요청받은 특정 권한을 삭제 합니다.
	 */
	@RequestMapping(value="/auth/{authNo}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteAuthByAuthNo(@PathVariable(required=true) Integer authNo){
		return authService.deleteAuthByAuthNo(authNo);
	}
}
