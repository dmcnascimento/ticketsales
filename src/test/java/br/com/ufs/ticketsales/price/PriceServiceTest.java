package br.com.ufs.ticketsales.price;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.PriceService;

public class PriceServiceTest {
	private PriceService priceService;
	private PriceDao priceDao;
	
	@Before
	public void init() {
		this.priceDao = Mockito.mock(PriceDao.class);
		this.priceService = new PriceService(priceDao, Mockito.mock(EventDao.class));
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesPriceEvent() {
		Price price = createPrice();
		price.setEvent(null);
		priceService.persist(price);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesPriceWeekDay() {
		Price price = createPrice();
		price.setWeekDay(null);
		priceService.persist(price);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesPriceStartHour() {
		Price price = createPrice();
		price.setStartHour(null);
		priceService.persist(price);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesPriceEndHour() {
		Price price = createPrice();
		price.setEndHour(null);
		priceService.persist(price);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesPriceValue() {
		Price price = createPrice();
		price.setTicketPrice(null);
		priceService.persist(price);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesPriceStartAndEndHour() {
		Price price = createPrice();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		price.setEndHour(calendar.getTime());
		priceService.persist(price);
	}
	
	@Test
	public void persistSuccessful() {
		Price price = createPrice();
		priceService.persist(price);
		Mockito.verify(priceDao).persist(price);
	}
	
	private Price createPrice() {
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
		return price;
	}
}
