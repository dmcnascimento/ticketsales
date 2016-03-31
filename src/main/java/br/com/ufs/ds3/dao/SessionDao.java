package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Session;

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
}
