package br.com.ufs.ds3.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Price {
	private Integer id;
	private WeekDay weekDay;
	private Date start;
	private BigDecimal ticketPrice;
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
