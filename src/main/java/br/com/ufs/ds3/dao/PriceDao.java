package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;

public class PriceDao {
	private DB db;
	
	public PriceDao() {
		this.db = new DB();
	}
	
	public PriceDao(DB db) {
		this.db = db;
	}
	
	public List<Price> getPricesForEvent(Event event) {
		EntityManager entityManager = db.createEntityManager();
		List<Price> prices = entityManager.createQuery("select o from Price o where o.event = :event", Price.class)
			.setParameter("event", event).getResultList();
		return prices;
	}
}
