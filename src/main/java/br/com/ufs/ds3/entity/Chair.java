package br.com.ufs.ds3.entity;

public class Chair {
	private Integer id;
	private String row;
	private Integer number;
	private Theatre theatre;
	
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
}
