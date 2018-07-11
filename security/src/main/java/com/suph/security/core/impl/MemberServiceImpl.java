package com.suph.security.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.suph.security.core.dao.BlockMemberDAO;
import com.suph.security.core.dao.MemberDAO;
import com.suph.security.core.dto.BlockInfoDTO;
import com.suph.security.core.dto.MemberDTO;
import com.suph.security.core.dto.PaginationResponse;
import com.suph.security.core.dto.ResourceDTO;
import com.suph.security.core.enums.MemberState;
import com.suph.security.core.service.MemberAuthService;
import com.suph.security.core.service.MemberService;
import com.suph.security.core.userdetails.MemberInfo;

@Service("memberService")
public class MemberServiceImpl implements MemberService{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private MemberAuthService memberAuthService;
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private BlockMemberDAO blockMemberDAO;
		
	protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	protected Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
		// DB로부터 해당 ID의 계정 정보 조회
		MemberDTO user = memberDAO.getMemberInfoById(id);
		
		// DB로부터 해당 ID와 일치하는 계정을 찾지 못했다면 예외 발생
		if(user == null){
			logger.debug("Query returned no results for user '" + id + "'");
			UsernameNotFoundException ue = new UsernameNotFoundException(
					messages.getMessage("JdbcDaoImpl.notFound", new Object[]{ id },
					"User {0} not found")
			);
			
			throw ue;
		}
		
		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		
		// 이 계정의 권한 목록 반환
		dbAuthsSet.addAll( memberAuthService.loadUserAuthorities(user.getMemNo()) );
		
		// 계정은 있지만 어떠한 권한도 조회되지 않았다면 예외 발생.
		if(dbAuthsSet.size() == 0){
			// 이 계정에는 어떠한 권한도 없으며, '찾을 수 없음'으로 간주/처리 됩니다.
			logger.debug("User '" + id + "' has no authorities and will be treated as 'not found'");
			UsernameNotFoundException ue = new UsernameNotFoundException(
					messages.getMessage(
							"JdbcDaoImpl.noAuthority", 
							new Object[] {id},
							"User {0} has no GrantedAuthority"
					)
			);
			
			throw ue;
		}

		// 이 계정의 권한들을 Set -> List로 변환하여 계정 객체에 저장.
		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		
		// 이 계정의 현재 차단 정보를 조회하여 계정 객체에 저장.
		List<BlockInfoDTO> blockInfo = blockMemberDAO.selectCurrentBlockMemberInfoByMemNo(user.getMemNo());
		
		MemberInfo result = new MemberInfo(
				user.getMemNo(),
				user.getMemId(),
				user.getMemPassword(),
				user.getMemNicknm(),
				MemberState.ACTIVE.toString().equals( user.getMemState() ),
				dbAuths,
				blockInfo
		);
		
		return result;
	}
	
	@Override
	public Map<String, Object> getMember(MemberDTO memberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			List<MemberDTO> list = memberDAO.selectActiveMember(memberDTO);
			int totalRows = memberDAO.selectActiveMemberTotalRows(memberDTO);
			
			PaginationResponse<MemberDTO> data = new PaginationResponse<MemberDTO>(list, totalRows);
			
			returnMap.put("list", data);
			returnMap.put("result", "success");
		}catch(DataAccessException de){
			returnMap.put("result", "fail");
			de.printStackTrace();
		}
		
		return returnMap;
	}
	
	public Map<String, Object> getMember(){
		return getMember(null);
	}

	@Override
	public Map<String, Object> postMemberIdDuplicateCheck(String memId){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{	
			if(StringUtils.hasText(memId) == false){
				// 비어있는 문자열일 시 NullPointerException 발생
				throw new NullPointerException();
			}
			
			memId = memId.trim();
			String dbId = memberDAO.selectMemId(memId);
			
			if(memId.equals(dbId) == true){
				// 아이디 중복시 DupicateName 예외 발생
				throw new DuplicateName();
			}
			
			returnMap.put("result", "success");
			returnMap.put("message", memId + "는 사용 가능 합니다.");
			
		}catch(NullPointerException npe){
			returnMap.put("result", "empty");
			returnMap.put("message", "아이디를 입력해 주세요.");
			//npe.printStackTrace();
		}catch(DuplicateName dn){
			returnMap.put("result", "duplicate");
			returnMap.put("message", memId + "는 이미 사용 중 입니다.");
			//dn.printStackTrace();
		}catch(DataAccessException de){
			returnMap.put("result", "fail");
			returnMap.put("message", "중복 검사에 실패했습니다.\n잠시 후 다시 시도해 주세요.");
			//de.printStackTrace();
		}
		
		return returnMap;
	}
	
	@Override
	public Map<String, Object> postMember(MemberDTO memberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		memberDTO.setMemPassword(
				passwordEncoder.encode( memberDTO.getMemPassword() )
		);
		
		// 마지막 로그인 날짜(여기선 생성일 개념으로 사용)에 현재 시간 입력
		memberDTO.setLastLoginDate(new java.util.Date());
		logger.debug("memberDTO {}", memberDTO);
		try{
			memberDAO.insertMember(memberDTO);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			returnMap.put("message", "등록에 실패했습니다.");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> patchMember(int memNo, MemberDTO memberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		memberDTO.setMemNo(memNo);
		
		try{
			memberDAO.updateMember(memberDTO);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> deleteMember(int memNo){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			memberDAO.deleteMember(memNo);
			returnMap.put("result", "success");
		}catch(DataIntegrityViolationException dive){
			returnMap.put("result", "fail");
			returnMap.put("message", "제약 조건에 걸려 삭제할 수 없습니다. 연결된 권한이 없는지 확인해주세요.");
			dive.printStackTrace();
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			returnMap.put("message", "삭제에 실패했습니다.");
			dae.printStackTrace();
		}
		
		return returnMap;
	}
}









