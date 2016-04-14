package br.com.ufs.ticketsales.fixtures;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Rating;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.service.EventService;
import br.com.ufs.ds3.service.PriceService;

public class PriceServiceFixture {
	public void create() {
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
		
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		Price price1 = new Price();
		price1.setEvent(event);
		price1.setWeekDay(WeekDay.FRI);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price1.setEndHour(calendar.getTime());
		price1.setTicketPrice(new BigDecimal(1d));
		
		Price price2 = new Price();
		price2.setEvent(event);
		price2.setWeekDay(WeekDay.MON);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		price2.setEndHour(calendar.getTime());
		price2.setTicketPrice(new BigDecimal(1d));
		
		PriceService priceService = new PriceService();
		priceService.persist(price1);
		priceService.persist(price2);
	}
}
