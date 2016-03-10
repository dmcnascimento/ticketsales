package br.com.ufs.ticketsales.theatre;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Theatre;

public class TheatreTest {
	
	@Test
	public void testAtLeastOneTheatreExists() {
		TheatreDao theatreDao = new TheatreDao();
		List<Theatre> theatres = theatreDao.listTheatres();
		Assert.assertFalse(theatres.isEmpty());
	}
}
