package br.com.ufs.ds3.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.ufs.ds3.bean.TicketSaleBean;
import br.com.ufs.ds3.bean.TicketSaleBean.TicketInfo;
import br.com.ufs.ds3.dao.SaleDao;
import br.com.ufs.ds3.dao.TicketDao;
import br.com.ufs.ds3.entity.PaidTicket;
import br.com.ufs.ds3.entity.Sale;
import br.com.ufs.ds3.exception.TicketSalesException;

public class TicketService {
	private TicketDao ticketDao;
	private SaleDao saleDao;
	
	public TicketService() {
		this.ticketDao = new TicketDao();
		this.saleDao = new SaleDao();
	}
	
	public void sellTickets(TicketSaleBean ticketSaleBean) {
		if (ticketSaleBean.getFullPriceTickets().isEmpty() && ticketSaleBean.getHalfPriceTickets().isEmpty()) {
			throw new TicketSalesException("A quantidade de ingressos deve ser maior do que 0");
		}
		BigDecimal total = calculatePrice(ticketSaleBean.getBasePrice(), ticketSaleBean.getFullPriceTickets().size(), ticketSaleBean.getHalfPriceTickets().size());
		Sale sale = new Sale();
		sale.setSaleDate(new Date());
		sale.setSaleValue(total);
		sale.setSession(ticketSaleBean.getSession());
		saleDao.persist(sale);
		
		for (TicketInfo ticketInfo : ticketSaleBean.getFullPriceTickets()) {
			PaidTicket paidTicket = new PaidTicket();
			paidTicket.setSale(sale);
			paidTicket.setPaidValue(ticketSaleBean.getBasePrice());
			paidTicket.setChair(ticketInfo.getChair());
			paidTicket.setSession(ticketSaleBean.getSession());
			ticketDao.persist(paidTicket);
		}
		
		for (TicketInfo ticketInfo : ticketSaleBean.getHalfPriceTickets()) {
			PaidTicket paidTicket = new PaidTicket();
			paidTicket.setSale(sale);
			paidTicket.setPaidValue(ticketSaleBean.getBasePrice().divide(new BigDecimal(2)));
			paidTicket.setChair(ticketInfo.getChair());
			paidTicket.setSession(ticketSaleBean.getSession());
			ticketDao.persist(paidTicket);
		}
	}
	
	public BigDecimal calculatePrice(BigDecimal basePrice, int fullPriceTickets, int halfPriceTickets) {
		BigDecimal calculatedPrice = basePrice.multiply(new BigDecimal(fullPriceTickets));
		return calculatedPrice.add(basePrice.divide(new BigDecimal(2)).multiply(new BigDecimal(halfPriceTickets)));
	}
}
