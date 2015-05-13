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
import com.lsc.ors.util.FeatureKeyGenerator;
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
	int[] percentageList = new int[100];
	private void updateView(OutpatientLogCharacters[] list, String character, int characterLayer, int diagramType){
		this.dataList = list;
		this.character = character;
		this.characterLayer = characterLayer;
		this.diagramType = diagramType;
		
		characterValueCount.clear();
		
		OutpatientLogCharacters olc = null;
		String crctr = null;
		if(diagramType == DIAGRAMTYPE_PERCENTAGE){
			if(hasPercentageDiagram()){
				setMaxNumber();
				for (int i = 0; i < percentageList.length; i++) {
					percentageList[i] = 0;
				}
				Integer number = 0;
				for (int j = 0; j < list.length; j++) {
					olc = list[j];
					crctr = olc.get(this.character);
					number = extractNumber(crctr);
					if(number == null) percentageList[99] ++;
					else{
						percentageList[number * 100 / maxNumber] ++;
					}
				}
			}
		} else{
			String[] split = null;
			int count = 0;
			for (int i = 0; i < list.length; i++) {
				//get generalized character values
				olc = list[i];
				crctr = olc.get(this.character);
				crctr = FeatureKeyGenerator.generalization(crctr, this.character);
				split = crctr.split(",");
				for (int j = 0; j < split.length; j++) {
					if(characterValueCount.get(split[j]) == null){
						characterValueCount.put(split[j], 0);
					}
					count = characterValueCount.get(split[j]);
					characterValueCount.put(split[j], count + 1);
				}
				
			}//sort character values by support count
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
		}
		
		isRepaintable = true;
	}
	protected void setMaxNumber(){
		if(character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_REGISTRATION]) || 
				character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_RECEPTION])){
			maxNumber = 24 * 60;
		}
		maxNumber = 0;
		OutpatientLogCharacters olc = null;
		String value = null;
		Integer number = null;
		for (int i = 0; i < dataList.length; i++) {
			olc = dataList[i];
			value = olc.get(character);
			number = extractNumber(value);
			if(number != null && number > maxNumber) 
				maxNumber = number;
		}
		maxNumber ++;
	}
	private int maxNumber;
	private Integer extractNumber(String oldKey){
		if(oldKey == null){
			ConsoleOutput.pop("ADboard.generalization", "key is null");
			return null;
		}
		Integer newKey = null;
		switch (OutpatientLogCharacters.getIndex(character)) {
		case OutpatientLogCharacters.INDEX_PATIENT_AGE:
		case OutpatientLogCharacters.INDEX_WAIT:
			newKey = Integer.parseInt(oldKey);
			break;
		case OutpatientLogCharacters.INDEX_REGISTRATION:
		case OutpatientLogCharacters.INDEX_RECEPTION:
			Date date = TimeFormatter.deformat(oldKey, null);
			newKey = FeatureKeyGenerator.getMinutesAmountFromDate(date);
		default:
			break;
		}
		return newKey;
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
			if(character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_DIAGNOSES])){
				return;
			}
			int rectHeight = (HEIGHT - RULER_WIDTH) / sortedKeys.length - 5;
			int maxWidth = WIDTH - 6 * RULER_WIDTH;
			int maxCount = characterValueCount.get(sortedKeys[0]);
			for (int i = 0; i < sortedKeys.length; i++) {
				count = characterValueCount.get(sortedKeys[i]);
				int width = count * maxWidth / maxCount;
				int y = 5 + i * (rectHeight + 5);
				graphic.setColor(new Color((int)(Math.random() * 0xffffff)));
				graphic.fillRect(0, y, width, rectHeight);
				graphic.drawString(sortedKeys[i] + ":", width + 5, y + 10);
				graphic.drawString("" + count, img.getWidth(null) - 2 * RULER_WIDTH, y + 10);
			}
			break;
		case DIAGRAMTYPE_PERCENTAGE:
			if(hasPercentageDiagram()){
				count = 0;
				int bottom = HEIGHT - RULER_WIDTH;
				graphic.setColor(Color.GRAY);
				int maxW = WIDTH - 2 * RULER_WIDTH;
				int maxH = bottom - 2 * RULER_WIDTH;
				for (int i = 0; i < percentageList.length; i++) {
					count += percentageList[i];
					graphic.fillOval(i * maxW / 100, bottom - RULER_WIDTH - count * maxH / dataList.length - 5, 5, 5);
				}
			}
			break;
		default:break;
		}
		g.drawImage(img, RULER_WIDTH + offsetX, RULER_WIDTH + offsetY, WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, null);
		if(diagramType == DIAGRAMTYPE_PERCENTAGE){
			int bottom = HEIGHT - RULER_WIDTH;
			g.drawLine(RULER_WIDTH, 0, RULER_WIDTH, HEIGHT);
			g.drawLine(0, bottom, WIDTH, bottom);
		}
	}

	private boolean hasPercentageDiagram(){
		return character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_PATIENT_AGE]) ||
				character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_RECEPTION]) ||
				character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_REGISTRATION]) ||
				character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_WAIT]);
	}
}
