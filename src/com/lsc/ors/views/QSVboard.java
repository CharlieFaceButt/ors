package com.lsc.ors.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;

public class QSVboard extends VisualizationBoard {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	private static final int MARGIN = 10;
	/**
	 * 当前特征变量标识,对应OutpaitnetLog属性的index
	 */
	private int featureType = OutpatientLog.INDEX_DOCTOR;
	/**
	 * 当前特征可能取值,以及各取值对应的数量
	 */
	private Map<String, Integer> featureWaitingCountList = null;
	private Map<String, Integer> featureReceptCountList = null;
	/**
	 * 全部数据
	 */
	private OutpatientLog[] list = null;
	/**
	 * 等待队列
	 */
	private LinkedList<OutpatientLog> waitingList = new LinkedList<OutpatientLog>();
	/**
	 * 已就诊列表
	 */
	private LinkedList<OutpatientLog> receptList = new LinkedList<OutpatientLog>();
	/**
	 * 当前数据显示的对应时刻
	 */
	private int currentTime = 0;
	/**
	 * 标尺对应时间，目标时间
	 */
	int targetTime = 0;
	/**
	 * 基准时间，最早时间
	 */
	private int baseTime = 0;
	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -2108585612607783570L;
	
	public QSVboard(OutpatientLog[] list, ActionListener listener){
		super(listener, list);
		setBounds(0, 0, WIDTH, HEIGHT);
		setData(list);
	}

	public void setTargetTime(int time){
		targetTime = time;
		isRepaintable = true;
	}
	/**
	 * 
	 * @param type feature的对应index， 0-17
	 */
	public void setFeatureType(int fType){
		featureType = fType;
		initFeatureList(fType);
		repaint();
	}
	@Override
	public void setData(OutpatientLog[] list, int type) {
		// TODO Auto-generated method stub
		featureType = OutpatientLog.INDEX_DOCTOR;
		setData(list, type, featureType);
	}
	public void setData(OutpatientLog[] list, int type, int fType){
		this.list = list;
		offsetX = offsetY = 0;
		sortListByRegistrationTime();
		initFeatureList(fType);
		baseTime = getMinutesAmountFromDate(list[0].getRegistration_time());
		currentTime = baseTime;
		targetTime = baseTime;
	}
	/**
	 * 生成feature取值列表
	 * @param fType
	 */
	private void initFeatureList(int fType){
		this.featureType = fType;
		featureWaitingCountList = new HashMap<String, Integer>();
		featureReceptCountList = new HashMap<String, Integer>();
		waitingList = new LinkedList<OutpatientLog>();
		receptList = new LinkedList<OutpatientLog>();
		if(list == null) return;
		for(OutpatientLog ol : list){
			String f = ol.get(featureType);
			f = getFeatureValue(f);
			if(!featureWaitingCountList.containsKey(f)){
				featureWaitingCountList.put(f, 0);
				featureReceptCountList.put(f, 0);
				ConsoleOutput.pop("QSVboard.beforePaint", "feature:" + f);
			}
		}
	}
	private void sortListByRegistrationTime(){
		int key = 0;
		if(list == null) return;
		for(int i = 1 ; i < list.length ; i ++){
			OutpatientLog ol = list[i];
			key = getMinutesAmountFromDate(ol.getRegistration_time());
			int j = i - 1;
			while(j >= 0 && getMinutesAmountFromDate(list[j].getRegistration_time()) > key){
				list[j+1] = list[j];
				j --;
			}
			list[j + 1] = ol;
		}
	}
	
	public int getCurrentTime(){
		return currentTime;
	}
	public int getTargetTime(){
		return targetTime;
	}
	public int getID(){
		return (int)serialVersionUID;
	}
	
	private int startX = 0, startY = 0;
	private int originalX = 0, originalY = 0;
	private boolean isReleased = true; 
	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		startX = e.getX();
		startY = e.getY();
		originalX = offsetX;
		originalY = offsetY;
		isReleased =false;
		isRepaintable = true;
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		isReleased = true;
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		offsetX = originalX + (e.getX() - startX);
		offsetY = originalY + (e.getY() - startY);
		isRepaintable = true;
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseY = e.getY();
		mouseAlignEnabled = true;
		repaint();
	}

	private int middleLineY = HEIGHT / 2;
	private int rectWidth = 40;
	private int rectHeightUnit = 8;
	private int rectMargin = MARGIN;
	private int ageDivider = 4;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		Set<String> keySet = featureWaitingCountList.keySet();
		String[] keys = new String[keySet.size()];
		if(featureType == OutpatientLog.INDEX_PATIENT_AGE){
			keys = new String[ageDivider + 1];
			for(int i = 0 ; i < ageDivider ; i ++){
				int quatient = 80 / ageDivider;
				keys[i] = "" + (i * quatient) + "-" + ((i + 1) * quatient);
			}
			keys[ageDivider] = "80-";
		}else{
			keySet.toArray(keys);
		}
		g.drawLine(
				offsetX + RULER_WIDTH, 
				offsetY + middleLineY - MARGIN, 
				offsetX + RULER_WIDTH + rectMargin + (rectWidth + rectMargin) * keys.length,
				offsetY + middleLineY - MARGIN);
		g.drawLine(
				offsetX + RULER_WIDTH, 
				offsetY + middleLineY + MARGIN,
				offsetX + RULER_WIDTH + rectMargin + (rectWidth + rectMargin) * keys.length,
				offsetY + middleLineY + MARGIN);
		
		Integer count = 0;
		int i = 0;
		g.setColor(Color.YELLOW);
		for(String key : keys){
			count = featureWaitingCountList.get(key);
			if(count == null) continue;
			g.fillRect(
					offsetX + RULER_WIDTH + rectMargin + (rectWidth + rectMargin) * i, 
					offsetY + middleLineY - MARGIN - rectHeightUnit * count,
					rectWidth, rectHeightUnit * count);
			i ++;
		}
		i = 0;
		g.setColor(Color.GREEN);
		for(String key: keys){
			count = featureReceptCountList.get(key);
			if(count == null) continue;
			g.fillRect(
					offsetX + RULER_WIDTH + rectMargin + (rectWidth + rectMargin) * i, 
					offsetY + middleLineY + MARGIN,
					rectWidth, rectHeightUnit * count);
			i ++;
		}
		g.setColor(Color.BLACK);
		i = 0;
		for(String key : keys){
			count = featureReceptCountList.get(key);
			if(count == null) continue;
			if(key == null || key.length() == 0){
				key = "null";
			}
			g.drawString(
					key,
					offsetX + RULER_WIDTH + rectMargin + (rectWidth + rectMargin) * i, 
					offsetY + middleLineY + MARGIN / 2);
			i ++;
		}
		paintMouseAlign(g);
		paintRulers(g);
	}
	
	private boolean mouseAlignEnabled = false;
	private int mouseY = 0;
	private void paintMouseAlign(Graphics g){
		if(!mouseAlignEnabled) return;
		int count = 0;
		int upperBase = offsetY + middleLineY - MARGIN;
		int lowerBase = offsetY + middleLineY + MARGIN;
		if(mouseY < upperBase){
			count = (upperBase - mouseY + rectHeightUnit / 2) / rectHeightUnit;
			mouseY = upperBase - count * rectHeightUnit;
		} else if(mouseY > lowerBase){
			count = (mouseY - lowerBase + rectHeightUnit / 2) / rectHeightUnit;
			mouseY = lowerBase + count * rectHeightUnit;
		}
		g.drawLine(RULER_WIDTH, mouseY, WIDTH, mouseY);
		g.drawString("" + count, WIDTH - RULER_WIDTH, mouseY);
		mouseAlignEnabled = false;
	}
	
	private int slash = 10;
	private void paintRulers(Graphics g){
		g.drawLine(RULER_WIDTH, 0, RULER_WIDTH, HEIGHT);
		int k = 0;
		for (int i = offsetY + middleLineY - MARGIN; i > 0; i -= rectHeightUnit * slash) {
			g.drawLine(RULER_WIDTH - 3, i, RULER_WIDTH, i);
			g.drawString("" + k, 0, i + MARGIN / 2);
			k += slash;
		}
		k = 0;
		for (int j = offsetY + middleLineY + MARGIN; j < HEIGHT; j += rectHeightUnit * slash) {
			g.drawLine(RULER_WIDTH - 3, j, RULER_WIDTH, j);
			g.drawString("" + k, 0, j + MARGIN / 2);
			k += slash;
		}
	}

	private int adjustSpeed = 3;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		//update waiting list and reception list and their counts
		if(targetTime != currentTime){
			currentTime = targetTime;
			OutpatientLog ol = null;
			String f = null;
			for (int i = 0; i < list.length; i++) {
				ol = list[i];
				f = ol.get(featureType);
				f = getFeatureValue(f);
				int count = 0;
				//if reception time
				if(currentTime > getMinutesAmountFromDate(ol.getReception_time())){
					//add to reception list
					if(!receptList.contains(ol)){
						receptList.add(ol);
						count = featureReceptCountList.get(f);
						featureReceptCountList.put(f, count + 1);
					}
					//remove from waiting list
					if(waitingList.contains(ol)){
						waitingList.remove(ol);
						count = featureWaitingCountList.get(f);
						featureWaitingCountList.put(f, count - 1);
					}
				}else{	//if not reception time
					//remove from reception list
					if(receptList.contains(ol)){
						receptList.remove(ol);
						count = featureReceptCountList.get(f);
						featureReceptCountList.put(f, count - 1);
					}
					//if registration time
					if(currentTime > getMinutesAmountFromDate(ol.getRegistration_time())){
						//add to waiting list
						if(!waitingList.contains(ol)){
							waitingList.add(ol);
							count = featureWaitingCountList.get(f);
							featureWaitingCountList.put(f, count + 1);
						}
					}
					//if not registration time
					else if(waitingList.contains(ol)){
						//remove from waiting list
						waitingList.remove(ol);
						count = featureWaitingCountList.get(f);
						featureWaitingCountList.put(f, count - 1);
					}
				}
			}
		}
		
		//automatically adjust offsets
		if(isReleased){
			if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
			else if(offsetX > 0) offsetX--;

			if(offsetY > HEIGHT / 3 + 5) offsetY -= ((offsetY - HEIGHT / 3) / adjustSpeed);
			else if(offsetY > HEIGHT / 3) offsetY--;

			if(offsetY < -HEIGHT / 3 - 5) offsetY += ((-offsetY - HEIGHT / 3) / adjustSpeed);
			else if(offsetY < -HEIGHT / 3) offsetY++;
			
			if(offsetX <= 0 && offsetY <= HEIGHT / 3 && offsetY >= -HEIGHT / 3) isRepaintable = false;
		}
		
		//view details
		if(featureType == OutpatientLog.INDEX_DIAGNOSES){
			rectWidth = 80;
		}
		else rectWidth = 40;
	}
	private String getFeatureValue(String feature){
		String newFeature = "null";
		if(featureType == (OutpatientLog.INDEX_PATIENT_AGE)){
			if(feature == null) return newFeature;
			Integer age = Integer.parseInt(feature);
			int quatient = 80 / ageDivider;
			newFeature = "" + (quatient * (age / quatient)) + "-";
			if(age < 80) newFeature += ("" + ((age / quatient + 1) * quatient));
		} else if(feature == null || feature.length() == 0){
			newFeature = "null";
		} else{
			newFeature = feature;
		}
		return newFeature;
	}

	@Override
	protected void onMouseWheel(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		targetTime += e.getWheelRotation();
		ConsoleOutput.pop("QSVboard.onMouseWheel", "targetTime:" + targetTime / 60 + ":" + targetTime % 60);
		isRepaintable = true;
		al.actionPerformed(new ActionEvent(this, (int)serialVersionUID, StringSet.MOUSE_WHEEL));
	}

	@Override
	protected void onMouseExit(MouseEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}
}
