package br.com.ufs.ds3.entity;

public class Ticket {
	private Integer id;
	private Session session;
	private Chair chair;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Chair getChair() {
		return chair;
	}

	public void setChair(Chair chair) {
		this.chair = chair;
	}
}
