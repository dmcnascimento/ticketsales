package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.Theatre;
import br.com.ufs.ds3.entity.Ticket;

public class TicketDao extends Dao<Ticket> {
	public TicketDao() {
	}
	
	public TicketDao(DB db) {
		super(db);
	}
	
	public List<Ticket> listTickets(Theatre theatre, Event event, Session session, Integer number) {
		EntityManager entityManager = db.createEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
		Root<Ticket> ticket = query.from(Ticket.class);
		Predicate where = cb.and();
		
		if (theatre != null) {
			where = cb.and(where, cb.equal(ticket.get("session").get("event").get("theatre"), theatre));
		}
		if (event != null) {
			where = cb.and(where, cb.equal(ticket.get("session").get("event"), event));
		}
		if (session != null) {
			where = cb.and(where, cb.equal(ticket.get("session"), session));
		}
		if (number != null) {
			where = cb.and(where, cb.equal(ticket.get("number"), number));
		}
		
		query.where(where);
		query.orderBy(cb.asc(ticket.get("session").get("day")), cb.asc(ticket.get("session").get("startHour")), cb.asc(ticket.get("session").get("event").get("title")));
		
		List<Ticket> tickets = entityManager.createQuery(query).getResultList();
		entityManager.close();
		return tickets;
	}
}
