package com.lsc.ors.views.analysis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import com.lsc.ors.beans.OutpatientLogCharacters;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.util.FeatureKeyGenerator;

public class DGFboard extends AnalysisBoard {

	/**
	 * generated serial ID
	 */
	private static final long serialVersionUID = 3329170050927273154L;

	private int modelFlag;
	private int diagramType;
	public DGFboard(OutpatientLogCharacters[] logList, ActionListener listener) {
		super(logList, listener);
		// TODO Auto-generated constructor stub
		modelFlag = StringSet.CMD_DIAGNOSIS_INCIDENCE;
		diagramType = DIAGRAMTYPE_CHART;
		setData(logList);
	}

	@Override
	public void setData(OutpatientLogCharacters[] list) {
		// TODO Auto-generated method stub
		updateStatus(list, modelFlag, diagramType);
		miningAllData();
	}
	public void changeDiagramType(int diagramType){
		updateStatus(dataList, modelFlag, diagramType);
	}
	public void changeModelFlag(String modelFlag){
		updateStatus(dataList, StringSet.getInstance().getCommandIndex(modelFlag), diagramType);
	}
	private void updateStatus(OutpatientLogCharacters[] list, int mFlag, int dType){
		this.dataList = list;
		this.modelFlag = mFlag;
		this.diagramType = dType;
		isRepaintable = true;
	}

	class DiagnosisFeature{
		public int incidence;
		public int consultation;
		public Map<String, Integer> complicationMap;
		public DiagnosisFeature(){
			incidence = consultation = 0;
			complicationMap = new HashMap<String, Integer>();
		}
	}

	private int totalAmount;
	private int diagnoseAmount;
	private Map<String, DiagnosisFeature> miningMap;
	@Override
	public void miningAllData() {
		// TODO Auto-generated method stub
		diagnoseAmount = 0;
		totalAmount = 0;
		String dg = null;
		miningMap = new HashMap<String, DiagnosisFeature>();
		
		//set features for diagnosis
		DiagnosisFeature dgFeature = null;
		for (int i = 0; i < dataList.length; i++) {
			totalAmount ++;
			dg = dataList[i].getDiagnosis();
			
			//if has diagnosed then set features
			if(dg != null){
				diagnoseAmount ++;
				
				if(dg.length() > 6){
					ConsoleOutput.pop("DGFboard.miningAllData", "diagnosis:" + dg);
				}
				dg = FeatureKeyGenerator.generalization(dg, OutpatientLogCharacters.INDEX_DIAGNOSES);
				
				String[] split = dg.split(",");
				Integer cpCount = 0;
				
				//multiple diagnosis
				for (int j = 0; j < split.length; j++) {
					//feature object for each diagnosis of a transaction
					dgFeature = miningMap.get(split[j]);
					if(dgFeature == null){
						dgFeature = new DiagnosisFeature();
						miningMap.put(split[j], dgFeature);
					}
					
					//incidence
					dgFeature.incidence ++;
					//consultation
					if("Y".equals(dataList[i].getFurther_consultation())){
						dgFeature.consultation ++;
					}
					//complication
					for (int k = 0; k < split.length; k++) {
						if(k != j){
							cpCount = dgFeature.complicationMap.get(split[k]);
							if(cpCount == null || cpCount == 0){
								cpCount = 1;
							} else{
								cpCount ++;
							}
							dgFeature.complicationMap.put(split[k], cpCount);
						}
					}
				}		
			}
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
	protected void onMouseExit(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseAlign = false;
		isRepaintable = true;
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean mouseAlign = false;
	private int alignX = 0;
	private int alignY = 0;
	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseAlign = true;
		isRepaintable = true;
		alignX = e.getX();
		alignY = e.getY();
	}

	private float multiple = 1.0f;
	@Override
	protected void onMouseWheel(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int rotation = e.getWheelRotation();
		if(multiple > 0.1f)
			multiple += rotation * 0.1f;
		isRepaintable = true;
	}


	private float maxIncidenceRate = 1.0f; 
	private int maxComplication = 0;
	private int adjustSpeed = 3;
	@Override
	protected void beforePaint() {
		// TODO Auto-generated method stub
		if(!isReleased) return;
		
		if(offsetX > 5) offsetX -= (offsetX / adjustSpeed);
		else if(offsetX > 0) offsetX--;
		
		if(offsetY > 5) offsetY -= (offsetY / adjustSpeed);
		else if(offsetY > 0) offsetY--;
		
		if(offsetX <= 0 && offsetY == 0) 
			isRepaintable = false;
		
		//max complication
		DiagnosisFeature dgFeature = null;
		Integer count = 0;
		for(String diagnosis : miningMap.keySet()){
			dgFeature = miningMap.get(diagnosis);
			for(String complication : dgFeature.complicationMap.keySet()){
				count = dgFeature.complicationMap.get(complication);
				if(count != null && count > maxComplication) maxComplication = count; 
			}
		}
	}

	@Override
	protected void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		if(dataList == null){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			return;
		}
		
		Image img = new BufferedImage(WIDTH - RULER_WIDTH - offsetX, HEIGHT - RULER_WIDTH - offsetY, BufferedImage.TYPE_INT_RGB);
		Graphics graphic = img.getGraphics();
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
		graphic.setColor(Color.BLACK);

		//表格
		if(diagramType == DIAGRAMTYPE_CHART){
			drawChart(graphic);
		}//图
		else{
			drawDiagram(graphic);
		}

		g.drawImage(img,
				RULER_WIDTH + offsetX, 
				RULER_WIDTH + offsetY,
				WIDTH - RULER_WIDTH - offsetX,
				HEIGHT - RULER_WIDTH - offsetY,
				null);
	}
	
	private int highIncidentRate = 5;
	private float highConsultationRate = 0.5f;
	private int minXItemWidth = 100;
	private void drawChart(Graphics g){
		int x = RULER_WIDTH;
		int xItemWidth = (WIDTH - RULER_WIDTH * 2) / miningMap.size();
		if(xItemWidth < minXItemWidth) xItemWidth = minXItemWidth;
		int y = RULER_WIDTH;
		DecimalFormat dFormat = new DecimalFormat("0.00##");
		
		DiagnosisFeature dgFeature = null;
		for(String diagnosis : miningMap.keySet()){
			y = RULER_WIDTH;
			dgFeature = miningMap.get(diagnosis);
			g.drawString(diagnosis, x, y);
			switch (modelFlag) {
			case StringSet.CMD_DIAGNOSIS_INCIDENCE:
				g.drawString("" + 
						dFormat.format(((float)dgFeature.incidence / totalAmount)), x, y + 2 * RULER_WIDTH);
				if(dgFeature.incidence * highIncidentRate > diagnoseAmount)
					g.drawString("高发", x, y + 4 * RULER_WIDTH);
				else g.drawString("非高发", x, y + 4 * RULER_WIDTH);
				break;
			case StringSet.CMD_DIAGNOSIS_CONSULTATION_RATE:
				float consultationRate = (float)dgFeature.consultation / dgFeature.incidence;
				g.drawString("" + dgFeature.consultation + " / " + dgFeature.incidence + 
						"\r\n = " + dFormat.format(consultationRate), 
						x, y + 2 * RULER_WIDTH);
				if(consultationRate > highConsultationRate){
					g.drawString("高复诊率", x, y + 4 * RULER_WIDTH);
				} else g.drawString("低复诊率", x, y + 4 * RULER_WIDTH);
				break;
			case StringSet.CMD_DIAGNOSIS_COMPLICATION:
				for(String complication : dgFeature.complicationMap.keySet()){
					g.drawString(complication, x, y + 2 * RULER_WIDTH);
					g.drawString("" + dgFeature.complicationMap.get(complication) + " : " + dgFeature.incidence,
							x, y + 3 * RULER_WIDTH);
					y += (2 * RULER_WIDTH);
				}
				break;
			default:
				break;
			}
			x += xItemWidth;
		}
	}
	private void drawDiagram(Graphics g){
		int x = RULER_WIDTH / 2;
		int xItemWidth = (WIDTH - RULER_WIDTH * 2) / miningMap.size();
		if(xItemWidth < minXItemWidth) xItemWidth = minXItemWidth;
		int xItemGap = xItemWidth / 4;
		xItemWidth -= xItemGap;
		DecimalFormat dFormat = new DecimalFormat("0.00##");
		int maxHeight = HEIGHT - 2 * RULER_WIDTH;
		int alignXIndex = 0;
		int alignYIndex = 0;
		
		DiagnosisFeature dgFeature = null;
		DiagnosisFeature alignFeature = null;
		switch (modelFlag) {
		case StringSet.CMD_DIAGNOSIS_INCIDENCE:
			g.drawString("" + dFormat.format(maxIncidenceRate), 0, 2 * RULER_WIDTH);
			g.drawLine(0, RULER_WIDTH, WIDTH - 2 * RULER_WIDTH - offsetX, RULER_WIDTH);
			
			//align index
			alignXIndex = (alignX - offsetX - RULER_WIDTH * 3 / 2) / (xItemWidth + xItemGap);
			int icdIndex = 0;
			
			//draw bars
			int incidentHeight = 0;
			for (String diagnosis : miningMap.keySet()) {
				g.setColor(Color.BLACK);
				g.drawString(diagnosis, x, RULER_WIDTH / 2);
				dgFeature = miningMap.get(diagnosis);
				incidentHeight = dgFeature.incidence * maxHeight / ((int)(maxIncidenceRate * diagnoseAmount));
				
				g.setColor(Color.BLUE);
				g.fillRect(x, HEIGHT - RULER_WIDTH - incidentHeight, xItemWidth, incidentHeight);
				x += (xItemWidth + xItemGap);
				
				if(icdIndex == alignXIndex){
					alignFeature = dgFeature;
				}
				icdIndex ++;
			}
			
			//draw aligns
			if(mouseAlign && alignFeature != null &&
					alignY - RULER_WIDTH + alignFeature.incidence * maxHeight / 
					((int)(maxIncidenceRate * diagnoseAmount)) > maxHeight){
				g.setColor(new Color(0f, 0f, 0f, 0.4f));
				g.fillRect(alignX - offsetX - RULER_WIDTH, alignY - RULER_WIDTH - 20, 50, 20);
				g.setColor(Color.WHITE);
				g.drawString("" + alignFeature.incidence + " / " + diagnoseAmount, 
						alignX - offsetX - RULER_WIDTH + 5, alignY - RULER_WIDTH - 5);
			}
			break;
		case StringSet.CMD_DIAGNOSIS_CONSULTATION_RATE:
			//align index
			alignXIndex = (alignX - offsetX - RULER_WIDTH * 3 / 2) / (xItemWidth + xItemGap);
			int cstIndex = 0;
			
			int cstHeight = 0;
			for(String diagnosis : miningMap.keySet()){
				g.setColor(Color.BLACK);
				g.drawString(diagnosis, x, RULER_WIDTH / 2);
				dgFeature = miningMap.get(diagnosis);
				cstHeight = dgFeature.consultation * maxHeight / dgFeature.incidence;
				
				g.setColor(Color.YELLOW);
				g.fillRect(x, HEIGHT - RULER_WIDTH - cstHeight, xItemWidth, cstHeight);
				x += (xItemWidth + xItemGap);

				if(cstIndex == alignXIndex){
					alignFeature = dgFeature;
				}
				cstIndex ++;
			}
			g.setColor(Color.BLACK);
			g.drawString("100%", 0, 2 * RULER_WIDTH);
			g.drawLine(0, RULER_WIDTH, WIDTH - 2 * RULER_WIDTH - offsetX, RULER_WIDTH);

			//draw aligns
			if(mouseAlign && alignFeature != null &&
					alignY - RULER_WIDTH + alignFeature.incidence * maxHeight / 
					((int)(maxIncidenceRate * diagnoseAmount)) > maxHeight){
				g.setColor(new Color(0f, 0f, 0f, 0.4f));
				g.fillRect(alignX - offsetX - RULER_WIDTH, alignY - RULER_WIDTH - 20, 50, 20);
				g.setColor(Color.WHITE);
				g.drawString("" + alignFeature.consultation + " / " + alignFeature.incidence, 
						alignX - offsetX - RULER_WIDTH + 5, alignY - RULER_WIDTH - 5);
			}
			break;
		case StringSet.CMD_DIAGNOSIS_COMPLICATION:
			xItemWidth = (WIDTH - 2 * RULER_WIDTH - x) / miningMap.size();
			int itemHeight = (HEIGHT - 2 * RULER_WIDTH) / miningMap.size();
			if(itemHeight < xItemWidth) xItemWidth = itemHeight;
			else itemHeight = xItemWidth;
			xItemWidth = (int)(xItemWidth * multiple);
			itemHeight = (int)(itemHeight * multiple);
			Integer cpc = 0;
			int y = RULER_WIDTH;
			int leftMargin = 5 * RULER_WIDTH;
			
			//align index
			alignXIndex = (alignX - offsetX - RULER_WIDTH - leftMargin) / xItemWidth;
			alignYIndex = (alignY - offsetY - RULER_WIDTH * 2) / itemHeight;
			String alignDiagnosis = null;
			String alignComplication = null;
			
			for(String dignosis : miningMap.keySet()){
				//align X index
				if(alignYIndex == 0) alignDiagnosis = dignosis;
				alignYIndex --;
				
				//draw title
				g.setColor(Color.BLACK);
				g.drawString(dignosis, 5, y + itemHeight / 2);
				
				//draw a line of blocks
				dgFeature = miningMap.get(dignosis);
				x = leftMargin;
				for(String complication : miningMap.keySet()){
					cpc = dgFeature.complicationMap.get(complication);
					if(cpc == null) cpc = 0;
					
					int blue = 150;
					int green = 255;
					int red = 150;
					if(maxComplication != 0) {
						blue -= 150 * cpc / maxComplication;
						red -= 150 * cpc / maxComplication;
						green -= 100 * cpc / maxComplication;
					}
					if(cpc == 0){
						g.setColor(new Color(200,210,200));
					}else{
						g.setColor(new Color(red,green,blue));
					}
					g.fillRect(x, y, xItemWidth, itemHeight);
					
					//align Y index
					if(alignYIndex == 0){
						if(alignXIndex == 0) alignComplication = complication;
						alignXIndex --;
					}
					
					x += xItemWidth;
				}
				y += itemHeight;
			}
			g.setColor(Color.WHITE);
			int N = miningMap.size();
			for (int i = 1; i < N; i++) {
				g.drawLine(leftMargin + i * xItemWidth, RULER_WIDTH,
						leftMargin + i * xItemWidth, RULER_WIDTH + itemHeight * N);
				g.drawLine(leftMargin, RULER_WIDTH + i * itemHeight, 
						leftMargin + itemHeight * N, RULER_WIDTH + i * itemHeight);
			}

			//draw aligns
			if(mouseAlign){
				alignFeature = miningMap.get(alignDiagnosis);
				if(alignFeature == null) break;
				Integer cpcCount = alignFeature.complicationMap.get(alignComplication);
				if(cpcCount == null) cpcCount = 0;
				String text = "" + alignDiagnosis + " + " + alignComplication;
				g.setColor(new Color(0f, 0f, 0f, 0.4f));
				g.fillRect(alignX - offsetX - RULER_WIDTH, alignY - RULER_WIDTH - 40, 12 * text.length(), 40);
				g.setColor(Color.WHITE);
				g.drawString(text, 
						alignX - offsetX - RULER_WIDTH + 5, alignY - RULER_WIDTH - 25);
				g.drawString("" + cpcCount + " / " + alignFeature.incidence,
						alignX - offsetX - RULER_WIDTH + 5, alignY - RULER_WIDTH - 5);
			}
			break;
		default:
			break;
		}
	}
}
