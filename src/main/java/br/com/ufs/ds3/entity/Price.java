package br.com.ufs.ds3.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table
public class Price {
	
	@Id
	@Column
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WeekDay weekDay;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date start;
	
	@Column(nullable = false, columnDefinition = "NUMERIC(10,2)")
	private BigDecimal ticketPrice;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private Event event;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public WeekDay getWeekDay() {
		return weekDay;
	}
	
	public void setWeekDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}
	
	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
}
