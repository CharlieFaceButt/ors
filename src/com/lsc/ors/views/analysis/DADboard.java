package com.lsc.ors.views.analysis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.util.DataMiner;
import com.lsc.ors.util.FeatureKeyGenerator;

public class DADboard extends AnalysisBoard {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -7625254347500812802L;
	public int getID(){
		return (int)serialVersionUID;
	}
	public static final String[] WTKeys = new String[]{
		"0-120,120-","0-120,120-240,240-360,360-",
		"0-60,60-120,120-180,180-240,240-",
		"0-30,30-60,60-90,90-120,120-150,150-180,180-210,210-240,240-",
	};

	private String character;
	private int wtClass;
	
	public DADboard(OutpatientLogCharacters[] logList, ActionListener listener) {
		super(logList, listener);
		// TODO Auto-generated constructor stub
		character = OutpatientLogCharacters.KEYS[0];
		setData(dataList);
	}

	@Override
	public void setData(OutpatientLogCharacters[] list) {
		// TODO Auto-generated method stub
		update(list, character, diagramType, wtClass);
	}
	/**
	 * 更改特征
	 * @param character
	 */
	public void changeCharacter(String character){
		update(dataList, character, diagramType, wtClass);
	}
	/**
	 * 更改视图
	 * @param diagramType
	 */
	public void changeDiagramType(int diagramType){
		update(dataList, character, diagramType, wtClass);
	}
	/**
	 * 更改等待时间划分方式
	 * @param wtClass
	 */
	public void changeWaitingTimeClassification(int wtClass){
		update(dataList, character, diagramType, wtClass);
	}
	Map<String, Integer> characterValueCount = new HashMap<String, Integer>();
	String[] sortedKeys = null;
	int[] percentageList = new int[100];
	private void update(OutpatientLogCharacters[] list, String character, int diagramType, int wtClass){
		this.dataList = list;
		this.character = character;
		this.diagramType = diagramType;
		this.wtClass = wtClass;

		associationMap = null;
		characterValueCount.clear();
		OutpatientLogCharacters olc = null;
		String crctr = null;
		String[] split = null;
		int count = 0;
		//get generalized character values
		for (int i = 0; i < list.length; i++) {
			olc = list[i];
			crctr = olc.get(this.character);
			crctr = FeatureKeyGenerator.generalization(crctr, this.character);
			split = crctr.split(",");
			for (int j = 0; j < split.length; j++) {
				if(characterValueCount.get(crctr) == null){
					characterValueCount.put(crctr, 0);
				}
				count = characterValueCount.get(crctr);
				characterValueCount.put(crctr, count + 1);
			}
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
		
		associationMap = DataMiner.associationOf2Character(
				dataList,
				WTKeys[wtClass].split(","), OutpatientLogCharacters.INDEX_WAIT,
				sortedKeys, OutpatientLogCharacters.getIndex(character));
		
		isRepaintable = true;
	}

	@Override
	public void miningAllData() {
		// TODO Auto-generated method stub

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

	int[][] associationMap;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub

		if(dataList == null) return;
		
		Image img = new BufferedImage(WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, BufferedImage.TYPE_INT_RGB);
		Graphics graphic = img.getGraphics();
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
		graphic.setColor(Color.BLACK);		
		
		String[] wtSplit = WTKeys[wtClass].split(",");
		int tdWidth = (WIDTH - 2 * RULER_WIDTH) / (sortedKeys.length + 1);
		if(tdWidth < 100) tdWidth = 100;
		int trHeight = (HEIGHT - 2 * RULER_WIDTH) / (wtSplit.length + 1);
		if(trHeight > 30) trHeight = 30;
		if(trHeight < 20) trHeight = 20;
		String key = null;
		int characterWidth = 50;
		for (int i = 0; i < sortedKeys.length; i++) {
			key = sortedKeys[i];
			if(character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_DIAGNOSES])
					&& key.length() > 5)
				key = key.substring(0, 4) + "...";
			graphic.drawString(key, characterWidth + i * tdWidth, trHeight - 20);
			graphic.drawString("" + associationMap[wtSplit.length][i], characterWidth + i * tdWidth, (wtSplit.length + 1) * trHeight);
		}
		int count = 0;
		int N = dataList.length;
		for (int i = 0; i < wtSplit.length; i++) {
			graphic.drawString(wtSplit[i], 0, (i + 1) * trHeight);
			graphic.drawString("" + associationMap[i][sortedKeys.length], characterWidth + sortedKeys.length * tdWidth, (i + 1) * trHeight);
			for (int j = 0; j < sortedKeys.length; j++) {
				count = associationMap[i][j];
				graphic.drawString("" + count, characterWidth + j * tdWidth, (i + 1) * trHeight);
				graphic.drawString(
						"[" + ((float)(count * 1000 / N) / 10) + "%," + ((float)(count * 1000 / associationMap[wtSplit.length][j] ) / 10)+ "%]",
						2 * characterWidth + j * tdWidth, (i + 1) * trHeight);
			}
		}
		graphic.drawString(
				"" + associationMap[wtSplit.length][sortedKeys.length],
				50 + sortedKeys.length * tdWidth,
				(wtSplit.length + 1) * trHeight);
		
		
		g.drawImage(img, RULER_WIDTH + offsetX, RULER_WIDTH + offsetY, WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, null);
	}

}
