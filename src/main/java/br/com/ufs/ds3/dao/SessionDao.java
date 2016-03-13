package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;

public class SessionDao {
	public void persist(Session session) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(session);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public Session update(Session session) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		session = entityManager.merge(session);
		entityManager.getTransaction().commit();
		entityManager.close();
		return session;
	}
	
	public void remove(Session session) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(session);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public List<Session> listSessions() {
		EntityManager entityManager = DB.createEntityManager();
		List<Session> sessions = entityManager.createQuery("select o from Session o order by o.day, o.event.title", Session.class)
				.getResultList();
		entityManager.close();
		return sessions;
	}
	
	public List<Session> listSessionsFromEvent(Event event) {
		EntityManager entityManager = DB.createEntityManager();
		List<Session> sessions = entityManager.createQuery("select o from Session o where o.event = :event order by o.day, o.startHour", Session.class)
				.setParameter("event", event).getResultList();
		entityManager.close();
		return sessions;
	}
}
