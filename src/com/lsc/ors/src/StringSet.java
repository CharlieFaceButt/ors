package com.lsc.ors.src;

import java.util.HashMap;
import java.util.Map;

import com.lsc.ors.beans.OutpatientLog;

public class StringSet {
	public static final String DEFAULT_DATA_PATH = "E://seme7/毕设/修复2科门诊日志统计11-13/11/11-1.xls";
	
	//function
	public static final String PLATFORM_TITLE = "ORS可视化界面";
	
	/**
	 * for function: visualization
	 */
	public static final String VISUALIZE = "数据可视化";
	public static final int CMD_VISUALIZE = 0;
	/**
	 * for visualization: waiting record distribution
	 */
	public static final String VSL_WAITING_RECORD_DISTRIBUTION = "等待时间分布记录";
	public static final int CMD_VSL_WAITING_RECORD_DISTRIBUTION = 4;
	/**
	 * for visualization: waiting record of queue status
	 */
	public static final String VSL_WAITING_RECORD_QUEUE_STATUS = "时刻排队状态变化图";
	public static final int CMD_VSL_WAITING_RECORD_QUEUE_STATUS = 5;
	/**
	 * for visualization: population distribution
	 */
	public static final String VSL_POPULATION_DISTRIBUTION = "等待时间人数分布图";
	public static final int CMD_VSL_POPULATION_DISTRIBUTION = 6;
	/**
	 * for visualization: waiting time distribution
	 */
	public static final String VSL_WAITING_TIME_DISTRIBUTION = "平均等待时间分布图";
	public static final int CMD_VSL_WAITING_TIME_DISTRIBUTION = 7;
	
	/**
	 * for function: analysis
	 */
	public static final String ANALYZE = "数据分析";
	public static final int CMD_ANALYZE = 1;
	
	/**
	 * for function: recommendation
	 */
	public static final String RECOMMEND = "ORS推荐";
	public static final int CMD_RECOMMEND = 2;
	
	//universal
	public static final String VACANT_CONTENT = "暂无内容";
	public static final int CMD_VACANT_CONTENT = 8;
	public static final String CONTROL_CLOSED = "界面关闭";
	
	//jargon
	public static final String DEPARTMENT_NAME = "科室名称"; 
	public static final String CURRENT_DATE = "当前日期";
	public static final String CURRENT_TIME = "当前时间";
	public static final String BEGIN_DATE = "开始时间";
	public static final String FINISH_DATE = "结束时间";
	
	//menu command
	public static final String MENU_DATA = "数据";
	public static final String MENU_FUNC = "任务";
	
	//menu file command
	public static final String MENUITEM_IMPORT = "导入数据";
	public static final int CMD_IMPORT = 16;
	public static final String MENUITEM_EXPORT = "导出数据";
	public static final int CMD_EXPORT = 17;
	public static final String MENUITEM_TRUNCATE = "清空数据库";
	public static final int CMD_TRUNCATE = 22;

	//button command
	public static final String NEXT_DAY = "之后一天";
	public static final int CMD_NEXT_DAY = 18;
	public static final String LAST_DAY = "之前一天";
	public static final int CMD_LAST_DAY = 19;
	public static final String NEXT_WEEK = "之后一周";
	public static final int CMD_NEXT_WEEK = 20;
	public static final String LAST_WEEK = "之前一周";
	public static final int CMD_LAST_WEEK = 21;
	public static final String NEXT_MONTH = "之后一月";
	public static final int CMD_NEXT_MONTH = 27;
	public static final String LAST_MONTH = "之前一月";
	public static final int CMD_LAST_MONTH = 28;
	public static final String NEXT_YEAR = "之后一年";
	public static final int CMD_NEXT_YEAR = 29;
	public static final String LAST_YEAR = "之前一年";
	public static final int CMD_LAST_YEAR = 30;
	public static final String TIME_UNIT_DAY = "时间单位（天）";
	public static final int CMD_TIME_UNIT_DAY = 23;
	public static final String TIME_UNIT_WEEK = "时间单位（周）"; 
	public static final int CMD_TIME_UNIT_WEEK = 24;
	public static final String TIME_UNIT_MONTH = "时间单位（月）"; 
	public static final int CMD_TIME_UNIT_MONTH = 25;
	public static final String TIME_UNIT_YEAR = "时间单位（年）"; 
	public static final int CMD_TIME_UNIT_YEAR = 26;
	public static final String SELECT_ALL = "全选";
	public static final int CMD_SELECT_ALL = 50;
	
	public static enum CMD_FEATURE{;
		public static final int OUTPATIENT_BASE = 31;//31-49
	}
	public static final String TOTAL = "总计";
	public static final int CMD_TOTAL = 51;
	
	//action command
	public static final String MOUSE_CLICK = "鼠标点击";
	public static final int CMD_MOUSE_CLICK = 64;
	public static final String MOUSE_WHEEL = "鼠标滚轮";
	public static final int CMD_MOUSE_WHEEL = 65;
	public static final String COMBO_BOX_CHANGED = "comboBoxChanged";
	public static final int CMD_COMBO_BOX_CHANGED = 66;
	
	public static final String INFO_START = "初始化搞定";
	
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
		commandMap.put(MENUITEM_TRUNCATE, CMD_TRUNCATE);
		
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
		
		commandMap.put(MOUSE_CLICK, CMD_MOUSE_CLICK);
		commandMap.put(MOUSE_WHEEL, CMD_MOUSE_WHEEL);
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
	public static final String DEBUG_SAMPLE = "示例";
}
