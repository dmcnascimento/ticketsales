package br.com.ufs.ds3.util;

import java.util.Date;

public class DateUtil {
	public static boolean lessOrEq(Date date1, Date date2) {
		return date1.before(date2) || date1.equals(date2);
	}
	
	public static boolean greaterOrEq(Date date1, Date date2) {
		return date1.after(date2) || date1.equals(date2);
	}
}
