package com.suph.security.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.suph.security.core.dao.BlockMemberDAO;
import com.suph.security.core.dto.BlockMemberDTO;
import com.suph.security.core.dto.SearchBlockMemberDTO;
import com.suph.security.core.service.BlockMemberService;

@Service
public class BlockMemberServiceImpl implements BlockMemberService{
	private static Logger LOGGER = LoggerFactory.getLogger(BlockMemberServiceImpl.class);
	
	@Autowired
	private BlockMemberDAO blockMemberDAO;
		
	@Override
	public Map<String, Object> getBlockMemberByMemNoAndExpireDateIsAfterTheCurrentDate(Integer memNo){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<BlockMemberDTO> list = null;
		
		try{
			list = blockMemberDAO.selectBlockMemberByMemNo(memNo);
			returnMap.put("list", list);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getBlockMember(SearchBlockMemberDTO searchBlockMemberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<BlockMemberDTO> list = null;
		
		LOGGER.debug("searchBlockMemberDTO: {}", searchBlockMemberDTO);
		
		try{
			list = blockMemberDAO.selectBlockMemberBySearchValue(searchBlockMemberDTO);
			
			returnMap.put("list", list);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> postBlockMember(BlockMemberDTO blockMemberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			blockMemberDAO.insertBlockMember(blockMemberDTO);
			returnMap.put("result", "success");
		}catch(DuplicateKeyException dke){
			returnMap.put("result", "fail");
			returnMap.put("message", "계정 당 하나씩의 차단을 걸 수 있습니다. 계정 일련 번호가 중복되진 않았는지 확인해주세요.");
			dke.printStackTrace();
		}catch(Exception e){
			returnMap.put("result", "fail");
			e.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> patchBlockMember(Integer blockNo, BlockMemberDTO blockMemberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();		
		blockMemberDTO.setBlockNo(blockNo);
		LOGGER.debug("이  값으로 수행: {}", blockMemberDTO);
		try{
			blockMemberDAO.updateBlockMember(blockMemberDTO);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> deleteBlockMember(Integer blockNo){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			blockMemberDAO.deleteBlockMember(blockNo);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	
}
