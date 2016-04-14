package br.com.ufs.ticketsales.session.it;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.SessionService;
import br.com.ufs.ticketsales.fixtures.SessionServiceFixture;

public class SessionServiceIT {

	@Test(expected = TicketSalesException.class)
	public void noPriceExistsForSession() {
		EventDao eventDao = new EventDao();
		Event event = eventDao.getEventWithTitle("Teste 2");
		Session session = eventDao.getSessionsFromEvent(event).get(0);
		SessionService sessionService = new SessionService();
		sessionService.getPriceForSession(session);
	}
	
	@Test
	public void findCorrectPriceForSession() {
		EventDao eventDao = new EventDao();
		Event event = eventDao.getEventWithTitle("Teste");
		Session session = eventDao.getSessionsFromEvent(event).get(0);
		SessionService sessionService = new SessionService();
		Price price = sessionService.getPriceForSession(session);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(price.getStartHour());
		Assert.assertEquals(11, calendar.get(Calendar.HOUR_OF_DAY));
	}
	
	@BeforeClass
	public static void setupTestPersistenceUnit() {
		DB.setPersistenceUnit("ticketsalesTestPU");
		new SessionServiceFixture().create();
	}
}
