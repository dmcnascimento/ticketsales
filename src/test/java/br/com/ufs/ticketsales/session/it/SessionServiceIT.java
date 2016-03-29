package br.com.ufs.ticketsales.session.it;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.NumberedChairSession;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.service.SessionService;

public class SessionServiceIT {

	private SessionService sessionService;
	private PriceDao priceDao;
	
	@Before
	public void init() {
		SessionDao sessionDao = Mockito.mock(SessionDao.class);
		this.priceDao = Mockito.mock(PriceDao.class);
		this.sessionService = new SessionService(sessionDao, priceDao);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void noPriceExistsForSession() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		Session session = new NumberedChairSession();
		session.setDay(new Date());
		session.setStartHour(calendar.getTime());
		session.setEvent(new Event());
		
		Price price1 = new Price();
		price1.setEvent(session.getEvent());
		price1.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		price1.setEndHour(calendar.getTime());
		
		Price price2 = new Price();
		price2.setEvent(session.getEvent());
		price2.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		price2.setEndHour(calendar.getTime());
		
		Mockito.when(priceDao.getPricesForEvent(session.getEvent())).thenReturn(Arrays.asList(price1, price2));
		
		sessionService.getPriceForSession(session);
	}
	
	@Test
	public void findCorrectPriceForSession() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		
		Session session = new NumberedChairSession();
		session.setDay(new Date());
		session.setStartHour(calendar.getTime());
		session.setEvent(new Event());
		
		Price price1 = new Price();
		price1.setEvent(session.getEvent());
		price1.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		price1.setEndHour(calendar.getTime());
		
		Price price2 = new Price();
		price2.setEvent(session.getEvent());
		price2.setWeekDay(WeekDay.fromDate(session.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		price2.setEndHour(calendar.getTime());
		
		Mockito.when(priceDao.getPricesForEvent(session.getEvent())).thenReturn(Arrays.asList(price1, price2));
		
		Price result = sessionService.getPriceForSession(session);
		Assert.assertSame(price1, result);
	}
}
