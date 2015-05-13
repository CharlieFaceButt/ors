package com.lsc.ors.util;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.db.dbo.OutpatientLogDBO;

public class DataExtractor {

	public static OutpatientLogCharacters[] extractCharacterFromOutpatientLogList(OutpatientLog[] list){
		OutpatientLogCharacters[] resultList = new OutpatientLogCharacters[list.length];
		for (int i = 0; i < resultList.length; i++) {
			resultList[i] = list[i].generateCharacters();
		}
		return resultList;
	}
	
	public static String generateKeyByLayer(int attrIndex, int layer){
		return null;
	}
	
	public static String generateConceptLayer(int attrIndex, String oldValue){
		return oldValue;
	}
	
	/**
	 * ��ȱֵ����
	 * @param attrIndex
	 * @param log
	 * @return �Ƿ�ɾ��Ԫ��
	 */
	public static boolean processMissingValue(int attrIndex, OutpatientLog log){
		switch(attrIndex){
		case 4:
		case 11:
		case 12:
		case 13:
		case 14:return true;
		case 5:log.setPatient_age(OutpatientLogDBO.getAverageValue(attrIndex));
		}
		return false;
	}
	/**
	 * �������������㴦��
	 * @param attrIndex
	 * @param oldValue
	 * @return
	 */
	public static String processNoise(int attrIndex, String oldValue){
		switch(attrIndex){
		case 3:return encode(oldValue);
		case 4:
		case 13:return "" + OutpatientLogDBO.getConvergeValue(attrIndex, oldValue);
		case 10:return commaAlign(oldValue);
		}
		return oldValue;
	}
	/**
	 * ����
	 * @param oldValue
	 * @return
	 */
	private static String encode(String oldValue){
		char[] array = oldValue.toCharArray();
		for (int i = 0; i < array.length; i++) {
			array[i] = (char)(array[i] + 1);
		}
		return array.toString();
	}
	/**
	 * ���Ÿ�ʽһ��
	 * @param oldValue
	 * @return
	 */
	private static String commaAlign(String oldValue){
		if(oldValue != null){
			oldValue.replaceAll("��", ",");
			oldValue.replaceAll(";", ",");
		}
		return oldValue;
	}
}
