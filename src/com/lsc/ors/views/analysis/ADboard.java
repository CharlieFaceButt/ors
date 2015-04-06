package com.lsc.ors.views.analysis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.util.TimeFormatter;

public class ADboard extends AnalysisBoard {

	/**
	 * generate serial id
	 */
	private static final long serialVersionUID = 1323172859068201393L;
	public int getID(){
		return (int)serialVersionUID;
	}
	
	private String character;
	private int characterLayer;
	

	public ADboard(OutpatientLogCharacters[] logList, ActionListener listener) {
		super(logList, listener);
		// TODO Auto-generated constructor stub
		character = OutpatientLogCharacters.KEYS[0];
		setData(dataList);
	}
	
	@Override
	public void miningAllData() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setData(OutpatientLogCharacters[] list) {
		// TODO Auto-generated method stub
		changeData(list);
	}
	/**
	 * 更改数据
	 * @param list
	 */
	public void changeData(OutpatientLogCharacters[] list){
		updateView(list, character, 0, diagramType);
	}
	/**
	 * 更改特征
	 * @param character
	 */
	public void changeCharacter(String character){
		updateView(dataList, character, 0, diagramType);
	}
	/**
	 * 更改特征概化层次
	 * @param diagramType
	 */
	public void changeCharacterLayer(int characterLayer){
		updateView(dataList, character, characterLayer, diagramType);
	}
	/**
	 * 更改视图
	 * @param diagramType
	 */
	public void changeDiagramType(int diagramType){
		updateView(dataList, character, characterLayer, diagramType);
	}
	Map<String, Integer> characterValueCount = new HashMap<String, Integer>();
	String[] sortedKeys = null;
	private void updateView(OutpatientLogCharacters[] list, String character, int characterLayer, int diagramType){
		this.dataList = list;
		this.character = character;
		this.characterLayer = characterLayer;
		this.diagramType = diagramType;
		
		characterValueCount.clear();
		//get generalized character values
		OutpatientLogCharacters olc = null;
		String crctr = null;
		for (int i = 0; i < list.length; i++) {
			olc = list[i];
			crctr = olc.get(this.character);
			crctr = generalization(crctr, this.character);
			if(characterValueCount.get(crctr) == null){
				characterValueCount.put(crctr, 0);
			}
			int count = characterValueCount.get(crctr);
			characterValueCount.put(crctr, count + 1);
		}
		
		//sort character values by support count
		sortedKeys = new String[characterValueCount.keySet().size()];
		sortedKeys = characterValueCount.keySet().toArray(sortedKeys);
		int[] countList = new int[sortedKeys.length];
		for (int j = 0; j < sortedKeys.length; j++) {
			countList[j] = characterValueCount.get(sortedKeys[j]);
		}
		int key = 0;
		String kStr = null;
		for (int k = 1; k < countList.length; k++) {
			key = countList[k];
			kStr = sortedKeys[k];
			int l = k - 1;
			while (l >= 0 && countList[l] < key) {
				countList[l+1] = countList[l];
				sortedKeys[l+1] = sortedKeys[l];
				l --;
			}
			countList[l+1] = key;
			sortedKeys[l+1] = kStr;
		}
		
		isRepaintable = true;
	}
	int ageDivider = 10;
	int waitingTimeDivider = 30;
	int registTimeDivider = 120;
	private String generalization(String oldKey, String character){
		if(oldKey == null){
			ConsoleOutput.pop("ADboard.generalization", "key is null");
			return "null";
		}
		String newKey = null;
		switch (OutpatientLogCharacters.getIndex(character)) {
		case OutpatientLogCharacters.INDEX_PATIENT_AGE:
			Integer age = Integer.parseInt(oldKey);
			if(age == null) 
				return newKey;
			newKey = generateKeyStrByDividingValue(age, ageDivider);
			break;
		case OutpatientLogCharacters.INDEX_REGISTRATION:
		case OutpatientLogCharacters.INDEX_RECEPTION:
			Date date = TimeFormatter.deformat(oldKey, null);
			int minuteAmount = getMinutesAmountFromDate(date);
			newKey = generateKeyStrByDividingMinutes(minuteAmount, registTimeDivider);
			break;
		case OutpatientLogCharacters.INDEX_WAIT:
			Integer wait = Integer.parseInt(oldKey);
			if(wait == null) return newKey;
			newKey = generateKeyStrByDividingValue(wait, waitingTimeDivider);
			break;
		case OutpatientLogCharacters.INDEX_PATIENT_GENDER:
		case OutpatientLogCharacters.INDEX_DOCTOR:
		case OutpatientLogCharacters.INDEX_DIAGNOSES:
		case OutpatientLogCharacters.INDEX_FURTHER_CONSULTATION:
			newKey = oldKey; //有外部更改风险
			break;
		default:
			break;
		}
		return newKey;
	}
	private String generateKeyStrByDividingValue(int value, int divider){
		int quotient = value / divider;
		return "" + (quotient * divider) + "-" + ((quotient + 1) * divider);
	}
	private String generateKeyStrByDividingMinutes(int value, int divider){
		int quotient = value / divider;
		int minutesAmount = quotient * divider;
		return "" + (minutesAmount / 60) + ":" + (minutesAmount % 60) + "-" +
				((minutesAmount + divider) / 60) + ":" +((minutesAmount +divider) % 60);
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
	protected void onMouseExit(MouseEvent e) {
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
	protected void onMouseWheel(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	private int adjustSpeed = 3;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		if(!isReleased) return;
		
		if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
		else if(offsetX > 0) offsetX--;
		
		if(offsetY > 5) offsetY -= (offsetY / adjustSpeed);
		else if(offsetY > 0) offsetY--;
		
		if(offsetX <= 0 && offsetY <= 0) 
			isRepaintable = false;
	}

	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		if(dataList == null) return;
		
		Image img = new BufferedImage(WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, BufferedImage.TYPE_INT_RGB);
		Graphics graphic = img.getGraphics();
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
		graphic.setColor(Color.BLACK);		

		int count = 0;
		switch(diagramType){
		case DIAGRAMTYPE_CHART:
			int trHeight = 40;
			int tdWidth = 100;
			graphic.drawString(character, 0, trHeight);
			graphic.drawString("count", tdWidth, trHeight);
			graphic.drawString("percentage", 2 * tdWidth, trHeight);
			for (int i = 0; i < sortedKeys.length; i++) {
				graphic.drawString(sortedKeys[i], 0, trHeight * (i + 2));
				count = characterValueCount.get(sortedKeys[i]);
				graphic.drawString("" + count, tdWidth, trHeight * (i + 2));
				graphic.drawString("" + DecimalFormat.getInstance().format((float)count * 100 / dataList.length) + "%", 2 * tdWidth, trHeight * (i + 2));
			}
			break;
		case DIAGRAMTYPE_PIE:
			if(character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_DIAGNOSES])){
				return;
			}
			int diameter = HEIGHT - 3 * RULER_WIDTH;
			int startCount = 0;
			for (int i = 0; i < sortedKeys.length; i++) {
				count = characterValueCount.get(sortedKeys[i]);
				graphic.setColor(new Color((int)(Math.random() * 0xffffff)));
				graphic.fillArc(RULER_WIDTH, RULER_WIDTH, diameter, diameter, 
						(int)(360 * (float)startCount / dataList.length),
						(int)(360 * (float)count / dataList.length) + 1);
				graphic.drawString(sortedKeys[i], 6 * RULER_WIDTH + diameter, (1 + i) * RULER_WIDTH);
				graphic.drawString("" + (int)(100 * (float)count / dataList.length) + "%", 3 * RULER_WIDTH + diameter, (1 + i) * RULER_WIDTH);
				startCount += count;
			}
			break;
		case DIAGRAMTYPE_BAR:
			
		default:break;
		}
		g.drawImage(img, RULER_WIDTH + offsetX, RULER_WIDTH + offsetY, WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, null);
	}

}
