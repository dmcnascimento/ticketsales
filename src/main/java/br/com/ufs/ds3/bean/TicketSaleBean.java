package br.com.ufs.ds3.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.Session;

public class TicketSaleBean {
	private Session session;
	private BigDecimal basePrice;
	private List<TicketInfo> fullPriceTickets = new ArrayList<>();
	private List<TicketInfo> halfPriceTickets = new ArrayList<>();
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}

	public List<TicketInfo> getFullPriceTickets() {
		return fullPriceTickets;
	}

	public void setFullPriceTickets(List<TicketInfo> fullPriceTickets) {
		this.fullPriceTickets = fullPriceTickets;
	}

	public List<TicketInfo> getHalfPriceTickets() {
		return halfPriceTickets;
	}

	public void setHalfPriceTickets(List<TicketInfo> halfPriceTickets) {
		this.halfPriceTickets = halfPriceTickets;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	
	public static class TicketInfo {
		private Chair chair;
		
		public Chair getChair() {
			return chair;
		}
		
		public void setChair(Chair chair) {
			this.chair = chair;
		}
	}
}
