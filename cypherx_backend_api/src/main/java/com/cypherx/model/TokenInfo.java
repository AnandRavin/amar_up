package com.cypherx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@Entity
@Table(name = "Token_Info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "Available_tokens")
	@NotNull
	private Double availableTokens;
	
	@Column(name = "Sold_Tokens")
	@NotNull
	private Double soldTokens;
	
	@Column(name = "Burned_Tokens")
	@NotNull
	private Double burnedTokens;
	
	@Column(name = "Freezed_Tokens")
	@NotNull
	private Double freezedTokens;
	
	@Column(name = "cypherx_coin_rate")
	@NotNull
	private Double cypherxRate;
	
	@Column(name= "total_tokens")
	@NotNull
	private Double totalTokens;

	public Double getTotalTokens() {
		return totalTokens;
	}

	public void setTotalTokens(Double totalTokens) {
		this.totalTokens = totalTokens;
	}

	public Double getTtCoinRate() {
		return cypherxRate;
	}

	public void setTtCoinRate(Double ttCoinRate) {
		this.cypherxRate = ttCoinRate;
	}

	public Double getFreezedTokens() {
		return freezedTokens;
	}

	public void setFreezedTokens(Double freezedTokens) {
		this.freezedTokens = freezedTokens;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAvailableTokens() {
		return availableTokens;
	}

	public void setAvailableTokens(Double availableTokens) {
		this.availableTokens = availableTokens;
	}

	public Double getSoldTokens() {
		return soldTokens;
	}

	public void setSoldTokens(Double soldTokens) {
		this.soldTokens = soldTokens;
	}

	public Double getBurnedTokens() {
		return burnedTokens;
	}

	public void setBurnedTokens(Double burnedTokens) {
		this.burnedTokens = burnedTokens;
	}
	
}

