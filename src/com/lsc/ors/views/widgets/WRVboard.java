package com.lsc.ors.views.widgets;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.beans.OutpatientLog;

public class WRVboard extends Canvas{

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 7888750754621935303L;

	/**
	 * mouse action listener
	 */
	private WRVBoardMouseListener wbml = new WRVBoardMouseListener();
	
	/**
	 * visual data
	 */
	private OutpatientLog[] dataList = null;
	
	public WRVboard(OutpatientLog[] list) throws HeadlessException {
		super();

		this.dataList = list;
		
		setBounds(0, 0, 600, 400);
		setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		
		addMouseListener(wbml);
		addMouseMotionListener(wbml);
		
		new Thread(new AnimThread()).start();
	}
	
	/**
	 * Set the date of the displaying part of data set
	 * @param date
	 */
	public void setData(OutpatientLog[] list){
		dataList = list;
	}

	private static final int RECTHEIGHT = 10;
	private static final int RECTGAP = 2;
	private static final int RECTWIDTHUNIT = 1;
	private int offsetX,offsetY = 0;
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		
		if(dataList == null || dataList.length == 0) 
			return;
		
		autoAdjustOffset();
		//draw data
		Date rt = dataList[0].getRegistration_time();
		if(rt == null) return;
		long base = rt.getTime();
		g.setColor(Color.BLACK);
		OutpatientLog vdb = null;
		for(int i=0 ; i<dataList.length ; i++){
			vdb = dataList[i];
			g.drawRect(offsetX + RECTWIDTHUNIT * (int)(vdb.getRegistration_time().getTime() - base)/(60 * 1000),
					offsetY + i * (RECTHEIGHT + RECTGAP),
					RECTWIDTHUNIT * vdb.getWaiting_time(),
					RECTHEIGHT);
		}
	}
	
	private int adjustSpeed = 3;
	private boolean isReleased = true; 
	private void autoAdjustOffset(){
		if(!isReleased) return;
		
		if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
		else if(offsetX > 0) offsetX--;
		
		if(offsetY > 5) offsetY -= (offsetY / adjustSpeed);
		else if(offsetY > 0) offsetY--;
	}
	
	class WRVBoardMouseListener implements MouseListener,MouseMotionListener{

		private int firstX,firstY = 0;
		
		private int originalX, originalY = 0;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			isReleased = false;
			///debug
//			System.out.println("first:(" + e.getX() + ", " + e.getY() + ")");
			//set start point
			firstX = e.getX();
			firstY = e.getY();
			
			//save original offset
			originalX = offsetX;
			originalY = offsetY;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			isReleased = true;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			offsetX = originalX + (e.getX() - firstX);
			offsetY = originalY + (e.getY() - firstY);
			///debug
//			System.out.println("drag:(" + offsetX + ", " + offsetY + ")");
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	
	class AnimThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!Thread.currentThread().isInterrupted()){
				try {
					///debug
					repaint();
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
