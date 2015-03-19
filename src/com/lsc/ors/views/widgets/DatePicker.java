package com.lsc.ors.views.widgets;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.util.TimeFormatter;


public class DatePicker extends JPanel {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -8546855420509137118L;

	private static final int DEFAULT_WIDTH = 580;
	
	SliderMouseListener sml = new SliderMouseListener();
	ChangeListener cListener = null;
	
	//data
//	private WRVboard board = null;
	private Date currentDate = null;
	private Date beginDate = null;
	private Date finishDate = null;
	private float process = 0.0f;
	
	//view
	private JSlider dateSlider = new JSlider(
										JSlider.HORIZONTAL, 
										0, 
										DEFAULT_WIDTH/2, 
										DEFAULT_WIDTH/4-5);
	private TextField currentYear = new TextField(4);
	private TextField currentMonth = new TextField(2);
	private TextField currentDay = new TextField(2);
	private Label sliderCurrent = new Label(StringSet.CURRENT_DATE);
//	private DateEditor currentDateEditor = new DateEditor(
//			new JSpinner(new SpinnerDateModel(
//					currentDate, 
//					beginDate, 
//					finishDate, 
//					Calendar.DAY_OF_MONTH)));
	
	public DatePicker(
			final Date begin, 
			final Date finish, 
			final Date date, 
			ChangeListener listener) throws HeadlessException {
		super();
		
		//data
		this.beginDate = new Date(begin.getTime());
		this.finishDate = new Date(finish.getTime());
		this.cListener = listener;
		setPickerDate(beginDate);
		
		//views
		this.setLayout(null);
		dateSlider.setBounds(10, 10, DEFAULT_WIDTH, 20);
		dateSlider.setPreferredSize(new Dimension( DEFAULT_WIDTH, 20));
		Label sliderBegin = new Label(StringSet.BEGIN_DATE);
		Label sliderFinish = new Label(StringSet.FINISH_DATE);
		sliderBegin.setBounds(5, 30, 50, 20);
		sliderFinish.setBounds(DEFAULT_WIDTH - 40, 30, 50, 20);
		updateLabels();
		
		//add views
		add(dateSlider);
		add(sliderBegin);
		add(sliderFinish);
		add(sliderCurrent);
		
		//listener
		dateSlider.addMouseListener(sml);
		dateSlider.addMouseMotionListener(sml);
	}
	
	/**
	 * 判断DatePicker是否初始化了时间范围
	 * @return
	 */
	private boolean haveDateRange(){
		return (beginDate != null && finishDate != null);
	}
	
	/**
	 * 判断给定日期时间是否在DatePicker时间范围内
	 * @param date
	 * @return
	 */
	public boolean withinRange(Date date){
		if(date == null || !haveDateRange()){
			ConsoleOutput.pop("DatePicker.withinRange", "false-null pointer");
			return false;
		}
		if(date.getTime() - beginDate.getTime() >= 0 && finishDate.getTime() - date.getTime() >= 0){
			ConsoleOutput.pop("DatePicker.withinRange", "true");
			return true;
		}
		ConsoleOutput.pop("DatePicker.withinRange", "false-not within range");
		return false;
	}
	
	/**
	 * 设置当前时间,并更新显示<br>
	 * 输入为空时不进行操作；输入不在时间范围内时不进行操作
	 */
	public void setPickerDate(final Date date){
		//输入不能为空
		if(date == null || !withinRange(date)){
			ConsoleOutput.pop("DatePicker.setCurrentDate", "input error");
			return;
		}
		
		//set current date
		setCurrentDate(date);
		//set slider process
		setSliderProcessByDate(date);
		//set slider
		dateSlider.setValue((int)(process * dateSlider.getMaximum()));
		//set labels
		updateLabels();
	}
	
	/**
	 * 设置current date
	 * @param date
	 */
	private void setCurrentDate(Date date){
		if(date == null) return;
		currentDate = new Date(date.getTime());
	}
	
	/**
	 * 通过输入时间日期设置process和slider
	 * @param date
	 */
	private void setSliderProcessByDate(Date date){
		if(date == null) return;
		float fraction = (float)(date.getTime() - beginDate.getTime());
		ConsoleOutput.pop("portion fraction", "" + fraction);
		float dominator = (finishDate.getTime() - beginDate.getTime());
		ConsoleOutput.pop("portion dominator", "" + dominator);
		if(dominator != 0)
			process = fraction / dominator;
	}
	
	/**
	 * 获得当前date picker时间
	 * @return
	 */
	public Date getCurrentDate(){
		return currentDate;
	}
	
	/**
	 * 更新时间显示的文字,需要process提前设置
	 */
	private void updateLabels(){
		Date date = new Date((long)((1 - process) * beginDate.getTime() + process * finishDate.getTime()));
		String text = TimeFormatter.format(date, null);
		if(text == null) text = "null";
		sliderCurrent.setBounds(70 + (int)((DEFAULT_WIDTH - 280) * process) , 20, 150, 40);
		sliderCurrent.setText(text.substring(0,11) + "weekday No." + ((date.getDay() + 6) % 7 + 1));
		ConsoleOutput.pop("DatePicker.updateLabels", "datepicker文字更新为" + text);
	}
	
	private class SliderMouseListener implements MouseListener, MouseMotionListener{

		private boolean sliderDrag = false;
		private int lastValue = 0;
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if(sliderDrag && lastValue != dateSlider.getValue()){
				//set process
				process = (float)dateSlider.getValue() / dateSlider.getMaximum();
				//set labels
				updateLabels();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			sliderDrag = true;
			lastValue = dateSlider.getValue();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			//set process
			process = (float)dateSlider.getValue() / dateSlider.getMaximum();
			//set current date
			setCurrentDate(new Date((long)
					(beginDate.getTime() * (1 - process) + finishDate.getTime() * process)));
			//set labels
			updateLabels();
			if(cListener != null)
				cListener.stateChanged(null);
			sliderDrag = false;
			lastValue = 0;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
