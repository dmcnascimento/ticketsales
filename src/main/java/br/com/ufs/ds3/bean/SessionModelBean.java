package br.com.ufs.ds3.bean;

import java.util.Date;

import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.SessionType;
import br.com.ufs.ds3.entity.WeekDay;

public class SessionModelBean {
	private Event event;
	private WeekDay day;
	private Date startHour;
	private SessionType sessionType;
	
	public SessionModelBean(Event event, WeekDay day, Date startHour, SessionType sessionType) {
		this.event = event;
		this.day = day;
		this.startHour = startHour;
		this.sessionType = sessionType;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public WeekDay getDay() {
		return day;
	}

	public void setDay(WeekDay day) {
		this.day = day;
	}

	public Date getStartHour() {
		return startHour;
	}

	public void setStartHour(Date startHour) {
		this.startHour = startHour;
	}

	public SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}
}
