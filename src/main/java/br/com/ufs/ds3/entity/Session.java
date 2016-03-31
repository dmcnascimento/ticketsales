package br.com.ufs.ds3.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table
public abstract class Session {
	
	@Id
	@SequenceGenerator(name = "SessionGenerator")
	@GeneratedValue(generator = "SessionGenerator", strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date day;
	
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Date startHour;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Event event;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "session")
	private List<Ticket> tickets = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Date getStartHour() {
		return startHour;
	}

	public void setStartHour(Date startHour) {
		this.startHour = startHour;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}
	
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(day) + " - " + hourFormat.format(startHour);
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
		if (!(obj instanceof Session))
			return false;
		Session other = (Session) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
