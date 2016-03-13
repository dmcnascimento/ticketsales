package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.ChairCondition;
import br.com.ufs.ds3.entity.Session;

public class ChairDao {
	private DB db;
	
	public ChairDao() {
		this.db = new DB();
	}
	
	public ChairDao(DB db) {
		this.db = db;
	}
	
	public List<Chair> listAvailableChairsForSession(Session session) {
		EntityManager entityManager = db.createEntityManager();
		List<Chair> chairs = entityManager.createQuery(
			"select o from Chair o "
			+ "where o.theatre = :theatre and o.chairCondition = :chairCondition "
			+ "and not exists(select 1 from ChairSession cs where cs.session = :session and cs.chair = o) "
			+ "order by o.row, o.number", Chair.class)
			.setParameter("theatre", session.getEvent().getTheatre())
			.setParameter("chairCondition", ChairCondition.NORMAL)
			.setParameter("session", session).getResultList();
		entityManager.close();
		return chairs;
	}
}
