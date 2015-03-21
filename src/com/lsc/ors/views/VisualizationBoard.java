package com.lsc.ors.views;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Date;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.src.StringSet;

public abstract class VisualizationBoard extends Canvas {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = 2703670296950660542L;

	boolean isRepaintable = false;
	
	protected int timeUnitType = StringSet.CMD_TIME_UNIT_DAY;
	
	/**
	 * ����Ӧ���ݸ�board�ĵ�����
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

	public int getTimeUnitType(){
		return timeUnitType;
	}

	public void setData(OutpatientLog[] list){
		setData(list, timeUnitType);
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
	

	class BoardMouseListener implements MouseListener,MouseMotionListener,MouseWheelListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			onMouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			onMousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			onMouseReleased(e);
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
		}
		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		beforePaint();
		super.paint(g);
		onPaint(g);
	}
	
	/**
	 * ����ʱ��ȷ������׼�ߵ�x����
	 * @param date
	 * @return
	 */
	protected int getMinutesAmountFromDate(Date date){
		if(date == null) return 0;
		return date.getHours() * 60 + date.getMinutes();
	}

	/**
	 * Set the date of the displaying part of data set
	 * @param date
	 */
	public abstract void setData(OutpatientLog[] list, int type);
	protected abstract void onMouseClicked(MouseEvent e);
	protected abstract void onMousePressed(MouseEvent e);
	protected abstract void onMouseReleased(MouseEvent e);
	protected abstract void onMouseDragged(MouseEvent e);
	protected abstract void onMouseMoved(MouseEvent e);
	protected abstract void onMouseWheel(MouseEvent e);
	protected abstract void beforePaint();
	protected abstract void onPaint(Graphics g);
}
