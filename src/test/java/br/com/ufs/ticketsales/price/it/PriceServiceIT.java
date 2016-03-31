package br.com.ufs.ticketsales.price.it;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.PriceService;

public class PriceServiceIT {

	private PriceService priceService;
	
	@Before
	public void init() {
		PriceDao priceDao = Mockito.mock(PriceDao.class);
		this.priceService = new PriceService(priceDao);
	}
	
	@Test(expected = TicketSalesException.class)
	public void checkPriceAlreadyExists() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		Price price = new Price();
		price.setEvent(new Event());
		price.setWeekDay(WeekDay.FRI);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		price.setEndHour(calendar.getTime());
		price.setTicketPrice(new BigDecimal(1d));
		
		Price price1 = new Price();
		price1.setEvent(new Event());
		price1.setWeekDay(WeekDay.FRI);
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		price1.setEndHour(calendar.getTime());
		price1.setTicketPrice(new BigDecimal(1d));
		
		Price price2 = new Price();
		price2.setEvent(new Event());
		price2.setWeekDay(WeekDay.MON);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		price2.setEndHour(calendar.getTime());
		price2.setTicketPrice(new BigDecimal(1d));
		
		price.getEvent().getPrices().add(price1);
		price.getEvent().getPrices().add(price2);
		
		priceService.checkPriceAlreadyExists(price);
	}
	
	@Test
	public void checkPriceNoConflict() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		Price price = new Price();
		price.setEvent(new Event());
		price.setWeekDay(WeekDay.FRI);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		price.setEndHour(calendar.getTime());
		price.setTicketPrice(new BigDecimal(1d));
		
		Price price1 = new Price();
		price1.setEvent(new Event());
		price1.setWeekDay(WeekDay.FRI);
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		price1.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 15);
		price1.setEndHour(calendar.getTime());
		price1.setTicketPrice(new BigDecimal(1d));
		
		Price price2 = new Price();
		price2.setEvent(new Event());
		price2.setWeekDay(WeekDay.MON);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price2.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		price2.setEndHour(calendar.getTime());
		price2.setTicketPrice(new BigDecimal(1d));
		
		price.getEvent().getPrices().add(price1);
		price.getEvent().getPrices().add(price2);
		
		priceService.checkPriceAlreadyExists(price);
	}
}
