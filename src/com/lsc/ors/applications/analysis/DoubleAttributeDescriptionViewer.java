package com.lsc.ors.applications.analysis;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComboBox;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.applications.waitingtime.WaitingTimeDistributionViewer;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.views.analysis.ADboard;
import com.lsc.ors.views.analysis.DADboard;

public class DoubleAttributeDescriptionViewer extends AnalysisModelObject{

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -7480910825263157881L;

	JComboBox characterChooser;
	JComboBox diagramTypeChooser;
	JComboBox waitingTimeClassChooser;
	
	public DoubleAttributeDescriptionViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		board = new DADboard(dataList, mocl);
		board.setBackground(Color.WHITE);
		characterChooser = new JComboBox(OutpatientLogCharacters.KEYS);
		diagramTypeChooser = new JComboBox(new String[]{"频度交叉表","盒图","散布图","回归曲线"});
		waitingTimeClassChooser = new JComboBox(DADboard.WTKeys);
		Label diagramTypeTitle = new Label("图示种类：");
		Label characterChooserTitle = new Label("特征选择：");
		Label waitingTimeClassTitle = new Label("等待时间划分");
		
		//bounds
		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		diagramTypeChooser.setBounds(
				MARGIN + BUTTON_WIDTH, MARGIN,
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		diagramTypeTitle.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, CHOOSER_HEIGHT);
		characterChooser.setBounds(
				MARGIN + BUTTON_WIDTH, 2 * MARGIN + CHOOSER_HEIGHT, 
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		characterChooserTitle.setBounds(MARGIN, 2 * MARGIN + CHOOSER_HEIGHT, BUTTON_WIDTH, CHOOSER_HEIGHT);
		waitingTimeClassChooser.setBounds(
				MARGIN + BUTTON_WIDTH, 3 * MARGIN + 2 * CHOOSER_HEIGHT, 
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		waitingTimeClassTitle.setBounds(MARGIN, 3 * MARGIN + 2 * CHOOSER_HEIGHT, BUTTON_WIDTH, CHOOSER_HEIGHT);
		
		displayer.add(board);
		analyzer.add(diagramTypeChooser);
		analyzer.add(diagramTypeTitle);
		analyzer.add(characterChooser);
		analyzer.add(characterChooserTitle);
		analyzer.add(waitingTimeClassChooser);
		analyzer.add(waitingTimeClassTitle);
		
		characterChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DADboard)board).changeCharacter(characterChooser.getSelectedItem().toString());
			}
		});
		diagramTypeChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DADboard)board).changeDiagramType(diagramTypeChooser.getSelectedIndex());
			}
		});
		waitingTimeClassChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DADboard)board).changeWaitingTimeClassification(waitingTimeClassChooser.getSelectedIndex());
				
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

}
