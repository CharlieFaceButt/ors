package com.lsc.ors.views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.applications.ModelObject;
import com.lsc.ors.applications.WRDModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.util.TimeFormatter;
import com.lsc.ors.views.widgets.DatePicker;
import com.lsc.ors.views.widgets.WRVboard;
import com.lsc.ors.src.StringSet;

/**
 * give a WR view<br>
 * WR: waiting record
 * @author charlieliu
 *
 */
public class WaitingRecordViewer extends ModelObject{

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -3891273426875938845L;
	
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_WIDTH = 100;
	
	//view
	WRVboard board = null;
	DatePicker datePicker = null;
	Button nextDay = null;
	Button lastDay = null;
	Button nextWeek = null;
	Button lastWeek = null;
	Button nextMonth = null;
	Button lastMonth = null;
	Button nextYear = null;
	Button lastYear = null;
	
	JComboBox timeUnitChooser = null;
	
	//data
	Date firstDate = null;
	Date lastDate = null;
	Date currentDate = null;
	
	//listener
	MultipleOnClickListener mocl = new MultipleOnClickListener();

	public WaitingRecordViewer(WRDModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		//data
		initData();
		
		//initialize views
		setLayout(null);
		board = new WRVboard(getDataByDate(currentDate));
		board.setBackground(Color.WHITE);
		JPanel displayer = new JPanel(new BorderLayout());
		JPanel analyzer = new JPanel();
		Label department = new Label(StringSet.DEPARTMENT_NAME);
		datePicker = new DatePicker(firstDate, lastDate, currentDate, new DatePickerListener());
		lastDay = new Button(StringSet.LAST_DAY);
		nextDay = new Button(StringSet.NEXT_DAY);
		lastWeek = new Button(StringSet.LAST_WEEK);
		nextWeek = new Button(StringSet.NEXT_WEEK);
		lastMonth = new Button(StringSet.LAST_MONTH);
		nextMonth = new Button(StringSet.NEXT_MONTH);
		lastYear = new Button(StringSet.LAST_YEAR);
		nextYear = new Button(StringSet.NEXT_YEAR);
		timeUnitChooser = new JComboBox(new String[]{
				StringSet.TIME_UNIT_DAY,StringSet.TIME_UNIT_WEEK,
				StringSet.TIME_UNIT_MONTH,StringSet.TIME_UNIT_YEAR});

		//bounds
		int height = 620;
		setBounds(100, 50, 930, height);
		displayer.setBounds(10, 10, 600, 400);	//top left
		analyzer.setBounds(620, 10, 300, 610);	//right
		datePicker.setBounds(10, 420, 600, 80);	//bottom left
		int lineOfButtonsY = height - 3 * (BUTTON_HEIGHT + 10);
		lastDay.setBounds(10, lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextDay.setBounds(10 + (BUTTON_WIDTH + 10), lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		lastWeek.setBounds(10 + (BUTTON_WIDTH + 10) * 2, lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextWeek.setBounds(10 + (BUTTON_WIDTH + 10) * 3, lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		lineOfButtonsY += (BUTTON_HEIGHT + 10);
		lastMonth.setBounds(10, lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextMonth.setBounds(10 + (BUTTON_WIDTH + 10), lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		lastYear.setBounds(10 + (BUTTON_WIDTH + 10) * 2, lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextYear.setBounds(10 + (BUTTON_WIDTH + 10) * 3, lineOfButtonsY, BUTTON_WIDTH, BUTTON_HEIGHT);
		timeUnitChooser.setBounds(200, 50, 200, 20);
		
		//add views
		add(displayer);
		add(analyzer);
		add(datePicker);
		add(lastDay);
		add(nextDay);
		add(lastWeek);
		add(nextWeek);
		add(lastMonth);
		add(nextMonth);
		add(lastYear);
		add(nextYear);
		displayer.add(board);
		analyzer.add(timeUnitChooser);
		
		//listeners
		lastDay.addActionListener(mocl);
		nextDay.addActionListener(mocl);
		lastWeek.addActionListener(mocl);
		nextWeek.addActionListener(mocl);
		lastMonth.addActionListener(mocl);
		nextMonth.addActionListener(mocl);
		lastYear.addActionListener(mocl);
		nextYear.addActionListener(mocl);
		timeUnitChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange() == ItemEvent.SELECTED){
					String s=(String)timeUnitChooser.getSelectedItem();
					Integer msg = StringSet.getInstance().getCommandIndex(s);
					if(msg == null){
						ConsoleOutput.pop("WaitingRecordViewer.itemStateChanged", "msg is null");
						return;
					}
					switch(msg){
					case StringSet.CMD_TIME_UNIT_DAY:
						lastDay.setEnabled(true);
						nextDay.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_WEEK:
						lastWeek.setEnabled(true);
						nextWeek.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_MONTH:
						lastMonth.setEnabled(true);
						nextMonth.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_YEAR:
						board.setData(getDataByDateRange(currentDate, msg), msg);
						setButtonsDisable(msg);
						break;
					default:break;
					}
	            }

			}
		});
	}
	
	private void setButtonsDisable(int msg){
		switch (msg) {
		case StringSet.CMD_TIME_UNIT_YEAR:
			lastMonth.setEnabled(false);
			nextMonth.setEnabled(false);
		case StringSet.CMD_TIME_UNIT_MONTH:
			lastWeek.setEnabled(false);
			nextWeek.setEnabled(false);
		case StringSet.CMD_TIME_UNIT_WEEK:
			lastDay.setEnabled(false);
			nextDay.setEnabled(false);
		default:
			break;
		}
	}
	
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		getLastDate();
		getFirstDate();
		setCurrentDate(firstDate);
	}
	
	/**
	 * 获取最后记录日期
	 * @return
	 */
	private Date getLastDate() {
		// TODO Auto-generated method stub
		if(lastDate == null){
			Object obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_LAST_DATE, null);
			if(obj != null) lastDate = (Date)obj;
		}
		return lastDate;
	}

	/**
	 * 获取最早记录日期
	 * @return
	 */
	private Date getFirstDate() {
		// TODO Auto-generated method stub
		if(firstDate == null){
			Object obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_FIRST_DATE, null);
			if(obj != firstDate) firstDate = (Date)obj;
		}
		return firstDate;
	}

	/**
	 * 根据时间获得数据集合
	 * @param date
	 * @return
	 */
	private OutpatientLog[] getDataByDate(Date date){
		if(board == null)
			return getDataByDateRange(date, StringSet.CMD_TIME_UNIT_DAY);
		return getDataByDateRange(date, board.getTimeUnitType());
	}
	
	private OutpatientLog[] getDataByDateRange(Date date, int rangeFlag){
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
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(625, 0, 625, 620);
	}

	/**
	 * 仅对currentDate进行设置，不对view做更新
	 * @param date
	 * @return currentDate是否变动了
	 */
	private boolean setCurrentDate(Date date){
		if(date == null)
			return false;
		boolean changed = false;
		changed = !TimeFormatter.sameDay(currentDate, date);
		if(changed)
			currentDate = new Date(date.getTime());
		return changed;
	}
	
	class DatePickerListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			if(setCurrentDate(datePicker.getCurrentDate()))
				board.setData(getDataByDate(currentDate));
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
			default:
				break;
			}
		}
		
	}
	
	/**
	 * 日期变更操作
	 * @param dayAmount 变更的天数，正数表示向后，负数表示向以前
	 */
	private void increaseDate(int field, int amount){
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
	
	private void popDateNotWithinRangeAlert(){
		JOptionPane.showMessageDialog(null, "你查询的日期不在数据范围内");
	}

	/**
	 * 更新控件datePicker和board的数据
	 */
	private void updateViewsData(){
		datePicker.setPickerDate(currentDate);
		board.setData(getDataByDate(currentDate));
	}
}
