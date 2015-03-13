package com.lsc.ors.views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.applications.ModelObject;
import com.lsc.ors.applications.WRDModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.util.TimeFormatter;
import com.lsc.ors.views.widgets.DatePicker;
import com.lsc.ors.views.widgets.DatePickerListener;
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
	
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_WIDTH = 100;
	
	//view
	WRVboard board = null;
	DatePicker datePicker = null;
	Button nextDay = null;
	Button lastDay = null;
	Button nextWeek = null;
	Button lastWeek = null;
	
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

		//bounds
		setBounds(100, 50, 930, 620);
		displayer.setBounds(10, 10, 600, 400);	//top left
		analyzer.setBounds(620, 10, 300, 610);	//right
		datePicker.setBounds(10, 420, 600, 80);	//bottom left
		lastDay.setBounds(10, 520, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextDay.setBounds(10 + (BUTTON_WIDTH + 10), 520, BUTTON_WIDTH, BUTTON_HEIGHT);
		lastWeek.setBounds(10 + (BUTTON_WIDTH + 10) * 2, 520, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextWeek.setBounds(10 + (BUTTON_WIDTH + 10) * 3, 520, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		//add views
		add(displayer);
		add(analyzer);
		add(datePicker);
		add(lastDay);
		add(nextDay);
		add(lastWeek);
		add(nextWeek);
		displayer.add(board);
		analyzer.add(department);
		
		//listeners
		lastDay.addActionListener(mocl);
		nextDay.addActionListener(mocl);
		lastWeek.addActionListener(mocl);
		nextWeek.addActionListener(mocl);
	}
	
	/**
	 * 日期变更操作
	 * @param dayAmount 变更的天数，正数表示向后，负数表示向以前
	 */
	private void increaseDate(int dayAmount){
		if(currentDate == null) return;
		long time = currentDate.getTime();
		time += (dayAmount * 24 * 3600 * 1000);
		currentDate.setTime(time);
		updateViewsData();
	}

	/**
	 * 更新控件datePicker和board的数据
	 */
	private void updateViewsData(){
		datePicker.setPickerDate(currentDate);
		board.setData(getDataByDate(currentDate));
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
		Object obj = null;
		//get target date
		if(date == null){
			obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_FIRST_DATE, null);
			if(obj == null) return null;
		}
		else obj = date;
		//get data by time stamp
		obj = OutpatientLogDBO.getData(OutpatientLogDBO.RECORD_OF_DATE, obj);
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
				increaseDate(-1);
				break;
			case StringSet.CMD_NEXT_DAY:
				increaseDate(1);
				break;
			case StringSet.CMD_LAST_WEEK:
				increaseDate(-7);
				break;
			case StringSet.CMD_NEXT_WEEK:
				increaseDate(7);
				break;
			default:
				break;
			}
		}
		
	}
}
