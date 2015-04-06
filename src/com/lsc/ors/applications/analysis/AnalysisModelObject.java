package com.lsc.ors.applications.analysis;

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

import com.lsc.ors.applications.ModelObject;
import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.util.DataExtractor;
import com.lsc.ors.util.TimeFormatter;
import com.lsc.ors.views.analysis.AnalysisBoard;
import com.lsc.ors.views.widgets.DatePicker;
import com.lsc.ors.views.widgets.TimeButtonGroup;

public abstract class AnalysisModelObject extends ModelObject {

	/**
	 * generate serial id
	 */
	private static final long serialVersionUID = 2151840753012864239L;
	public int getID(){
		return (int)serialVersionUID;
	}
	protected static final int HEIGHT = 630;
	protected static final int WIDTH = 980;
	protected static final int MARGIN = 10;
	protected static final int BOARD_HEIGHT = 400;
	protected static final int BOARD_WIDTH = 600;
	protected static final int ANALYZER_WIDTH = 350;
	protected static final int ANALYZER_HEIGHT = 600;
	protected static final int BUTTON_HEIGHT = 30;
	protected static final int BUTTON_WIDTH = 100;
	protected static final int CHOOSER_HEIGHT = 20;
	protected static final int CHOOSER_WIDHT = 150;

	int timeUnitType = StringSet.CMD_TIME_UNIT_DAY;
	Date currentDate;
	protected AnalysisBoard board;
	DatePicker datePicker;
	TimeButtonGroup timeBtns;
	OutpatientLogCharacters[] dataList;

	//listener
	MultipleOnClickListener mocl = new MultipleOnClickListener();
	
	//views
	JPanel displayer = null;
	JPanel analyzer = null;
	
	public AnalysisModelObject(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setCurrentDate(firstDate);
		setDataByDateRange(currentDate, timeUnitType);
		
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
	
	protected class DatePickerListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			if(datePicker == e.getSource()){
				ConsoleOutput.pop("VisualizationModelObject.DatePicker", "state changed");
				if(setCurrentDate(datePicker.getCurrentDate())){
					setDataByDateRange(currentDate, timeUnitType);
					board.setData(dataList);
				}
				return;
			}
		}
	}
	
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
			case StringSet.CMD_MOUSE_MOVE:
				if(e.getID() == board.getID())
					onMouseMoveOnBoard(e.getSource());
				break;
			case StringSet.CMD_TIME_UNIT_DAY:
			case StringSet.CMD_TIME_UNIT_WEEK:
			case StringSet.CMD_TIME_UNIT_MONTH:
			case StringSet.CMD_TIME_UNIT_YEAR:
				setDataByDateRange(currentDate, msg);
				board.setData(dataList);
				break;
			case StringSet.CMD_TIME_UNIT_ALL:
				board.miningAllData();
				break;
			default:
				break;
			}
		}
		
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
	protected void setDataByDateRange(Date date, int rangeFlag){
		Object obj = null;
		//get target range start date
		if(date == null){
			obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_FIRST_DATE, null);
			if(obj == null) return;
		}
		else obj = date;
		//get data by time range
		obj = OutpatientLogDBO.getData(rangeFlag, obj);
		if(obj == null) return;
		else dataList = DataExtractor.extractCharacterFromOutpatientLogList((OutpatientLog[])obj);
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
			setDataByDateRange(currentDate, timeUnitType);
			board.setData(dataList);
		} else {
			popDateNotWithinRangeAlert();
		}
	}
	protected void popDateNotWithinRangeAlert(){
		JOptionPane.showMessageDialog(null, "你查询的日期不在数据范围内");
	}

	protected abstract void onMouseWheelOnBoard(MouseWheelEvent e);
	protected abstract void onMouseClickOnBoard(Object source);
	protected abstract void onMouseMoveOnBoard(Object source);
}
