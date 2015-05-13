package com.lsc.ors.resource;

import java.util.HashMap;
import java.util.Map;

import com.lsc.ors.beans.OutpatientLog;

public class StringSet {
	public static final String DEFAULT_DATA_PATH = "E://seme7/����/�޸�2��������־ͳ��11-13/11/11-1.xls";
	
	//function
	public static final String PLATFORM_TITLE = "ORS���ӻ�����";
	
	/**
	 * for function: visualization
	 */
	public static final String VISUALIZE = "����ͳ��";
	public static final int CMD_VISUALIZE = 0;
	/**
	 * for visualization: waiting record distribution
	 */
	public static final String VSL_WAITING_RECORD_DISTRIBUTION = "�ȴ�ʱ��ֲ���¼";
	public static final int CMD_VSL_WAITING_RECORD_DISTRIBUTION = 4;
	/**
	 * for visualization: waiting record of queue status
	 */
	public static final String VSL_WAITING_RECORD_QUEUE_STATUS = "ʱ���Ŷ�״̬�仯ͼ";
	public static final int CMD_VSL_WAITING_RECORD_QUEUE_STATUS = 5;
	/**
	 * for visualization: population distribution
	 */
	public static final String VSL_POPULATION_DISTRIBUTION = "�ȴ�ʱ�������ֲ�ͼ";
	public static final int CMD_VSL_POPULATION_DISTRIBUTION = 6;
	/**
	 * for visualization: waiting time distribution
	 */
	public static final String VSL_WAITING_TIME_DISTRIBUTION = "ƽ���ȴ�ʱ��ֲ�ͼ";
	public static final int CMD_VSL_WAITING_TIME_DISTRIBUTION = 7;
	
	/**
	 * for function: analysis
	 */
	public static final String ANALYZE = "���ݷ���";
	public static final int CMD_ANALYZE = 1;
	
	public static final String WAITING_TIME_ANALYSIS = "�Ŷӵȴ�ʱ�����";
	public static final int CMD_WAITING_TIME_ANALTSIS = 12;
	public static final String DOCTOR_ANALYSIS = "ҽ����������";
	public static final int CMD_DOCTOR_ANALTSIS = 13;
	public static final String OTHER_ANALYSIS = "��������";
	public static final int CMD_OTHER_ANALTSIS = 14;
	/**
	 * for analysis: waiting time analysis
	 */
	public static final String ANL_ATTRIBUTE_DESCRIPTION = "�����Ը�������";
	public static final int CMD_ANL_ATTRIBUTE_DESCRIPTION = 9;
	/**
	 * for analysis: waiting time analysis
	 */
	public static final String ANL_DOUBLE_ATTRIBUTES_DESCRIPTION = "˫���Ը�������";
	public static final int CMD_ANL_DOUBLE_ATTRIBUTES_DESCRIPTION = 10;
	/**
	 * for analysis: waiting time analysis
	 */
	public static final String ANL_WAITING_TIME = "�ȴ�ʱ��ԭ�����";
	public static final int CMD_ANL_WAITING_TIME = 11;
	
	public static final String DCT_FEATURE = "ҽ����������";
	public static final int CMD_DCT_FEATURE = 67;
	public static final String DCT_COMPARISON = "ҽ�������Ƚ�";
	public static final int CMD_DCT_COMPARISON = 68;
	public static final String DIAGNOSIS_INCIDENCE = "�߷���֢�뺱����֢";
	public static final int CMD_DIAGNOSIS_INCIDENCE = 69;
	public static final String DIAGNOSIS_CONSULTATION_RATE = "��/�͸����ʲ�֢";
	public static final int CMD_DIAGNOSIS_CONSULTATION_RATE = 71;
	public static final String DIAGNOSIS_COMPLICATION = "�߲�����֢";
	public static final int CMD_DIAGNOSIS_COMPLICATION = 72;
	public static final String DIAGNOSIS_COMPARISON = "��֢�Աȷ���";
	public static final int CMD_DIAGNOSIS_COMPARISON = 70;
	
	/**
	 * for function: recommendation
	 */
	public static final String RECOMMEND = "ORS�Ƽ�";
	public static final int CMD_RECOMMEND = 2;
	
	//universal
	public static final String VACANT_CONTENT = "��������";
	public static final int CMD_VACANT_CONTENT = 8;
	public static final String CONTROL_CLOSED = "����ر�";
	
	//jargon
	public static final String DEPARTMENT_NAME = "��������"; 
	public static final String CURRENT_DATE = "��ǰ����";
	public static final String CURRENT_TIME = "��ǰʱ��";
	public static final String BEGIN_DATE = "��ʼʱ��";
	public static final String FINISH_DATE = "����ʱ��";
	
	//menu command
	public static final String MENU_DATA = "����";
	public static final String MENU_FUNC = "����";
	
	//menu file command
	public static final String MENUITEM_IMPORT = "��������";
	public static final int CMD_IMPORT = 16;
	public static final String MENUITEM_EXPORT = "��������";
	public static final int CMD_EXPORT = 17;
	public static final String MENUITEM_TRUNCATE = "������ݿ�";
	public static final int CMD_TRUNCATE = 22;

	//button command
	public static final String ST_SMR = "����ſ�";
	public static final int CMD_ST_SMR = 3;
	public static final String NEXT_DAY = "֮��һ��";
	public static final int CMD_NEXT_DAY = 18;
	public static final String LAST_DAY = "֮ǰһ��";
	public static final int CMD_LAST_DAY = 19;
	public static final String NEXT_WEEK = "֮��һ��";
	public static final int CMD_NEXT_WEEK = 20;
	public static final String LAST_WEEK = "֮ǰһ��";
	public static final int CMD_LAST_WEEK = 21;
	public static final String NEXT_MONTH = "֮��һ��";
	public static final int CMD_NEXT_MONTH = 27;
	public static final String LAST_MONTH = "֮ǰһ��";
	public static final int CMD_LAST_MONTH = 28;
	public static final String NEXT_YEAR = "֮��һ��";
	public static final int CMD_NEXT_YEAR = 29;
	public static final String LAST_YEAR = "֮ǰһ��";
	public static final int CMD_LAST_YEAR = 30;
	public static final String TIME_UNIT_DAY = "ʱ�䵥λ���죩";
	public static final int CMD_TIME_UNIT_DAY = 23;
	public static final String TIME_UNIT_WEEK = "ʱ�䵥λ���ܣ�"; 
	public static final int CMD_TIME_UNIT_WEEK = 24;
	public static final String TIME_UNIT_MONTH = "ʱ�䵥λ���£�"; 
	public static final int CMD_TIME_UNIT_MONTH = 25;
	public static final String TIME_UNIT_YEAR = "ʱ�䵥λ���꣩"; 
	public static final int CMD_TIME_UNIT_YEAR = 26;
	public static final String TIME_UNIT_ALL = "ʱ�䵥λ��ȫ����";
	public static final int CMD_TIME_UNIT_ALL = 53;
	public static final String SELECT_ALL = "ȫѡ";
	public static final int CMD_SELECT_ALL = 50;
	
	public static enum CMD_FEATURE{;
		public static final int OUTPATIENT_BASE = 31;//31-49
	}
	public static final String TOTAL = "�ܼ�";
	public static final int CMD_TOTAL = 51;
	public static final String AVERAGE = "ƽ��";
	public static final int CMD_AVERAGE = 52;
	
	//action command
	public static final String MOUSE_CLICK = "�����";
	public static final int CMD_MOUSE_CLICK = 64;
	public static final String MOUSE_WHEEL = "������";
	public static final int CMD_MOUSE_WHEEL = 65;
	public static final String MOUSE_MOVE = "����ƶ�";
	public static final int CMD_MOUSE_MOVE = 66;
	public static final String COMBO_BOX_CHANGED = "comboBoxChanged";
	public static final int CMD_COMBO_BOX_CHANGED = 66;
	
	public static final String INFO_START = "��ʼ���㶨";
	
	public static Map<String , Integer> commandMap = new HashMap<String, Integer>();
	
	private static StringSet instance = null;
	public static synchronized StringSet getInstance(){
		if(instance==null) instance = new StringSet();
		return instance;
	}
	private StringSet() {
		System.out.println("commandMap initiated as a singleton");
		commandMap.put(VISUALIZE, CMD_VISUALIZE);
		commandMap.put(WAITING_TIME_ANALYSIS, CMD_WAITING_TIME_ANALTSIS);
		commandMap.put(DOCTOR_ANALYSIS, CMD_DOCTOR_ANALTSIS);
		commandMap.put(OTHER_ANALYSIS, CMD_OTHER_ANALTSIS);
		commandMap.put(RECOMMEND, CMD_RECOMMEND);
		
		commandMap.put(VSL_WAITING_TIME_DISTRIBUTION, CMD_VSL_WAITING_TIME_DISTRIBUTION);
		commandMap.put(VSL_POPULATION_DISTRIBUTION, CMD_VSL_POPULATION_DISTRIBUTION);
		commandMap.put(VSL_WAITING_RECORD_QUEUE_STATUS, CMD_VSL_WAITING_RECORD_QUEUE_STATUS);
		commandMap.put(VSL_WAITING_RECORD_DISTRIBUTION, CMD_VSL_WAITING_RECORD_DISTRIBUTION);
		commandMap.put(ANL_ATTRIBUTE_DESCRIPTION, CMD_ANL_ATTRIBUTE_DESCRIPTION);
		commandMap.put(ANL_DOUBLE_ATTRIBUTES_DESCRIPTION, CMD_ANL_DOUBLE_ATTRIBUTES_DESCRIPTION);
		commandMap.put(ANL_WAITING_TIME, CMD_ANL_WAITING_TIME);
		commandMap.put(DCT_FEATURE, CMD_DCT_FEATURE);
		commandMap.put(DCT_COMPARISON, CMD_DCT_COMPARISON);
		commandMap.put(DIAGNOSIS_INCIDENCE, CMD_DIAGNOSIS_INCIDENCE);
		commandMap.put(DIAGNOSIS_CONSULTATION_RATE, CMD_DIAGNOSIS_CONSULTATION_RATE);
		commandMap.put(DIAGNOSIS_COMPLICATION, CMD_DIAGNOSIS_COMPLICATION);
		commandMap.put(DIAGNOSIS_COMPARISON, CMD_DIAGNOSIS_COMPARISON);
		
		commandMap.put(MENUITEM_IMPORT, CMD_IMPORT);
		commandMap.put(MENUITEM_EXPORT, CMD_EXPORT);
		commandMap.put(MENUITEM_TRUNCATE, CMD_TRUNCATE);
		
		commandMap.put(ST_SMR, CMD_ST_SMR);
		commandMap.put(LAST_DAY, CMD_LAST_DAY);
		commandMap.put(NEXT_DAY, CMD_NEXT_DAY);
		commandMap.put(LAST_WEEK, CMD_LAST_WEEK);
		commandMap.put(NEXT_WEEK, CMD_NEXT_WEEK);
		commandMap.put(LAST_MONTH, CMD_LAST_MONTH);
		commandMap.put(NEXT_MONTH, CMD_NEXT_MONTH);
		commandMap.put(LAST_YEAR, CMD_LAST_YEAR);
		commandMap.put(NEXT_YEAR, CMD_NEXT_YEAR);
		
		commandMap.put(TIME_UNIT_DAY, CMD_TIME_UNIT_DAY);
		commandMap.put(TIME_UNIT_WEEK, CMD_TIME_UNIT_WEEK);
		commandMap.put(TIME_UNIT_MONTH, CMD_TIME_UNIT_MONTH);
		commandMap.put(TIME_UNIT_YEAR, CMD_TIME_UNIT_YEAR);
		commandMap.put(TIME_UNIT_ALL, CMD_TIME_UNIT_ALL);
		
		commandMap.put(MOUSE_CLICK, CMD_MOUSE_CLICK);
		commandMap.put(MOUSE_WHEEL, CMD_MOUSE_WHEEL);
		commandMap.put(MOUSE_MOVE, CMD_MOUSE_MOVE);
		commandMap.put(COMBO_BOX_CHANGED, CMD_COMBO_BOX_CHANGED);
		commandMap.put(VACANT_CONTENT, CMD_VACANT_CONTENT);
		
		for(int i = 0 ; i < OutpatientLog.KEYS.length ; i ++){
			commandMap.put(OutpatientLog.KEYS[i], CMD_FEATURE.OUTPATIENT_BASE + i);
		}
		commandMap.put(SELECT_ALL, CMD_SELECT_ALL);
	}
	
	public Integer getCommandIndex(String command){
		return commandMap.get(command);
	}
	
	//debug
	public static final String DEBUG_SAMPLE = "ʾ��";
}
