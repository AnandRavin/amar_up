package com.cypherx.model;


import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Referral_Received_Info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReferralReceivedInfo {

	//private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "Referent_Person_Name")
	@NotNull
	private String referentPersonName;
	
	@Column(name = "amount")
	@NotNull
	private BigInteger amount;
	
	@Column(name = "created_Date")
	private Date createdDate;

	@Column(name = "userId")
	@NotNull
	private Integer userId;
	
	@Column(name = "ether_Wallet_address")
	private String etherWalletAddress;
	
	@Column(name = "referral_transferStatus")
	private Integer referralTransferStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReferentPersonName() {
		return referentPersonName;
	}

	public void setReferentPersonName(String referentPersonName) {
		this.referentPersonName = referentPersonName;
	}

	public BigInteger getAmount() {
		return amount;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEtherWalletAddress() {
		return etherWalletAddress;
	}

	public void setEtherWalletAddress(String etherWalletAddress) {
		this.etherWalletAddress = etherWalletAddress;
	}

	public Integer getReferralTransferStatus() {
		return referralTransferStatus;
	}

	public void setReferralTransferStatus(Integer referralTransferStatus) {
		this.referralTransferStatus = referralTransferStatus;
	}




}






