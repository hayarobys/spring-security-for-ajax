package com.suph.security.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dto.ResourceDTO;
import com.suph.security.core.service.ResourceService;

@Controller
public class ResourceController{
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 리소스 관리 페이지로 이동
	 */
	@RequestMapping(value="/resource/edit", method=RequestMethod.GET)
	public String getResourceEdit(){
		return "resource/resource";
	}
	
	/**
	 * 모든 리소스 목록을 JSON 형식으로 반환합니다.
	 * @return
	 */
	@RequestMapping(value="/resource", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getResource(){
		return resourceService.getResourceList();
	}
	
	/**
	 * 모든 리소스 목록 중 패당 페이지 만큼 JSON 형식으로 반환합니다.
	 * @return
	 */
	@RequestMapping(value="/resource/page/{pageNo}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getResourceByPage(){
		return null;
	}
	
	/**
	 * 리소스 추가
	 */
	@RequestMapping(value="/resource", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> postResource(@RequestBody ResourceDTO resourceDTO){
		return resourceService.insertResource(resourceDTO);
	}
	
	/**
	 * 리소스 수정
	 */
	@RequestMapping(value="/resource/{resourceNo}", method=RequestMethod.PATCH)
	public Map<String, Object> patchResourceByResourceNo(){
		return null;
	}
	
	/**
	 * 리소스 삭제
	 */
	@RequestMapping(value="/resource/{resourceNo}", method=RequestMethod.DELETE)
	public Map<String, Object> deleteResourceByResourceNo(){
		return null;
	}
}
