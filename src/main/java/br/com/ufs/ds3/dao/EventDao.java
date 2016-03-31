package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;

public class EventDao extends Dao<Event> {
	public EventDao() {
	}
	
	public EventDao(DB db) {
		super(db);
	}
	
	public List<Session> getSessionsFromEvent(Event event) {
		EntityManager entityManager = db.createEntityManager();
		List<Session> sessions = entityManager.createQuery("select o from Session o where o.event = :event", Session.class)
				.setParameter("event", event).getResultList();
		entityManager.close();
		return sessions;
	}
	
	public List<Price> getPricesForEvent(Event event) {
		EntityManager entityManager = db.createEntityManager();
		List<Price> prices = entityManager.createQuery("select o from Price o where o.event = :event", Price	.class)
				.setParameter("event", event).getResultList();
		entityManager.close();
		return prices;
	}
}
