package br.com.ufs.ds3.service;

import java.util.Comparator;
import java.util.List;

import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;

public class SessionService {
	private SessionDao sessionDao;
	private PriceDao priceDao;
	
	public SessionService() {
		this.sessionDao = new SessionDao();
		this.priceDao = new PriceDao();
	}
	
	public SessionService(SessionDao sessionDao, PriceDao priceDao) {
		this.sessionDao = sessionDao;
		this.priceDao = priceDao;
	}
	
	public void persist(Session session) {
		if (session.getEvent() == null) {
			throw new TicketSalesException("O evento deve ser informado");
		}
		if (session.getDay() == null) {
			throw new TicketSalesException("A data da sessão deve ser informada");
		}
		if (session.getStartHour() == null) {
			throw new TicketSalesException("A hora de início da sessão deve ser informada");
		}
//		boolean sessionExists = sessionDao.sessionExistsAtTheatre(session.getEvent().getTheatre(), session.getDay(), session.getStartHour(), session.getEndHour());
//		if (sessionExists) {
//			throw new TicketSalesException("Uma sessão existente choca com o horário informado");
//		}
//		
		sessionDao.persist(session);
	}
	
	public Price getPriceForSession(Session session) {
		List<Price> eventPrices = priceDao.getPricesForEvent(session.getEvent());
		WeekDay weekDay = WeekDay.fromDate(session.getDay());
		return eventPrices.stream().filter(price -> price.getWeekDay() == weekDay)
			.filter(price -> price.getStartHour().before(session.getStartHour()) || price.getStartHour().equals(session.getStartHour()))
			.filter(price -> price.getEndHour().after(session.getStartHour()))
		.sorted(new Comparator<Price>() {
			@Override
			public int compare(Price o1, Price o2) {
				return o2.getStartHour().compareTo(o1.getStartHour());
			}
		}).findFirst().get();
	}
}
