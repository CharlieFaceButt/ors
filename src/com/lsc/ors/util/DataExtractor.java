package com.lsc.ors.util;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.beans.OutpatientLogCharacters;

public class DataExtractor {

	public static OutpatientLogCharacters[] extractCharacterFromOutpatientLogList(OutpatientLog[] list){
		OutpatientLogCharacters[] resultList = new OutpatientLogCharacters[list.length];
		for (int i = 0; i < resultList.length; i++) {
			resultList[i] = list[i].generateCharacters();
		}
		return resultList;
	}
}
