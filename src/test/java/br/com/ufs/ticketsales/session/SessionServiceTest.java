package br.com.ufs.ticketsales.session;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.ufs.ds3.bean.SessionModelBean;
import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.SessionType;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.SessionService;
import br.com.ufs.ds3.service.TheatreService;

public class SessionServiceTest {
	
	private SessionService sessionService;
	private SessionDao sessionDao;
	
	@Before
	public void init() {
		this.sessionDao = Mockito.mock(SessionDao.class);
		TheatreService theatreService = Mockito.mock(TheatreService.class);
		this.sessionService = new SessionService(sessionDao, theatreService, Mockito.mock(EventDao.class));
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionEvent() {
		SessionModelBean sessionModelBean = new SessionModelBean(null, WeekDay.FRI, new Date(), SessionType.NUMBERED_CHAIR);
		sessionService.createSessions(sessionModelBean);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionDay() {
		SessionModelBean sessionModelBean = new SessionModelBean(new Event(), null, new Date(), SessionType.NUMBERED_CHAIR);
		sessionService.createSessions(sessionModelBean);
	}

	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionStartHour() {
		SessionModelBean sessionModelBean = new SessionModelBean(new Event(), WeekDay.FRI, null, SessionType.NUMBERED_CHAIR);
		sessionService.createSessions(sessionModelBean);
	}

	@Test(expected = TicketSalesException.class)
	public void persistValidatesSessionType() {
		SessionModelBean sessionModelBean = new SessionModelBean(new Event(), WeekDay.FRI, new Date(), null);
		sessionService.createSessions(sessionModelBean);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesEventWeekday() {
		Event event = new Event();
		Calendar calendar = Calendar.getInstance();
		event.setStartDate(calendar.getTime());
		event.setEndDate(calendar.getTime());
		WeekDay weekDay = WeekDay.fromDate(event.getStartDate());
		for (WeekDay w : WeekDay.values()) {
			if (w != weekDay) {
				weekDay = w;
				break;
			}
		}
		SessionModelBean sessionModelBean = new SessionModelBean(event, weekDay, new Date(), SessionType.FREE_CHAIR);
		sessionService.createSessions(sessionModelBean);
	}

	@Test
	public void persistSuccess() {
		Event event = new Event();
		Calendar calendar = Calendar.getInstance();
		event.setStartDate(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 8);
		event.setEndDate(calendar.getTime());
		SessionModelBean sessionModelBean = new SessionModelBean(event, WeekDay.fromDate(event.getStartDate()), new Date(), SessionType.FREE_CHAIR);
		sessionService.createSessions(sessionModelBean);
		Mockito.verify(sessionDao, Mockito.times(2)).persist(Mockito.any());
	}
}
