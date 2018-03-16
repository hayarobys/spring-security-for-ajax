package com.suph.security.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.suph.security.core.dao.BlockMemberDAO;
import com.suph.security.core.dto.BlockMemberDTO;
import com.suph.security.core.service.BlockMemberService;

@Service
public class BlockMemberServiceImpl implements BlockMemberService{
	@Autowired
	private BlockMemberDAO blockMemberDAO;
	
	@Override
	public Map<String, Object> getBlockMemberByMemNo(Integer memNo){
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
	public Map<String, Object> getBlockMember(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<BlockMemberDTO> list = null;
		
		try{
			list = blockMemberDAO.selectBlockMember();
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
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Map<String, Object> patchBlockMember(Integer memNo, BlockMemberDTO blockMemberDTO){
		Map<String, Object> returnMap = new HashMap<String, Object>();		
		blockMemberDTO.setMemNo(memNo);
		
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
	public Map<String, Object> deleteBlockMember(Integer memNo){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try{
			blockMemberDAO.deleteBlockMember(memNo);
			returnMap.put("result", "success");
		}catch(DataAccessException dae){
			returnMap.put("result", "fail");
			dae.printStackTrace();
		}
		
		return returnMap;
	}

	
}
