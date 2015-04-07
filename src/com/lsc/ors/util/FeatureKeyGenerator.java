package com.lsc.ors.util;

import java.util.Date;

import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.debug.ConsoleOutput;

public class FeatureKeyGenerator {

	
	static int ageDivider = 10;
	static int waitingTimeDivider = 30;
	static int registTimeDivider = 120;
	public static String generalization(String oldKey, String character){
		return generalization(oldKey, OutpatientLogCharacters.getIndex(character));
	}
	public static String generalization(String oldKey, int characterIndex){
		if(oldKey == null){
			ConsoleOutput.pop("ADboard.generalization", "key is null");
			return "null";
		}
		String newKey = null;
		switch (characterIndex) {
		case OutpatientLogCharacters.INDEX_PATIENT_AGE:
			Integer age = Integer.parseInt(oldKey);
			if(age == null) 
				return newKey;
			newKey = generateKeyStrByDividingValue(age, ageDivider);
			break;
		case OutpatientLogCharacters.INDEX_REGISTRATION:
		case OutpatientLogCharacters.INDEX_RECEPTION:
			Date date = TimeFormatter.deformat(oldKey, null);
			int minuteAmount = getMinutesAmountFromDate(date);
			newKey = generateKeyStrByDividingMinutes(minuteAmount, registTimeDivider);
			break;
		case OutpatientLogCharacters.INDEX_WAIT:
			Integer wait = Integer.parseInt(oldKey);
			if(wait == null) return newKey;
			newKey = generateKeyStrByDividingValue(wait, waitingTimeDivider);
			break;
		case OutpatientLogCharacters.INDEX_PATIENT_GENDER:
		case OutpatientLogCharacters.INDEX_DOCTOR:
		case OutpatientLogCharacters.INDEX_DIAGNOSES:
		case OutpatientLogCharacters.INDEX_FURTHER_CONSULTATION:
			newKey = oldKey; //有外部更改风险
			break;
		default:
			break;
		}
		return newKey;
	}
	public static String generateKeyStrByDividingValue(int value, int divider){
		int quotient = value / divider;
		return "" + (quotient * divider) + "-" + ((quotient + 1) * divider);
	}
	public static String generateKeyStrByDividingMinutes(int value, int divider){
		int quotient = value / divider;
		int minutesAmount = quotient * divider;
		return "" + (minutesAmount / 60) + ":" + (minutesAmount % 60) + "-" +
				((minutesAmount + divider) / 60) + ":" +((minutesAmount +divider) % 60);
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
}
