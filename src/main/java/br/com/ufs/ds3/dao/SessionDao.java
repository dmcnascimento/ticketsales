package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.Ticket;

public class SessionDao extends Dao<Session> {
	public SessionDao() {
	}
	
	public SessionDao(DB db) {
		super(db);
	}
	
	public List<Session> listSessions() {
		EntityManager entityManager = db.createEntityManager();
		List<Session> sessions = entityManager.createQuery("select o from Session o order by o.day, o.event.title", Session.class)
				.getResultList();
		entityManager.close();
		return sessions;
	}
	
	public List<Ticket> listTickets(Session session) {
		EntityManager entityManager = db.createEntityManager();
		List<Ticket> tickets = entityManager.createQuery("select o from Ticket o where o.session = :session", Ticket.class)
				.setParameter("session", session).getResultList();
		entityManager.close();
		return tickets;
	}
}
