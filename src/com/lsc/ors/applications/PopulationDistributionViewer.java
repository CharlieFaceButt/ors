package com.lsc.ors.applications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.views.PDVboard;
import com.lsc.ors.views.WRVboard;
import com.lsc.ors.views.widgets.TimeButtonGroup;

public class PopulationDistributionViewer extends VisualizationModelObject {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -1625390664392395884L;
	
	PDVboard board = null;
	TimeButtonGroup timeBtns = null;
	JComboBox featureChooser = null;
	
	MultipleOnClickListener mocl = new MultipleOnClickListener();

	public PopulationDistributionViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		
		//initialize views
		setLayout(null);
		board = new PDVboard(mocl, getDataByDate(currentDate));
		board.setBackground(Color.WHITE);
		JPanel displayer = new JPanel(new BorderLayout());
		JPanel analyzer = new JPanel(new BorderLayout());
		featureChooser = new JComboBox(new String[]{
				OutpatientLog.KEYS[OutpatientLog.INDEX_DEPARTMENT],
				OutpatientLog.KEYS[OutpatientLog.INDEX_DOCTOR],
				OutpatientLog.KEYS[OutpatientLog.INDEX_PATIENT_GENDER],
				OutpatientLog.KEYS[OutpatientLog.INDEX_PATIENT_AGE],
				OutpatientLog.KEYS[OutpatientLog.INDEX_DIAGNOSES],
				OutpatientLog.KEYS[OutpatientLog.INDEX_WAIT],
				OutpatientLog.KEYS[OutpatientLog.INDEX_FURTHER_CONSULTATION]
				});
		featureChooser.addActionListener(mocl);
		
		//bounds
		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		displayer.setBounds(10, 10, 600, 400);	//top left
		analyzer.setBounds(620, 10, ANALYZER_WIDTH, ANALYZER_HEIGHT);	//right
		datePicker.setBounds(10, 420, 600, 80);	//bottom left
		timeBtns = new TimeButtonGroup(500, 10, BUTTON_WIDTH, BUTTON_HEIGHT, mocl);
		featureChooser.setBounds(MARGIN + (BUTTON_WIDTH + MARGIN) * 4, timeBtns.getTop(), 150, 20);
		
		//add views
		add(displayer);
		add(analyzer);
		for(Component c : timeBtns.getAllComponents()){
			add(c);
		}
		add(featureChooser);
		add(datePicker);
		displayer.add(board);
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
			case StringSet.CMD_TIME_UNIT_DAY:
			case StringSet.CMD_TIME_UNIT_WEEK:
			case StringSet.CMD_TIME_UNIT_MONTH:
			case StringSet.CMD_TIME_UNIT_YEAR:
				board.setData(getDataByDateRange(currentDate, msg), msg);
				break;
			default:break;
			}
		}
		
	}
}
