package com.lsc.ors.beans;

public interface BeanObject {
	
	abstract public void set(int key, Object value);
	
	/**
	 * ��������λ�����������ֵ
	 * @param key
	 * @return 
	 */
	abstract public String get(int key);
	
	abstract public String[] keySet();
}
