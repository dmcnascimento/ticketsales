package br.com.ufs.ds3.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Theatre;
import br.com.ufs.ds3.entity.Ticket;

public class TicketDao {
	public List<Ticket> listTicketsFromTheatre(Theatre theatre) {
		EntityManager entityManager = DB.createEntityManager();
		List<Ticket> tickets = entityManager.createQuery("select o from Ticket o where o.chairSession.session.event.theatre = :theatre order by o.title", Ticket.class)
			.setParameter("theatre", theatre).getResultList();
		entityManager.close();
		return tickets;
	}
	
	public void persist(Ticket ticket) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(ticket);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public Ticket update(Ticket ticket) {
		EntityManager entityManager = DB.createEntityManager();
		entityManager.getTransaction().begin();
		ticket = entityManager.merge(ticket);
		entityManager.getTransaction().commit();
		entityManager.close();
		return ticket;
	}
}
