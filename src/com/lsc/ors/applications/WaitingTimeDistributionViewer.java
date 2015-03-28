package com.lsc.ors.applications;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.views.WTDVboard;

public class WaitingTimeDistributionViewer extends VisualizationModelObject{

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -1059537914692615450L;
	
	//view
	JComboBox featureChooser = null;

	public WaitingTimeDistributionViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
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
		
	}

	@Override
	protected void onMouseClickOnBoard(Object source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimeUnitChanged() {
		// TODO Auto-generated method stub
		
	}

}
