package com.lsc.ors.src;

import java.util.HashMap;
import java.util.Map;

public class StringSet {
	public static final String DEFAULT_DATA_PATH = "E://seme7/����/�޸�2��������־ͳ��11-13/11/11-1.xls";
	
	//function
	public static final String PLATFORM_TITLE = "ORS���ӻ�����";
	
	/**
	 * for function: visualization
	 */
	public static final String VISUALIZE = "���ݿ��ӻ�";
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
	
	/**
	 * for function: recommendation
	 */
	public static final String RECOMMEND = "ORS�Ƽ�";
	public static final int CMD_RECOMMEND = 2;
	
	//universal
	public static final String VACANT_CONTENT = "��������";
	public static final String CONTROL_CLOSED = "����ر�";
	
	//jargon
	public static final String DEPARTMENT_NAME = "��������"; 
	public static final String CURRENT_DATE = "��ǰʱ��";
	public static final String BEGIN_DATE = "��ʼʱ��";
	public static final String FINISH_DATE = "����ʱ��";
	
	
	//menu command
	public static final String MENU_FILE = "�ļ�";
	public static final String MENU_FUNC = "����";
	
	//menu file command
	public static final String MENUITEM_IMPORT = "��������";
	public static final int CMD_IMPORT = 16;
	public static final String MENUITEM_EXPORT = "��������";
	public static final int CMD_EXPORT = 17;

	//button command
	public static final String NEXT_DAY = "֮��һ��";
	public static final int CMD_NEXT_DAY = 18;
	public static final String LAST_DAY = "֮ǰһ��";
	public static final int CMD_LAST_DAY = 19;
	public static final String NEXT_WEEK = "֮��һ��";
	public static final int CMD_NEXT_WEEK = 20;
	public static final String LAST_WEEK = "֮ǰһ��";
	public static final int CMD_LAST_WEEK = 21;
	
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
		commandMap.put(ANALYZE, CMD_ANALYZE);
		commandMap.put(RECOMMEND, CMD_RECOMMEND);
		
		commandMap.put(VSL_WAITING_TIME_DISTRIBUTION, CMD_VSL_WAITING_TIME_DISTRIBUTION);
		commandMap.put(VSL_POPULATION_DISTRIBUTION, CMD_VSL_POPULATION_DISTRIBUTION);
		commandMap.put(VSL_WAITING_RECORD_QUEUE_STATUS, CMD_VSL_WAITING_RECORD_QUEUE_STATUS);
		commandMap.put(VSL_WAITING_RECORD_DISTRIBUTION, CMD_VSL_WAITING_RECORD_DISTRIBUTION);
		
		commandMap.put(MENUITEM_IMPORT, CMD_IMPORT);
		commandMap.put(MENUITEM_EXPORT, CMD_EXPORT);
		
		commandMap.put(LAST_DAY, CMD_LAST_DAY);
		commandMap.put(NEXT_DAY, CMD_NEXT_DAY);
		commandMap.put(LAST_WEEK, CMD_LAST_WEEK);
		commandMap.put(NEXT_WEEK, CMD_NEXT_WEEK);
		
	}
	
	public Integer getCommandIndex(String command){
		return commandMap.get(command);
	}
	
	//debug
	public static final String DEBUG_SAMPLE = "ʾ��";
}
