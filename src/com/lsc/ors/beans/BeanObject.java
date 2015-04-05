package com.lsc.ors.beans;

public interface BeanObject {
	
	abstract public void set(int key, Object value);
	
	/**
	 * 根据属性位置来获得属性值
	 * @param key
	 * @return 
	 */
	abstract public String get(int key);
	
	abstract public String[] keySet();
}
