package br.com.ufs.ticketsales.fixtures;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.NumberedChairSession;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Rating;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.service.EventService;
import br.com.ufs.ds3.service.PriceService;

public class SessionServiceFixture {
	public void create() {
		createEventTeste();
		createEventTeste2();
	}

	private void createEventTeste() {
		TheatreDao theatreDao = new TheatreDao();
		Calendar calendar = Calendar.getInstance();
		
		Event event = new Event();
		event.setDescription("Teste Descrição");
		event.setDuration(120);
		event.setIntervalDuration(30);
		event.setRating(Rating.C16);
		event.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 2);
		event.setEndDate(calendar.getTime());
		event.setTitle("Teste");
		event.setTheatre(theatreDao.listTheatres().get(0));
		EventService eventService = new EventService();
		eventService.persist(event);
		
		Session session = new NumberedChairSession();
		session.setDay(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		session.setStartHour(calendar.getTime());
		session.setEvent(event);
		
		Price price1 = new Price();
		price1.setEvent(event);
		price1.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		price1.setEndHour(calendar.getTime());
		price1.setTicketPrice(new BigDecimal(1.0));
		
		Price price2 = new Price();
		price2.setEvent(event);
		price2.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		price2.setEndHour(calendar.getTime());
		price2.setTicketPrice(new BigDecimal(1.0));
		
		PriceService priceService = new PriceService();
		priceService.persist(price1);
		priceService.persist(price2);
		
		SessionDao sessionDao = new SessionDao();
		sessionDao.persist(session);
	}
	
	private void createEventTeste2() {
		TheatreDao theatreDao = new TheatreDao();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		
		Event event = new Event();
		event.setDescription("Teste Descrição");
		event.setDuration(120);
		event.setIntervalDuration(30);
		event.setRating(Rating.C16);
		event.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 2);
		event.setEndDate(calendar.getTime());
		event.setTitle("Teste 2");
		event.setTheatre(theatreDao.listTheatres().get(0));
		EventService eventService = new EventService();
		eventService.persist(event);
		
		Session session = new NumberedChairSession();
		session.setDay(new Date());
		session.setStartHour(calendar.getTime());
		session.setEvent(event);
		
		WeekDay weekDay = WeekDay.fromDate(event.getStartDate());
		for (WeekDay w : WeekDay.values()) {
			if (w != weekDay) {
				weekDay = w;
				break;
			}
		}
		
		Price price1 = new Price();
		price1.setEvent(event);
		price1.setWeekDay(weekDay);
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		price1.setEndHour(calendar.getTime());
		price1.setTicketPrice(new BigDecimal(1.0));
		
		Price price2 = new Price();
		price2.setEvent(event);
		price2.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		price2.setEndHour(calendar.getTime());
		price2.setTicketPrice(new BigDecimal(1.0));
		
		PriceService priceService = new PriceService();
		priceService.persist(price1);
		priceService.persist(price2);
		
		SessionDao sessionDao = new SessionDao();
		sessionDao.persist(session);
		
	}
}
