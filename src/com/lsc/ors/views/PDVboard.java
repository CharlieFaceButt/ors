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
	 * ��ǰ����ά��
	 */
	Integer featureType = null;
	/**
	 * ��¼���ܵ�����ά��
	 */
	int[] featureList = new int[]{
		OutpatientLog.INDEX_DEPARTMENT, OutpatientLog.INDEX_DOCTOR,
		OutpatientLog.INDEX_PATIENT_GENDER, OutpatientLog.INDEX_PATIENT_AGE,
		OutpatientLog.INDEX_REGISTRATION, OutpatientLog.INDEX_RECEPTION,
		OutpatientLog.INDEX_DIAGNOSES, OutpatientLog.INDEX_FURTHER_CONSULTATION
	};
	/**
	 * ��ǰ����ά�ȵĿ���ȡֵ
	 */
	String[] featureValues = null;
	/**
	 * ��ά���ݴ洢���ȴ�����=countLists������ȡֵ���ȴ�ʱ�䣩
	 */
	Map<String,Map<String, Integer>> countLists = new HashMap<String, Map<String,Integer>>();
	/**
	 * ÿ�εķ�Χ
	 */
	int waitingTimeDivider = 10;
	
	public PDVboard(ActionListener listener, OutpatientLog[] dataList) {
		super(listener, dataList);
		// TODO Auto-generated constructor stub
		
		setBounds(0, 0, WIDTH, HEIGHT);
		setData(dataList);
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
		initFeatureList(fType);
		
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
	private void initFeatureList(Integer fType){
		if(featureType.equals(fType))
			return;
		featureType = fType;
		//remove old keys
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
					if(!map.containsKey(countStr))
						map.put(countStr, 0);
				}else{
					map = countLists.get(key);
				}
				//add waiting count
				count = map.get(countStr);
				map.put(key, count + 1);
			}
		}
		//set feature list
		Set<String> featureValueKeys = countLists.keySet();
		featureValues = new String[featureValueKeys.size()];
		featureValueKeys.toArray(featureValues);
	}
	/**
	 * ���ݵȴ�ʱ�����ɶ�Ӧ��ȡֵ��Χkey
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
			getMinutesAmountFromDate(date);
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

	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub

	}

}
