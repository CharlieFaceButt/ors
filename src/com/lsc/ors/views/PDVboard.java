package com.lsc.ors.views;

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
	Integer featureType = null;
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
	private int waitingTimeDivider = 10;
	
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
		if(waitingTimeDivider == 0) waitingTimeDivider = 10;
		setFeature(fType);
		
		//init lists
		OutpatientLog ol = null;
		int wait = 0;
		int quotient = 0;
		String key = null;
		for (int i = 0; i < dataList.length; i++) {
			ol = dataList[i];
			//set count list
			wait = ol.getWaiting_time();
			quotient = wait / waitingTimeDivider;
			key = "" + (quotient * waitingTimeDivider) + "-" + ((quotient + 1)* waitingTimeDivider);
			//set feature list
		}
	}
	private void setFeature(Integer fType){
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
		
		//remove old keys
		if(countLists == null)
			countLists = new HashMap<String, Map<String,Integer>>();
		for(String f : countLists.keySet()){
			if(f != null && !f.equals("all")){
				countLists.remove(f);
			}
		}
		//initiate key: all
		if(!countLists.containsKey("all")) 
			countLists.put("all", new HashMap<String, Integer>());
		//initiate other keys for all feature values
		if(featureType != null){
			OutpatientLog ol = null;
			String key = null;
			Map<String, Integer> map = null;
			int count = 0;
			String countStr = null;
			for (int i = 0; i < dataList.length; i++) {
				ol = dataList[i];
				key = ol.get(featureType);
				key = generateKeyValue(key);
				countStr = generateCountStr(ol.getWaiting_time());
				//locate feature value
				if(!countLists.containsKey(key)){
					map = new HashMap<String, Integer>();
					countLists.put(key, map);
				}else{
					map = countLists.get(key);
				}
				//add waiting count
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
			if(age == null) return newKey;
			newKey = generateKeyStrByDividingValue(age, ageDivider);
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
		
		if(offsetY > 5) offsetY -= (offsetY / adjustSpeed);
		else if(offsetY > 0) offsetY--;
		
		if(offsetX <= 0 || offsetY <= 0) isRepaintable = false;
	}

	private int rectGap = 10;
	private int rectWidth = 10;
	private int rectHeightUnit = 10;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> map = null;
		int bottom = HEIGHT - RULER_WIDTH;
		for(String featureKey : countLists.keySet()){
			if(!featureValues.get(featureKey)) continue;
			
			map = (HashMap<String, Integer>)countLists.get(featureKey);
			String[] split = null; 
			for(String waitStr : map.keySet()){
				if(waitStr.equals("null")){
					g.drawRect(
							offsetX + RULER_WIDTH + rectGap,  
							bottom - map.get(waitStr) * rectHeightUnit , 
							rectWidth, 
							map.get(waitStr) * rectHeightUnit);
				} else {
					split = waitStr.split("-");
					Integer wait = Integer.parseInt(split[0]);
					if(wait == null) continue;
					g.drawRect(
							offsetX + RULER_WIDTH + rectGap + (rectWidth + rectGap) * (wait / waitingTimeDivider),  
							bottom - map.get(waitStr) * rectHeightUnit, 
							rectWidth, 
							map.get(waitStr) * rectHeightUnit);
				}
			}
		}
	}

}
