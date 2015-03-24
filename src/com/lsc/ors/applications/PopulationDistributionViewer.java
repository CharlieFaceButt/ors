package com.lsc.ors.applications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
	
	JComboBox featureChooser = null;

	public PopulationDistributionViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		
		//initialize views
		board = new PDVboard(mocl, getDataByDate(currentDate));
		board.setBackground(Color.WHITE);
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
		featureChooser.setBounds(MARGIN + (BUTTON_WIDTH + MARGIN) * 4, timeBtns.getTop(), 150, 20);
		
		//add views
		add(featureChooser);
		displayer.add(board);
		
		initAnalyzer();
	}
	
	ArrayList<JComponent> analyzerChangableComponent;
	Label featureTitle;
	JComboBox recordTypeChooser;
	private void initAnalyzer(){
		if(analyzerChangableComponent == null)
			analyzerChangableComponent = new ArrayList<JComponent>();
		//feature value label
		String label = OutpatientLog.KEYS[((PDVboard)board).getFeatureType()];
		featureTitle = new Label();
		featureTitle.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, 20);
		analyzer.add(featureTitle);
		featureTitle.setText(label);
		//feature values
		Map<String, Boolean> mfv = ((PDVboard)board).getFeatureValues();
		int i = 1;
		for(String text : mfv.keySet()){
			JCheckBox jcb = new JCheckBox(text, mfv.get(text));
			jcb.setBounds(MARGIN * 2, MARGIN + (20 + MARGIN) * i, BUTTON_WIDTH, 20);
			analyzer.add(jcb);
			analyzerChangableComponent.add(jcb);
			i ++;
		}
		//record type chooser
		recordTypeChooser = new JComboBox(new String[]{
				"等待人数", "已就诊人数"
		});
		recordTypeChooser.setBounds(MARGIN * 2, ANALYZER_HEIGHT - 6 * MARGIN, ANALYZER_WIDTH - 4 * MARGIN, 20);
		recordTypeChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		analyzer.add(recordTypeChooser);
	}
	private void updateAnalyzer(){
		
	}

	@Override
	protected void onDateChanged() {
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

	@Override
	protected void onMouseWheelOnBoard(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseClickOnBoard(Object source) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(BOARD_WIDTH + MARGIN * 2, 0, BOARD_WIDTH + MARGIN * 2, HEIGHT);
		g.drawLine(BOARD_WIDTH + MARGIN * 2, HEIGHT - 6 * MARGIN, WIDTH, HEIGHT - 6 * MARGIN);
		g.drawLine(BOARD_WIDTH + MARGIN * 2, HEIGHT - 18 * MARGIN, WIDTH, HEIGHT - 18 * MARGIN);
	}
}
