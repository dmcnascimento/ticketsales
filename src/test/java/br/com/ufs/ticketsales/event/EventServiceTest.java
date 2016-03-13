package br.com.ufs.ticketsales.event;

import org.junit.Test;
import org.mockito.Mockito;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Rating;
import br.com.ufs.ds3.entity.Theatre;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.service.EventService;

public class EventServiceTest {
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesEventDuration() {
		EventDao eventDao = Mockito.mock(EventDao.class);
		EventService eventService = new EventService(eventDao);
		
		Event event = new Event();
		event.setTheatre(Mockito.mock(Theatre.class));
		event.setDescription("Teste");
		event.setRating(Rating.C14);
		event.setTitle("Teste");
		event.setDuration(0);
		
		eventService.persist(event);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesEventTheatre() {
		EventDao eventDao = Mockito.mock(EventDao.class);
		EventService eventService = new EventService(eventDao);
		
		Event event = new Event();
		event.setTheatre(null);
		event.setDescription("Teste");
		event.setRating(Rating.C14);
		event.setTitle("Teste");
		event.setDuration(10);
		
		eventService.persist(event);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesEventDescription() {
		EventDao eventDao = Mockito.mock(EventDao.class);
		EventService eventService = new EventService(eventDao);
		
		Event event = new Event();
		event.setTheatre(Mockito.mock(Theatre.class));
		event.setDescription("");
		event.setRating(Rating.C14);
		event.setTitle("Teste");
		event.setDuration(10);
		
		eventService.persist(event);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesEventRating() {
		EventDao eventDao = Mockito.mock(EventDao.class);
		EventService eventService = new EventService(eventDao);
		
		Event event = new Event();
		event.setTheatre(Mockito.mock(Theatre.class));
		event.setDescription("Teste");
		event.setRating(null);
		event.setTitle("Teste");
		event.setDuration(10);
		
		eventService.persist(event);
	}
	
	@Test(expected = TicketSalesException.class)
	public void persistValidatesEventTitle() {
		EventDao eventDao = Mockito.mock(EventDao.class);
		EventService eventService = new EventService(eventDao);
		
		Event event = new Event();
		event.setTheatre(Mockito.mock(Theatre.class));
		event.setDescription("Teste");
		event.setRating(Rating.C14);
		event.setTitle("  ");
		event.setDuration(10);
		
		eventService.persist(event);
	}
}
