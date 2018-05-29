package com.cypherx.model;
import java.io.Serializable;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Token_Request_Info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "ether_Wallet_address")
	private String etherWalletAddress;
	
	@Column(name = "bitcoin_Wallet_address")
	private String bitcoinWalletAddress;
	
	@Column(name="transfer_amount")
	private Double transferAmount;
	
	@Column(name = "request_token")
	private BigInteger requestToken;

	@Column(name = "createdDate")
	private Date createdDate;

	@Column(name = "emailId")
	private String emailId;
	
	@Column(name = "transaction_type")
	private Integer transactionType;
	
	@Column(name = "isTransfer_Status")
	private Integer isTransferStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEtherWalletAddress() {
		return etherWalletAddress;
	}

	public void setEtherWalletAddress(String etherWalletAddress) {
		this.etherWalletAddress = etherWalletAddress;
	}

	public String getBitcoinWalletAddress() {
		return bitcoinWalletAddress;
	}

	public void setBitcoinWalletAddress(String bitcoinWalletAddress) {
		this.bitcoinWalletAddress = bitcoinWalletAddress;
	}

	public Double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public BigInteger getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(BigInteger requestToken) {
		this.requestToken = requestToken;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getIsTransferStatus() {
		return isTransferStatus;
	}

	public void setIsTransferStatus(Integer isTransferStatus) {
		this.isTransferStatus = isTransferStatus;
	}

}
