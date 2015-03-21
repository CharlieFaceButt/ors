package com.lsc.ors.views;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.src.StringSet;

public class QSVboard extends VisualizationBoard {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	/**
	 * 当前特征变量标识
	 */
	private int featureType = StringSet.CMD_TIME_UNIT_DAY;
	/**
	 * 当前特征可能取值
	 */
	private LinkedList<String> featureList = null;
	/**
	 * 全部数据
	 */
	private OutpatientLog[] list = null;
	/**
	 * 等待队列
	 */
	private LinkedList<OutpatientLog> waitingList = new LinkedList<OutpatientLog>();
	/**
	 * 已就诊列表
	 */
	private LinkedList<OutpatientLog> receptList = new LinkedList<OutpatientLog>();
	/**
	 * 当前数据显示的对应时刻
	 */
	private int currentTime = 0;
	/**
	 * 基准时间，最早时间
	 */
	private int baseTime = 0;
	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -2108585612607783570L;
	
	public QSVboard(OutpatientLog[] list, ActionListener listener){
		super(listener, list);
		setBounds(0, 0, WIDTH, HEIGHT);
		setData(list);
	}

	@Override
	public void setData(OutpatientLog[] list, int type) {
		// TODO Auto-generated method stub
		setData(list, type, featureType);
	}
	public void setData(OutpatientLog[] list, int type, int fType){
		this.list = list;
		sortListByRegistrationTime();
		generateFeatureList(fType);
		baseTime = getMinutesAmountFromDate(list[0].getRegistration_time());
		currentTime = baseTime;
		targetTime = baseTime;
	}
	private void generateFeatureList(int fType){
		
	}
	private void sortListByRegistrationTime(){
		int key = 0;
		for(int i = 1 ; i < list.length ; i ++){
			OutpatientLog ol = list[i];
			key = getMinutesAmountFromDate(ol.getRegistration_time());
			int j = i - 1;
			while(j >= 0 && getMinutesAmountFromDate(list[j].getRegistration_time()) > key){
				list[j+1] = list[j];
				j --;
			}
			list[j + 1] = ol;
		}
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		if(receptList != null && receptList.size() > 0){
			
		}
	}

	int targetTime = 0;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		//update waiting list and reception list
	}

	@Override
	protected void onMouseWheel(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
