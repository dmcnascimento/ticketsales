package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Theatre;

public class TheatreDao {
	private DB db;
	
	public TheatreDao() {
		this.db = new DB();
	}
	
	public TheatreDao(DB db) {
		this.db = db;
	}
	
	public List<Theatre> listTheatres() {
		EntityManager entityManager = db.createEntityManager();
		List<Theatre> theatres = entityManager.createQuery("select o from Theatre o order by o.name", Theatre.class)
				.getResultList();
		entityManager.close();
		return theatres;
	}
}
