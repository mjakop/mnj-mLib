package com.mjakop.lib.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SimpleDateTime {
		
	public static Calendar buildCalendar(int year, int month, int day){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, 0, 0, 0);
		return cal;
	}
	
	public static String formatDateShortLocale(Calendar cal) {
		String result = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(cal.getTimeInMillis()));
		return result;
	}
	
	public static String formatDateTimeLocale(Calendar cal){
		String result = DateFormat.getDateTimeInstance().format(new Date(cal.getTimeInMillis()));
		return result;
	}
	
	public static String formatDateTimeShortLocale(Calendar cal){
		String result = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(cal.getTimeInMillis()));
		return result;
	}
	
	public static String formatDateTime(Calendar cal){
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US).format(new Date(cal.getTimeInMillis()));
	}
	
	/**
	 * Returns diff in seconds
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long dateDiff(Calendar d1, Calendar d2) {
		long dt1 = d1.getTimeInMillis();
		long dt2 = d2.getTimeInMillis();
		long diff = 0;
		if (dt1 > dt2) {
			diff = dt1 - dt2;
		} else {
			diff = dt2 - dt1;
		}
		return (long)(diff / 1000.0);
	}
	
	public static String niceDuration(int durationInSeconds){
		StringBuilder buff = new StringBuilder();
		
		int days = durationInSeconds / (60*60*24);
		durationInSeconds = durationInSeconds - days*60*60*24;
		
		int hours = durationInSeconds / (60*60);
		durationInSeconds = durationInSeconds - hours*60*60;
		
		int min = durationInSeconds / 60;
		durationInSeconds = durationInSeconds - min*60;
		
		int sec = durationInSeconds;
		boolean prev = false;
		if (days > 0) {
			buff.append(days+" d");
			prev = true;
		}
		
		if (hours > 0) {
			if (prev){
				buff.append(" ");
			}
			buff.append(hours+" h");
			prev = true;
		} else {
			prev = false;
		}
		
		if (min > 0) {
			if (prev){
				buff.append(" ");
			}
			buff.append(min+" min");
			prev = true;
		}else {
			prev = false;
		}
		
		if (sec > 0) {
			if (prev){
				buff.append(" ");
			}
			buff.append(sec+" sec");
		}		
		return buff.toString();
	}
	
}
