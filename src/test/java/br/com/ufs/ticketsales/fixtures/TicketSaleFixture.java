package br.com.ufs.ticketsales.fixtures;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.ufs.ds3.bean.SessionModelBean;
import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Rating;
import br.com.ufs.ds3.entity.SessionType;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.service.EventService;
import br.com.ufs.ds3.service.SessionService;

public class TicketSaleFixture {
	public void create() {
		TheatreDao theatreDao = new TheatreDao();
		
		Event event = new Event();
		event.setDescription("Teste Descrição");
		event.setDuration(120);
		event.setIntervalDuration(30);
		event.setRating(Rating.C16);
		Calendar calendar = Calendar.getInstance();
		event.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 2);
		event.setEndDate(calendar.getTime());
		event.setTitle("Teste");
		event.setTheatre(theatreDao.listTheatres().get(0));
		EventService eventService = new EventService();
		eventService.persist(event);
		
		Calendar startHour = Calendar.getInstance();
		startHour.set(1970, 0, 1, 15, 0, 0);
		SessionModelBean sessionModelBean = new SessionModelBean(event, WeekDay.FRI, startHour.getTime(), SessionType.NUMBERED_CHAIR);
		SessionService sessionService = new SessionService();
		sessionService.createSessions(sessionModelBean);
		
		Price price = new Price();
		price.setEvent(event);
		price.setStartHour(startHour.getTime());
		Calendar endHour = Calendar.getInstance();
		endHour.set(1970, 0, 1, 18, 0, 0);
		price.setEndHour(endHour.getTime());
		price.setTicketPrice(new BigDecimal(7.5d));
		price.setWeekDay(WeekDay.FRI);
		PriceDao priceDao = new PriceDao();
		priceDao.persist(price);
	}
}
