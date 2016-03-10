package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Theatre;

public class EventDao {
	public List<Event> listEventsFromTheatre(Theatre theatre) {
		EntityManager entityManager = DB.createEntityManager();
		List<Event> events = entityManager.createQuery("select o from Event o where o.theatre = :theatre order by o.title", Event.class)
			.setParameter("theatre", theatre).getResultList();
		entityManager.close();
		return events;
	}
	
	public List<Event> listEvents() {
		EntityManager entityManager = DB.createEntityManager();
		List<Event> events = entityManager.createQuery("select o from Event o order by o.title, o.theatre.name", Event.class).getResultList();
		entityManager.close();
		return events;
	}
	
	public void persist(Event event) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(event);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public Event update(Event event) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		event = entityManager.merge(event);
		entityManager.getTransaction().commit();
		entityManager.close();
		return event;
	}
}
