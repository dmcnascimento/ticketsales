package br.com.ufs.ds3.service;

import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.exception.TicketSalesException;

public class SessionService {
	private SessionDao sessionDao;
	
	public SessionService() {
		this.sessionDao = new SessionDao();
	}
	
	public SessionService(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}
	
	public void persist(Session session) {
		if (session.getEvent() == null) {
			throw new TicketSalesException("O evento deve ser informado");
		}
		if (session.getDay() == null) {
			throw new TicketSalesException("A data da sessão deve ser informada");
		}
		if (session.getStartHour() == null) {
			throw new TicketSalesException("A hora de início da sessão deve ser informada");
		}
		if (session.getEndHour() == null) {
			throw new TicketSalesException("A hora de fim da sessão deve ser informada");
		}
		boolean sessionExists = sessionDao.sessionExistsAtTheatre(session.getEvent().getTheatre(), session.getDay(), session.getStartHour(), session.getEndHour());
		if (sessionExists) {
			throw new TicketSalesException("Uma sessão existente choca com o horário informado");
		}
		
		sessionDao.persist(session);
	}
}
