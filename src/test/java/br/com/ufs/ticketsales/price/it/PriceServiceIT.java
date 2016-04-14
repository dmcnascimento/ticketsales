package br.com.ufs.ticketsales.price.it;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.PriceService;
import br.com.ufs.ticketsales.fixtures.PriceServiceFixture;

public class PriceServiceIT {
	
	@Test//(expected = TicketSalesException.class)
	public void checkPriceAlreadyExists() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		EventDao eventDao = new EventDao();
		Event event = eventDao.getEventWithTitle("Teste");
		
		Price price = new Price();
		price.setEvent(event);
		price.setWeekDay(WeekDay.FRI);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		price.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		price.setEndHour(calendar.getTime());
		price.setTicketPrice(new BigDecimal(1d));
		
		PriceService priceService = new PriceService();
		priceService.checkPriceAlreadyExists(price);
	}
	
	@Test
	public void checkPriceNoConflict() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		EventDao eventDao = new EventDao();
		Event event = eventDao.getEventWithTitle("Teste");
		
		Price price = new Price();
		price.setEvent(event);
		price.setWeekDay(WeekDay.MON);
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		price.setStartHour(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		price.setEndHour(calendar.getTime());
		price.setTicketPrice(new BigDecimal(1d));
		
		PriceService priceService = new PriceService();
		priceService.checkPriceAlreadyExists(price);
	}
	
	@BeforeClass
	public static void setupTestPersistenceUnit() {
		DB.setPersistenceUnit("ticketsalesTestPU");
		new PriceServiceFixture().create();
	}
}
