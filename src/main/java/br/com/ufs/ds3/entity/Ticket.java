package br.com.ufs.ds3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public abstract class Ticket {
	
	@Id
	@SequenceGenerator(name = "TicketGenerator")
	@GeneratedValue(generator = "TicketGenerator", strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@Column(nullable = false)
	private Integer number;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(nullable = true)
	private Chair chair;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Session session;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

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
		if (!(obj instanceof Ticket))
			return false;
		Ticket other = (Ticket) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
