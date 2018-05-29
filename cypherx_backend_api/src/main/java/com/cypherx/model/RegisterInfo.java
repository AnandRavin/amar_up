package com.cypherx.model;

import java.io.Serializable;
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
@Table(name = "Register_Info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class RegisterInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "User_Name")
	@NotNull
	private String userName;
	
	@Column(name = "Email_Id")
	@NotNull
	private String emailId;
	
	@Column(name = "Password")
	@NotNull
	private String password;
	
	@Column(name ="Role_Id")
	@NotNull
	private Integer roleId;
	
	@Column(name = "Ether_Wallet_Address")
	@NotNull
	private String etherWalletAddress;
	
	@Column(name="Bitcoin_Wallet_Address")
	@NotNull
	private String bitcoinWalletAddress;
	
	@Column(name = "ether_Wallet_Password")
	@NotNull
	private String etherWalletPassword;
	
	@Column(name = "bitcoin_Wallet_Password")
	@NotNull
	private String bitcoinWalletPassword;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "Activation")
	private Boolean activation;
	
//	@Column(name = "referenceId")
//	@NotNull
//	private Integer referenceId;
	
	
	

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

	public String getBitcoinWalletPassword() {
		return bitcoinWalletPassword;
	}

	public void setBitcoinWalletPassword(String bitcoinWalletPassword) {
		this.bitcoinWalletPassword = bitcoinWalletPassword;
	}

//	public Integer getReferenceId() {
//		return referenceId;
//	}
//
//	public void setReferenceId(Integer referenceId) {
//		this.referenceId = referenceId;
//	}
//	
	
	
	
}
