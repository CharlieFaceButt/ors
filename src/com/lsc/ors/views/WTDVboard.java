package com.lsc.ors.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


import com.lsc.ors.beans.AverageValueObject;
import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.util.TimeFormatter;

public class WTDVboard extends VisualizationBoard {

	/**
	 * generated serial ID	
	 */
	private static final long serialVersionUID = 1224787845530459977L;
	public int getID() {return (int)serialVersionUID;}
	/**
	 * 当前特征维度
	 */
	Integer featureType;
	LinkedList<String> featureValues;
	/**
	 * 二维数据存储：等待人数=countLists（特征取值，等待时间）
	 */
	Map<String, AverageValueObject> waitingTimeList;
	
	private int maxWaitingTime;
	
	public WTDVboard(ActionListener listener, OutpatientLog[] dataList) {
		super(listener, dataList);
		// TODO Auto-generated constructor stub
		
		setBounds(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void setData(OutpatientLog[] list, int type) {
		// TODO Auto-generated method stub
		setData(list, type, featureType);
		isRepaintable = true;
	}
	public void setData(OutpatientLog[] list, int type, Integer featureType){
		this.dataList = list;
		timeUnitType = type;
		setFeatureType(featureType);
		setTimeList();
		
		//set max waiting time
		AverageValueObject avo = null;
		maxWaitingTime = 0;
		for(String featureKey: waitingTimeList.keySet()){
			avo = waitingTimeList.get(featureKey);
			if(avo.divide == 0) continue;
			int time = (Integer)(avo.get(AverageValueObject.INDEX_AVERAGE));
			if(maxWaitingTime < time){
				maxWaitingTime = time;
			}
		}
	}
	private synchronized void setFeatureType(Integer fType){
		//set feature type
		featureType = fType;
		if(featureType == null)
			featureType = OutpatientLog.INDEX_DEPARTMENT;
		ConsoleOutput.pop("PDVboard.setFeature", "" + OutpatientLog.KEYS[featureType]);
		//initiate feature value list
		if(featureValues == null)
			featureValues = new LinkedList<String>();
		featureValues.clear();
	}
	private void setTimeList(){
		//remove old keys
		if(waitingTimeList == null)
			waitingTimeList = new HashMap<String,AverageValueObject>();
		else waitingTimeList.clear();
		AverageValueObject totalAVP = new AverageValueObject();
		waitingTimeList.put(StringSet.AVERAGE, totalAVP);
		featureValues.add(StringSet.AVERAGE);
		
		//initiate keys for all feature values
		if(dataList == null) return;
		if(featureType != null){
			OutpatientLog ol = null;
			String featureKey = null;
			AverageValueObject avo = null;
			for (int i = 0; i < dataList.length; i++) {
				ol = dataList[i];
				featureKey = ol.get(featureType);
				featureKey = generateKeyValue(featureKey);
				if(!waitingTimeList.containsKey(featureKey)){
					waitingTimeList.put(featureKey, new AverageValueObject());
					featureValues.add(featureKey);
				}
				avo = waitingTimeList.get(featureKey);
				avo.add(ol.getWaiting_time());
				
				totalAVP.add(ol.getWaiting_time());
			}
		}
	}
	private int ageDivider = 10;
	private int timeDivider = 120;
	private String generateKeyValue(String oldKey){
		String newKey = "null";
		if(oldKey == null) return newKey;
		switch (featureType) {
		case OutpatientLog.INDEX_PATIENT_AGE:
			Integer age = Integer.parseInt(oldKey);
			if(age == null) 
				return newKey;
			newKey = generateKeyStrByDividingValue(age, ageDivider);
			break;
		case OutpatientLog.INDEX_REGISTRATION:
		case OutpatientLog.INDEX_RECEPTION:
			Date date = TimeFormatter.deformat(oldKey, null);
			int minuteAmount = getMinutesAmountFromDate(date);
			newKey = generateKeyStrByDividingMinutes(minuteAmount, timeDivider);
			break;
		default:
			newKey = oldKey;
			break;
		}
		return newKey;
	}
	private String generateKeyStrByDividingValue(int value, int divider){
		int quotient = value / divider;
		return "" + (quotient * divider) + "-" + ((quotient + 1) * divider);
	}
	private String generateKeyStrByDividingMinutes(int value, int divider){
		int quotient = value / divider;
		int minutesAmount = quotient * divider;
		return "" + (minutesAmount / 60) + ":" + (minutesAmount % 60) + "-" +
				((minutesAmount + divider) / 60) + ":" +((minutesAmount + divider) % 60);
	}
	private int decodeMinutesFromKeyStr(String keyStr, int divider){
		if(keyStr == null) return -1;
		String[] timeSplit = keyStr.split("-");
		String[] digitSplit = timeSplit[0].split(":");
		Integer minutes = Integer.parseInt(digitSplit[0]) * 60;
		minutes += Integer.parseInt(digitSplit[1]);
		return minutes;
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
		hoverPos = -2;
		al.actionPerformed(new ActionEvent(new AverageValueObject(), getID(), StringSet.MOUSE_MOVE));
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private int alignX;
	private int alignY;
	private int hoverPos = -2;
	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		alignX = e.getX(); 
		alignY = e.getY();
		hoverPos = (alignX - RULER_WIDTH - rectGap - offsetX) / (rectWidth + rectGap);
		isRepaintable = true;

		String key = null;
		if(featureType == OutpatientLog.INDEX_RECEPTION || featureType == OutpatientLog.INDEX_REGISTRATION 
				|| featureType == OutpatientLog.INDEX_PATIENT_AGE){
			for(int i = 0 ; i < featureValues.size() ; i ++){
				if(hoverPos == calculateKeyPos(featureValues.get(i))){
					key = featureValues.get(i);
				}
			}
			if(key == null){
				al.actionPerformed(new ActionEvent(new AverageValueObject(), getID(), StringSet.MOUSE_MOVE));
			} else{
				al.actionPerformed(new ActionEvent(waitingTimeList.get(key), getID(), StringSet.MOUSE_MOVE));
			}
		} else if(hoverPos >= 0 && hoverPos < featureValues.size()){
			key = featureValues.get(hoverPos);
			al.actionPerformed(new ActionEvent(waitingTimeList.get(key), getID(), StringSet.MOUSE_MOVE));
		} else{
			al.actionPerformed(new ActionEvent(new AverageValueObject(), getID(), StringSet.MOUSE_MOVE));
		}
	}

	@Override
	protected void onMouseWheel(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	int adjustSpeed = 3;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		//set slash height and slash number
		for (int j = 1; j <= 10; j++) {
			if(maxWaitingTime / (slashSpan * j) <= maxSlashNum){ 
				slashSpan *= j;
				break;
			}
		}
		if(maxWaitingTime < maxSlashNum)
			slashSpan = 1;
		
		//set rect width and rect gap
		if(featureType == OutpatientLog.INDEX_DIAGNOSES){
			rectWidth = 50;
		} else{
			rectWidth = 40;
		}
		
		//auto adjust
		if(!isReleased) return;
		
		if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
		else if(offsetX > 0) offsetX--;
		
		if(offsetX <= 0) isRepaintable = false;
	}
    
	private static final int valueHeight = 30;
	private int rectWidth = 30;
	private int rectGap = 10;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		Integer xPos = 0;
		int bottom = HEIGHT - valueHeight;
		int maxRectHeight = bottom - RULER_WIDTH;
		AverageValueObject avo = null;
		int rectHeight = 0;
		for(String featureKey : waitingTimeList.keySet()){
			//get position
			xPos = calculateKeyPos(featureKey);
			if(xPos == null) continue;
			if(xPos == hoverPos){
				g.setColor(Color.ORANGE);
			} else{
				g.setColor(Color.LIGHT_GRAY);
			}
			//draw rect on that position
			avo = waitingTimeList.get(featureKey);
			int x = offsetX + RULER_WIDTH + rectGap + xPos * (rectWidth + rectGap);
			if(avo.divide == 0){
				g.fillRect(x, bottom, rectWidth, 9);
				g.drawString("0", x + 5, bottom - 5);
			} else{
				rectHeight = (int)((float)maxRectHeight * avo.total / (avo.divide * maxWaitingTime));
				g.fillRect(x, bottom - rectHeight, rectWidth, rectHeight);
				g.drawString("" + avo.total / avo.divide, x + 5, bottom - rectHeight - 5);
			}
		}
		drawRulers(g);
		drawMouseAlign(g);
	}
	private void drawMouseAlign(Graphics g){
	}
	private int maxSlashNum = 20;
	private int slashSpan = 10;
	private void drawRulers(Graphics g){
		g.setColor(Color.BLACK);
		int bottom = HEIGHT - valueHeight;
		//draw y axis
		g.drawLine(RULER_WIDTH, 0, RULER_WIDTH, HEIGHT);
		int slashHeight = 0;
		for(int i = 0 ; i <= maxWaitingTime ; i += slashSpan){
			slashHeight = (int)((bottom - RULER_WIDTH) * (float)(maxWaitingTime - i) / maxWaitingTime) + RULER_WIDTH;
			g.drawLine(RULER_WIDTH - SLASH_LENGTH, slashHeight, RULER_WIDTH, slashHeight);
			g.drawString("" + i, 0, slashHeight);
		}
		
		//draw x axis
		g.drawLine(0, bottom, WIDTH, bottom);
		int x = 0;
		if(featureType == OutpatientLog.INDEX_RECEPTION || featureType == OutpatientLog.INDEX_REGISTRATION){
			g.drawString(StringSet.AVERAGE, offsetX + RULER_WIDTH + rectGap + 5, bottom + RULER_WIDTH);
			for(int j = 0 ; j <= 1440 ; j += timeDivider){
				x = offsetX + RULER_WIDTH + rectGap + (j / timeDivider + 1) * (rectWidth + rectGap);
				g.drawLine(x, bottom, x, bottom + SLASH_LENGTH);
				g.drawString("" + j / 60 + ":" + j % 60, x, HEIGHT);
			}
		} else if(featureType == OutpatientLog.INDEX_PATIENT_AGE){
			g.drawString(StringSet.AVERAGE, offsetX + RULER_WIDTH + rectGap + 5, bottom + RULER_WIDTH);
			int maxX = (WIDTH - RULER_WIDTH - rectGap - offsetX) / (rectWidth + rectGap);
			for(int j = 0 ; j / timeDivider <= maxX ; j += timeDivider){
				x = offsetX + RULER_WIDTH + rectGap + (j / timeDivider + 1) * (rectWidth + rectGap);
				g.drawLine(x, bottom, x, bottom + SLASH_LENGTH);
				g.drawString("" + j / ageDivider, x, HEIGHT);
			}
		} else{
			String drawKey = null;
			for(String key : waitingTimeList.keySet()){
				drawKey = null;
				if(key.length() > 5){
					drawKey = key.substring(0, 5);
					drawKey += "...";
				}
				else drawKey = key;
				g.drawString(drawKey, offsetX + RULER_WIDTH + rectGap + calculateKeyPos(key) * (rectWidth + rectGap) + 5, bottom + RULER_WIDTH);
			}
		}
	}
	/**
	 * 计算一个特征值对应的绘画位置<br>
	 * 时间值会根据值来计算新位置，其他类型值则按照list中的顺序不变
	 * @param featureKey
	 * @return
	 */
	private Integer calculateKeyPos(String featureKey){
		if(featureKey == null)
			return null;
		switch (featureType) {
		case OutpatientLog.INDEX_PATIENT_AGE:
			if(featureKey == StringSet.AVERAGE) return 0;
			String[] split = featureKey.split("-");
			Integer age = Integer.parseInt(split[0]);
			if(age == null) return null;
			return age / ageDivider;
		case OutpatientLog.INDEX_RECEPTION:
		case OutpatientLog.INDEX_REGISTRATION:
			if(featureKey == StringSet.AVERAGE) return 0;
			int minutes = decodeMinutesFromKeyStr(featureKey, timeDivider);
			return minutes / timeDivider + 1;
		default:
			for (int i = 0; i < featureValues.size(); i++) {
				if(featureKey.equals(featureValues.get(i))){
					return i;
				}
			}
			break;
		}
		return 0;
	}
	
		/**
	 * 设置特征值类型并更新绘画数据
	 * @param featureKey
	 */
	public void changeFeatureAndUpdate(int featureKey){
		setFeatureType(featureKey);
		setTimeList();
		isRepaintable = true;
		//set max waiting time
		AverageValueObject avo = null;
		maxWaitingTime = 0;
		for(String feature: waitingTimeList.keySet()){
			avo = waitingTimeList.get(feature);
			if(avo.divide == 0) continue;
			int time = (int)(avo.total / avo.divide);
			if(maxWaitingTime < time){
				maxWaitingTime = time;
			}
		}
	}
}
