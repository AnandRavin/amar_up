package com.cypherx.dto;

import java.util.List;

public class StatusResponseDTO {

   private String status;
	
	private String message;
	
	private LoginDTO loginInfo;
	
	private TokenDTO TokenBalanceInfo;
	
	private RegisterDTO etherBalanceInfo;
	
	private RegisterDTO bitcoinBalanceInfo;
	
	private String exchange;
	
	private String bitcoinResponse;
	
	private List<TokenDTO> listToken;
	
	
	public LoginDTO getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginDTO loginInfo) {
		this.loginInfo = loginInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TokenDTO getTokenBalanceInfo() {
		return TokenBalanceInfo;
	}

	public void setTokenBalanceInfo(TokenDTO tokenBalanceInfo) {
		TokenBalanceInfo = tokenBalanceInfo;
	}

	public RegisterDTO getBitcoinBalanceInfo() {
		return bitcoinBalanceInfo;
	}

	public void setBitcoinBalanceInfo(RegisterDTO bitcoinBalanceInfo) {
		this.bitcoinBalanceInfo = bitcoinBalanceInfo;
	}

	public RegisterDTO getEtherBalanceInfo() {
		return etherBalanceInfo;
	}

	public void setEtherBalanceInfo(RegisterDTO etherBalanceInfo) {
		this.etherBalanceInfo = etherBalanceInfo;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getBitcoinResponse() {
		return bitcoinResponse;
	}

	public void setBitcoinResponse(String bitcoinResponse) {
		this.bitcoinResponse = bitcoinResponse;
	}

	public List<TokenDTO> getListToken() {
		return listToken;
	}

	public void setListToken(List<TokenDTO> listToken) {
		this.listToken = listToken;
	}



}




