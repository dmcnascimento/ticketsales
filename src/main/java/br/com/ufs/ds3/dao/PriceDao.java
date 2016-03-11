package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.WeekDay;

public class PriceDao {
	public Price getPriceForSession(Session session) {
		EntityManager entityManager = DB.createEntityManager();
		List<Price> price = entityManager.createQuery("select o from Price o where o.event = :event and "
				+ "o.weekDay = :weekday and o.startHour <= :startHour order by o.startHour desc", Price.class)
			.setParameter("event", session.getEvent()).setParameter("weekday", WeekDay.fromDate(session.getDay()))
			.setParameter("startHour", session.getStartHour()).setMaxResults(1).getResultList();
		if (price.isEmpty()) {
			return null;
		}
		return price.get(0);
	}
}
