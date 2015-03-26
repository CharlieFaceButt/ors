package com.lsc.ors.applications;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.views.PDVboard;

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
	JCheckBox selectAll;
	Label outpatientAmount;
	Label averageWaitingTime;
	private void initAnalyzer(){
		if(analyzerChangableComponent == null)
			analyzerChangableComponent = new ArrayList<JComponent>();
		
		//feature value label
		String label = OutpatientLog.KEYS[((PDVboard)board).getFeatureType()];
		featureTitle = new Label();
		featureTitle.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, 20);
		analyzer.add(featureTitle);
		featureTitle.setText(label);
		
		//select-all check box
		if(selectAll == null){
			selectAll = new JCheckBox(StringSet.SELECT_ALL,true);
			selectAll.setBounds(ANALYZER_WIDTH - 2 * MARGIN - BUTTON_WIDTH, MARGIN, BUTTON_WIDTH, 20);
			selectAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					selectAll.setSelected(selectAll.isSelected());
					((PDVboard)board).changeAllFeatureValue(selectAll.isSelected());
					updateAnalyzer();
				}
			});
			analyzer.add(selectAll);
		}
		
		//statistic section
		outpatientAmount = new Label();
		averageWaitingTime = new Label();
		outpatientAmount.setBounds(MARGIN * 2, statisticTop, ANALYZER_WIDTH - 4 * MARGIN, 20);
		averageWaitingTime.setBounds(MARGIN * 2, statisticTop + 3 * MARGIN, ANALYZER_WIDTH - 4 * MARGIN, 20);
		outpatientAmount.setText("总看病人数:\t" + board.getTotalOutpatientNumber());
		averageWaitingTime.setText("人均等待时间:\t" + ((PDVboard)board).getAverageWaitingTime());
		analyzer.add(outpatientAmount);
		analyzer.add(averageWaitingTime);
		
		//feature values
		featureJP = new JPanel();
		featureJP.setBounds(MARGIN, 0, ANALYZER_WIDTH, ANALYZER_HEIGHT);
		featureJP.setLayout(null);
//		JScrollPane jsp = new JScrollPane();
//		jsp.setBounds(0, 3 * MARGIN, ANALYZER_WIDTH, 100);
//		jsp.setViewportView(featureJP);
//		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		jsp.setWheelScrollingEnabled(true);
//		jsp.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
		analyzer.add(featureJP);
		
		updateAnalyzer();
	}
	
	JPanel featureJP;
	/**
	 * 更新分析器显示
	 */
	private void updateAnalyzer(){
		
		//delete old feature values
		JComponent jc = null;
		for(int i = 0 ; i < analyzerChangableComponent.size() ; i ++){
			jc = analyzerChangableComponent.get(i);
			featureJP.remove(jc);
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
			if(i % 2 == 1){
				jcb.setBounds(MARGIN * 2, MARGIN + (20 + MARGIN) * ((i + 1) / 2), ANALYZER_WIDTH / 2 - MARGIN, 20);
			} else{
				jcb.setBounds(ANALYZER_WIDTH / 2 + MARGIN, MARGIN + (20 + MARGIN) * ((i + 1) / 2), ANALYZER_WIDTH / 2 - MARGIN, 20);
			}
			jcb.addActionListener(fvl);
			jcb.setSelected(mfv.get(text));
			featureJP.add(jcb);
			analyzerChangableComponent.add(jcb);
			if(!mfv.get(text)) allSelected = false;
			i ++;
		}
		jcb = new JCheckBox(StringSet.TOTAL, mfv.get(StringSet.TOTAL));
		jcb.setBounds(MARGIN * 2, MARGIN + (20 + MARGIN) * i, ANALYZER_WIDTH - 4 * MARGIN, 20);
		jcb.addActionListener(fvl);
		jcb.setSelected(mfv.get(StringSet.TOTAL));
		featureJP.add(jcb);
		analyzerChangableComponent.add(jcb);
		if(!mfv.get(StringSet.TOTAL)) allSelected = false;

		//select-all check box
		selectAll.setSelected(allSelected);
		repaint();
		featureJP.repaint();
	}

	@Override
	protected void onDateChanged() {
		// TODO Auto-generated method stub
		updateStatistics();
	}

	@Override
	protected void onDatePickerChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		updateStatistics();
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
	
	private int statisticTop = HEIGHT - 12 * MARGIN;
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(BOARD_WIDTH + MARGIN * 2, 0, BOARD_WIDTH + MARGIN * 2, HEIGHT);
		g.drawLine(BOARD_WIDTH + MARGIN * 2, statisticTop, WIDTH, statisticTop);
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
	@Override
	protected void onTimeUnitChanged() {
		// TODO Auto-generated method stub
		updateStatistics();
		featureChooser.setSelectedIndex(0);
	}
	
	private void updateStatistics(){
		outpatientAmount.setText("总看病人数:\t" + board.getTotalOutpatientNumber());
		averageWaitingTime.setText("人均等待时间:\t" + ((PDVboard)board).getAverageWaitingTime());
	}
}
