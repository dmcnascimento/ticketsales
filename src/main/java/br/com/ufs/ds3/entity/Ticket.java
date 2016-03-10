package br.com.ufs.ds3.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Ticket {
	
	@Id
	@Column
	private Integer id;
	
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private ChairSession chairSession;
	
	@Column(nullable = false)
	private Integer number;
	
	@Column(nullable = false, columnDefinition = "NUMERIC(10,2)")
	private BigDecimal paidValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public BigDecimal getPaidValue() {
		return paidValue;
	}

	public void setPaidValue(BigDecimal paidValue) {
		this.paidValue = paidValue;
	}
	
	public ChairSession getChairSession() {
		return chairSession;
	}
	
	public void setChairSession(ChairSession chairSession) {
		this.chairSession = chairSession;
	}
}
