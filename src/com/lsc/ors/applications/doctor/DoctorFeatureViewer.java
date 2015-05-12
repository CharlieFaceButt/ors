package com.lsc.ors.applications.doctor;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

import javax.swing.JComboBox;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.views.analysis.DCTADboard;

public class DoctorFeatureViewer extends DCTAnalysisModelObject {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -7648838624365828211L;

	JComboBox characterChooser;
	JComboBox diagramTypeChooser;
	JComboBox doctorChooser;
	
	String[] doctorList = null;
	
	public DoctorFeatureViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		
		board = new DCTADboard(dataList, mocl);
		characterChooser = new JComboBox(OutpatientLogCharacters.KEYS);
		diagramTypeChooser = new JComboBox(new String[]{"频度表","饼图","条状图","百分位图"});
		refreshDoctorList();
		
		board.setBackground(Color.WHITE);
		((DCTADboard)board).setDoctor(doctorList[0]);
		Label diagramTypeTitle = new Label("图示种类：");
		Label doctorTitle = new Label("医生选择：");

		//bounds
		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		characterChooser.setBounds(
				MARGIN + 4 * (BUTTON_WIDTH + MARGIN), 
				HEIGHT - MARGIN - 3 * (BUTTON_HEIGHT + MARGIN),
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		diagramTypeChooser.setBounds(
				MARGIN + BUTTON_WIDTH, MARGIN,
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		diagramTypeTitle.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, CHOOSER_HEIGHT);
		doctorTitle.setBounds(MARGIN, MARGIN * 2 + BUTTON_HEIGHT, BUTTON_WIDTH, CHOOSER_HEIGHT);
		
		displayer.add(board);
		add(characterChooser);
		analyzer.add(diagramTypeChooser);
		analyzer.add(diagramTypeTitle);
		analyzer.add(doctorTitle);
		characterChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DCTADboard)board).changeCharacter(characterChooser.getSelectedItem().toString());
			}
		});
		diagramTypeChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DCTADboard)board).changeDiagramType(diagramTypeChooser.getSelectedIndex());
			}
		});
	}
	
	/**
	 * 更新医生列表
	 */
	private void refreshDoctorList(){
		if(dataList == null) doctorList = null;
		LinkedList<String> list = new LinkedList<String>();
		for (OutpatientLogCharacters olc : dataList) {
			if(!list.contains(olc.getDoctor_name())){
				list.add(olc.getDoctor_name());
			}
		}
		doctorList = new String[list.size()];
		doctorList = list.toArray(doctorList);
		if(doctorChooser != null)
			analyzer.remove(doctorChooser);
		doctorChooser = new JComboBox(doctorList);
		analyzer.add(doctorChooser);
		doctorChooser.setBounds(
				MARGIN + BUTTON_WIDTH, 
				MARGIN * 2 + BUTTON_HEIGHT, 
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		doctorChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DCTADboard)board).setDoctor(doctorChooser.getSelectedItem().toString());
			}
		});
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
	protected void onMouseMoveOnBoard(Object source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDataListChanged() {
		// TODO Auto-generated method stub
		if(doctorChooser != null)
			refreshDoctorList();
		if(board != null && doctorList != null)
			((DCTADboard)board).setDoctor(doctorList[0]);
	}

}
