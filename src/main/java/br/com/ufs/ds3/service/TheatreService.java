package br.com.ufs.ds3.service;

import br.com.ufs.ds3.bean.PhysicalMap;
import br.com.ufs.ds3.entity.Theatre;

public class TheatreService {
	public PhysicalMap generatePhysicalMap(Theatre theatre) {
		PhysicalMap physicalMap = new PhysicalMap(theatre);
		physicalMap.getChairs().addAll(theatre.getChairs());
		return physicalMap;
	}
}
