package com.lsc.ors.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;

public class WRVboard extends VisualizationBoard{

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 7888750754621935303L;
	
	public static long getSerialID(){
		return serialVersionUID;
	}

	
	
	public WRVboard(OutpatientLog[] list, ActionListener listener){
		super(listener, list);
		
		setBounds(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void setData(OutpatientLog[] list, int type){
		if(type < StringSet.CMD_TIME_UNIT_DAY || type > StringSet.CMD_TIME_UNIT_YEAR) return;
		timeUnitType = type;
		dataList = list;
		offsetX = RULER_WIDTH;
		offsetY = RULER_WIDTH;
		firstRegistLogIndex = 0;
		firstRecepLogIndex = 0;
		sortLogs();
		repaint();
	}
	
	private int firstRegistLogIndex = 0;
	private int firstRecepLogIndex = 0;
	private void sortLogs(){
		if(dataList == null) return;
		int minutes = getMinutesAmountFromDate(dataList[0].getReception_time());
		int temp = 0;
		for(int i = 1 ; i < dataList.length ; i ++){
			temp = getMinutesAmountFromDate(dataList[i].getReception_time());
			if(minutes > temp){
				minutes = temp;
				firstRecepLogIndex = i;
			}
		}
		minutes = getMinutesAmountFromDate(dataList[0].getRegistration_time());
		temp = 0;
		for(int k = 0 ; k < dataList.length ; k++){
			temp = getMinutesAmountFromDate(dataList[k].getRegistration_time());
			if(minutes > temp){
				minutes = temp;
				firstRegistLogIndex = k;
			}
		}
		ConsoleOutput.pop("WRVboard.sortLogs", "first reg:" + firstRegistLogIndex + "\tfirst recp:" + firstRecepLogIndex);
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




	private int firstX = 0,firstY = 0;
	
	private int originalX = 0, originalY = 0;

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int y = e.getY();
		int index = -1;
		if(timeUnitType == StringSet.CMD_TIME_UNIT_DAY)
			index = (y - RULER_WIDTH) / (RECTHEIGHT + RECTGAP);
		else
			index = -1;
		if(al != null){
			if(index < 0 || index >= dataList.length)
				al.actionPerformed(new ActionEvent(StringSet.VACANT_CONTENT, (int)serialVersionUID, StringSet.VACANT_CONTENT));
			else
				al.actionPerformed(new ActionEvent(dataList[index], (int)serialVersionUID, StringSet.MOUSE_CLICK));
		}
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		isReleased = false;
		isRepaintable = true;
		///debug
//		System.out.println("first:(" + e.getX() + ", " + e.getY() + ")");
		//set start point
		firstX = e.getX();
		firstY = e.getY();
		
		//save original offset
		originalX = offsetX;
		originalY = offsetY;
	}



	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		isReleased = true;
	}



	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		offsetX = originalX + (e.getX() - firstX);
		offsetY = originalY + (e.getY() - firstY);
	}



	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		alignX = e.getX();
		alignY = e.getY();
		mouseAlignEnabled = true;
		repaint();
	}

	private static final int RECTHEIGHT = 10;
	private static final int RECTGAP = 2;
	private static final int RECTWIDTHUNIT = 1;
	private static final int RULER_WIDTH = 20;
	private int offsetX = RULER_WIDTH,offsetY = RULER_WIDTH;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		autoAdjustOffset();
		
		if(dataList == null || dataList.length == 0) {
			g.drawString(StringSet.VACANT_CONTENT, WIDTH / 2 - 20, HEIGHT / 2 -3);
			return;
		}
		
		//draw data
		int registBase = getMinutesAmountFromDate(dataList[firstRegistLogIndex].getRegistration_time());
		int receptBase = getMinutesAmountFromDate(dataList[firstRecepLogIndex].getReception_time());
		OutpatientLog opl = null;
		int diameter = RECTWIDTHUNIT;
		switch (timeUnitType) {
		case StringSet.CMD_TIME_UNIT_DAY:
			g.setColor(Color.BLACK);
			for(int i=0 ; i<dataList.length ; i++){
				opl = dataList[i];
				g.drawRect(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - registBase),
						offsetY + i * (RECTHEIGHT + RECTGAP),
						RECTWIDTHUNIT * opl.getWaiting_time(),
						RECTHEIGHT);
				g.drawString("" + opl.getWaiting_time(), 
						offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - registBase), 
						offsetY + i * (RECTHEIGHT + RECTGAP) + RECTHEIGHT);
			}
			break;
		case StringSet.CMD_TIME_UNIT_WEEK:
			diameter = RECTWIDTHUNIT * 8;
			for (int i = 0; i < dataList.length; i++) {
				opl = dataList[i];
				g.setColor(Color.GREEN);
				g.drawOval(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - receptBase),
						offsetY + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - registBase),
						diameter, diameter);
			}
			g.setColor(Color.BLACK);
			break;
		case StringSet.CMD_TIME_UNIT_MONTH:
			diameter = RECTWIDTHUNIT * 5;
			for (int i = 0; i < dataList.length; i++) {
				opl = dataList[i];
				g.setColor(Color.GREEN);
				g.fillOval(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - receptBase),
						offsetY + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - registBase),
						diameter, diameter);
			}
			g.setColor(Color.BLACK);
			break;
		case StringSet.CMD_TIME_UNIT_YEAR:
			diameter = RECTWIDTHUNIT * 3;
			for (int i = 0; i < dataList.length; i++) {
				opl = dataList[i];
				g.setColor(Color.GREEN);
				g.fillOval(offsetX + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getReception_time()) - receptBase),
						offsetY + RECTWIDTHUNIT * (getMinutesAmountFromDate(opl.getRegistration_time()) - registBase),
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
	private int alignY = 0;
	private void paintMouseAlign(Graphics g){
		if(!mouseAlignEnabled) return;
		g.drawLine(alignX, RULER_WIDTH, alignX, HEIGHT - SLASH_LENGTH);
		int baseTime = 0;
		if(timeUnitType == StringSet.CMD_TIME_UNIT_DAY) 
			baseTime = getMinutesAmountFromDate(dataList[firstRegistLogIndex].getRegistration_time());
		else baseTime = getMinutesAmountFromDate(dataList[firstRecepLogIndex].getReception_time());
		int mouseTime = baseTime + (alignX - offsetX) / RECTWIDTHUNIT;
		g.drawString("" + (mouseTime / 60) + ":" + (mouseTime % 60), alignX - 10, HEIGHT);
		if(timeUnitType != StringSet.CMD_TIME_UNIT_DAY){
			g.drawLine(RULER_WIDTH, alignY, WIDTH, alignY);
			baseTime = getMinutesAmountFromDate(dataList[firstRegistLogIndex].getRegistration_time());
			mouseTime = baseTime + (alignY - offsetY) / RECTWIDTHUNIT;
			g.drawString("" + ((mouseTime / 60) % 24) + ":" + (mouseTime % 60), WIDTH - 50, alignY);
		}
		mouseAlignEnabled = false;
	}
	
	private static final int SLASH_LENGTH = 10;
	private void paintRulers(Graphics g){
		
		g.drawLine(0, RULER_WIDTH, WIDTH, RULER_WIDTH);
		
		//time dimension x
		int baseTime = 0;
		OutpatientLog opl = null;
		if(timeUnitType == StringSet.CMD_TIME_UNIT_DAY) {
			opl = dataList[firstRegistLogIndex];
			baseTime = getMinutesAmountFromDate(opl.getRegistration_time());
		}
		else{
			opl = dataList[firstRecepLogIndex];
			baseTime = getMinutesAmountFromDate(opl.getReception_time());
		}
		int minutesOffset = baseTime % 60;
		int slash = baseTime / 60;
		for(int slashPos = offsetX - minutesOffset * RECTWIDTHUNIT ; slashPos < WIDTH ; slashPos += (60 * RECTWIDTHUNIT)){
			g.drawLine(slashPos, RULER_WIDTH - SLASH_LENGTH, slashPos, RULER_WIDTH);
			String str = "" + (slash % 24) + ":00";
			g.drawString(str, slashPos - 15, RULER_WIDTH - SLASH_LENGTH);
			slash ++;
		}
		//time dimension y
		if(timeUnitType != StringSet.CMD_TIME_UNIT_DAY){
			g.drawLine(RULER_WIDTH, 0, RULER_WIDTH, HEIGHT);
			opl = dataList[firstRegistLogIndex];
			baseTime = getMinutesAmountFromDate(opl.getRegistration_time());
			minutesOffset = baseTime % 60;
			slash = baseTime / 60;
			for (int slashPos = offsetY - minutesOffset * RECTWIDTHUNIT; slashPos < WIDTH; slashPos += (60 * RECTWIDTHUNIT)) {
				g.drawLine(RULER_WIDTH - SLASH_LENGTH, slashPos, RULER_WIDTH, slashPos);
				String str = "" + (slash % 24) + ":00";
				g.drawString(str, RULER_WIDTH, slashPos - 3);
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

}
