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
@Table(name = "Transaction_History")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "from_address")
	@NotNull
	private String fromAddress;

	@Column(name = "to_address")
	@NotNull
	private String toAddress;

	@Column(name = "amount")
	@NotNull
	private Double amount;

	@Column(name = "created_Date")
	private Date createdDate;
	
	@Column(name="transfer_Mode")
	@NotNull
	private String transferMode;
	
//	@Column(name = "bitcoin_transfered_amount")
//	@NotNull
//	private Double bitcoinTransferedAmount;
//	
//	@Column(name = "ether_transfered_amount")
//	@NotNull
//	private Double etherTransferedAmount;
//	
	@Column(name = "isTransfer_Status")
	@NotNull
	private boolean isTransferStatus;
	
	
	
//	@Column(name = "request_id")
//	@NotNull
//	private Integer requestId;
	
	public boolean isTransferStatus() {
		return isTransferStatus;
	}

	public void setTransferStatus(boolean isTransferStatus) {
		this.isTransferStatus = isTransferStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

//	public Double getBitcoinTransferedAmount() {
//		return bitcoinTransferedAmount;
//	}
//
//	public void setBitcoinTransferedAmount(Double bitcoinTransferedAmount) {
//		this.bitcoinTransferedAmount = bitcoinTransferedAmount;
//	}
//
//	public Double getEtherTransferedAmount() {
//		return etherTransferedAmount;
//	}
//
//	public void setEtherTransferedAmount(Double etherTransferedAmount) {
//		this.etherTransferedAmount = etherTransferedAmount;
//	}
//
//	public Integer getRequestId() {
//		return requestId;
//	}
//
//	public void setRequestId(Integer requestId) {
//		this.requestId = requestId;
//	}

}

