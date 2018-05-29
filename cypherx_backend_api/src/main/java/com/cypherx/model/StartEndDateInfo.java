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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Start_End_Date_Info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StartEndDateInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "start_Date")
	@NotNull
	private Date startDate;
	
	@Column(name = "end_Date")
	@NotNull
	private Date endDate;
	
	@Column(name = "maximum_value")
	@NotNull
	private BigInteger maximumValue;

	@Column(name = "minimum_value")
	@NotNull
	private BigInteger minimumValue;
	
	@Column(name = "total_Token")
	private BigInteger totalToken;
	
	@Column(name = "initial_Token")
	private BigInteger initialToken;
	
	@Column(name = "available_Tokens")
	private BigInteger availableTokens;
	
	@Column(name = "totalsold_Tokens")
	private BigInteger totalSoldTokens;
	
	@Column(name = "burned_Tokens")
	private BigInteger burnedTokens;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigInteger getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(BigInteger maximumValue) {
		this.maximumValue = maximumValue;
	}

	public BigInteger getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(BigInteger minimumValue) {
		this.minimumValue = minimumValue;
	}

	public BigInteger getTotalToken() {
		return totalToken;
	}

	public void setTotalToken(BigInteger totalToken) {
		this.totalToken = totalToken;
	}

	public BigInteger getInitialToken() {
		return initialToken;
	}

	public void setInitialToken(BigInteger initialToken) {
		this.initialToken = initialToken;
	}

	public BigInteger getAvailableTokens() {
		return availableTokens;
	}

	public void setAvailableTokens(BigInteger availableTokens) {
		this.availableTokens = availableTokens;
	}

	public BigInteger getTotalSoldTokens() {
		return totalSoldTokens;
	}

	public void setTotalSoldTokens(BigInteger totalSoldTokens) {
		this.totalSoldTokens = totalSoldTokens;
	}

	public BigInteger getBurnedTokens() {
		return burnedTokens;
	}

	public void setBurnedTokens(BigInteger burnedTokens) {
		this.burnedTokens = burnedTokens;
	}

}
