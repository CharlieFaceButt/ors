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
import com.lsc.ors.db.DBOperator;
import com.lsc.ors.db.listener.OutpatientLogDBOpeListener;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.src.StringSet;

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
	public static final int RECORD_OF_DATE = 2;
	
	
	
	/**
	 * 根据flag和constraint获得数据
	 * @param flag
	 * <br> - TIME_FIRST_DATE 获得数据第一天的日期
	 * <br> - TIME_LAST_DATE 获得数据最后一天的日期
	 * <br> - RECORD_OF_DATE 获得一个日期的记录
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
		case RECORD_OF_DATE:
			//target date
			Date targetDate = null;
			if(constraint == null) return null;
			else targetDate = (Date)constraint;
			
			//where clause
			String whereClause = generateWhereClause(WHERE_DATE, targetDate);
			
			//get result
			rs = selectInSync(connection, null, table, whereClause);
			
			//deal with result
			OutpatientLog[] list = null;
			list = extractLogsFromResultSet(rs);
			result = list;
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
	
	private static final int WHERE_DATE = 0;
	/**
	 * 生成where子句
	 * @param targetDate
	 * @return
	 */
	private static String generateWhereClause(int flag, Object obj){
		String clause = null;
		switch (flag) {
		case WHERE_DATE:	//sql:(where) registration_time > "yyyy-MM-dd 00:00:00" and registration_time < "yyyy-MM-DD 00:00:00"
			if(obj == null) return null;

			Date targetDate = (Date)obj;
			Calendar cal = Calendar.getInstance();
			cal.setTime(targetDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			targetDate = cal.getTime();
			targetDate.setMinutes(0);
			targetDate.setSeconds(0);
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			clause = OutpatientLog.KEYS[OutpatientLog.INDEX_ARRIVAL] + " > \"" + format.format(targetDate) + "\" and ";
			
			cal.setTime(targetDate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			targetDate = cal.getTime();
			
			clause += OutpatientLog.KEYS[OutpatientLog.INDEX_ARRIVAL] + " < \"" + format.format(targetDate) + "\"";
			break;
		default:
			break;
		}
		return clause;
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
			System.out.println(StringSet.MENU_FILE + "\"" +
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
		
		//iterator and keys
		Object[] keys = null;
		keys = values.keySet();
		if(keys == null || keys.length <= 0){
			ConsoleOutput.pop(OutpatientLogDBO.class.toString(), 
					"insert failed cause no key name input");
		} 
		
		String valueStr = " values(";
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
		valueStr += ")";
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
}
