package com.lsc.ors.views.analysis;

import java.awt.event.ActionListener;

import com.lsc.ors.beans.OutpatientLogCharacters;

public class DCTADboard extends ADboard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5905108872610056581L;

	String doctor;
	OutpatientLogCharacters[] unfilteredList;
	
	public void setDoctor(String doctorName){
		doctor = doctorName;
		setData(unfilteredList);
	}
	public String getDoctor(){
		return doctor;
	}
	
	public DCTADboard(OutpatientLogCharacters[] logList, ActionListener listener) {
		super(logList, listener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setData(OutpatientLogCharacters[] list) {
		// TODO Auto-generated method stub
		unfilteredList = list;
		generateFilteredList(list);
		super.setData(dataList);
	}
	
	/**
	 * 根据list生成某个医生的dataList
	 * @param list
	 */
	private void generateFilteredList(OutpatientLogCharacters[] list){
		if(doctor == null) dataList = list;
		else{
			int count = 0;
			for (int i = 0; i < list.length; i++) {
				if(doctor.equals(list[i].getDoctor_name())) count ++;
			}
			dataList = new OutpatientLogCharacters[count];
			int index = 0;
			for (int j = 0; j < list.length; j++) {
				if(doctor.equals(list[j].getDoctor_name())){
					dataList[index] = list[j];
					index ++;
				}
			}
		}
	}

}
