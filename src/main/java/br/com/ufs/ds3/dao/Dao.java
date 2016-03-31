package br.com.ufs.ds3.dao;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;

public abstract class Dao<T> {
	protected DB db;
	
	public Dao() {
		this.db = new DB();
	}
	
	public Dao(DB db) {
		this.db = db;
	}
	
	public void persist(T object) {
		EntityManager entityManager = db.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(object);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public T update(T object) {
		EntityManager entityManager = db.createEntityManager();
		entityManager.getTransaction().begin();
		object = entityManager.merge(object);
		entityManager.getTransaction().commit();
		entityManager.close();
		return object;
	}
	
	public void remove(T object) {
		EntityManager entityManager = db.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(object);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
