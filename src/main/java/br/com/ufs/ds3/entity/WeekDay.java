package br.com.ufs.ds3.entity;

import java.util.Calendar;
import java.util.Date;

public enum WeekDay {
	SUN, MON, TUE, WED, THU, FRI, SAT;
	
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
