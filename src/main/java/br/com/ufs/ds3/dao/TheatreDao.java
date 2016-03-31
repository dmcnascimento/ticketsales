package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Theatre;

public class TheatreDao extends Dao<Theatre> {
	public TheatreDao() {
	}
	
	public TheatreDao(DB db) {
		super(db);
	}
	
	public List<Theatre> listTheatres() {
		EntityManager entityManager = db.createEntityManager();
		List<Theatre> theatres = entityManager.createQuery("select o from Theatre o order by o.name", Theatre.class)
				.getResultList();
		entityManager.close();
		return theatres;
	}
	
	public List<Event> listEventsFromTheatre(Theatre theatre) {
		EntityManager entityManager = db.createEntityManager();
		List<Event> events = entityManager.createQuery("select o from Event o where o.theatre = :theatre", Event.class)
				.setParameter("theatre", theatre).getResultList();
		entityManager.close();
		return events;
	}
	
	public List<Chair> listChairsFromTheatre(Theatre theatre) {
		EntityManager entityManager = db.createEntityManager();
		List<Chair> chairs = entityManager.createQuery("select o from Chair o where o.theatre = :theatre", Chair.class)
				.setParameter("theatre", theatre).getResultList();
		entityManager.close();
		return chairs;
	}
}
