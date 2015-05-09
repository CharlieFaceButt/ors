package com.lsc.ors.applications.statistics;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComboBox;

import com.lsc.ors.applications.ModelObject;
import com.lsc.ors.applications.analysis.AnalysisModelObject;
import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.views.analysis.ADboard;
import com.lsc.ors.views.visualization.WRVboard;

public class AttributeDescriptionViewer extends StatisticsModelObject{

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 5280794829008571577L;
	
	
	JComboBox characterChooser;
	JComboBox diagramTypeChooser;

	public AttributeDescriptionViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		board = new ADboard(dataList, mocl);
		board.setBackground(Color.WHITE);
		characterChooser = new JComboBox(OutpatientLogCharacters.KEYS);
		diagramTypeChooser = new JComboBox(new String[]{"频度表","饼图","条状图","百分位图"});
		Label diagramTypeTitle = new Label("图示种类：");

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
		
		displayer.add(board);
		add(characterChooser);
		analyzer.add(diagramTypeChooser);
		analyzer.add(diagramTypeTitle);
		
		characterChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((ADboard)board).changeCharacter(characterChooser.getSelectedItem().toString());
			}
		});
		diagramTypeChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((ADboard)board).changeDiagramType(diagramTypeChooser.getSelectedIndex());
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
