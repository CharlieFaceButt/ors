package com.lsc.ors.applications;

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

import com.lsc.ors.applications.listener.WRDModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.util.TimeFormatter;
import com.lsc.ors.views.WRVboard;
import com.lsc.ors.views.widgets.DatePicker;
import com.lsc.ors.src.StringSet;

/**
 * Reference of Model 1<br>
 * Give a WRD view<br>
 * WRD: waiting record distribution
 * @author charlieliu
 *
 */
public class WaitingRecordViewer extends VisualizationModelObject{

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -3891273426875938845L;
	
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_WIDTH = 100;
	private static final int LABEL_LEFT_MARGIN = 20;
	private static final int LABEL_GAP = 10;
	private static final int LABEL_WIDTH = 150;
	private static final int LABEL_HEIGHT = 20;
	
	//view
	WRVboard board = null;
	Button nextDay = null;
	Button lastDay = null;
	Button nextWeek = null;
	Button lastWeek = null;
	Button nextMonth = null;
	Button lastMonth = null;
	Button nextYear = null;
	Button lastYear = null;
	JComboBox timeUnitChooser = null;
	Label[] logInfos = null;
	
	//listener
	MultipleOnClickListener mocl = new MultipleOnClickListener();

	public WaitingRecordViewer(WRDModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		//int time range
		setCurrentDate(firstDate);
		
		//initialize views
		setLayout(null);
		board = new WRVboard(getDataByDate(currentDate), mocl);
		board.setBackground(Color.WHITE);
		JPanel displayer = new JPanel(new BorderLayout());
		JPanel analyzer = new JPanel(new BorderLayout());
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
		setBounds(100, 50, 980, height);
		setResizable(false);
		displayer.setBounds(10, 10, 600, 400);	//top left
		analyzer.setBounds(620, 10, 350, 610);	//right
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
		timeUnitChooser.setBounds(10 + (BUTTON_WIDTH + 10) * 4, lineOfButtonsY, BUTTON_WIDTH, 20);
		
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
		add(timeUnitChooser);
		displayer.add(board);
		int i = 0;
		logInfos = new Label[OutpatientLog.KEYS.length];
		for(String key : OutpatientLog.KEYS){
			Label label = new Label(key);
			label.setBounds(LABEL_LEFT_MARGIN, LABEL_GAP + (LABEL_GAP + LABEL_HEIGHT) * i, LABEL_WIDTH, LABEL_HEIGHT);
			analyzer.add(label);
			
			logInfos[i] = new Label(StringSet.VACANT_CONTENT);
			logInfos[i].setBounds(LABEL_LEFT_MARGIN * 2 + LABEL_WIDTH, LABEL_GAP + (LABEL_GAP + LABEL_HEIGHT) * i, LABEL_WIDTH, LABEL_HEIGHT);
			analyzer.add(logInfos[i]);
			i ++;
			ConsoleOutput.pop("WaitingRecordViewer.create", "label" + i + "added");
		}
		analyzer.add(new Label(""));
		
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
	 * 根据时间获得数据集合
	 * @param date
	 * @return
	 */
	private OutpatientLog[] getDataByDate(Date date){
		if(board == null)
			return getDataByDateRange(date, StringSet.CMD_TIME_UNIT_DAY);
		return getDataByDateRange(date, board.getTimeUnitType());
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(619, 0, 619, 620);
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
			case StringSet.CMD_MOUSE_CLICK:
				if(e.getID() == (int)WRVboard.getSerialID()){
					OutpatientLog ol = null;
					if(e.getSource() != null)
						ol = (OutpatientLog)e.getSource();
					for (int i = 0; i < logInfos.length; i++) {
						if(ol != null)
							logInfos[i].setText(ol.get(i));
						else
							logInfos[i].setText(StringSet.VACANT_CONTENT);
					}
				}
				break;
			case StringSet.CMD_VACANT_CONTENT:
				if(e.getID() == (int)WRVboard.getSerialID()){
					for (int i = 0; i < logInfos.length; i++) {
						logInfos[i].setText(StringSet.VACANT_CONTENT);
					}
				}
				break;
			default:
				break;
			}
		}
		
	}


	@Override
	protected void updateViewsData() {
		datePicker.setPickerDate(currentDate);
		board.setData(getDataByDate(currentDate));
	}

	@Override
	protected void onDatePickerChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if(setCurrentDate(datePicker.getCurrentDate()))
			board.setData(getDataByDate(currentDate));
	}
	
}
