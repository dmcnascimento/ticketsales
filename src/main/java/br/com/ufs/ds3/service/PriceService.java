package br.com.ufs.ds3.service;

import java.util.function.Predicate;

import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.util.DateUtil;

public class PriceService {
	private PriceDao priceDao;
	
	public PriceService() {
		this.priceDao = new PriceDao();
	}
	
	public PriceService(PriceDao priceDao) {
		this.priceDao = priceDao;
	}
	
	public void persist(Price price) {
		if (price.getEvent() == null) {
			throw new TicketSalesException("O evento deve ser informado");
		}
		if (price.getWeekDay() == null) {
			throw new TicketSalesException("O dia deve ser informado");
		}
		if (price.getStartHour() == null) {
			throw new TicketSalesException("A hora inicial deve ser informada");
		}
		if (price.getEndHour() == null) {
			throw new TicketSalesException("A hora final deve ser informada");
		}
		if (price.getTicketPrice() == null) {
			throw new TicketSalesException("O valor deve ser informado");
		}
		if (price.getStartHour().after(price.getEndHour())) {
			throw new TicketSalesException("A hora inicial deve ser anterior à hora final");
		}
		checkPriceAlreadyExists(price);
		
		priceDao.persist(price);
	}

	public void checkPriceAlreadyExists(Price price) {
		Predicate<Price> predicate = p -> {
			if (price.getStartHour().equals(p.getStartHour())) {
				return true;
			}
			if (DateUtil.greaterOrEq(price.getEndHour(), p.getStartHour()) && DateUtil.lessOrEq(p.getEndHour(), price.getEndHour())) {
				return true;
			}
			return false;
		};
		if (price.getEvent().getPrices().stream().filter(p -> p.getWeekDay() == price.getWeekDay()).anyMatch(predicate)) {
			throw new TicketSalesException("Existe choque de horário entre os preços");
		}
	}
}
