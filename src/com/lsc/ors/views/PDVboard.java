package com.lsc.ors.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.util.TimeFormatter;

public class PDVboard extends VisualizationBoard {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -1462378128134629296L;
	/**
	 * 当前特征维度
	 */
	Integer featureType;
	/**
	 * 记录可能的特征维度
	 */
	LinkedList<Integer> featureList;
	/**
	 * 当前特征维度的可能取值,以及该取值是否被显示
	 */
	Map<String, Boolean> featureValues;
	/**
	 * 二维数据存储：等待人数=countLists（特征取值，等待时间）
	 */
	Map<String,Map<String, Integer>> countLists;
	/**
	 * 每段的范围
	 */
	private int waitingTimeDivider;
	private int maxWaitingTime;
	
	public Map<String, Boolean> getFeatureValues(){
		return featureValues;
	}
	public Integer getFeatureType(){
		return featureType;
	}
	
	public PDVboard(ActionListener listener, OutpatientLog[] dataList) {
		super(listener, dataList);
		// TODO Auto-generated constructor stub
		
		setBounds(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void setData(OutpatientLog[] list, int type) {
		// TODO Auto-generated method stub
		setData(list, type, featureType);
	}
	public void setData(OutpatientLog[] list, int type, Integer fType){
		this.dataList = list;
		offsetX = offsetY = 0;
		sortListByRegistrationTime();
		setFeature(fType, 30);
		setCountLists();
		
		//init lists
		if(dataList == null) return;
		OutpatientLog ol = null;
		int wait = 0;
		int quotient = 0;
		String key = null;
		maxWaitingTime = 0;
		for (int i = 0; i < dataList.length; i++) {
			ol = dataList[i];
			//set count list
			wait = ol.getWaiting_time();
			if(wait > maxWaitingTime) maxWaitingTime = wait;
			quotient = wait / waitingTimeDivider;
			key = "" + (quotient * waitingTimeDivider) + "-" + ((quotient + 1)* waitingTimeDivider);
		}
	}
	private synchronized void setFeature(Integer fType, int divider){
		waitingTimeDivider = divider;
		//init feature list
		if(featureList == null || featureList.size() == 0){
			featureList = new LinkedList<Integer>();
			featureList.add(OutpatientLog.INDEX_DEPARTMENT);
			featureList.add(OutpatientLog.INDEX_DOCTOR);
			featureList.add(OutpatientLog.INDEX_PATIENT_GENDER);
			featureList.add(OutpatientLog.INDEX_PATIENT_AGE);
			featureList.add(OutpatientLog.INDEX_REGISTRATION);
			featureList.add(OutpatientLog.INDEX_RECEPTION);
			featureList.add(OutpatientLog.INDEX_DIAGNOSES);
			featureList.add(OutpatientLog.INDEX_FURTHER_CONSULTATION);
		}
		//if feature type not change then don't do anything
		if(featureType != null && featureType.equals(fType))
			return;
		//set feature type
		featureType = fType;
		//make sure feature type is legal
		if(featureType == null || !featureList.contains(featureType)) 
			featureType = featureList.get(0);
		ConsoleOutput.pop("PDVboard.setFeature", "" + OutpatientLog.KEYS[featureType]);
	}
	private void setCountLists(){
		
		//remove old keys
		if(countLists == null)
			countLists = new HashMap<String, Map<String,Integer>>();
		String[] fKeys = new String[countLists.keySet().size()];
		countLists.keySet().toArray(fKeys);
		for(String f : fKeys){
			if(f != null && !f.equals("all")){
				countLists.remove(f);
			}
		}
		//initiate key: all
		if(dataList == null) return;
		if(!countLists.containsKey("all")) 
			countLists.put("all", new HashMap<String, Integer>());
		//initiate other keys for all feature values
		if(featureType != null){
			OutpatientLog ol = null;
			String featureKey = null;
			Map<String, Integer> map = null;
			int count = 0;
			String countStr = null;
			for (int i = 0; i < dataList.length; i++) {
				ol = dataList[i];
				featureKey = ol.get(featureType);
				featureKey = generateKeyValue(featureKey);
				countStr = generateCountStr(ol.getWaiting_time());
				//locate feature value
				if(!countLists.containsKey(featureKey)){
					map = new HashMap<String, Integer>();
					countLists.put(featureKey, map);
				}else{
					map = countLists.get(featureKey);
				}
				//add waiting count
				if(countStr.equals("null")){
					continue;
				}
				if(!map.containsKey(countStr))
					map.put(countStr, 0);
				count = map.get(countStr);
				map.put(countStr, count + 1);
			}
		}
		//set feature list
		if(featureValues == null)
			featureValues = new HashMap<String, Boolean>();
		for(String featureValueKey : countLists.keySet()){
			featureValues.put(featureValueKey, true);
		}
	}
	/**
	 * 根据等待时间生成对应的取值范围key
	 * @param wait
	 * @return
	 */
	private String generateCountStr(int wait){
		return generateKeyStrByDividingValue(wait, waitingTimeDivider);
	}
	private String generateKeyStrByDividingValue(int value, int divider){
		int quotient = value / divider;
		return "" + (quotient * divider) + "-" + ((quotient + 1) * divider);
	}
	int ageDivider = 20;
	int timeDivider = 30;
	private String generateKeyValue(String key){
		String newKey = "null";
		if(key == null) return newKey;
		
		switch (featureType) {
		case OutpatientLog.INDEX_PATIENT_AGE:
			Integer age = Integer.parseInt(key);
			if(age == null) 
				return newKey;
			newKey = generateKeyStrByDividingValue(age, ageDivider);
			break;
		case OutpatientLog.INDEX_WAIT:
			Integer wait = Integer.parseInt(key);
			if(wait == null) return newKey;
			newKey = generateKeyStrByDividingValue(wait, waitingTimeDivider);
			break;
		case OutpatientLog.INDEX_RECEPTION:
		case OutpatientLog.INDEX_REGISTRATION:
			Date date = new Date(key);
			newKey = "" + getMinutesAmountFromDate(date);
			break;
		default:
			newKey = key;
			break;
		}
		return newKey;
	}
	private void sortListByRegistrationTime(){
		int key = 0;
		if(dataList == null) return;
		for(int i = 1 ; i < dataList.length ; i ++){
			OutpatientLog ol = dataList[i];
			key = getMinutesAmountFromDate(ol.getRegistration_time());
			int j = i - 1;
			while(j >= 0 && getMinutesAmountFromDate(dataList[j].getRegistration_time()) > key){
				dataList[j+1] = dataList[j];
				j --;
			}
			dataList[j + 1] = ol;
		}
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
	protected void onMouseExit(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseY = e.getY();
		mouseX = e.getX();
		mouseAlignEnabled = true;
		isRepaintable = true;
	}

	@Override
	protected void onMouseWheel(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	private int adjustSpeed = 3;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		if(!isReleased) return;
		
		if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
		else if(offsetX > 0) offsetX--;
		
		if(offsetX <= 0) isRepaintable = false;
	}

	private int rectWidth = 30;
	private int rectHeightUnit = 20;
	private int pointRadius = 3;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> map = null;
		int bottom = HEIGHT - RULER_WIDTH;
		g.setColor(Color.GREEN);
		int nPoints = maxWaitingTime / waitingTimeDivider + 2;
		int[] xPoints = null;
		int[] yPoints = null;
		for(String featureKey : countLists.keySet()){
			if(!featureValues.get(featureKey)) continue;
			map = (HashMap<String, Integer>)countLists.get(featureKey);
			String[] split = null; 
			xPoints = new int[nPoints];
			yPoints = new int[nPoints];
			for (int j = 0; j < nPoints; j ++) {
				xPoints[j] = offsetX + RULER_WIDTH + rectWidth * j + rectWidth / 2;
				yPoints[j] = HEIGHT - RULER_WIDTH;
			}
			int i = 0;
			for(String waitStr : map.keySet()){
				split = waitStr.split("-");
				Integer wait = Integer.parseInt(split[0]);
				if(wait == null) continue;
				i = wait / waitingTimeDivider;
				yPoints[i] = bottom - map.get(waitStr) * rectHeightUnit;
			}
			g.drawPolyline(xPoints, yPoints, nPoints);
			for (int k = 0; k < nPoints; k++) {
				g.fillOval(xPoints[k] - pointRadius, yPoints[k] - pointRadius, 2 * pointRadius, 2 * pointRadius);
			}
		}
		
		g.setColor(Color.BLACK);
		paintRulers(g);
		paintMouseAlign(g);
	}
	private int ySlash = 1;
	private void paintRulers(Graphics g){
		//draw axisX
		g.drawLine(0, HEIGHT - RULER_WIDTH, WIDTH, HEIGHT - RULER_WIDTH);
		for(int i = 0 ; i <= maxWaitingTime ; i += waitingTimeDivider){
			int x = offsetX + RULER_WIDTH + rectWidth * (i / waitingTimeDivider);
			g.drawString("" + i, x - rectWidth / 3,	HEIGHT);
			g.drawLine(x, HEIGHT - RULER_WIDTH, x, HEIGHT - RULER_WIDTH + SLASH_LENGTH);
		}
		//draw axisY
		g.drawLine(RULER_WIDTH, 0, RULER_WIDTH, HEIGHT);
		for(int i = HEIGHT - RULER_WIDTH , j = 0 ; i > 0 ; i -= (rectHeightUnit * ySlash), j += ySlash){
			g.drawLine(RULER_WIDTH - SLASH_LENGTH, i, RULER_WIDTH, i);
			g.drawString("" + j, 0, i);
		}
	}
	private boolean mouseAlignEnabled = false;
	private int mouseX = 0, mouseY = 0;
	private void paintMouseAlign(Graphics g){
		if(!mouseAlignEnabled) return;
		if(mouseY < HEIGHT - RULER_WIDTH && mouseX > RULER_WIDTH){
			mouseY = (HEIGHT - RULER_WIDTH - mouseY + rectHeightUnit / 2) / rectHeightUnit;
			mouseY = HEIGHT - RULER_WIDTH - rectHeightUnit * mouseY;
			g.drawLine(RULER_WIDTH, mouseY, WIDTH, mouseY);
		}
		mouseAlignEnabled = false;
	}

}
