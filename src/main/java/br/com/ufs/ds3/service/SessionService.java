package br.com.ufs.ds3.service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;

import br.com.ufs.ds3.bean.ChairAvailability;
import br.com.ufs.ds3.bean.ChairAvailability.ChairStatus;
import br.com.ufs.ds3.bean.OccupationMap;
import br.com.ufs.ds3.bean.OccupationMap.OccupationMapBuilder;
import br.com.ufs.ds3.bean.PhysicalMap;
import br.com.ufs.ds3.bean.SessionModelBean;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.ChairCondition;
import br.com.ufs.ds3.entity.FreeChairSession;
import br.com.ufs.ds3.entity.NumberedChairSession;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.SessionType;
import br.com.ufs.ds3.entity.TicketType;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.util.DateUtil;

public class SessionService {
	private SessionDao sessionDao;
	private TheatreService theatreService;
	
	public SessionService() {
		this.sessionDao = new SessionDao();
		this.theatreService = new TheatreService();
	}
	
	public SessionService(SessionDao sessionDao, TheatreService theatreService) {
		this.sessionDao = sessionDao;
		this.theatreService = theatreService;
	}

	public void createSessions(SessionModelBean sessionModelBean) {
		if (sessionModelBean.getEvent() == null) {
			throw new TicketSalesException("O evento deve ser informado");
		}
		if (sessionModelBean.getDay() == null) {
			throw new TicketSalesException("O dia da sessão deve ser informado");
		}
		if (sessionModelBean.getStartHour() == null) {
			throw new TicketSalesException("A hora de início da sessão deve ser informada");
		}
		if (sessionModelBean.getSessionType() == null) {
			throw new TicketSalesException("O tipo da sessão deve ser informado");
		}
		
		Date sessionDate;
		if (WeekDay.fromDate(sessionModelBean.getEvent().getStartDate()) == sessionModelBean.getDay()) {
			sessionDate = sessionModelBean.getEvent().getStartDate();
		} else {
			sessionDate = findNextDate(sessionModelBean.getEvent().getStartDate(), sessionModelBean.getDay());
		}
		while (DateUtil.lessOrEq(sessionDate, sessionModelBean.getEvent().getEndDate())) {
			Session session;
			if (sessionModelBean.getSessionType() == SessionType.FREE_CHAIR) {
				session = new FreeChairSession();
			} else {
				session = new NumberedChairSession();
			}
			
			session.setEvent(sessionModelBean.getEvent());
			session.setDay(sessionDate);
			session.setStartHour(sessionModelBean.getStartHour());
			sessionDao.persist(session);
			
			sessionDate = findNextDate(sessionDate, sessionModelBean.getDay());
		}
	}
	
	private Date findNextDate(Date start, WeekDay day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		while (WeekDay.fromDate(calendar.getTime()) != day) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return calendar.getTime();
	}

	public Price getPriceForSession(Session session) {
		WeekDay weekDay = WeekDay.fromDate(session.getDay());
		try {
			return session.getEvent().getPrices().stream().filter(price -> price.getWeekDay() == weekDay)
				.filter(price -> DateUtil.lessOrEq(price.getStartHour(), session.getStartHour()))
				.filter(price -> price.getEndHour().after(session.getStartHour()))
			.sorted(new Comparator<Price>() {
				@Override
				public int compare(Price o1, Price o2) {
					return o2.getStartHour().compareTo(o1.getStartHour());
				}
			}).findFirst().get();
		} catch (NoSuchElementException e) {
			throw new TicketSalesException("Não existe preço cadastrado para a sessão informada");
		}
	}
	
	public OccupationMap generateOccupationMap(NumberedChairSession session) {
		PhysicalMap physicalMap = theatreService.generatePhysicalMap(session.getEvent().getTheatre());
		OccupationMapBuilder builder = OccupationMapBuilder.fromSession(session);
		
		physicalMap.getChairs().stream().filter(chair -> chair.getChairCondition() == ChairCondition.NORMAL)
			.forEach(chair -> {
				ChairAvailability chairAvailability = new ChairAvailability();
				chairAvailability.setChair(chair);
				chairAvailability.setChairStatus(ChairStatus.FREE);
				builder.addChairAvailability(chairAvailability);
		});
		
		OccupationMap occupationMap = builder.build();
		
		session.getTickets().forEach(ticket -> {
			ChairAvailability chairAvailability = occupationMap.getChairAvailability(ticket.getChair());
			if (TicketType.fromClass(ticket.getClass()) == TicketType.PAID_TICKET) {
				chairAvailability.setChairStatus(ChairStatus.OCCUPIED);
			} else {
				chairAvailability.setChairStatus(ChairStatus.RESERVED);
			}
		});
		
		return occupationMap;
	}
}
