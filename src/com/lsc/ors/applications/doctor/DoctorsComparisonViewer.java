package com.lsc.ors.applications.doctor;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

import javax.swing.JComboBox;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.views.analysis.DCTCPboard;

public class DoctorsComparisonViewer extends DCTAnalysisModelObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8806924478076663590L;

	JComboBox characterChooser;
	public DoctorsComparisonViewer(ModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		board = new DCTCPboard(dataList, mocl);
		characterChooser = new JComboBox(OutpatientLogCharacters.KEYS);
		board.setBackground(Color.WHITE);
		
		//bounds
		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		characterChooser.setBounds(
				MARGIN + 4 * (BUTTON_WIDTH + MARGIN), 
				HEIGHT - MARGIN - 3 * (BUTTON_HEIGHT + MARGIN),
				CHOOSER_WIDHT, CHOOSER_HEIGHT);
		
		displayer.add(board);
		add(characterChooser);
		characterChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				((DCTCPboard)board).changeCharacter(characterChooser.getSelectedItem().toString());
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
	}

}
