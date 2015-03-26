package com.lsc.ors.applications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
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
				OutpatientLog.KEYS[OutpatientLog.INDEX_REGISTRATION],
				OutpatientLog.KEYS[OutpatientLog.INDEX_RECEPTION],
				OutpatientLog.KEYS[OutpatientLog.INDEX_DIAGNOSES],
				OutpatientLog.KEYS[OutpatientLog.INDEX_FURTHER_CONSULTATION]
				});//init feature list
		featureChooser.addItemListener(new FeatureChooserListener());
		
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
	JCheckBox featureAll;
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
		updateAnalyzer();
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
		JComponent jc = null;
		for(int i = 0 ; i < analyzerChangableComponent.size() ; i ++){
			jc = analyzerChangableComponent.get(i);
			analyzer.remove(jc);
		}
		analyzerChangableComponent.clear();
		//feature values
		Map<String, Boolean> mfv = ((PDVboard)board).getFeatureValues();
		int i = 1;
		JCheckBox jcb = null;
		boolean allSelected = true;
		for(String text : mfv.keySet()){
			if(text.equals(StringSet.TOTAL))
				continue;
			jcb = new JCheckBox(text, mfv.get(text));
			jcb.setBounds(MARGIN * 2, MARGIN + (20 + MARGIN) * i, ANALYZER_WIDTH - 4 * MARGIN, 20);
			jcb.addActionListener(fvl);
			jcb.setSelected(mfv.get(text));
			analyzer.add(jcb);
			analyzerChangableComponent.add(jcb);
			if(!mfv.get(text)) allSelected = false;
			i ++;
		}
		jcb = new JCheckBox(StringSet.TOTAL, mfv.get(StringSet.TOTAL));
		jcb.setBounds(MARGIN * 2, MARGIN + (20 + MARGIN) * i, ANALYZER_WIDTH - 4 * MARGIN, 20);
		jcb.addActionListener(fvl);
		jcb.setSelected(mfv.get(StringSet.TOTAL));
		analyzer.add(jcb);
		analyzerChangableComponent.add(jcb);
		if(!mfv.get(StringSet.TOTAL)) allSelected = false;

		//select all check box
		if(featureAll == null){
			featureAll = new JCheckBox(StringSet.SELECT_ALL,allSelected);
			featureAll.setBounds(MARGIN * 2, ANALYZER_HEIGHT - 24 * MARGIN, ANALYZER_WIDTH - 4 * MARGIN, 20);
			featureAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					featureAll.setSelected(featureAll.isSelected());
					((PDVboard)board).changeAllFeatureValue(featureAll.isSelected());
					updateAnalyzer();
				}
			});
			analyzer.add(featureAll);
		}
		featureAll.setSelected(allSelected);
		repaint();
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
		int change = e.getWheelRotation();
		increaseDate(Calendar.DAY_OF_YEAR, change);
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
	
	class FeatureChooserListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			ConsoleOutput.pop("PopulationDistributionViewer.FeatureChooserListener", "item Changed:" + e.getItem());
			int fType = StringSet.getInstance().getCommandIndex(featureChooser.getSelectedItem().toString()) - StringSet.CMD_FEATURE.OUTPATIENT_BASE;
			if(((PDVboard)board).changeFeatureType(fType)){
				updateAnalyzer();
			}
			
		}
		
	}
	
	private FeatureValueListener fvl = new FeatureValueListener();
	class FeatureValueListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			ConsoleOutput.pop("PopulationDistributionViewer.FeatureValueListener", "source:" + e.getSource() + "\tchange to:" + ((JCheckBox)e.getSource()).isSelected());
			JCheckBox box = (JCheckBox)e.getSource();
			((PDVboard)board).changeFeatureValue(box.getText(), box.isSelected());
			updateAnalyzer();
		}
		
	}
}
