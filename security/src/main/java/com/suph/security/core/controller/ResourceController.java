package com.suph.security.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suph.security.core.dto.PaginationRequest;
import com.suph.security.core.dto.ResourceDTO;
import com.suph.security.core.service.ResourceService;

@Controller
public class ResourceController{
	//private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	public @ResponseBody Map<String, Object> getResource(
			@RequestParam(name="pagenum", required=false, defaultValue="1") int pagenum,
			@RequestParam(name="pagesize", required=false, defaultValue="20") int pagesize
	){
		PaginationRequest paginationRequest = new PaginationRequest();
		paginationRequest.setPagenum(pagenum);
		paginationRequest.setPagesize(pagesize);
		return resourceService.getResourceList(paginationRequest);
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
		return resourceService.postResource(resourceDTO);
	}
	
	/**
	 * 리소스 수정
	 */
	@RequestMapping(value="/resource/{resourceNo}", method=RequestMethod.PATCH)
	public @ResponseBody Map<String, Object> patchResourceByResourceNo(
			@PathVariable(required=true) Integer resourceNo,
			@RequestBody ResourceDTO resourceDTO
	){
		return resourceService.patchResourceByResourceNo(resourceNo, resourceDTO);
	}
	
	/**
	 * 요청받은 리소스를 삭제 합니다.
	 * https://localhost:8443/security/resource/2,4,6
	 */
	@RequestMapping(value="/resource/{resourceNoList}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteResourceByResourceNoList(@PathVariable(required=true) List<Integer> resourceNoList){
		return resourceService.deleteResourceNoList(resourceNoList);
	}
	
	/**
	 * 모든 HTTP METHOD 목록을 JSON 형식으로 반환합니다.
	 * @return
	 */
	@RequestMapping(value="/http-method", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getHttpMethodList(){
		return resourceService.getHttpMethodList();
	}
	
}
