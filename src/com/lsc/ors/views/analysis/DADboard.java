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

import com.lsc.ors.beans.FiveNumberObject;
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
	protected static final int DIAGRAMTYPE_BOX = 1;
	protected static final int DIAGRAMTYPE_DECRETE = 2;
	protected static final int DIAGRAMTYPE_CURVE = 3;

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
		
		switch (diagramType) {
		case DIAGRAMTYPE_CHART:
			associationMap = DataMiner.associationOf2Character(
					dataList,
					WTKeys[wtClass].split(","), OutpatientLogCharacters.INDEX_WAIT,
					sortedKeys, OutpatientLogCharacters.getIndex(character));
			break;
		case DIAGRAMTYPE_BOX:
			boxList = DataMiner.getFiveNumberBoxesForWaitingTime(
					dataList, sortedKeys, 
					OutpatientLogCharacters.getIndex(character));
			break;
		case DIAGRAMTYPE_DECRETE:
			break;
		case DIAGRAMTYPE_CURVE:
			break;
		default:
			break;
		}
		
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
	FiveNumberObject[] boxList;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub

		if(dataList == null) return;
		
		Image img = new BufferedImage(WIDTH - offsetX, HEIGHT - offsetY, BufferedImage.TYPE_INT_RGB);
		Graphics graphic = img.getGraphics();
		
		//background
		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0, 0, imgWidth, imgHeight);
		graphic.setColor(Color.BLACK);		

		int characterWidth = 2 * RULER_WIDTH;
		switch (diagramType) {
		case DIAGRAMTYPE_CHART:
			//waiting time classes
			String[] wtSplit = WTKeys[wtClass].split(",");
			
			//tabel cell height and width
			int tdWidth = (WIDTH - 2 * RULER_WIDTH) / (sortedKeys.length + 1);
			if(tdWidth < 150) tdWidth = 150;
			int trHeight = (HEIGHT - 2 * RULER_WIDTH) / (wtSplit.length + 1);
			if(trHeight > 60) trHeight = 60;
			if(trHeight < 50) trHeight = 50;
			
			//draw table content
			int count = 0;
			int N = dataList.length;
			float confPercent = 0f;
			for (int i = 0; i < wtSplit.length; i++) {
				for (int j = 0; j < sortedKeys.length; j++) {
					count = associationMap[i][j];
					//count
					graphic.drawString("" + count, characterWidth + j * tdWidth, (i + 1) * trHeight);
					//support and confidence
					confPercent = ((float)(count * 1000 / associationMap[wtSplit.length][j] ) / 10);
					graphic.drawString(
							"[" + ((float)(count * 1000 / N) / 10) + "%," + confPercent + "%]",
							characterWidth + j * tdWidth, (i + 1) * trHeight + 15);
					//correlation
					graphic.drawString("corr = " + confPercent * N / (associationMap[i][sortedKeys.length] * 100),
							characterWidth + j * tdWidth, (i + 1) * trHeight + 30);
				}
			}
			//draw rulers
			graphic.setColor(Color.WHITE);
			graphic.fillRect(0, imgHeight - RULER_WIDTH, imgWidth, RULER_WIDTH);
			graphic.fillRect(imgWidth - 2 * RULER_WIDTH, 0, 2 * RULER_WIDTH, imgHeight);
			graphic.fillRect(-offsetX, -offsetY, imgWidth, RULER_WIDTH);
			graphic.fillRect(-offsetX, -offsetY, characterWidth, imgHeight);
			graphic.setColor(Color.BLACK);
			
			//character values
			int sumY = imgHeight;
			int chaY = RULER_WIDTH - 5;
			if(offsetY < 0) chaY -= offsetY;
			String key = null;
			for (int i = 0; i < sortedKeys.length; i++) {
				key = sortedKeys[i];
				if(character.equals(OutpatientLogCharacters.KEYS[OutpatientLogCharacters.INDEX_DIAGNOSES])
						&& key.length() > 5)
					key = key.substring(0, 4) + "...";
				//title
				graphic.drawString(sortedKeys[i], characterWidth + i * tdWidth, chaY);
				//sum
				graphic.drawString("" + associationMap[wtSplit.length][i], characterWidth + i * tdWidth, sumY);
			}
			//waiting time classes
			int sumX = imgWidth - 2 * RULER_WIDTH;
			int chaX = 0;
			if(offsetX < 0) chaX -= offsetX;
			for (int i = 0; i < wtSplit.length; i++) {
				//title
				graphic.drawString(wtSplit[i], chaX, (i + 1) * trHeight);
				//sum
				graphic.drawString("" + associationMap[i][sortedKeys.length], sumX, (i + 1) * trHeight);
			}
			
			//total
			graphic.setColor(Color.WHITE);
			graphic.fillRect(sumX, sumY - RULER_WIDTH, 2 * RULER_WIDTH, RULER_WIDTH);
			graphic.fillRect(-offsetX, -offsetY, characterWidth, RULER_WIDTH);
			graphic.setColor(Color.BLACK);
			graphic.drawString(
					"" + associationMap[wtSplit.length][sortedKeys.length],
					sumX, sumY);
			break;
		default:
			break;
		}

		g.drawImage(img, offsetX, offsetY, WIDTH - offsetX, HEIGHT - offsetY, null);
		//ruler lines
		g.drawLine(0, HEIGHT - RULER_WIDTH, WIDTH, HEIGHT - RULER_WIDTH);
		g.drawLine(WIDTH - 2 * RULER_WIDTH, 0, WIDTH - 2 * RULER_WIDTH, HEIGHT);
		g.drawLine(0, RULER_WIDTH, WIDTH, RULER_WIDTH);
		g.drawLine(characterWidth, 0, characterWidth, HEIGHT);
	}

}
