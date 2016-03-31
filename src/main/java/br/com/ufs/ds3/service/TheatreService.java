package br.com.ufs.ds3.service;

import br.com.ufs.ds3.bean.PhysicalMap;
import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Theatre;

public class TheatreService {
	private TheatreDao theatreDao;
	
	public TheatreService() {
		this.theatreDao = new TheatreDao();
	}
	
	public TheatreService(TheatreDao theatreDao) {
		this.theatreDao = theatreDao;
	}
	
	public PhysicalMap generatePhysicalMap(Theatre theatre) {
		PhysicalMap physicalMap = new PhysicalMap(theatre);
		physicalMap.getChairs().addAll(theatreDao.listChairsFromTheatre(theatre));
		return physicalMap;
	}
}
