package br.com.ufs.ticketsales.session;

import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.Theatre;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.SessionService;

public class SessionServiceTest {
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionEvent() {
		SessionDao sessionDao = Mockito.mock(SessionDao.class);
		SessionService sessionService = new SessionService(sessionDao);
		Session session = new Session();
		session.setDay(new Date());
		session.setStartHour(new Date());
		session.setEndHour(new Date());
		session.setEvent(null);
		sessionService.persist(session);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionDay() {
		SessionDao sessionDao = Mockito.mock(SessionDao.class);
		SessionService sessionService = new SessionService(sessionDao);
		Session session = new Session();
		session.setDay(null);
		session.setStartHour(new Date());
		session.setEndHour(new Date());
		session.setEvent(Mockito.mock(Event.class));
		sessionService.persist(session);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionStartHour() {
		SessionDao sessionDao = Mockito.mock(SessionDao.class);
		SessionService sessionService = new SessionService(sessionDao);
		Session session = new Session();
		session.setDay(new Date());
		session.setStartHour(null);
		session.setEndHour(new Date());
		session.setEvent(Mockito.mock(Event.class));
		sessionService.persist(session);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionEndHour() {
		SessionDao sessionDao = Mockito.mock(SessionDao.class);
		SessionService sessionService = new SessionService(sessionDao);
		Session session = new Session();
		session.setDay(new Date());
		session.setStartHour(new Date());
		session.setEndHour(null);
		session.setEvent(Mockito.mock(Event.class));
		sessionService.persist(session);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionExistsAtDate() {
		Session session = new Session();
		session.setDay(new Date());
		session.setStartHour(new Date());
		session.setEndHour(new Date());
		
		Event mockEvent = Mockito.mock(Event.class);
		Mockito.when(mockEvent.getTheatre()).thenReturn(Mockito.mock(Theatre.class));
		session.setEvent(mockEvent);
		
		SessionDao sessionDao = Mockito.mock(SessionDao.class);
		Mockito.when(sessionDao.sessionExistsAtTheatre(mockEvent.getTheatre(), session.getDay(), session.getStartHour(), session.getEndHour())).thenReturn(true);
		
		SessionService sessionService = new SessionService(sessionDao);
		sessionService.persist(session);
	}
}
