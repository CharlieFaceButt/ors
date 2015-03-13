package com.lsc.ors.views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.lsc.ors.applications.WRDModelListener;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.db.listener.OutpatientLogDBOpeListener;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.util.ExcelFileFilter;
import com.lsc.ors.src.StringSet;

/**
 * view of main program
 * @author charlieliu
 *
 */
public class PlatformController {
	
	private static final int INFO_LAST_LINE = 0;

	//data
	/**
	 * data package model for visualization
	 */
	WaitingRecordViewer WRDModel = null;
	int waitingRecordCount = 0;
	
	
	//view
	Frame frame = new Frame(StringSet.PLATFORM_TITLE);
	Panel funcPanel = new Panel();
	
	//for menu
	Menu file = new Menu(StringSet.MENU_FILE);
	MenuItem menuItemImport = new MenuItem(StringSet.MENUITEM_IMPORT);
	MenuItem menuItemExport = new MenuItem(StringSet.MENUITEM_EXPORT);
	Menu func = new Menu(StringSet.MENU_FUNC);
	MenuItem funcV = new MenuItem(StringSet.VISUALIZE);
	MenuItem funcQ = new MenuItem(StringSet.ANALYZE);
	MenuItem funcS = new MenuItem(StringSet.RECOMMEND);
	
	//for function
	Button bt_WRD = new Button(StringSet.VSL_WAITING_RECORD_DISTRIBUTION);
	Button bt_WRQS = new Button(StringSet.VSL_WAITING_RECORD_QUEUE_STATUS);
	Button bt_PD = new Button(StringSet.VSL_POPULATION_DISTRIBUTION);
	Button bt_WTD = new Button(StringSet.VSL_WAITING_TIME_DISTRIBUTION);
	
	//listener
	MultipleActionListener mal = new MultipleActionListener();
	
	//info
	int maxLineAcount = 100;
	String processInfo = StringSet.INFO_START;
	TextArea processInfoArea = new TextArea(processInfo);
	
	public PlatformController(){
		//panel
		funcPanel.add(new Label(StringSet.VACANT_CONTENT));
		funcPanel.setBackground(Color.GRAY);
		
		//menu
		file.add(menuItemImport);file.add(menuItemExport);
		func.add(funcV);func.add(funcQ);func.add(funcS);
		MenuBar mb = new MenuBar();
		mb.add(file);mb.add(func);
		menuItemImport.addActionListener(mal);
		menuItemExport.addActionListener(mal);
		funcV.addActionListener(mal);
		funcQ.addActionListener(mal);
		funcS.addActionListener(mal);
		
		//frame
		frame.setBounds(50, 50, 540, 300);
		frame.setMenuBar(mb);
		frame.setLayout(new BorderLayout(5, 5));
		frame.add(funcPanel,BorderLayout.WEST);
		frame.add(processInfoArea,BorderLayout.CENTER);
		frame.addWindowListener(mal);
		
		initData();
	}
	
	private void initData(){
		OutpatientLogDBO.getCount(new OutpatientLogDBOpeListener() {
			@Override
			public void onTransactionStart() {
				// TODO Auto-generated method stub
				addInfo("链接数据库");
			}
			
			@Override
			public void onTransactionCompletion(ResultSet rs) {
				// TODO Auto-generated method stub
				if(rs == null){
					addInfo("数据库链接错误");
					return;
				}
				int count;
				try {
					rs.first();
					waitingRecordCount = rs.getInt(1);
					addInfo("数据库链接成功:\ntable\t\tcount(*)\n" +
							"outpatient_log\t" + waitingRecordCount + "");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					addInfo("数据库数据获取失败");
				}
			}
			
			@Override
			public void onProgressUpdate(String progressStr, float progress, int max) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void show(){
		frame.setVisible(true);
	}
	

	/**
	 * 添加显示信息
	 * @param info
	 */
	public void addInfo(String info){
		if(info==null) return;
		processInfo = processInfoArea.getText() + "\n"+ info;
		String[] list = processInfo.split("\n");
		if(list.length > maxLineAcount){
			processInfo = "";
			for(int i=(list.length - maxLineAcount); i<list.length ; i++){
				processInfo += (list[i]+"\n");
			}
		}
		processInfoArea.setText(processInfo);
//		System.out.println(rowsOfInfo);
	}
	
	/**
	 * 更改显示信息
	 * @param lineFlag
	 * @param oldInfo
	 * @param newInfo
	 */
	public void modifyInfo(int lineFlag, String oldInfo, String newInfo){
		if(newInfo==null) return;
		if(lineFlag == INFO_LAST_LINE){
			String[] list = processInfo.split("\n");
			processInfo = "";
			for(int i = 0; i < list.length - 1 ; i++){
				processInfo += (list[i]+"\n");
			}
			processInfo += newInfo;
		}
		processInfoArea.setText(processInfo);
	}
	
	class MultipleActionListener implements WindowListener, ActionListener{

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			ConsoleOutput.pop("PlatformController", StringSet.CONTROL_CLOSED);
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
//			System.out.println(e.getActionCommand());
			addInfo(e.getActionCommand());
			Integer msg = StringSet.getInstance().getCommandIndex(e.getActionCommand());
			switch(msg){
			case StringSet.CMD_VISUALIZE:
				funcPanel.removeAll();
				GridLayout fl = new GridLayout(5, 1);
				funcPanel.setLayout(fl);
				funcPanel.add(bt_WRD, 0);bt_WRD.addActionListener(mal);
				funcPanel.add(bt_WRQS, 1);bt_WRQS.addActionListener(mal);
				funcPanel.add(bt_PD, 2);bt_PD.addActionListener(mal);
				funcPanel.add(bt_WTD, 3);bt_WTD.addActionListener(mal);
				frame.resize(600, 300);
				break;
			case StringSet.CMD_ANALYZE:
				break;
			case StringSet.CMD_RECOMMEND:
				break;
			case StringSet.CMD_VSL_WAITING_RECORD_DISTRIBUTION:
				if(waitingRecordCount <= 0){
					addInfo("木有数据，无法生成视图");
					JOptionPane.showMessageDialog(null, "没有数据，请通过\"文件\"-->\"导入数据\"来加载数据");
					break;
				}
				if(WRDModel == null){

					WRDModel = new WaitingRecordViewer(new WRDModelListener() {
						@Override
						public void onViewDestroy() {
							// TODO Auto-generated method stub
							addInfo("\"等待时间分布视图\"停止");
							WRDModel = null;
						}
						@Override
						public void onViewCreate() {
							// TODO Auto-generated method stub
							addInfo("\"等待时间分布视图\"打开");
						}
					});
				}
				else{
					addInfo("WRDModel后台已经运行");
				}
				WRDModel.show();
				break;
			case StringSet.CMD_VSL_WAITING_RECORD_QUEUE_STATUS:
				if(waitingRecordCount <= 0){
					addInfo("木有数据，无法生成视图");
					JOptionPane.showMessageDialog(null, "没有数据，请通过\"文件\"-->\"导入数据\"来加载数据");
					break;
				}
				break;
			case StringSet.CMD_VSL_POPULATION_DISTRIBUTION:
				if(waitingRecordCount <= 0){
					addInfo("木有数据，无法生成视图");
					JOptionPane.showMessageDialog(null, "没有数据，请通过\"文件\"-->\"导入数据\"来加载数据");
					break;
				}
				break;
			case StringSet.CMD_VSL_WAITING_TIME_DISTRIBUTION:
				if(waitingRecordCount <= 0){
					addInfo("木有数据，无法生成视图");
					JOptionPane.showMessageDialog(null, "没有数据，请通过\"文件\"-->\"导入数据\"来加载数据");
					break;
				}
				break;
			case StringSet.CMD_IMPORT:
				popImportDialog();
				break;
			case StringSet.CMD_EXPORT:
				break;
			default:break;
			}
			
		}
	}
	
	/**
	 * 弹出导入数据对话框，以及对话框响应
	 */
	private void popImportDialog(){
		//chooser
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(StringSet.MENU_FILE + StringSet.MENUITEM_IMPORT);
		chooser.setMultiSelectionEnabled(true);
		
		//filter
		ExcelFileFilter eff = new ExcelFileFilter();
		chooser.setCurrentDirectory(eff.getDefaultDictionary());
		chooser.setFileFilter(eff);
		
		//response
		int r = chooser.showOpenDialog(frame);
		if(r == JFileChooser.APPROVE_OPTION){
			// TODO file list to import
			File[] fileList = chooser.getSelectedFiles();
			addInfo("" + fileList.length + StringSet.MENU_FILE + ":");
			for(int i=0 ; i<fileList.length ; i++){
				addInfo("\t" + fileList[i].getName());
			}
			eff.setChooserPathInConfig(fileList[0].getPath());
//			VSLmodel = new OutpatientLogModel(fileList);//model 不开启view
			OutpatientLogDBO.importFromExcel(fileList, new OutpatientLogDBOpeListener() {
				@Override
				public void onTransactionStart() {
					// TODO Auto-generated method stub
					addInfo("数据库开始导入excel文件");
				}
				@Override
				public void onTransactionCompletion(ResultSet rs) {
					// TODO Auto-generated method stub
					addInfo("数据库导入文件结束，导入结束:");
					if(rs == null) return;
					try {
						for (int i = 0; i < rs.getRow(); i++) {
							addInfo(rs.getString(0));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void onProgressUpdate(String progressStr, float progress, int max) {
					// TODO Auto-generated method stub
					DecimalFormat df = new DecimalFormat("#0.0");
					if(progressStr != null){
						if(progressStr.equals("startProgress"))
							addInfo(progressStr);
						else
							modifyInfo(INFO_LAST_LINE, null, progressStr + 
									"进度：" + df.format(progress * 100) + "%");
					}
					else
						modifyInfo(INFO_LAST_LINE, null, progressStr + 
								"进度"+ df.format(progress * 100) + "%");
				}
			});
		}
	}
	
	public static void main(String[] args) {
		new PlatformController().show();
	}
}
