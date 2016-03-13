package br.com.ufs.ds3.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.Theatre;

public class SessionDao {
	private DB db;
	
	public SessionDao() {
		this.db = new DB();
	}
	
	public SessionDao(DB db) {
		this.db = db;
	}
	
	public void persist(Session session) {
		EntityManager entityManager = db.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(session);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public Session update(Session session) {
		EntityManager entityManager = db.createEntityManager();
		entityManager.getTransaction().begin();
		session = entityManager.merge(session);
		entityManager.getTransaction().commit();
		entityManager.close();
		return session;
	}
	
	public void remove(Session session) {
		EntityManager entityManager = db.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(session);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public List<Session> listSessions() {
		EntityManager entityManager = db.createEntityManager();
		List<Session> sessions = entityManager.createQuery("select o from Session o order by o.day, o.event.title", Session.class)
				.getResultList();
		entityManager.close();
		return sessions;
	}
	
	public List<Session> listSessionsFromEvent(Event event) {
		EntityManager entityManager = db.createEntityManager();
		List<Session> sessions = entityManager.createQuery("select o from Session o where o.event = :event order by o.day, o.startHour", Session.class)
				.setParameter("event", event).getResultList();
		entityManager.close();
		return sessions;
	}
	
	public boolean sessionExistsAtTheatre(Theatre theatre, Date day, Date startHour, Date endHour) {
		EntityManager entityManager = db.createEntityManager();
		Calendar c = Calendar.getInstance();
		c.setTime(endHour);
		c.add(Calendar.MINUTE, 30);
		endHour = c.getTime();
		return !entityManager.createQuery("select 1 from Session o where o.event.theatre = :theatre and o.day = :day and "
				+ "((o.startHour between :startHour and :endHour) or (o.endHour between :startHour and :endHour))")
			.setParameter("theatre", theatre).setParameter("day", day).setParameter("startHour", startHour).setParameter("endHour", endHour)
			.setMaxResults(1).getResultList().isEmpty();
	}
}
