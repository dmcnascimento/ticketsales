package br.com.ufs.ds3.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class FreeChairSession extends Session {

	private Integer totalAvailableChairs;
	private Integer reservedChairs;

	public Integer getTotalAvailableChairs() {
		return totalAvailableChairs;
	}

	public void setTotalAvailableChairs(Integer totalAvailableChairs) {
		this.totalAvailableChairs = totalAvailableChairs;
	}

	public Integer getReservedChairs() {
		return reservedChairs;
	}

	public void setReservedChairs(Integer reservedChairs) {
		this.reservedChairs = reservedChairs;
	}
}
