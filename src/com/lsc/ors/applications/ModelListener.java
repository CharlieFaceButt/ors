package com.lsc.ors.applications;

public interface ModelListener {

	/**
	 * 界面打开响应
	 */
	abstract void onViewCreate();
	
	/**
	 * 界面关闭响应分
	 */
	abstract void onViewDestroy();
}
