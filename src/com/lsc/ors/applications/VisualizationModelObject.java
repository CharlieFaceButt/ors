package com.lsc.ors.applications;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.util.TimeFormatter;
import com.lsc.ors.views.widgets.DatePicker;

public abstract class VisualizationModelObject extends ModelObject {

	/**
	 * generated version ID
	 */
	private static final long serialVersionUID = 3270627961643152768L;

	protected static final int HEIGHT = 620;
	protected static final int WIDTH = 980;
	protected static final int MARGIN = 10;
	protected static final int BOARD_HEIGHT = 400;
	protected static final int BOARD_WIDTH = 600;
	protected static final int ANALYZER_WIDTH = 350;
	protected static final int ANALYZER_HEIGHT = 600;
	protected static final int BUTTON_HEIGHT = 30;
	protected static final int BUTTON_WIDTH = 100;
	
	//data
	Date currentDate = null;
	DatePicker datePicker = null;
	
	/**
	 * set current date and init datePicker
	 * @param listener
	 */
	public VisualizationModelObject(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setCurrentDate(firstDate);
		datePicker = new DatePicker(firstDate, lastDate, currentDate, new DatePickerListener());
	}

	/**
	 * 仅对currentDate进行设置，不对view做更新
	 * @param date
	 * @return currentDate是否变动了
	 */
	protected boolean setCurrentDate(Date date){
		if(date == null)
			return false;
		boolean changed = false;
		changed = !TimeFormatter.sameDay(currentDate, date);
		if(changed)
			currentDate = new Date(date.getTime());
		return changed;
	}
	
	/**
	 * 
	 * @param date 起始查询日期
	 * @param rangeFlag
	 * <br> - TIME_FIRST_DATE 获得数据第一天的日期
	 * <br> - TIME_LAST_DATE 获得数据最后一天的日期
	 * <br> - RECORD_OF_DATE 获得一个日期的记录
	 * <br> - RECORD_OF_WEEK 获得一个星期的记录
	 * <br> - RECORD_OF_MONTH 获得一个月的记录
	 * <br> - RECORD_OF_YEAR 获得一年的记录
	 * @return
	 */
	protected OutpatientLog[] getDataByDateRange(Date date, int rangeFlag){
		Object obj = null;
		//get target range start date
		if(date == null){
			obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_FIRST_DATE, null);
			if(obj == null) return null;
		}
		else obj = date;
		//get data by time range
		obj = OutpatientLogDBO.getData(rangeFlag, obj);
		if(obj == null) return null;
		else return (OutpatientLog[])obj;
	}
	

	/**
	 * 日期变更操作
	 * @param dayAmount 变更的天数，正数表示向后，负数表示向以前
	 */
	protected void increaseDate(int field, int amount){
		if(currentDate == null) return;
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(field, amount);
		Date newDate = cal.getTime();
		if(datePicker.withinRange(newDate)){
			currentDate.setTime(newDate.getTime());
			updateViewsData();
		} else {
			popDateNotWithinRangeAlert();
		}
	}
	
	protected void popDateNotWithinRangeAlert(){
		JOptionPane.showMessageDialog(null, "你查询的日期不在数据范围内");
	}

	/**
	 * 更新控件datePicker和board的数据
	 */
	protected abstract void updateViewsData();
	
	protected class DatePickerListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			onDatePickerChanged(e);
		}
	}
	
	protected abstract void onDatePickerChanged(ChangeEvent e);
}
