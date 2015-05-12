package com.lsc.ors.views.analysis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.util.FeatureKeyGenerator;

public class DCTCPboard extends AnalysisBoard{

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = -7306209468131675644L;

	private String character;
	private int characterLayer;
	
	public DCTCPboard(OutpatientLogCharacters[] logList, ActionListener listener) {
		super(logList, listener);
		// TODO Auto-generated constructor stub
		character = OutpatientLogCharacters.KEYS[0];
		setData(dataList);
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
		updateView(list, character, 0);
	}
	public void changeCharacter(String c){
		updateView(dataList, c, 0);
	}

	String[] doctorList;
	private Map<String, Map<String, Integer>> valueMap;
	private void updateView(OutpatientLogCharacters[] list, String character, int characterLayer){
		if(this.dataList != list){
			this.dataList = list;
			refreshDoctorList();
		}
		this.character = character;
		this.characterLayer = characterLayer;
		//generate characters
		generateCharacters();
		
		isRepaintable = true;
	}
	/**
	 * 更新医生列表
	 */
	private void refreshDoctorList(){
		if(dataList == null){
			doctorList = null;
			return;
		}
		LinkedList<String> list = new LinkedList<String>();
		for (OutpatientLogCharacters olc : dataList) {
			if(!list.contains(olc.getDoctor_name())){
				list.add(olc.getDoctor_name());
			}
		}
		doctorList = new String[list.size()];
		doctorList = list.toArray(doctorList);
	}
	/**
	 * 生成值分布
	 */
	private void generateCharacters(){
		valueMap = new HashMap<String, Map<String,Integer>>();
		Map<String,Integer> characterMap = null;
		String key = null;
		Integer count = null;
		for(OutpatientLogCharacters olc : dataList){
			key = olc.get(character);
			key = FeatureKeyGenerator.generalization(key, character);
			characterMap = valueMap.get(key);
			if(characterMap == null){
				characterMap = new HashMap<String, Integer>();
				valueMap.put(key, characterMap);
			}
			count = characterMap.get(olc.getDoctor_name());
			if(count == null || count == 0){
				count = 1;
			} else{
				count ++;
			}
			characterMap.put(olc.getDoctor_name(), count);
		}
		refreshDoctorList();
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

	Color[] colorList;
	private int maxAmount = 0;
	private int adjustSpeed = 3;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		//movements
		if(!isReleased) return;
		
		if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
		else if(offsetX > 0) offsetX--;
		
		if(offsetY > 5) offsetY -= (offsetY / adjustSpeed);
		else if(offsetY > 0) offsetY--;
		
		if(offsetX <= 0 && offsetY <= 0) 
			isRepaintable = false;
		
		//max amount of character
		Map<String,Integer> map = null;
		for(String cKey : valueMap.keySet()){
			map = valueMap.get(cKey);
			int amount = 0;
			for(String dKey : map.keySet()){
				amount += map.get(dKey);					
			}
			if(amount > maxAmount) maxAmount = amount;
		}
		
		//color list
		if(doctorList != null){
			colorList = new Color[doctorList.length];
			for (int i = 0; i < colorList.length; i++) {
				colorList[i] = new Color((int)(Math.random() * 0xffffff));
			}
		}
	}

	private float yPercent = 0.2f;
	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		if(doctorList == null || valueMap == null) return;
		Image img = new BufferedImage(WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, BufferedImage.TYPE_INT_RGB);
		Graphics graphic = img.getGraphics();
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
		graphic.setColor(Color.BLACK);	
		
		//yHeight and yGap
		int height = (HEIGHT - RULER_WIDTH) / valueMap.size();
		int yGap = 3;
		if(height < 5){
			height = 4;
			yGap = 1;
		}else{
			yGap = (int)(yPercent * height);
			height -= yGap;
		}
		
		
		Map<String,Integer> characterMap = null;
		int y = RULER_WIDTH;
		for(String crctr : valueMap.keySet()){
			characterMap = valueMap.get(crctr);
			int x = 0;
			int width = 0;
			for( int i = 0 ; i < doctorList.length ; i ++ ){
				if(characterMap.get(doctorList[i]) != null){
					width = (WIDTH - RULER_WIDTH * 2) * characterMap.get(doctorList[i]) / maxAmount;
					x += width;
					graphic.setColor(colorList[i]);
					graphic.fillRect(x - width, y, width, height);
				}
			}
			y += (height + yGap);
		}
		
		//draw rulers
		drawRulers(graphic, height + yGap);
		
		g.drawImage(img, RULER_WIDTH + offsetX, RULER_WIDTH + offsetY, WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, null);
		
	}
	private void drawRulers(Graphics g, int yHeight){
		int textWidth = 60;
		for (int i = 0, x = RULER_WIDTH; i < doctorList.length; i++) {
			g.setColor(colorList[i]);
			g.drawString(doctorList[i], x, RULER_WIDTH / 2);
			x += textWidth;
		}
		g.setColor(new Color(0));
		int j = 0;
		for(String cKey : valueMap.keySet()){
			g.drawString(cKey, RULER_WIDTH * 2, RULER_WIDTH * 2 + j * yHeight);
			j++;
		}
	}

}
