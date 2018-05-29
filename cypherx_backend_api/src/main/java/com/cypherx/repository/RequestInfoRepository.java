package com.cypherx.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cypherx.model.RequestInfo;

public interface RequestInfoRepository extends CrudRepository<RequestInfo, Integer>{
	
	public List<RequestInfo> findAll();
	
	public RequestInfo findById(Integer id);
	
	public RequestInfo findRequestInfoByEmailId(String emailId);
	
	public RequestInfo findRequestInfoByIsTransferStatus(Integer isTransferStatus);
}