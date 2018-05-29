package com.cypherx.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cypherx.model.ReferralReceivedInfo;



@Service
public interface ReferralReceivedInfoRepository extends CrudRepository<ReferralReceivedInfo, Integer>{

	public List<ReferralReceivedInfo> findAll();
	
	public ReferralReceivedInfo findById(Integer userId);
	
	public List<ReferralReceivedInfo> findByUserId(Integer userId);
	
}
