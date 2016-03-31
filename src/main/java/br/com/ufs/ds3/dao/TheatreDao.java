package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
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
}
