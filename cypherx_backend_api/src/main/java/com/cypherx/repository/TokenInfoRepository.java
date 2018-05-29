package com.cypherx.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cypherx.model.TokenInfo;



@Service
public interface TokenInfoRepository extends CrudRepository<TokenInfo, Integer> {
	
	TokenInfo findTokenInfoById(Integer id);

}
