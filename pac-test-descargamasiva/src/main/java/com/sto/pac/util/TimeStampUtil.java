package com.sto.pac.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimeStampUtil {
	private final static String TEMPLATE = "<u:Timestamp u:Id=\"_0\"><u:Created>2019-02-08T06:38:08.000Z</u:Created><u:Expires>2019-02-08T06:43:08.000Z</u:Expires></u:Timestamp>";
	private final static long MINUTES_INTERVAL = 5;
	
	private final static int endBlock01 = 34;
	private final static int initBlock02 = 53;
	private final static int endBlock02 = 81;
	private final static int initBlock03 = 100;
	
	public static String getTimeStamp() {
		OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime utcAdded = utc.plusMinutes(MINUTES_INTERVAL);
		
		int day = utc.getDayOfMonth();
		int month = utc.getMonthValue();
		int year = utc.getYear();
		int hour = utc.getHour();
		int minute = utc.getMinute();
		int second = utc.getSecond();
		
		int dayAdded = utcAdded.getDayOfMonth();
		int monthAdded = utcAdded.getMonthValue();
		int yearAdded = utcAdded.getYear();
		int hourAdded = utcAdded.getHour();
		int minuteAdded = utcAdded.getMinute();
		int secondAdded = utcAdded.getSecond();
				
		String templateTemp = TEMPLATE;
		int length = templateTemp.length();
		
		String firstBlock = templateTemp.substring(0, endBlock01);
		String secondBlock = templateTemp.substring(initBlock02, endBlock02);
		String thirdBlock = templateTemp.substring(initBlock03, length);
		
		templateTemp = firstBlock + build(year, month, day, hour, minute, second) + secondBlock + build(yearAdded, monthAdded, dayAdded, hourAdded, minuteAdded, secondAdded) + thirdBlock;
		return templateTemp;
	}
	
	private static String parse(int num, String texto) {
		if(num >= 0 && num <= 9)
			return texto + num;
		else
			return String.valueOf(num);
	}
	
	private static String build(int year, int month, int day, int hour, int minute, int second) {
		return year + "-" + parse(month, "0") + "-" + parse(day, "0") + "T" + parse(hour, "0") + ":" + parse(minute, "0") + ":" + parse(second, "0");
	}
}