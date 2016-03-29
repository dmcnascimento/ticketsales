package br.com.ufs.ds3.entity;

import java.util.Calendar;
import java.util.Date;

public enum WeekDay {
	SUN("Domingo"), MON("Segunda"), TUE("Terça"), WED("Quarta"), THU("Quinta"), FRI("Sexta"), SAT("Sábado");
	
	private String label;
	
	private WeekDay(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	public static WeekDay fromDate(Date day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		int calendarWeekday = calendar.get(Calendar.DAY_OF_WEEK);
		switch (calendarWeekday) {
		case Calendar.SUNDAY:
			return SUN;
		case Calendar.MONDAY:
			return MON;
		case Calendar.TUESDAY:
			return TUE;
		case Calendar.WEDNESDAY:
			return WED;
		case Calendar.THURSDAY:
			return THU;
		case Calendar.FRIDAY:
			return FRI;
		case Calendar.SATURDAY:
			return SAT;
		}
		return null;
	}
}