package br.com.ufs.ds3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class ChairSession {
	
	@Id
	@Column
	private Integer id;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Chair chair;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Session session;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChairStatus chairStatus;

	public Chair getChair() {
		return chair;
	}

	public void setChair(Chair chair) {
		this.chair = chair;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ChairStatus getChairStatus() {
		return chairStatus;
	}

	public void setChairStatus(ChairStatus chairStatus) {
		this.chairStatus = chairStatus;
	}
}
