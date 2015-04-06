package com.lsc.ors.views.widgets;

import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;


import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;

public class TimeButtonGroup {
	
	Button nextDay = null;
	Button lastDay = null;
	Button nextWeek = null;
	Button lastWeek = null;
	Button nextMonth = null;
	Button lastMonth = null;
	Button nextYear = null;
	Button lastYear = null;
	JComboBox timeUnitChooser = null;
	
	int top = 0;
	int left = 0;

	public TimeButtonGroup(int top, int left, int buttonWidth, int buttonHeight, final ActionListener listener){
		this.top = top;
		this.left = left;
		
		lastDay = new Button(StringSet.LAST_DAY);
		nextDay = new Button(StringSet.NEXT_DAY);
		lastWeek = new Button(StringSet.LAST_WEEK);
		nextWeek = new Button(StringSet.NEXT_WEEK);
		lastMonth = new Button(StringSet.LAST_MONTH);
		nextMonth = new Button(StringSet.NEXT_MONTH);
		lastYear = new Button(StringSet.LAST_YEAR);
		nextYear = new Button(StringSet.NEXT_YEAR);
		timeUnitChooser = new JComboBox(new String[]{
				StringSet.TIME_UNIT_DAY,StringSet.TIME_UNIT_WEEK,
				StringSet.TIME_UNIT_MONTH,StringSet.TIME_UNIT_YEAR,
				StringSet.TIME_UNIT_ALL});
		
		int lineOfButtonsY = top;
		lastDay.setBounds(left, lineOfButtonsY, buttonWidth, buttonHeight);
		nextDay.setBounds(left + (buttonWidth + 10), lineOfButtonsY, buttonWidth, buttonHeight);
		lastWeek.setBounds(left + (buttonWidth + 10) * 2, lineOfButtonsY, buttonWidth, buttonHeight);
		nextWeek.setBounds(left + (buttonWidth + 10) * 3, lineOfButtonsY, buttonWidth, buttonHeight);
		lineOfButtonsY += (buttonHeight + 10);
		lastMonth.setBounds(left, lineOfButtonsY, buttonWidth, buttonHeight);
		nextMonth.setBounds(left + (buttonWidth + 10), lineOfButtonsY, buttonWidth, buttonHeight);
		lastYear.setBounds(left + (buttonWidth + 10) * 2, lineOfButtonsY, buttonWidth, buttonHeight);
		nextYear.setBounds(left + (buttonWidth + 10) * 3, lineOfButtonsY, buttonWidth, buttonHeight);
		timeUnitChooser.setBounds(left + (buttonWidth + 10) * 4, lineOfButtonsY, buttonWidth, 20);
		
		//listeners
		lastDay.addActionListener(listener);
		nextDay.addActionListener(listener);
		lastWeek.addActionListener(listener);
		nextWeek.addActionListener(listener);
		lastMonth.addActionListener(listener);
		nextMonth.addActionListener(listener);
		lastYear.addActionListener(listener);
		nextYear.addActionListener(listener);
		timeUnitChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange() == ItemEvent.SELECTED){
					String s=(String)((JComboBox)e.getSource()).getSelectedItem();
					Integer msg = StringSet.getInstance().getCommandIndex(s);
					if(msg == null){
						ConsoleOutput.pop("WaitingRecordViewer.itemStateChanged", "msg is null");
						return;
					}
					switch(msg){
					case StringSet.CMD_TIME_UNIT_DAY:
						lastDay.setEnabled(true);
						nextDay.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_WEEK:
						lastWeek.setEnabled(true);
						nextWeek.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_MONTH:
						lastMonth.setEnabled(true);
						nextMonth.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_YEAR:
						lastYear.setEnabled(true);
						nextYear.setEnabled(true);
					case StringSet.CMD_TIME_UNIT_ALL:
						listener.actionPerformed(new ActionEvent(timeUnitChooser, msg, s));
						setButtonsDisable(msg);
						break;
					default:break;
					}
	            }

			}
		});
		
	}
	private void setButtonsDisable(int msg){
		switch (msg) {
		case StringSet.CMD_TIME_UNIT_ALL:
			lastYear.setEnabled(false);
			nextYear.setEnabled(false);
		case StringSet.CMD_TIME_UNIT_YEAR:
			lastMonth.setEnabled(false);
			nextMonth.setEnabled(false);
		case StringSet.CMD_TIME_UNIT_MONTH:
			lastWeek.setEnabled(false);
			nextWeek.setEnabled(false);
		case StringSet.CMD_TIME_UNIT_WEEK:
			lastDay.setEnabled(false);
			nextDay.setEnabled(false);
			break;
		default:
			break;
		}
	}
	public void disableTimeUnitChooser(){
		timeUnitChooser.setEnabled(false);
	}
	public Component[] getAllComponents(){
		Component[] group = new Component[]{
				lastDay, nextDay, lastWeek, nextWeek,
				lastMonth, nextMonth, lastYear, nextYear,
				timeUnitChooser
		};
		return group;
	}
	
	public int getTop(){
		return top;
	}
}
