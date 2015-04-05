package com.lsc.ors.applications.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.AverageValueObject;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.views.WTDVboard;

public class WaitingTimeDistributionViewer extends VisualizationModelObject{

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -1059537914692615450L;
	
	//view
	JComboBox featureChooser = null;
	Label[] infoLabels;

	private int LABEL_HEIGHT = 20;
	public WaitingTimeDistributionViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setTitle(StringSet.VSL_WAITING_TIME_DISTRIBUTION);
		
		//initialize views
		board = new WTDVboard(mocl, getDataByDate(currentDate));
		board.setBackground(Color.WHITE);
		featureChooser = new JComboBox(new String[]{
				OutpatientLog.KEYS[OutpatientLog.INDEX_DEPARTMENT],
				OutpatientLog.KEYS[OutpatientLog.INDEX_DOCTOR],
				OutpatientLog.KEYS[OutpatientLog.INDEX_PATIENT_GENDER],
				OutpatientLog.KEYS[OutpatientLog.INDEX_PATIENT_AGE],
				OutpatientLog.KEYS[OutpatientLog.INDEX_DIAGNOSES],
				OutpatientLog.KEYS[OutpatientLog.INDEX_REGISTRATION],
				OutpatientLog.KEYS[OutpatientLog.INDEX_RECEPTION],
				OutpatientLog.KEYS[OutpatientLog.INDEX_FURTHER_CONSULTATION]
				});
		featureChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Integer msg = StringSet.getInstance().getCommandIndex(featureChooser.getSelectedItem().toString());
				if(msg > StringSet.CMD_FEATURE.OUTPATIENT_BASE && msg < StringSet.CMD_FEATURE.OUTPATIENT_BASE + 18){
					((WTDVboard)board).changeFeatureAndUpdate(msg - StringSet.CMD_FEATURE.OUTPATIENT_BASE);
				}
			}
		});
		String[] infoKeys = AverageValueObject.KEYS;
		infoLabels = new Label[infoKeys.length];
		for(int i = 0 ; i < infoKeys.length ; i ++){
			infoLabels[i] = new Label(infoKeys[i] + ":\t" + StringSet.VACANT_CONTENT);
			infoLabels[i].setBounds(MARGIN * 2,	MARGIN + i * (LABEL_HEIGHT + MARGIN), ANALYZER_WIDTH - 4 * MARGIN, LABEL_HEIGHT);
			analyzer.add(infoLabels[i]);
		}
		
		//bounds
		featureChooser.setBounds(MARGIN + (BUTTON_WIDTH + MARGIN) * 4, timeBtns.getTop(), BUTTON_WIDTH, 20);
		
		//add to the view
		displayer.add(board);
//		timeBtns.disableTimeUnitChooser();
		add(featureChooser);
	}

	@Override
	protected void onDateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDatePickerChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseWheelOnBoard(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int roll = e.getWheelRotation();
		increaseDate(Calendar.DAY_OF_YEAR, roll);
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
		AverageValueObject avo = (AverageValueObject)source;
		ConsoleOutput.pop("WaitingTimeDistribution.onMouseMoveOnBoard", source.toString());
		String[] infoKeys = AverageValueObject.KEYS;
		if(avo.divide == 0){
			for(int i = 0 ; i < infoKeys.length ; i ++){
				infoLabels[i].setText(infoKeys[i] + ":\t" + StringSet.VACANT_CONTENT);
			}
		} else{
			for (int j = 0; j < infoKeys.length; j++) {
				infoLabels[j].setText(infoKeys[j] + ":\t" + avo.get(j));
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(BOARD_WIDTH + 2 * MARGIN, 0, BOARD_WIDTH + 2 * MARGIN, HEIGHT);
	}
}
