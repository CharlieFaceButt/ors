package com.lsc.ors.applications;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.util.TimeFormatter;
import com.lsc.ors.views.QSVboard;
import com.lsc.ors.views.VisualizationBoard;
import com.lsc.ors.views.widgets.DatePicker;
import com.lsc.ors.views.widgets.TimeButtonGroup;

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
	VisualizationBoard board = null;
	DatePicker datePicker = null;
	TimeButtonGroup timeBtns = null;
	
	//views
	JPanel displayer = null;
	JPanel analyzer = null;

	//listener
	MultipleOnClickListener mocl = new MultipleOnClickListener();
	/**
	 * set current date and init datePicker
	 * @param listener
	 */
	public VisualizationModelObject(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setCurrentDate(firstDate);

		setLayout(null);
		displayer = new JPanel(new BorderLayout());
		analyzer = new JPanel(null);
		datePicker = new DatePicker(firstDate, lastDate, currentDate, new DatePickerListener());

		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		displayer.setBounds(MARGIN, MARGIN, BOARD_WIDTH, BOARD_HEIGHT);	//top left
		analyzer.setBounds(MARGIN * 2 + BOARD_WIDTH, MARGIN, ANALYZER_WIDTH, ANALYZER_HEIGHT);	//right
		datePicker.setBounds(MARGIN, MARGIN * 2 + BOARD_HEIGHT, BOARD_WIDTH, 80);	//bottom left
		timeBtns = new TimeButtonGroup(MARGIN * 2 + BOARD_HEIGHT + 80, MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT, mocl);

		add(displayer);
		add(datePicker);
		add(analyzer);
		for(Component c : timeBtns.getAllComponents()){
			add(c);
		}
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
	 * 根据时间获得数据集合
	 * @param date
	 * @return
	 */
	protected OutpatientLog[] getDataByDate(Date date){
		if(board == null)
			return getDataByDateRange(date, StringSet.CMD_TIME_UNIT_DAY);
		return getDataByDateRange(date, board.getTimeUnitType());
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
			datePicker.setPickerDate(currentDate);
			board.setData(getDataByDate(currentDate));
			onDateChanged();
		} else {
			popDateNotWithinRangeAlert();
		}
	}
	
	protected void popDateNotWithinRangeAlert(){
		JOptionPane.showMessageDialog(null, "你查询的日期不在数据范围内");
	}

	/**
	 * 点击时间按钮们的响应
	 */
	protected abstract void onDateChanged();
	
	protected class DatePickerListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			if(datePicker == e.getSource()){
				ConsoleOutput.pop("VisualizationModelObject.DatePicker", "state changed");
				if(setCurrentDate(datePicker.getCurrentDate()))
					board.setData(getDataByDate(currentDate));
				onDatePickerChanged(e);
				return;
			}
		}
	}
	
	protected abstract void onDatePickerChanged(ChangeEvent e);
	

	class MultipleOnClickListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Integer msg = StringSet.getInstance().getCommandIndex(e.getActionCommand());
			switch (msg) {
			case StringSet.CMD_LAST_DAY:
				increaseDate(Calendar.DAY_OF_YEAR, -1);
				break;
			case StringSet.CMD_NEXT_DAY:
				increaseDate(Calendar.DAY_OF_YEAR, 1);
				break;
			case StringSet.CMD_LAST_WEEK:
				increaseDate(Calendar.DAY_OF_YEAR, -7);
				break;
			case StringSet.CMD_NEXT_WEEK:
				increaseDate(Calendar.DAY_OF_YEAR, 7);
				break;
			case StringSet.CMD_LAST_MONTH:
				increaseDate(Calendar.MONTH, -1);
				break;
			case StringSet.CMD_NEXT_MONTH:
				increaseDate(Calendar.MONTH, 1);
				break;
			case StringSet.CMD_LAST_YEAR:
				increaseDate(Calendar.YEAR, -1);
				break;
			case StringSet.CMD_NEXT_YEAR:
				increaseDate(Calendar.YEAR, 1);
				break;
			case StringSet.CMD_MOUSE_WHEEL:
				if(e.getID() == board.getID())
					onMouseWheelOnBoard((MouseWheelEvent)e.getSource());
				break;
			case StringSet.CMD_MOUSE_CLICK:
				if(e.getID() == board.getID())
					onMouseClickOnBoard(e.getSource());
				break;
			case StringSet.CMD_TIME_UNIT_DAY:
			case StringSet.CMD_TIME_UNIT_WEEK:
			case StringSet.CMD_TIME_UNIT_MONTH:
			case StringSet.CMD_TIME_UNIT_YEAR:
				board.setData(getDataByDateRange(currentDate, msg), msg);
				onTimeUnitChanged();
				break;
			default:
				break;
			}
		}
		
	}
	
	protected abstract void onMouseWheelOnBoard(MouseWheelEvent e);
	protected abstract void onMouseClickOnBoard(Object source);
	protected abstract void onTimeUnitChanged();

}
