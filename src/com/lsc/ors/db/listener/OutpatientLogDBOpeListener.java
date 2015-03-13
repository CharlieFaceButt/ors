package com.lsc.ors.db.listener;

import com.lsc.ors.db.DBOpeListener;

public interface OutpatientLogDBOpeListener extends DBOpeListener{

	/**
	 * 门诊记录数据库操作进度监听
	 * @param progress
	 * @param max
	 */
	abstract void onProgressUpdate(String progressStr, float progress, int max);
}
