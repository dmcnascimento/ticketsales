package br.com.ufs.ds3.bean;

import java.util.ArrayList;
import java.util.List;

import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.Theatre;

public class PhysicalMap {
	private Theatre theatre;
	private List<Chair> chairs = new ArrayList<>();
	
	public PhysicalMap(Theatre theatre) {
		this.theatre = theatre;
	}
	
	public Theatre getTheatre() {
		return theatre;
	}
	
	public List<Chair> getChairs() {
		return chairs;
	}
}
