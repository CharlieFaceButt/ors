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
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.util.TimeFormatter;

public class WRVboard extends Canvas{

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
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
		
		setBounds(0, 0, WIDTH, HEIGHT);
		setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		
		addMouseListener(wbml);
		addMouseMotionListener(wbml);
		
		new Thread(new AnimThread()).start();
	}

	private int timeUnitType = StringSet.CMD_TIME_UNIT_DAY;
	public void setData(OutpatientLog[] list){
		setData(list, timeUnitType);
	}
	/**
	 * Set the date of the displaying part of data set
	 * @param date
	 */
	public void setData(OutpatientLog[] list, int type){
		if(type < StringSet.CMD_TIME_UNIT_DAY || type > StringSet.CMD_TIME_UNIT_YEAR) return;
		timeUnitType = type;
		dataList = list;
		offsetX = RULER_WIDTH;
		offsetY = RULER_WIDTH;
		repaint();
	}
	public int getTimeUnitType(){
		return timeUnitType;
	}
	
	/**
	 * 根据时间确定鼠标对准线的x坐标
	 * @param date
	 * @return
	 */
	private int getMinutesAmountFromDate(Date date){
		if(date == null) return 0;
		return date.getHours() * 60 + date.getMinutes();
	}

	private static final int RECTHEIGHT = 10;
	private static final int RECTGAP = 2;
	private static final int RECTWIDTHUNIT = 1;
	private static final int RULER_WIDTH = 20;
	private int offsetX = RULER_WIDTH,offsetY = RULER_WIDTH;
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		
		if(dataList == null || dataList.length == 0) {
			g.drawString(StringSet.VACANT_CONTENT, WIDTH / 2 - 20, HEIGHT / 2 -3);
			return;
		}
		
		autoAdjustOffset();
		//draw data
		Date rt = dataList[0].getRegistration_time();
		if(rt == null) return;
		int base = getMinutesAmountFromDate(rt);
		OutpatientLog opl = null;
		int diameter = RECTWIDTHUNIT;
		switch (timeUnitType) {
		case StringSet.CMD_TIME_UNIT_DAY:
			g.setColor(Color.BLACK);
			for(int i=0 ; i<dataList.length ; i++){
				opl = dataList[i];
				g.drawRect(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - base),
						offsetY + i * (RECTHEIGHT + RECTGAP),
						RECTWIDTHUNIT * opl.getWaiting_time(),
						RECTHEIGHT);
				g.drawString("" + opl.getWaiting_time(), 
						offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - base), 
						offsetY + i * (RECTHEIGHT + RECTGAP) + RECTHEIGHT);
			}
			break;
		case StringSet.CMD_TIME_UNIT_WEEK:
			diameter = RECTWIDTHUNIT * 8;
			for (int i = 0; i < dataList.length; i++) {
				opl = dataList[i];
				g.setColor(Color.GREEN);
				g.drawOval(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - base),
						offsetY + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - base),
						diameter, diameter);
			}
			g.setColor(Color.BLACK);
			break;
		case StringSet.CMD_TIME_UNIT_MONTH:
			diameter = RECTWIDTHUNIT * 5;
			for (int i = 0; i < dataList.length; i++) {
				opl = dataList[i];
				g.setColor(Color.GREEN);
				g.fillOval(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - base),
						offsetY + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - base),
						diameter, diameter);
			}
			g.setColor(Color.BLACK);
			break;
		case StringSet.CMD_TIME_UNIT_YEAR:
			diameter = RECTWIDTHUNIT * 3;
			for (int i = 0; i < dataList.length; i++) {
				opl = dataList[i];
				g.setColor(Color.GREEN);
				g.fillOval(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - base),
						offsetY + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - base),
						diameter, diameter);
			}
			g.setColor(Color.BLACK);
			break;
		default:
			break;
		}
		paintRulers(g);
		paintMouseAlign(g);
	}
	
	
	private boolean mouseAlignEnabled = false;
	private int alignX = 0;
	private void paintMouseAlign(Graphics g){
		if(!mouseAlignEnabled) return;
		g.drawLine(alignX, RULER_WIDTH, alignX, HEIGHT);
		int baseTime = getMinutesAmountFromDate(dataList[0].getRegistration_time());
		int mouseTime = baseTime + (alignX - offsetX) / RECTWIDTHUNIT;
		g.drawString("" + (mouseTime / 60) + ":" + (mouseTime % 60), alignX - 10, RULER_WIDTH / 2);
		mouseAlignEnabled = false;
	}
	
	private static final int SLASH_LENGTH = 10;
	private void paintRulers(Graphics g){
		
		g.drawLine(0, RULER_WIDTH, WIDTH, RULER_WIDTH);
		g.drawLine(RULER_WIDTH, 0, RULER_WIDTH, HEIGHT);
		
		//time dimension
		OutpatientLog opl = dataList[0];
		int baseTime = getMinutesAmountFromDate(opl.getReception_time());
		int minutesOffset = baseTime % 60;
		int slash = baseTime / 60;
//		slash --;
		for(int slashPos = offsetX - minutesOffset * RECTWIDTHUNIT ; slashPos < WIDTH ; slashPos += (60 * RECTWIDTHUNIT)){
			g.drawLine(slashPos, RULER_WIDTH - SLASH_LENGTH, slashPos, RULER_WIDTH);
			String str = "" + slash + ":00";
			g.drawString(str, slashPos, RULER_WIDTH);
			slash ++;
		}
		baseTime = getMinutesAmountFromDate(opl.getRegistration_time());
		minutesOffset = baseTime % 60;
		slash = baseTime / 60;
		if(timeUnitType != StringSet.CMD_TIME_UNIT_DAY){
			for (int slashPos = offsetY - minutesOffset * RECTWIDTHUNIT; slashPos < WIDTH; slashPos += (60 * RECTWIDTHUNIT)) {
				g.drawLine(RULER_WIDTH - SLASH_LENGTH, slashPos, RULER_WIDTH, slashPos);
				String str = "" + slash + ":00";
				g.drawString(str, RULER_WIDTH, slashPos + 5);
				slash ++;
			}
		}
	}
	
	private int adjustSpeed = 3;
	private boolean isReleased = true; 
	private void autoAdjustOffset(){
		if(!isReleased) return;
		
		if(offsetX > (RULER_WIDTH + 5)) offsetX -= ((offsetX - RULER_WIDTH) / adjustSpeed);
		else if(offsetX > RULER_WIDTH) offsetX--;
		
		if(offsetY > (RULER_WIDTH + 5)) offsetY -= ((offsetY - RULER_WIDTH) / adjustSpeed);
		else if(offsetY > RULER_WIDTH) offsetY--;
		
		if(offsetX <= RULER_WIDTH || offsetY <= RULER_WIDTH) isRepaintable = false;
	}
	
	class WRVBoardMouseListener implements MouseListener,MouseMotionListener{

		private int firstX = 0,firstY = 0;
		
		private int originalX = 0, originalY = 0;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			isReleased = false;
			isRepaintable = true;
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
			repaint();
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
//			ConsoleOutput.pop("WRVboard.mouseMoved", "(" + e.getX() +"," + e.getY() +")");
			alignX = e.getX();
			mouseAlignEnabled = true;
			repaint();
		}
		
	}

	boolean isRepaintable = false;
	class AnimThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!Thread.currentThread().isInterrupted()){
				try {
					///debug
					if(isRepaintable)
						repaint();
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
