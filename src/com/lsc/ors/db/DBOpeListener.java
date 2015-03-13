package com.lsc.ors.db;

import java.sql.ResultSet;

/**
 * 数据库操作监听
 * @author charlieliu
 *
 */
public interface DBOpeListener {

	/**
	 * 事务完成回调
	 */
	abstract void onTransactionCompletion(ResultSet rs);
	
	/**
	 * 事务开始回调
	 */
	abstract void onTransactionStart();
	
}
