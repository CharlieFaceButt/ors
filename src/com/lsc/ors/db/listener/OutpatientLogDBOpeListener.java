package com.lsc.ors.db.listener;

import com.lsc.ors.db.DBOpeListener;

public interface OutpatientLogDBOpeListener extends DBOpeListener{

	/**
	 * �����¼���ݿ�������ȼ���
	 * @param progress
	 * @param max
	 */
	abstract void onProgressUpdate(String progressStr, float progress, int max);
}
