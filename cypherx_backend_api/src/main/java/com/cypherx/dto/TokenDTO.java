package com.cypherx.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TokenDTO {
	
	private Integer id;
	private String emailId;
	private String fromAddress;
	private String toAddress;
	private String tokenName;
	private String tokenSymbol;
	private BigInteger decimalUnits;
	private Double tokenBalance;
	private Double noOfToken;
	private String sessionId;
	private String centralAdmin;
	private BigInteger tokens;
	private Double amount;
	private BigInteger etherBalance;
	private String etherWalletAddress;
	private String  etherWalletPassword;
	private String bitcoinWalletReceivingAddress;
	private Integer transferedToken;
	private String tokenAddress;
	private Date createdDate;
	private Integer receivedToken;
	private String requestAmount;
	private BigInteger transferAmount;
	private BigInteger gasValue;
	private BigInteger balance;
	private BigInteger totalUserCount;
	private BigDecimal mainBalance;
	private BigInteger transferTokenBalance;
	private Double requestTokens;
	private Integer selectTransactionType;
	private Double bitcoinTransferedAmount;
	private Double etherTransferedAmount;
	private boolean transferStatus;
	private BigDecimal etherTransferAmount;
	private Double crowdSaleTokenBalance;
	private Double crowdSaleSoldTokens;
	private String transferMode;
	
	
	
	
	public BigDecimal getEtherTransferAmount() {
		return etherTransferAmount;
	}


	public void setEtherTransferAmount(BigDecimal etherTransferAmount) {
		this.etherTransferAmount = etherTransferAmount;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public String getFromAddress() {
		return fromAddress;
	}


	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}


	public String getToAddress() {
		return toAddress;
	}


	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}


	public String getTokenName() {
		return tokenName;
	}


	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}


	public String getTokenSymbol() {
		return tokenSymbol;
	}


	public void setTokenSymbol(String tokenSymbol) {
		this.tokenSymbol = tokenSymbol;
	}


	public BigInteger getDecimalUnits() {
		return decimalUnits;
	}


	public void setDecimalUnits(BigInteger decimalUnits) {
		this.decimalUnits = decimalUnits;
	}

	public Double getTokenBalance() {
		return tokenBalance;
	}


	public void setTokenBalance(Double tokenBalance) {
		this.tokenBalance = tokenBalance;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public String getCentralAdmin() {
		return centralAdmin;
	}


	public void setCentralAdmin(String centralAdmin) {
		this.centralAdmin = centralAdmin;
	}


	public BigInteger getTokens() {
		return tokens;
	}


	public void setTokens(BigInteger tokens) {
		this.tokens = tokens;
	}


	public Double getNoOfToken() {
		return noOfToken;
	}


	public void setNoOfToken(Double noOfToken) {
		this.noOfToken = noOfToken;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public BigInteger getEtherBalance() {
		return etherBalance;
	}


	public void setEtherBalance(BigInteger etherBalance) {
		this.etherBalance = etherBalance;
	}


	public String getEtherWalletAddress() {
		return etherWalletAddress;
	}


	public void setEtherWalletAddress(String etherWalletAddress) {
		this.etherWalletAddress = etherWalletAddress;
	}


	public String getEtherWalletPassword() {
		return etherWalletPassword;
	}


	public void setEtherWalletPassword(String etherWalletPassword) {
		this.etherWalletPassword = etherWalletPassword;
	}


	public String getBitcoinWalletReceivingAddress() {
		return bitcoinWalletReceivingAddress;
	}


	public void setBitcoinWalletReceivingAddress(String bitcoinWalletReceivingAddress) {
		this.bitcoinWalletReceivingAddress = bitcoinWalletReceivingAddress;
	}


	public Integer getTransferedToken() {
		return transferedToken;
	}


	public void setTransferedToken(Integer transferedToken) {
		this.transferedToken = transferedToken;
	}


	public String getTokenAddress() {
		return tokenAddress;
	}


	public void setTokenAddress(String tokenAddress) {
		this.tokenAddress = tokenAddress;
	}


	public Date getCreatedDate() {
		return createdDate;
	}



	public BigDecimal getMainBalance() {
		return mainBalance;
	}

	public void setMainBalance(BigDecimal mainBalance) {
		this.mainBalance = mainBalance;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Integer getReceivedToken() {
		return receivedToken;
	}


	public void setReceivedToken(Integer receivedToken) {
		this.receivedToken = receivedToken;
	}


	public String getRequestAmount() {
		return requestAmount;
	}


	public void setRequestAmount(String requestAmount) {
		this.requestAmount = requestAmount;
	}


		public BigInteger getTransferAmount() {

			return transferAmount;

	}


	public void setTransferAmount(BigInteger transferAmount) {
		this.transferAmount = transferAmount;
	}


	public BigInteger getGasValue() {
		return gasValue;
	}


	public void setGasValue(BigInteger gasValue) {
		this.gasValue = gasValue;
	}


	public BigInteger getBalance() {
		return balance;
	}


	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}


	public BigInteger getTotalUserCount() {
		return totalUserCount;
	}


	public void setTotalUserCount(BigInteger totalUserCount) {
		this.totalUserCount = totalUserCount;
	}


	public BigInteger getTransferTokenBalance() {
		return transferTokenBalance;
	}


	public void setTransferTokenBalance(BigInteger transferTokenBalance) {
		this.transferTokenBalance = transferTokenBalance;
	}


	public Double getRequestTokens() {
		return requestTokens;
	}


	public void setRequestTokens(Double requestTokens) {
		this.requestTokens = requestTokens;
	}


	public Integer getSelectTransactionType() {
		return selectTransactionType;
	}


	public void setSelectTransactionType(Integer selectTransactionType) {
		this.selectTransactionType = selectTransactionType;
	}


	public Double getBitcoinTransferedAmount() {
		return bitcoinTransferedAmount;
	}


	public void setBitcoinTransferedAmount(Double bitcoinTransferedAmount) {
		this.bitcoinTransferedAmount = bitcoinTransferedAmount;
	}


	public Double getEtherTransferedAmount() {
		return etherTransferedAmount;
	}


	public void setEtherTransferedAmount(Double etherTransferedAmount) {
		this.etherTransferedAmount = etherTransferedAmount;
	}
	public Double getCrowdSaleTokenBalance() {
		return crowdSaleTokenBalance;
	}


	public void setCrowdSaleTokenBalance(Double crowdSaleTokenBalance) {
		this.crowdSaleTokenBalance = crowdSaleTokenBalance;
	}


	public Double getCrowdSaleSoldTokens() {
		return crowdSaleSoldTokens;
	}


	public void setCrowdSaleSoldTokens(Double crowdSaleSoldTokens) {
		this.crowdSaleSoldTokens = crowdSaleSoldTokens;
	}


	public String getTransferMode() {
		return transferMode;
	}


	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}


	public boolean isTransferStatus() {
		return transferStatus;
	}


	public void setTransferStatus(boolean transferStatus) {
		this.transferStatus = transferStatus;
	}
}

