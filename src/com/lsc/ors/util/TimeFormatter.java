package com.lsc.ors.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lsc.ors.debug.ConsoleOutput;

public class TimeFormatter {
	
	private static final String timePattern = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 格式化时间字符串
	 * @param time
	 * @param pattern 如果为空则使用与数据库相同滴默认格式
	 * @return
	 */
	public static final String format(Date time, String pattern){
		String result = null;
		if(pattern == null) 
			pattern = timePattern;
		Format fm = new SimpleDateFormat(pattern);
		if(time != null)
			result = fm.format(time);
		return result;
	}
	
	public static Date deformat(String source, String pattern){
		Date date = null;
		if(pattern == null)
			pattern = timePattern;
		Format fm = new SimpleDateFormat(pattern);
		if(source != null)
			try {
				date = (Date)fm.parseObject(source);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return date;
	}
	
	/**
	 * Give comparison of the year, month and date between two date, regardless of different time.
	 * @param d1 Date	
	 * @param d2 Date
	 * @return true
	 */
	@SuppressWarnings("deprecation")
	public static final boolean sameDay(final Date d1,final Date d2){
		if(d1 == null || d2 == null){
			///debug info
			ConsoleOutput.pop("WaitingRecordViewer.sameDay", "日期输入为空");
			return false;
		}
		if(d1.getDate() == d2.getDate() &&
				d1.getMonth() == d2.getMonth() &&
				d1.getYear() == d2.getYear())
			return true;
		else return false;
	}
}
