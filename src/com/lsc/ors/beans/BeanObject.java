package com.lsc.ors.beans;

import jxl.Cell;

public interface BeanObject {
	
	/**
	 * 从excel导入数据库时进行的数据转换
	 * @param key
	 * @param value
	 * @return
	 */
	abstract public String generateValueFromExcelCell(int key, Cell cell);
	
	abstract public void set(int key, Object value);
	
	/**
	 * 根据属性位置来获得属性值
	 * @param key
	 * @return 
	 */
	abstract public String get(int key);
	
	abstract public String[] keySet();
}
