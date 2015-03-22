package com.lsc.ors.views.widgets;

import java.awt.HeadlessException;
import java.util.Date;

import javax.swing.event.ChangeListener;

public class TimePicker extends DatePicker{

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -8894985239369948843L;
	public TimePicker(Date date, 
			ChangeListener listener) throws HeadlessException {
		super(date, date, date, listener);
		// TODO Auto-generated constructor stub
	}
	public void setEarliestTime(Date date){
		beginDate = date;
	}
	public void getLatestTime(Date date){
		finishDate = date;
	}
	
	@Override
	public Long getID() {
		// TODO Auto-generated method stub
		return serialVersionUID;
	}
	
}
