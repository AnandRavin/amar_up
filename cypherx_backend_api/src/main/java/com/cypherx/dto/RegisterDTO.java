package com.cypherx.dto;

import java.math.BigDecimal;
import java.util.Date;

public class RegisterDTO {
	
	private Integer id;
	
	private String userName;
	
	private String emailId;
	
	private String password;
	
	private String confirmPassword;
	
	private Integer roleId;

	private String etherWalletAddress;
	
	private String etherWalletPassword;
	
	private String bitcoinWalletAddress;
	
	private String bitcoinWalletReceivingAddress;
	
	private String bitcoinWalletPassword;
	
	private String sessionId;
	
	private BigDecimal bitcoinBalance;
	
	private String bitcoinToAddress;
	
	private String centralAdmin;
	
	private BigDecimal bitcoinTransferAmount;
	
	private BigDecimal etherBalance;
	
	public BigDecimal getBitcoinTransferAmount() {
		return bitcoinTransferAmount;
	}

	public void setBitcoinTransferAmount(BigDecimal bitcoinTransferAmount) {
		this.bitcoinTransferAmount = bitcoinTransferAmount;
	}

	private Date createdDate;
	
	private Boolean activation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public BigDecimal getBitcoinBalance() {
		return bitcoinBalance;
	}

	public void setBitcoinBalance(BigDecimal bitcoinBalance) {
		this.bitcoinBalance = bitcoinBalance;
	}

	public String getBitcoinToAddress() {
		return bitcoinToAddress;
	}

	public void setBitcoinToAddress(String bitcoinToAddress) {
		this.bitcoinToAddress = bitcoinToAddress;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getActivation() {
		return activation;
	}

	public void setActivation(Boolean activation) {
		this.activation = activation;
	}

	public String getBitcoinWalletAddress() {
		return bitcoinWalletAddress;
	}

	public void setBitcoinWalletAddress(String bitcoinWalletAddress) {
		this.bitcoinWalletAddress = bitcoinWalletAddress;
	}

	public String getBitcoinWalletReceivingAddress() {
		return bitcoinWalletReceivingAddress;
	}

	public void setBitcoinWalletReceivingAddress(String bitcoinWalletReceivingAddress) {
		this.bitcoinWalletReceivingAddress = bitcoinWalletReceivingAddress;
	}

	public String getBitcoinWalletPassword() {
		return bitcoinWalletPassword;
	}

	public void setBitcoinWalletPassword(String bitcoinWalletPassword) {
		this.bitcoinWalletPassword = bitcoinWalletPassword;
	}

	public String getCentralAdmin() {
		return centralAdmin;
	}

	public void setCentralAdmin(String centralAdmin) {
		this.centralAdmin = centralAdmin;
	}

	public BigDecimal getEtherBalance() {
		return etherBalance;
	}

	public void setEtherBalance(BigDecimal etherBalance) {
		this.etherBalance = etherBalance;
	}
	
}
