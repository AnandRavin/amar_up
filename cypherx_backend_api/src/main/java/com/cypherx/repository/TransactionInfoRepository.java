package com.cypherx.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cypherx.model.TransactionInfo;

@Service
public interface TransactionInfoRepository extends CrudRepository<TransactionInfo, Integer> {

	public List<TransactionInfo> findByFromAddressOrToAddressOrderByCreatedDateDesc(String walletAddress,
			String walletAddress2);
	
	public TransactionInfo findById(Integer Id);


}

