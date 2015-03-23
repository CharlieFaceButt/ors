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
	 * ����currentDate�������ã�����view������
	 * @param date
	 * @return currentDate�Ƿ�䶯��
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
	 * @param date ��ʼ��ѯ����
	 * @param rangeFlag
	 * <br> - TIME_FIRST_DATE ������ݵ�һ�������
	 * <br> - TIME_LAST_DATE ����������һ�������
	 * <br> - RECORD_OF_DATE ���һ�����ڵļ�¼
	 * <br> - RECORD_OF_WEEK ���һ�����ڵļ�¼
	 * <br> - RECORD_OF_MONTH ���һ���µļ�¼
	 * <br> - RECORD_OF_YEAR ���һ��ļ�¼
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
	 * ���ڱ������
	 * @param dayAmount �����������������ʾ��󣬸�����ʾ����ǰ
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
		JOptionPane.showMessageDialog(null, "���ѯ�����ڲ������ݷ�Χ��");
	}

	/**
	 * ���¿ؼ�datePicker��board������
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
