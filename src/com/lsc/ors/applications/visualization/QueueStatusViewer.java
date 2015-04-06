package com.lsc.ors.applications.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.lsc.ors.applications.listener.QSModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.views.visualization.QSVboard;
import com.lsc.ors.views.widgets.TimeButtonGroup;

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
	
	//view
	JComboBox featureChooser = null;
	JSlider timePicker = null;
	Label timeLabel = null;
	
	public QueueStatusViewer(QSModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setTitle(StringSet.VSL_WAITING_RECORD_QUEUE_STATUS);
		
		//initialize views
		board = new QSVboard(getDataByDate(currentDate), mocl);
		board.setBackground(Color.WHITE);
		featureChooser = new JComboBox(new String[]{
				OutpatientLog.KEYS[OutpatientLog.INDEX_DOCTOR],
				OutpatientLog.KEYS[OutpatientLog.INDEX_PATIENT_GENDER],
				OutpatientLog.KEYS[OutpatientLog.INDEX_PATIENT_AGE],
				OutpatientLog.KEYS[OutpatientLog.INDEX_DIAGNOSES],
				OutpatientLog.KEYS[OutpatientLog.INDEX_FURTHER_CONSULTATION]
				});
		featureChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Integer msg = StringSet.getInstance().getCommandIndex(featureChooser.getSelectedItem().toString());
				if(msg > StringSet.CMD_FEATURE.OUTPATIENT_BASE && msg < StringSet.CMD_FEATURE.OUTPATIENT_BASE + 18){
					((QSVboard)board).setFeatureType(msg - StringSet.CMD_FEATURE.OUTPATIENT_BASE);
				}
			}
		});
		timePicker = new JSlider(0, 100, 0);
		initTimePicker(currentDate);
		timePicker.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				((QSVboard)board).setTargetTime(timePicker.getValue());
				updateTimeLabel();
			}
		});
		timeLabel = new Label(StringSet.CURRENT_DATE);
		
		//bounds
		featureChooser.setBounds(MARGIN + (BUTTON_WIDTH + MARGIN) * 4, timeBtns.getTop(), BUTTON_WIDTH, 20);
		timePicker.setBounds(MARGIN, MARGIN, ANALYZER_WIDTH - 2 * MARGIN, 40);
		updateTimeLabel();
		
		//add to the view
		displayer.add(board);
		timeBtns.disableTimeUnitChooser();
		add(featureChooser);
		analyzer.add(timePicker);
		analyzer.add(timeLabel);
	}
	
	private void updateTimeLabel(){
		int minuteAmount = ((QSVboard)board).getTargetTime();
		timeLabel.setBounds(MARGIN, MARGIN + 40, ANALYZER_WIDTH - 2 * MARGIN, 40);
		timeLabel.setText("" + minuteAmount / 60 + ":" + minuteAmount % 60);
	}
	
	private void initTimePicker(Date date){
		Object obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_EARLIEST_OF_DAY, date);
		int min = 0;
		if(obj != null)	min = QSVboard.getMinutesAmountFromDate((Date)obj);
		obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_LATEST_OF_DAY, date);
		int max = 0;
		if(obj != null) max = QSVboard.getMinutesAmountFromDate((Date)obj);
		timePicker.setMinimum(min);
		timePicker.setMaximum(max + 5);
		timePicker.setValue(min);
	}

	@Override
	protected void onDatePickerChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		initTimePicker(currentDate);
	}

	@Override
	protected void onDateChanged() {
		// TODO Auto-generated method stub
		initTimePicker(currentDate);
	}

	@Override
	protected void onMouseWheelOnBoard(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		timePicker.setValue(((QSVboard)board).getTargetTime());
	}

	@Override
	protected void onMouseClickOnBoard(Object source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimeUnitChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseMoveOnBoard(Object source) {
		// TODO Auto-generated method stub
		
	}
}
