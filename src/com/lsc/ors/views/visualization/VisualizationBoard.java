package com.lsc.ors.views.visualization;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Date;


import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.resource.StringSet;

public abstract class VisualizationBoard extends Canvas {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = 2703670296950660542L;
	public int getID(){
		return (int)serialVersionUID;
	}
	
	protected static final int WIDTH = 600;
	protected static final int HEIGHT = 400;

	/**
	 * 判断是否需要重画
	 */
	boolean isRepaintable = false;
	/**
	 * 可能取值：<br>
	 * StringSet.CMD_TIME_UNIT_DAY<br>
	 * StringSet.CMD_TIME_UNIT_WEEK<br>
	 * StringSet.CMD_TIME_UNIT_MONTH<br>
	 * StringSet.CMD_TIME_UNIT_YEAR<br>
	 */
	protected int timeUnitType = StringSet.CMD_TIME_UNIT_DAY;
	
	/**
	 * 将响应传递给board的调用者
	 */
	protected ActionListener al = null;
	
	/**
	 * visual data
	 */
	protected OutpatientLog[] dataList = null;
	/**
	 * mouse action listener
	 */
	private BoardMouseListener bml = new BoardMouseListener();
	
	public VisualizationBoard(ActionListener listener, OutpatientLog[] dataList) {
		super();

		setData(dataList);
		al = listener;
		
		setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		
		addMouseListener(bml);
		addMouseMotionListener(bml);
		addMouseWheelListener(bml);
		
		new Thread(new AnimThread()).start();
	}
	/**
	 * 
	 * @return 可能取值:<br>
	 * StringSet.CMD_TIME_UNIT_DAY<br>
	 * StringSet.CMD_TIME_UNIT_WEEK<br>
	 * StringSet.CMD_TIME_UNIT_MONTH<br>
	 * StringSet.CMD_TIME_UNIT_YEAR<br>
	 */
	public int getTimeUnitType(){
		return timeUnitType;
	}

	public void setData(OutpatientLog[] list){
		setData(list, timeUnitType);
		isRepaintable = true;
	}
	
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
	

	protected int startX = 0, startY = 0;
	protected int originalX = 0, originalY = 0;
	protected boolean isReleased = true; 
	class BoardMouseListener implements MouseListener,MouseMotionListener,MouseWheelListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			onMouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			startX = e.getX();
			startY = e.getY();
			originalX = offsetX;
			originalY = offsetY;
			isReleased =false;
			isRepaintable = true;
			onMousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			isReleased = true;
			onMouseReleased(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			onMouseExit(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			offsetX = originalX + (e.getX() - startX);
			offsetY = originalY + (e.getY() - startY);
			isRepaintable = true;
			onMouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			onMouseMoved(e);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			onMouseWheel(e);
			al.actionPerformed(new ActionEvent(e, getID(), StringSet.MOUSE_WHEEL));
		}
		
	}
	
	protected static final int RULER_WIDTH = 20;
	protected static final int SLASH_LENGTH = 10;
	protected int offsetX = 0,offsetY = 0;
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		beforePaint();
		super.paint(g);
		if(dataList == null){
			g.drawString(StringSet.VACANT_CONTENT, WIDTH / 2 - 10, HEIGHT / 2);
		}else{
			onPaint(g);
		}
	}
	
	/**
	 * 根据时间确定鼠标对准线的x坐标
	 * @param date
	 * @return
	 */
	public static final int getMinutesAmountFromDate(Date date){
		if(date == null) return 0;
		return date.getHours() * 60 + date.getMinutes();
	}
	
	/**
	 * 获得总看病人数
	 * @return
	 */
	public int getTotalOutpatientNumber(){
		if(dataList == null) return -1;
		return dataList.length;
	}

	/**
	 * Set the date of the displaying part of data set
	 * @param date
	 */
	public abstract void setData(OutpatientLog[] list, int type);
	protected abstract void onMouseClicked(MouseEvent e);
	protected abstract void onMousePressed(MouseEvent e);
	protected abstract void onMouseReleased(MouseEvent e);
	protected abstract void onMouseExit(MouseEvent e);
	protected abstract void onMouseDragged(MouseEvent e);
	protected abstract void onMouseMoved(MouseEvent e);
	protected abstract void onMouseWheel(MouseWheelEvent e);
	protected abstract void beforePaint();
	protected abstract void onPaint(Graphics g);
}
