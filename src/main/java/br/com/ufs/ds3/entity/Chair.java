package br.com.ufs.ds3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Chair {
	
	@Id
	@SequenceGenerator(name = "ChairGenerator")
	@GeneratedValue(generator = "ChairGenerator", strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@Column(nullable = false)
	private String row;
	
	@Column(nullable = false)
	private Integer number;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Theatre theatre;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChairCondition chairCondition;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getRow() {
		return row;
	}
	
	public void setRow(String row) {
		this.row = row;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Theatre getTheatre() {
		return theatre;
	}
	
	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public ChairCondition getChairCondition() {
		return chairCondition;
	}

	public void setChairCondition(ChairCondition chairCondition) {
		this.chairCondition = chairCondition;
	}
	
	@Override
	public String toString() {
		return row + " - " + number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Chair))
			return false;
		Chair other = (Chair) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
