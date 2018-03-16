package com.suph.security.core.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.suph.security.core.dao.ResourceAuthDAO;
import com.suph.security.core.dto.ResourceAuthDTO;
import com.suph.security.core.service.RealTimeReflectableService;
import com.suph.security.core.service.ResourceAuthService;

@Service("resourceAuthService")
public class ResourceAuthServiceImpl implements ResourceAuthService{
	//private static final Logger logger = LoggerFactory.getLogger(ResourceAuthServiceImpl.class);
	
	public static final String TYPE_URL = "url";
	public static final String TYPE_METHOD = "method";
	public static final String TYPE_POINTCUT = "pointcut";
	
	@Autowired
	private ResourceAuthDAO resourceAuthDAO;
		
	/*
	public void setResourceAuthDAO(ResourceAuthDAO resourceAuthDAO){
		this.resourceAuthDAO = resourceAuthDAO;
	}
	*/
	/**
	 * URL 형식의 리소스(자원)별 필요한 권한 정보를 조회합니다.
	 * @return
	 * @throws Exception
	 */
	@Override
	public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getRolesAndUrl() throws Exception{
		LinkedHashMap<RequestMatcher, List<ConfigAttribute>> convertData = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();
		LinkedHashMap<Object, List<ConfigAttribute>> originalData = getRolesAndResources(ResourceAuthServiceImpl.TYPE_URL);
		
		Set<Object> keys = originalData.keySet();
		for(Object key : keys){
			convertData.put((AntPathRequestMatcher)key, originalData.get(key));
		}
		
		return convertData;
	}
	
	/**
	 * METHOD 형식의 자원별 필요한 권한 정보를 조회합니다.
	 * @return
	 * @throws Exception
	 */
	@Override
	public LinkedHashMap<String, List<ConfigAttribute>> getRolesAndMethod() throws Exception{
		LinkedHashMap<String, List<ConfigAttribute>> convertData = new LinkedHashMap<String, List<ConfigAttribute>>();
		LinkedHashMap<Object, List<ConfigAttribute>> originalData = getRolesAndResources(ResourceAuthServiceImpl.TYPE_METHOD);
		
		Set<Object> keys = originalData.keySet();
		for(Object key : keys){
			convertData.put((String)key, originalData.get(key));
		}
		
		return convertData;
	}
	
	/**
	 * POINTCUT 형식의 자원별 필요한 권한 정보를 조회합니다.
	 * @return
	 * @throws Exception
	 */
	@Override
	public LinkedHashMap<String, List<ConfigAttribute>> getRolesAndPointcut() throws Exception{
		LinkedHashMap<String, List<ConfigAttribute>> convertData = new LinkedHashMap<String, List<ConfigAttribute>>();
		LinkedHashMap<Object, List<ConfigAttribute>> originalData = getRolesAndResources(ResourceAuthServiceImpl.TYPE_POINTCUT);
		
		Set<Object> keys = originalData.keySet();
		for(Object key : keys){
			convertData.put((String)key, originalData.get(key));
		}
		
		return convertData;
	}
	
	/**
	 * 각 리소스 패턴별(url, method, pointcut) 필요한 권한 목록을 반환합니다.
	 * @param resourceType
	 * @return LinkedHashMap<Object, Collection<ConfigAttribute>>
	 * @throws Exception
	 */
	private LinkedHashMap<Object, List<ConfigAttribute>> getRolesAndResources(String resourceType) throws Exception{
		
		// DB로부터 URL별 필요 권한 목록을 조회
		List<ResourceAuthDTO> resultList = null;
		if( ResourceAuthServiceImpl.TYPE_METHOD.equals(resourceType) ){
			resultList = resourceAuthDAO.getRolesAndResources(ResourceAuthServiceImpl.TYPE_METHOD);
			
		}else if( ResourceAuthServiceImpl.TYPE_POINTCUT.equals(resourceType) ){
			resultList = resourceAuthDAO.getRolesAndResources(ResourceAuthServiceImpl.TYPE_POINTCUT);
			
		}else{
			resultList = resourceAuthDAO.getRolesAndResources(ResourceAuthServiceImpl.TYPE_URL);
			
		}
		
		
		// 스프링 시큐리티가 읽어들일 수 있는 형태의 '각 URL별 접근 가능한 권한 목록' Map생성
		LinkedHashMap<Object, List<ConfigAttribute>> returnMap = new LinkedHashMap<Object, List<ConfigAttribute>>();
		
		String pastResource = "";			// 이전 리소스 pattern(지난 루프에서 처리한 url패턴)
		String pastHttpMethod = "";	// 이전 HTTP METHOD 패턴(지난 루프에서 처리한 HTTP 메소드 패턴)
		
		String presentResourceStr = "";		// 현재 리소스 pattern
		String presentHttpMethod = "";	// 현재 HTTP 메소드 패턴
		
		Object presentResourceObj = null;	// 현재 리소스 pattern을 key에 넣을수 있는 상태로 만든 객체. AntPathRequestMatcher일 수도 있고, String 그대로일수도 있음.
		boolean isResourcesUrl = true;		// url 리소스 인가?
		
		// 각 ROW마다 스프링 시큐리티가 읽을 수 있는 형태로 변환작업 수행
		for(ResourceAuthDTO vo : resultList){
			// 리소스 패턴 얻기 (현재 작업중인 패턴에 저장)
			presentResourceStr = vo.getResourceNm();
			presentHttpMethod = vo.getHttpMethod();
			
			// url 리소스라면 ANT타입 객체로 변환한다.
			presentResourceObj = (isResourcesUrl ? new AntPathRequestMatcher(presentResourceStr, presentHttpMethod) : presentResourceStr);
			
			// 현재 url패턴에 접근 가능한 권한 목록을 담을 List객체 생성
			List<ConfigAttribute> configList = new LinkedList<ConfigAttribute>();	// 새로운 권한 목록
			
			// 처리해야할 URL패턴이 이전 URL패턴과 동일한 곳 을 가르키고 있다면
			if(		(StringUtils.hasText(pastResource) && presentResourceStr.equals(pastResource))
				&&	(StringUtils.hasText(pastHttpMethod) && presentHttpMethod.equals(pastHttpMethod))
			){
				
				// 최종적으로 반환할 Map객체로부터 지금까지 저장된 현재 URL패턴의 권한 목록 받아오기
				List<ConfigAttribute> pastAuthList = returnMap.get(presentResourceObj);	// 이전 권한 목록
				
				// 이전 권한 목록 객체에서 하나씩 꺼내서, 새로운 권한 목록 객체에 삽입.
				Iterator<ConfigAttribute> pastAuthItr = pastAuthList.iterator();
				while(pastAuthItr.hasNext()){					
					SecurityConfig tempConfig = (SecurityConfig)pastAuthItr.next();
					configList.add(tempConfig);
				}
			}
			
			// 현재 url패턴에 접근 가능한 권한 목록에 DB에서 조회한 정보 반영
			configList.add(
					new SecurityConfig( vo.getAuthNm() )
			);
			
			// 최종적으로 반환할 Map에 재할당. 같은 key(리소스 패턴이 같은게 있다면)가 있다면 이것으로 교체.
			// 최종적으로 반환할 Map객체에 (url패턴 - 권한 목록) 으로 저장.
			returnMap.put(presentResourceObj, configList);
			
			// 이전 리소스 정보 갱신. DB조회시 부터 리소스 패턴 순서에 따라 정확한 정렬이 되어있어야 올바르게 동작하는 코드. (세부적인 패턴 -> 포괄적인 패턴 순서로)
			pastResource = presentResourceStr;
			pastHttpMethod = presentHttpMethod;
		}
		//logger.debug("메모리에 다음의 리소스-권한 정보를 올립니다:\n{}", returnMap);
		return returnMap;
	}

	@Override
	@Transactional
	public Map<String, Object> changeResourceAuth(ResourceAuthDTO resourceAuthDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			resourceAuthDAO.deleteAuthListByResourceNo(resourceAuthDTO.getResourceNo());
			if(		resourceAuthDTO.getAuthNoList() != null
				&&	resourceAuthDTO.getAuthNoList().size() > 0
			){
				resourceAuthDAO.insertAuthListByResourceNo(resourceAuthDTO);
			}
			returnMap.put("result", "success");
		}catch(DataAccessException e){
			returnMap.put("result", "fail");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return returnMap;
	}

}


