package br.com.ufs.ds3.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PaidTicket extends Ticket {
	
	@Column(nullable = false, columnDefinition = "NUMERIC(10,2)")
	private BigDecimal paidValue;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private Sale sale;
	
	public BigDecimal getPaidValue() {
		return paidValue;
	}
	
	public void setPaidValue(BigDecimal paidValue) {
		this.paidValue = paidValue;
	}
	
	public Sale getSale() {
		return sale;
	}
	
	public void setSale(Sale sale) {
		this.sale = sale;
	}
}
