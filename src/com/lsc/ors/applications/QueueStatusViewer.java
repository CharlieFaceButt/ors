package com.lsc.ors.applications;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import com.lsc.ors.applications.WaitingRecordViewer.MultipleOnClickListener;
import com.lsc.ors.applications.listener.QSModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.views.QSVboard;
import com.lsc.ors.views.WRVboard;
import com.lsc.ors.views.widgets.DatePicker;

/**
 * Reference of Model 2<br>
 * Give a WRQS view<br>
 * WRQS: waiting record queue status
 * @author charlieliu
 *
 */
public class QueueStatusViewer extends VisualizationModelObject {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -7226527625633491877L;
	
	private static final int HEIGHT = 620;
	private static final int WIDTH = 980;
	private static final int MARGIN = 10;
	private static final int BOARD_HEIGHT = 400;
	private static final int BOARD_WIDTH = 600;
	private static final int ANALYZER_WIDTH = 350;
	private static final int ANALYZER_HEIGHT = 600;
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_WIDTH = 100;
	
	//view
	QSVboard board = null;
	Button nextDay = null;
	Button lastDay = null;
	Button nextWeek = null;
	Button lastWeek = null;
	Button nextMonth = null;
	Button lastMonth = null;
	Button nextYear = null;
	Button lastYear = null;
	JComboBox timeUnitChooser = null;
	JComboBox featureChooser = null;
	
	//listener
	MultipleOnClickListener mocl = new MultipleOnClickListener();
	public QueueStatusViewer(QSModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		
		//initialize views
		setLayout(null);
		board = new QSVboard(getDataByDate(currentDate), mocl);
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

		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		displayer.setBounds(MARGIN, MARGIN, BOARD_WIDTH, BOARD_HEIGHT);	//top left
		analyzer.setBounds(MARGIN * 2 + BOARD_WIDTH, MARGIN, ANALYZER_WIDTH, ANALYZER_HEIGHT);	//right
		datePicker.setBounds(10, 420, 600, 80);	//bottom left
		int lineOfButtonsY = HEIGHT - 3 * (BUTTON_HEIGHT + 10);
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
	protected void updateViewsData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDatePickerChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}
