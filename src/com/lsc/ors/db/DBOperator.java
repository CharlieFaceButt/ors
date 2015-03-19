package com.lsc.ors.db;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lsc.ors.beans.BeanObject;
import com.lsc.ors.debug.ConsoleOutput;

/**
 * ���ݿ��������
 * @author charlieliu
 *
 */
public class DBOperator{

	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/ors";
	private static String user = "root";
	private static String password = "dbjdsblj";
	
	/**
	 * ����select��ѯ���
	 * @param selectItems
	 * @param fromTable
	 * @param whereClause
	 * @return null tableֵΪ��
	 */
	private static String generateSelectSql(String[] selectItems, String fromTable, String whereClause){
		String sql = "select ";
		if(selectItems == null || selectItems.length == 0){
			sql += "*";
		}
		else{
			for (int i = 0; i < selectItems.length; i++) {
				sql += selectItems[i];
				if(i < selectItems.length - 1){
					sql += ",";
				}
			}
		}
		
		if(fromTable == null){
			ConsoleOutput.pop("DBOperator.generateSelectSql", "fromTable should not be null");
			return null;
		}
		sql += (" from " + fromTable);
		
		if(whereClause != null){
			sql += (" where " + whereClause);
		}
		
		return sql;
	}
	/**
	 * ����ȫ���ݿ��ѯ����,ͬ��,��ҪopenConnection,closeConnection
	 * @param connection
	 * @param selectClause ���Ϊ����Ĭ��select *
	 * @param fromTable
	 * @param whereClause
	 * @return
	 */
	protected static ResultSet selectInSync(Connection connection, String[] selectItems, String fromTable, String whereClause){
		
		String sql = generateSelectSql(selectItems, fromTable, whereClause);
		ConsoleOutput.pop("DBOperator.select", "<sql>" + sql + "</sql>");
		
		if(connection == null){
			ConsoleOutput.pop("DBOperator.insert", "�������ݿ�ʧ��");
			return null;
		}
		ResultSet rs =  executeInSync(sql, connection);
		return rs;
	}
	
	/**
	 * ���ݿ��ѯ����,�첽
	 * @param selectClause ���Ϊ����Ĭ��select *
	 * @param fromTable
	 * @param whereClause
	 * @param listener
	 * @return
	 */
	protected static void selectInAsync(
			final String[] selectItems, 
			final String fromTable, 
			final String whereClause, 
			final DBOpeListener listener){
		
		String sql = generateSelectSql(selectItems, fromTable, whereClause);
		ConsoleOutput.pop("DBOperator.select", "<sql>" + sql + "</sql>");
		executeInAsync(sql, listener);
		
	}
	
	/**
	 * ����update���
	 * @param table
	 * @param values
	 * @param whereClause
	 * @return
	 */
	private static String generateUpdateSql(String table, BeanObject values, String whereClause){
		if(table == null || values == null || whereClause == null){
			ConsoleOutput.pop("DBOperator.generateUpdateSql", "sql error");
			return null;
		}
		
		String sql = "update " + table + " set ";
		String[] keys = values.keySet();
		for (int i = 0; i < keys.length; i++) {
			String value = values.get(i);
			if(value == null || value.equals(""))
				continue;
			sql += keys[i] + " = '" + value + "'"; 
			if(i < keys.length)
				sql += ",";
		}
		sql += (" where " + whereClause);
		return sql;
	}
	/**
	 * ���ݿ���²���,�첽
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param listener
	 */
	protected static void updateInAsync(String table, BeanObject values, String whereClause, DBOpeListener listener){
		String sql = generateUpdateSql(table, values, whereClause);
		ConsoleOutput.pop("DBOperator.updateInAsync", "<sql>" + sql + "</sql>");
		executeInAsync(sql, listener);
	}
	
	/**
	 * ����delete���
	 * @param fromTable
	 * @param whereClause
	 * @return
	 */
	private static String generateDeleteSql(String fromTable, String whereClause){
		if(fromTable == null || whereClause == null){
			ConsoleOutput.pop("DBOperator.generateDeleteSql", "sql error");
			return null;
		}
		String sql = ("delete from " + fromTable);
		sql += (" where " + whereClause);
		return sql;
	}
	/**
	 * ���ݿ�ɾ������,�첽
	 * @param fromTable
	 * @param whereClause
	 * @param listener
	 */
	protected static void deleteInAsync(String fromTable, String whereClause, DBOpeListener listener){
		String sql = generateDeleteSql(fromTable, whereClause);
		ConsoleOutput.pop("DBOperator.deleteInAsync", "<sql>" + sql + "</sql>");
		executeInAsync(sql, listener);
	}
	
	/**
	 * ����insert���
	 * @param table
	 * @param values
	 * @return
	 */
	private static String generateInsertSql(String table, BeanObject values){
		String sql = "insert into " + table + "(";
		
		if(values == null){
			ConsoleOutput.pop(DBOperator.class.toString(), 
					"insert failed cause no data array input");
			return null;
		}
		
		//iterator and keys
		String[] keys = null;
		keys = values.keySet();
		if(keys == null || keys.length <= 0){
			ConsoleOutput.pop(DBOperator.class.toString(), 
					"insert failed cause no key name input");
		} 
		
		String valueStr = " values(";
		//generate statement
		for (int i = 0; i < keys.length; i++) {
			String value = values.get(i);
			if(value == null || values.equals(""))
				continue;
			sql += keys[i];
			valueStr += "'" + value + "'";
			if(i < keys.length - 1){
				sql += ",";
				valueStr += ",";
			}
		}
		sql += ")";
		valueStr += ")";
		sql += valueStr;
		return sql;
	}
	/**
	 * ���ݿ�������ݲ������첽
	 * @param table
	 * @param values
	 * @param listener
	 */
	protected static void insertInASync(String table, BeanObject values, DBOpeListener listener){
		String sql = generateInsertSql(table, values);
		ConsoleOutput.pop("DBOperator.insertInSync", "<sql>" + sql + "</sql>");
		executeInAsync(sql, listener);
	}
	
	/**
	 * ����ȫ���ݿ���������ͬ������ҪopenConnection,closeConnection
	 * @param connection
	 * @param table
	 * @param values
	 * @return
	 */
	protected static ResultSet insertInSync(Connection connection, String table, BeanObject values){
		if(connection == null){
			ConsoleOutput.pop("DBOperator.insert", "�������ݿ�ʧ��");
			return null;
		}
		String sql = generateInsertSql(table, values);
		ConsoleOutput.pop("DBOperator.insertInSync", "<sql>" + sql + "</sql>");
		ResultSet rs = executeInSync(sql, connection);
		return rs;
	}
	
	/**
	 * �첽ִ��һ��sql���,����������
	 * @param sql
	 */
	protected static void executeInAsync(final String sql, final DBOpeListener listener){
		if(sql == null){
			ConsoleOutput.pop("DBOperator.execute", "sql error");
			return;
		}
		
		//use thread to asynchronously execute SQL
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				//open connection to database
				Connection connection = null;
				int connectionCount = 0;
				while(true){
					connection = openConnection();
					connectionCount++;
					if(connection == null){
						ConsoleOutput.pop("DBOperator.execute", "connection open failed " 
								+ connectionCount + "times , retry");
						if(connectionCount < 3){
							try {
								wait(200);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else return;
					}
					else break;
				}
				
				//callback
				if(listener != null) listener.onTransactionStart();
				
				ConsoleOutput.pop("DBOperator.executeInAsync", "<sql>" + sql + "</sql>");
				//execute
				ResultSet rs = executeInSync(sql, connection);
				
				//callback
				if(listener != null) listener.onTransactionCompletion(rs);
				
				//close connection
				closeConnection(connection);
			}
		}).start();
	}	
	
	/**
	 * ����ȫsql���ִ�У�������������ҪopenConnection��closeConnection
	 * @param sql
	 * @param connection
	 * @param listener
	 */
	protected static ResultSet executeInSync(String sql, Connection connection){
		if(sql == null){
			ConsoleOutput.pop("DBOperator.executeInSync", "sql error");
			return null;
		}
		
		try {
			if(connection == null || connection.isClosed()){
				ConsoleOutput.pop("DBPOperator.executeInSync", "connection to database failed");
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//statement
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//execute 
		ResultSet rs = null;
		String exeSql = null;
		try {
			exeSql = new String(sql.getBytes(), "gb2312"); 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(exeSql != null){
			if(exeSql.contains("select"))
				try {
					rs = statement.executeQuery(exeSql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				try {
					statement.execute(exeSql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else
			ConsoleOutput.pop("DBOperator.executeInSync", "characterEncoding error");
		return rs;
	}
	
	/**
	 * ���������ݿ⽨��һ�������Խ���������
	 * @return null �������ʧ��
	 */
	protected static Connection openConnection(){
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		ConsoleOutput.pop("DBOperator.openConnection", "connect to database " + url + ":" + user);
		try {
			return DriverManager.getConnection(url + "?user=" + user + "&password=" + password + "&characterEncoding=gb2312");
//			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ������֮��ر�����
	 * @param connection
	 */
	protected static void closeConnection(Connection connection){
		if(connection == null) return;
		
		//close connection
		try {
			if(!connection.isClosed()){	//�쳣�׳�λ��
				connection.close();		//�쳣�׳�λ��2
				ConsoleOutput.pop("DBOperator.closeConnection", "connection closed");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ConsoleOutput.pop("DBOperator.closeConnection", "close connection to database failed");
		}
	}
	
	protected static void truncateTable(String table, DBOpeListener listener){
		executeInAsync("truncate " + table, listener);
	}
	
}
