package com.lsc.ors.views;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.src.StringSet;

public class QSVboard extends VisualizationBoard {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	
	private int feature = StringSet.CMD_TIME_UNIT_DAY;
	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -2108585612607783570L;
	
	public QSVboard(OutpatientLog[] list, ActionListener listener){
		super(listener, list);
		
		setBounds(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void setData(OutpatientLog[] list, int type) {
		// TODO Auto-generated method stub
		setData(list, type, feature);
	}
	public void setData(OutpatientLog[] list, int type, int feature){
		
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
