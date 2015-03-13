package com.lsc.ors.beans;

import jxl.Cell;

public interface BeanObject {
	
	/**
	 * ��excel�������ݿ�ʱ���е�����ת��
	 * @param key
	 * @param value
	 * @return
	 */
	abstract public String generateValueFromExcelCell(int key, Cell cell);
	
	abstract public void set(int key, Object value);
	
	/**
	 * ��������λ�����������ֵ
	 * @param key
	 * @return 
	 */
	abstract public String get(int key);
	
	abstract public String[] keySet();
}
