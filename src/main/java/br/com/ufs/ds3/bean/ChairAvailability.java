package br.com.ufs.ds3.bean;

import br.com.ufs.ds3.entity.Chair;

public class ChairAvailability {
	public enum ChairStatus {
		FREE, OCCUPIED, RESERVED;
	}
	
	private Chair chair;
	private ChairStatus chairStatus;
	
	public Chair getChair() {
		return chair;
	}
	
	public void setChair(Chair chair) {
		this.chair = chair;
	}
	
	public ChairStatus getChairStatus() {
		return chairStatus;
	}
	
	public void setChairStatus(ChairStatus chairStatus) {
		this.chairStatus = chairStatus;
	}
}
