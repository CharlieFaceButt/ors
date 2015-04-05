package com.lsc.ors.applications.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;


import com.lsc.ors.applications.listener.WRDModelListener;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.views.WRVboard;
import com.lsc.ors.views.widgets.TimeButtonGroup;

/**
 * Reference of Model 1<br>
 * Give a WRD view<br>
 * WRD: waiting record distribution
 * @author charlieliu
 *
 */
public class WaitingRecordViewer extends VisualizationModelObject{

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -3891273426875938845L;
	
	private static final int LABEL_LEFT_MARGIN = 20;
	private static final int LABEL_GAP = 10;
	private static final int LABEL_WIDTH = 150;
	private static final int LABEL_HEIGHT = 20;
	
	//view
	Label[] logInfos = null;

	public WaitingRecordViewer(WRDModelListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setTitle(StringSet.VSL_WAITING_RECORD_DISTRIBUTION);
		
		//initialize views
		board = new WRVboard(getDataByDate(currentDate), mocl);
		board.setBackground(Color.WHITE);

		//bounds
		setBounds(100, 50, WIDTH, HEIGHT);
		setResizable(false);
		
		//add views
		displayer.add(board);
		int i = 0;
		logInfos = new Label[OutpatientLog.KEYS.length];
		for(String key : OutpatientLog.KEYS){
			Label label = new Label(key);
			label.setBounds(LABEL_LEFT_MARGIN, LABEL_GAP + (LABEL_GAP + LABEL_HEIGHT) * i, LABEL_WIDTH, LABEL_HEIGHT);
			analyzer.add(label);
			
			logInfos[i] = new Label(StringSet.VACANT_CONTENT);
			logInfos[i].setBounds(LABEL_LEFT_MARGIN * 2 + LABEL_WIDTH, LABEL_GAP + (LABEL_GAP + LABEL_HEIGHT) * i, LABEL_WIDTH, LABEL_HEIGHT);
			analyzer.add(logInfos[i]);
			i ++;
			ConsoleOutput.pop("WaitingRecordViewer.create", "label" + i + "added");
		}
		analyzer.add(new Label(""));
		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(619, 0, 619, 620);
	}

	@Override
	protected void onDatePickerChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseWheelOnBoard(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if(board.getTimeUnitType() == StringSet.CMD_TIME_UNIT_DAY)
			increaseDate(Calendar.DAY_OF_YEAR, e.getWheelRotation());
	}

	@Override
	protected void onMouseClickOnBoard(Object source) {
		// TODO Auto-generated method stub
		if(source != null && StringSet.VACANT_CONTENT.equals(source.toString())){
			for (int i = 0; i < logInfos.length; i++) {
				logInfos[i].setText(StringSet.VACANT_CONTENT);
			}
		} else{
			OutpatientLog ol = null;
			if(source != null)
				ol = (OutpatientLog)source;
			for (int i = 0; i < logInfos.length; i++) {
				if(ol != null)
					logInfos[i].setText(ol.get(i));
				else
					logInfos[i].setText(StringSet.VACANT_CONTENT);
			}
		}
	}

	@Override
	protected void onTimeUnitChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseMoveOnBoard(Object source) {
		// TODO Auto-generated method stub
		
	}
	
}
