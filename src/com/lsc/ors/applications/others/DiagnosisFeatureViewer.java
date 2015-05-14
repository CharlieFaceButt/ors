package com.lsc.ors.applications.others;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComboBox;

import com.lsc.ors.applications.analysis.AnalysisModelObject;
import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.views.analysis.DGFboard;

public class DiagnosisFeatureViewer extends AnalysisModelObject {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = 31388050598910815L;

	JComboBox diagramTypeChooser;
	JComboBox modelChooser;
	
	public DiagnosisFeatureViewer(ModelListener listener, int modelFlag) {
		super(listener);
		// TODO Auto-generated constructor stub
		board = new DGFboard(dataList, mocl);
		diagramTypeChooser = new JComboBox(new String[]{"频度表","图"});
		modelChooser = new JComboBox(new String[]{
				StringSet.DIAGNOSIS_INCIDENCE,
				StringSet.DIAGNOSIS_CONSULTATION_RATE,
				StringSet.DIAGNOSIS_COMPLICATION});
		
		board.setBackground(Color.WHITE);
		Label diagramTypeTitle = new Label("图示种类：");
		Label modelTitle = new Label("功能选择：");

		//bounds
		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		diagramTypeChooser.setBounds(
				MARGIN + BUTTON_WIDTH, MARGIN,
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		modelChooser.setBounds(
				MARGIN + BUTTON_WIDTH, MARGIN * 2 + CHOOSER_HEIGHT,
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		diagramTypeTitle.setBounds(MARGIN, MARGIN, BUTTON_WIDTH, CHOOSER_HEIGHT);
		modelTitle.setBounds(MARGIN, MARGIN * 2 + BUTTON_HEIGHT, BUTTON_WIDTH, CHOOSER_HEIGHT);
		
		displayer.add(board);
		analyzer.add(diagramTypeChooser);
		analyzer.add(modelChooser);
		analyzer.add(diagramTypeTitle);
		analyzer.add(modelTitle);
		diagramTypeChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DGFboard)board).changeDiagramType(diagramTypeChooser.getSelectedIndex());
			}
		});
		modelChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DGFboard)board).changeModelFlag(modelChooser.getSelectedItem().toString());
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
