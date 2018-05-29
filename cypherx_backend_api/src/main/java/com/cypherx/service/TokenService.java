package com.cypherx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cypherx.dto.TokenDTO;


@Service
public interface TokenService {
	
	public Double tokenBalance(TokenDTO tokenDTO) throws Exception;
	
	public boolean tokenTransfer(TokenDTO tokenDTO) throws Exception;
	
	public boolean validAmount(TokenDTO tokenDTO) throws Exception;
	
	public List<TokenDTO> recentListTransactions() throws Exception;
	


}
