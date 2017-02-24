package com.ec.log.all.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final DateFormat FORMAT_HOUR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String dateFormat(Date date) {
		return FORMAT.format(date);
	}
	
	public static String dateFormatHour(Date date) {
		if ( date == null ) return "";
		return FORMAT_HOUR.format(date);
	}
}
