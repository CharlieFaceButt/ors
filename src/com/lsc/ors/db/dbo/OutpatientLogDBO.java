package com.lsc.ors.db.dbo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import net.sf.json.JSONObject;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.lsc.ors.beans.OutpatientLog;
import com.lsc.ors.db.DBOpeListener;
import com.lsc.ors.db.DBOperator;
import com.lsc.ors.db.listener.OutpatientLogDBOpeListener;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;
import com.lsc.ors.util.TimeFormatter;

/**
 * 对应Outpatient数据的数据库操作
 * @author charlieliu
 *
 */
public class OutpatientLogDBO extends DBOperator{
	
	private static Connection connection = null;
	private static String table = "outpatient_log";

	public static final int TIME_FIRST_DATE = 0;
	public static final int TIME_LAST_DATE = 1;
	public static final int TIME_EARLIEST_OF_DAY = 2;
	public static final int TIME_LATEST_OF_DAY = 3;
	public static final int RECORD_OF_DATE = StringSet.CMD_TIME_UNIT_DAY;
	public static final int RECORD_OF_WEEK = StringSet.CMD_TIME_UNIT_WEEK;
	public static final int RECORD_OF_MONTH = StringSet.CMD_TIME_UNIT_MONTH;
	public static final int RECORD_OF_YEAR = StringSet.CMD_TIME_UNIT_YEAR;
	
	/**
	 * 根据flag和constraint获得数据
	 * @param flag
	 * <br> - TIME_FIRST_DATE 获得数据第一天的日期
	 * <br> - TIME_LAST_DATE 获得数据最后一天的日期
	 * <br> - RECORD_OF_DATE 获得一个日期的记录
	 * <br> - RECORD_OF_WEEK 获得一个星期的记录
	 * <br> - RECORD_OF_MONTH 获得一个月的记录
	 * <br> - RECORD_OF_YEAR 获得一年的记录
	 * @param constraint
	 * @return
	 */
	public static Object getData(int flag, Object constraint){
		//open connection
		if(connection == null)
			connection = openConnection();
		if(connection != null)
			ConsoleOutput.pop("OutpatientDBO.getData", "数据库连接成功");
		else{
			ConsoleOutput.pop("OutpatientDBO.getData", "链接数据库失败");
			return null;
		}
		
		//result
		Object result = null;
		ResultSet rs = null;
		String whereClause = null;
		Date targetDate = null;
		switch(flag){
		case TIME_FIRST_DATE:
			rs = selectInSync(connection, new String[]{"min(registration_time)"}, table, null);
			if(rs != null){
				try {
					rs.next();				//异常抛出位置
					result = rs.getObject(1);	//异常抛出位置2
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ConsoleOutput.pop("OutpatientDBO.getFirstDate", "result set error");
				}
			}
			break;
		case TIME_LAST_DATE:
			rs = selectInSync(connection, new String[]{"max(registration_time)"}, table, null);
			if(rs != null){
				try {
					rs.next();				//异常抛出位置
					result = rs.getObject(1);	//异常抛出位置2
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ConsoleOutput.pop("OutpatientDBO.getFirstDate", "result set error");
				}
			}
			break;
		case TIME_EARLIEST_OF_DAY:
			//target date
			if(constraint == null) return null;
			else targetDate = (Date)constraint;
			//where clause
			whereClause = generateWhereClause(flag, targetDate);
			//get result
			rs = selectInSync(connection, new String[]{"min(registration_time)"}, table, whereClause);
			//deal with result
			if(rs != null){
				try {
					rs.next();
					result = rs.getObject(1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case TIME_LATEST_OF_DAY:
			//target date
			if(constraint == null) return null;
			else targetDate = (Date)constraint;
			//where clause
			whereClause = generateWhereClause(flag, targetDate);
			//get result
			rs = selectInSync(connection, new String[]{"max(reception_time)"}, table, whereClause);
			//deal with result
			if(rs != null){
				try {
					rs.next();
					result = rs.getObject(1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case RECORD_OF_DATE:
		case RECORD_OF_WEEK:
		case RECORD_OF_MONTH:
		case RECORD_OF_YEAR:
			//target date
			if(constraint == null) return null;
			else targetDate = (Date)constraint;
			//where clause
			whereClause = generateWhereClause(flag, targetDate);
			//get result
			rs = selectInSync(connection, null, table, whereClause);
			//deal with result
			result = extractLogsFromResultSet(rs);
			break;
		default:break;
		}
		
		//close connection
		closeConnection(connection);
		connection = null;
		
		return result;
	}
	
	/**
	 * 从结果集中提取log beans
	 * @param rs
	 * @return
	 */
	private static OutpatientLog[] extractLogsFromResultSet(ResultSet rs){
		if(rs == null) return null;
		int length = 0;
		try {
			rs.last();
			length = rs.getRow();
			rs.first();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return null;
		}
		if(length == 0) return null; 
		
		OutpatientLog[] list = new OutpatientLog[length];
		boolean isLast = false;
		int resultIndex = 0;
		while(true) {
			OutpatientLog ol = new OutpatientLog();
			for (int i = 0; i < OutpatientLog.KEYS.length; i++) {
				try {
					ol.set(i, rs.getObject(i + 1));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			list[resultIndex] = ol;
			
			try {
				isLast = rs.isLast();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			if(isLast) break;
			try {
				rs.next();
				resultIndex ++;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		return list;
	}
	
	private static final int WHERE_EXIST = 1;
	/**
	 * 生成where子句
	 * @param targetDate
	 * @return
	 */
	private static String generateWhereClause(int flag, Object obj){
		String clause = null;
		switch (flag) {
		case TIME_EARLIEST_OF_DAY:
		case TIME_LATEST_OF_DAY:
		case RECORD_OF_DATE:	//sql:(where) registration_time > "yyyy-MM-dd 00:00:00" and registration_time < "yyyy-MM-DD 00:00:00"
		case RECORD_OF_WEEK:
		case RECORD_OF_MONTH:
		case RECORD_OF_YEAR:
			if(obj == null) return null;

			Date targetDate = (Date)obj;
			Calendar cal = Calendar.getInstance();
			cal.setTime(targetDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			targetDate = cal.getTime();
			targetDate.setMinutes(0);
			targetDate.setSeconds(0);
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			clause = OutpatientLog.KEYS[OutpatientLog.INDEX_REGISTRATION] + " > \"" + format.format(targetDate) + "\" and ";
			
			cal.setTime(targetDate);
			cal.add(Calendar.DAY_OF_MONTH, getRange(flag));
			targetDate = cal.getTime();
			
			clause += OutpatientLog.KEYS[OutpatientLog.INDEX_REGISTRATION] + " < \"" + format.format(targetDate) + "\"";
			break;
		case WHERE_EXIST:
			if(obj == null) return null;
			
			OutpatientLog ol = (OutpatientLog)obj;
			String[] keys = ol.keySet();
			String value = null;
			clause = "";
			
			value = ol.get(OutpatientLog.INDEX_REGISTRATION);
			clause += (keys[OutpatientLog.INDEX_REGISTRATION] + "=");
			if(value == null) clause += "null";
			else clause += ("\"" + value + "\"");

			value = ol.get(OutpatientLog.INDEX_PATIENT);
			clause += (" and " + keys[OutpatientLog.INDEX_PATIENT] + "=");
			if(value == null) clause += "null";
			else clause += ("'" + value + "'");

			value = ol.get(OutpatientLog.INDEX_OUTPATIENT_NUM);
			clause += (" and " + keys[OutpatientLog.INDEX_OUTPATIENT_NUM] + "=");
			if(value == null) clause += "null";
			else clause += ("'" + value + "'");
		default:
			break;
		}
		return clause;
	}
	private static int getRange(int flag){
		switch (flag) {
		case TIME_EARLIEST_OF_DAY:
		case TIME_LATEST_OF_DAY:
		case RECORD_OF_DATE:
			return 1;
		case RECORD_OF_WEEK:
			return 7;
		case RECORD_OF_MONTH:
			return 30;
		case RECORD_OF_YEAR:
			return 365;
		default:
			return 0;
		}
	}
	
	/**
	 * 导入excel数据到数据库
	 * @param fileList
	 * @param listener
	 */
	public static void importFromExcel(File[] fileList, OutpatientLogDBOpeListener listener){
		if(fileList == null) return;
		
		OutpatientLog element = null;
		listener.onTransactionStart();
		
		if(connection == null)
			connection = openConnection();
		if(connection != null)
			ConsoleOutput.pop("DBOperator.executeInSync", "connection to database succeed");
		
		for(int i = 0 ; i < fileList.length ; i ++){
			
			if(fileList[i]==null || !fileList[i].exists()){
				ConsoleOutput.pop("OutpatientLogDBO.importFromExcel","filename error or file not exist when loading data");
				return;
			}
			
			//set workbook
			Workbook book = null;
			try {
				book = Workbook.getWorkbook(fileList[i]);
			} catch (BiffException e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//loadData
			System.out.println(StringSet.MENU_DATA + "\"" +
					fileList[i].getName() + "\"" + StringSet.MENUITEM_IMPORT + 
					", " + StringSet.DEBUG_SAMPLE + ":");
			Sheet sheet = book.getSheet(0);
			listener.onProgressUpdate("startProgress", 0, 0);
			int rows = sheet.getRows();
			int step = 32;
			for(int j = 1 ; j < rows ; j ++){
				element = new OutpatientLog();
				//index属性自动添加,k从第二个属性开始记录
				for(int k = 1 ; k < 18 ; k ++){
					Cell cell = sheet.getCell(k,j);
					element.generateValueFromExcelCell(k, cell);
				}
				insertLog(element);
				if(j % step == 0){
					listener.onProgressUpdate(
							"文件" + (i + 1) + "/" + fileList.length + ":",
							(float)j/rows, rows);
				}
			}
			listener.onProgressUpdate(
					"文件" + (i + 1) + "/" + fileList.length + ":",
					(float)1, rows);
		}
		closeConnection(connection);
		connection = null;
		listener.onTransactionCompletion(null);
	}
	
	/**
	 * 在外部定义openConnection,closeConnection
	 * @param values
	 * @param listener
	 */
	private static void insertLog(OutpatientLog values){
		String statement = "insert into " + table + "(";
		
		if(values == null){
			ConsoleOutput.pop(OutpatientLogDBO.class.toString(), 
					"insert failed cause no data array input");
			return;
		}
		if(values.get(11) == null || values.get(12) == null){
			ConsoleOutput.pop(OutpatientLogDBO.class.toString(), 
					"insert failed caused by invalid data input");
			return;
		}
		//check for existance
		String whereClause = generateWhereClause(WHERE_EXIST, values);
		if(whereClause != null){
			ConsoleOutput.suspendDebug();
			ResultSet rs = selectInSync(connection, new String[]{"count(*)"}, table, whereClause);
			ConsoleOutput.reopenDebug();
			try {
				rs.first();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Integer exist = rs.getInt(1);
				if(exist > 0){
					ConsoleOutput.pop("OutpatientLogDBO.insertLog", "重复导入");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//iterator and keys
		Object[] keys = null;
		keys = values.keySet();
		if(keys == null || keys.length <= 0){
			ConsoleOutput.pop(OutpatientLogDBO.class.toString(), 
					"insert failed cause no key name input");
		} 
		
		String valueStr = "";
		//generate statement
		for (int i = 0; i < keys.length; i++) {
			String value = values.get(i);
			if(value == null || value.equals(""))
				continue;
			statement += keys[i];
			valueStr += "'" + value + "'";
			if(i < keys.length - 1){
				statement += ",";
				valueStr += ",";
			}
		}  
		statement += ")";
		valueStr = (" values(" + valueStr + ")");
		statement += valueStr;
		
		if(connection == null)
			connection = openConnection();
		executeInSync(statement, connection);
	}

	/**
	 * 获得元组个数，在listener的oncompletion方法中获得
	 * @param listener
	 */
	public static void getCount(OutpatientLogDBOpeListener listener){
		selectInAsync(new String[]{"count(*)"}, table, null, listener);
	}
	
	public static int getCount(Connection connection, String whereClause){
		ResultSet rs = selectInSync(connection, new String[]{"count(*)"}, table, whereClause);
		try {
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void truncate(DBOpeListener listener){
		truncateTable(table, listener);
	}
}
