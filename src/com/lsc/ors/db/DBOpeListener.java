package com.lsc.ors.db;

import java.sql.ResultSet;

/**
 * ���ݿ��������
 * @author charlieliu
 *
 */
public interface DBOpeListener {

	/**
	 * ������ɻص�
	 */
	abstract void onTransactionCompletion(ResultSet rs);
	
	/**
	 * ����ʼ�ص�
	 */
	abstract void onTransactionStart();
	
}
