package br.com.ufs.ds3.service;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.exception.TicketSalesException;

public class EventService {
	private EventDao eventDao;
	
	public EventService() {
		this.eventDao = new EventDao();
	}
	
	public EventService(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	
	public void persist(Event event) {
		validateEvent(event);
		eventDao.persist(event);
	}
	
	public Event update(Event event) {
		validateEvent(event);
		return eventDao.update(event);
	}

	private void validateEvent(Event event) {
		if (event.getDuration() == null || event.getDuration() <= 0) {
			throw new TicketSalesException("A duração do evento deve ser maior que 0");
		}
		if (event.getDescription() == null || event.getDescription().trim().equals("")) {
			throw new TicketSalesException("A descrição do evento não pode estar vazia");
		}
		if (event.getTitle() == null || event.getTitle().trim().equals("")) {
			throw new TicketSalesException("O título do evento não pode estar vazio");
		}
		if (event.getRating() == null) {
			throw new TicketSalesException("A classificação do evento deve ser informada");
		}
		if (event.getTheatre() == null) {
			throw new TicketSalesException("O teatro do evento deve ser informado");
		}
	}
}
