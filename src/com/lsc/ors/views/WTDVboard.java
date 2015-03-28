package com.lsc.ors.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.util.TimeFormatter;

public class WTDVboard extends VisualizationBoard {

	/**
	 * generated serial ID	
	 */
	private static final long serialVersionUID = 1224787845530459977L;
	/**
	 * 当前特征维度
	 */
	Integer featureType;
	ArrayList<String> featureValues;
	/**
	 * 二维数据存储：等待人数=countLists（特征取值，等待时间）
	 */
	Map<String, AverageValuePair> waitingTimeList;
	
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
	}
	public void setData(OutpatientLog[] list, int type, Integer featureType){
		this.dataList = list;
		timeUnitType = type;
		setFeatureType(featureType);
		setTimeList();
		
		//set max waiting time
		AverageValuePair avp = null;
		maxWaitingTime = 0;
		for(String featureKey: waitingTimeList.keySet()){
			avp = waitingTimeList.get(featureKey);
			int time = (int)(avp.total / avp.divide);
			if(maxWaitingTime < time){
				maxWaitingTime = time;
			}
		}
	}
	private synchronized void setFeatureType(Integer fType){
		//if feature type not change then don't do anything
		if(featureType != null && featureType.equals(fType))
			return;
		//set feature type
		featureType = fType;
		if(featureType == null)
			featureType = OutpatientLog.INDEX_DEPARTMENT;
		ConsoleOutput.pop("PDVboard.setFeature", "" + OutpatientLog.KEYS[featureType]);
		//initiate feature value list
		featureValues = new ArrayList<String>();
	}
	private void setTimeList(){
		//remove old keys
		if(waitingTimeList == null)
			waitingTimeList = new HashMap<String,AverageValuePair>();
		else waitingTimeList.clear();
		AverageValuePair totalAVP = new AverageValuePair(0,0);
		waitingTimeList.put(StringSet.TOTAL, totalAVP);
		featureValues.add(StringSet.TOTAL);
		
		//initiate keys for all feature values
		if(dataList == null) return;
		if(featureType != null){
			OutpatientLog ol = null;
			String featureKey = null;
			AverageValuePair avp = null;
			for (int i = 0; i < dataList.length; i++) {
				ol = dataList[i];
				featureKey = ol.get(featureType);
				featureKey = generateKeyValue(featureKey);
				if(!waitingTimeList.containsKey(featureKey)){
					waitingTimeList.put(featureKey, new AverageValuePair(0, 0));
					featureValues.add(featureKey);
				}
				avp = waitingTimeList.get(featureKey);
				avp.total += ol.getWaiting_time();
				avp.divide ++;
				
				totalAVP.total += ol.getWaiting_time();
				totalAVP.divide ++;
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
		AverageValuePair avp = null;
		int rectHeight = 0;
		g.setColor(Color.BLUE);
		for(String featureKey : waitingTimeList.keySet()){
			//get position
			xPos = calculateKeyPos(featureKey);
			if(xPos == null) continue;
			//draw rect on that position
			avp = waitingTimeList.get(featureKey);
			rectHeight = (int)((float)maxRectHeight * avp.total / (avp.divide * maxWaitingTime));
			g.fillRect(offsetX + RULER_WIDTH + rectGap + xPos * (rectWidth + rectGap), bottom - rectHeight, rectWidth, rectHeight);
		}
		drawRulers(g);
	}
	private int maxSlashNum = 20;
	private int slashSpan = 10;
	private void drawRulers(Graphics g){
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
		if(featureType == OutpatientLog.INDEX_RECEPTION || featureType == OutpatientLog.INDEX_REGISTRATION){
			int x = 0;
			g.drawString(StringSet.TOTAL, offsetX + RULER_WIDTH + rectGap + 5, bottom + RULER_WIDTH);
			for(int j = 0 ; j < 1440 ; j += timeDivider){
				x = offsetX + RULER_WIDTH + rectGap + (j + 1) * (rectWidth + rectGap);
				g.drawLine(x, bottom, x, bottom + SLASH_LENGTH);
				g.drawString("" + j / 60 + ":" + j % 60, x, HEIGHT);
			}
		} else{
			int k = 0;
			String drawKey = null;
			for(String key : waitingTimeList.keySet()){
				drawKey = null;
				if(key.length() > 5){
					drawKey = key.substring(0, 5);
					drawKey += ("\n" + key.substring(5));
				}
				g.drawString(drawKey, offsetX + RULER_WIDTH + rectGap + k * (rectWidth + rectGap) + 5, bottom + RULER_WIDTH);
				k ++;
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
		case OutpatientLog.INDEX_RECEPTION:
		case OutpatientLog.INDEX_REGISTRATION:
			if(featureKey == StringSet.TOTAL) return 0;
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
	 * 存储每一个要绘画的柱形所对应的（总等待时间，等待人数），方便计算平均值
	 * @author charlieliu
	 *
	 */
	class AverageValuePair{
		public AverageValuePair(long total, long divide){
			this.total = total;
			this.divide = divide;
		}
		public long total;
		public long divide;
	}
	/**
	 * 设置特征值类型并更新绘画数据
	 * @param featureKey
	 */
	public void changeFeatureAndUpdate(int featureKey){
		setFeatureType(featureType);
		setTimeList();
	}
}
