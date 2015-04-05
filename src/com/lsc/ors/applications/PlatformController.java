package com.lsc.ors.applications;

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
import javax.swing.JTextArea;


import com.lsc.ors.applications.analysis.AttributeDescriptionViewer;
import com.lsc.ors.applications.analysis.DoubleAttributeDescriptionViewer;
import com.lsc.ors.applications.analysis.WaitingTimeAnalyzer;
import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.applications.listener.QSModelListener;
import com.lsc.ors.applications.listener.WRDModelListener;
import com.lsc.ors.applications.visualization.PopulationDistributionViewer;
import com.lsc.ors.applications.visualization.QueueStatusViewer;
import com.lsc.ors.applications.visualization.WaitingRecordViewer;
import com.lsc.ors.applications.visualization.WaitingTimeDistributionViewer;
import com.lsc.ors.db.DBOpeListener;
import com.lsc.ors.db.dbo.OutpatientLogDBO;
import com.lsc.ors.db.listener.OutpatientLogDBOpeListener;
import com.lsc.ors.debug.ConsoleOutput;
import com.lsc.ors.resource.StringSet;
import com.lsc.ors.util.ExcelFileFilter;

/**
 * view of main program
 * @author charlieliu
 *
 */
public class PlatformController {
	
	private static final int INFO_LAST_LINE = 0;

	//data
	/**
	 * waiting record distribution model for visualization
	 */
	WaitingRecordViewer WRDModel = null;
	/**
	 * waiting record queue status model for visualization
	 */
	QueueStatusViewer QSModel = null;
	/**
	 * population distribution model for visualization
	 */
	PopulationDistributionViewer PDModel = null;
	/**
	 * waiting time distribution model for visualization
	 */
	WaitingTimeDistributionViewer WTDModel = null;
	
	ModelObject model = null;
	
	int waitingRecordCount = 0;
	
	
	//view
	Frame frame = new Frame(StringSet.PLATFORM_TITLE);
	Panel funcPanel = new Panel();
	
	//for menu
	Menu file = new Menu(StringSet.MENU_DATA);
	MenuItem menuItemImport = new MenuItem(StringSet.MENUITEM_IMPORT);
	MenuItem menuItemExport = new MenuItem(StringSet.MENUITEM_EXPORT);
	MenuItem menuItemTruncate = new MenuItem(StringSet.MENUITEM_TRUNCATE);
	Menu func = new Menu(StringSet.MENU_FUNC);
	MenuItem funcV = new MenuItem(StringSet.VISUALIZE);
	MenuItem funcQ = new MenuItem(StringSet.ANALYZE);
	MenuItem funcS = new MenuItem(StringSet.RECOMMEND);
	
	//for function
	Button bt_WRD = new Button(StringSet.VSL_WAITING_RECORD_DISTRIBUTION);
	Button bt_WRQS = new Button(StringSet.VSL_WAITING_RECORD_QUEUE_STATUS);
	Button bt_PD = new Button(StringSet.VSL_POPULATION_DISTRIBUTION);
	Button bt_WTD = new Button(StringSet.VSL_WAITING_TIME_DISTRIBUTION);
	Button bt_ANL_AD = new Button(StringSet.ANL_ATTRIBUTE_DESCRIPTION);
	Button bt_ANL_DAD = new Button(StringSet.ANL_DOUBLE_ATTRIBUTES_DESCRIPTION);
	Button bt_ANL_WT = new Button(StringSet.ANL_WAITING_TIME);
	
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
		file.add(menuItemImport);file.add(menuItemExport);file.add(menuItemTruncate);
		func.add(funcV);func.add(funcQ);func.add(funcS);
		MenuBar mb = new MenuBar();
		mb.add(file);mb.add(func);
		menuItemImport.addActionListener(mal);
		menuItemExport.addActionListener(mal);
		menuItemTruncate.addActionListener(mal);
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
		
		informCountToUser();
	}
	
	private void informCountToUser(){
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
		processInfoArea.setCaretPosition(processInfo.length());
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
		processInfoArea.setCaretPosition(processInfo.length());
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
			if(waitingRecordCount <= 0){
				addInfo("木有数据，无法生成视图");
				JOptionPane.showMessageDialog(null, "没有数据，请通过\"文件\"-->\"导入数据\"来加载数据");
				return;
			}
			
			addInfo(e.getActionCommand());
			Integer msg = StringSet.getInstance().getCommandIndex(e.getActionCommand());
			switch(msg){
			case StringSet.CMD_VISUALIZE:
				funcPanel.removeAll();
				GridLayout gl = new GridLayout(5, 1);
				funcPanel.setLayout(gl);
				funcPanel.add(bt_WRD, 0);bt_WRD.addActionListener(mal);
				funcPanel.add(bt_WRQS, 1);bt_WRQS.addActionListener(mal);
				funcPanel.add(bt_PD, 2);bt_PD.addActionListener(mal);
				funcPanel.add(bt_WTD, 3);bt_WTD.addActionListener(mal);
				frame.resize(600, 300);
				break;
			case StringSet.CMD_ANALYZE:
				funcPanel.removeAll();
				GridLayout gl2 = new GridLayout(5, 1);
				funcPanel.setLayout(gl2);
				funcPanel.add(bt_ANL_AD, 0);bt_ANL_AD.addActionListener(mal);
				funcPanel.add(bt_ANL_DAD, 1);bt_ANL_DAD.addActionListener(mal);
				funcPanel.add(bt_ANL_WT, 2);bt_ANL_WT.addActionListener(mal);
				frame.resize(600, 301);
				break;
			case StringSet.CMD_RECOMMEND:
				break;
			//打开等待时间分布图
			case StringSet.CMD_VSL_WAITING_RECORD_DISTRIBUTION:
				if(WRDModel == null){
					WRDModel = new WaitingRecordViewer(new WRDModelListener() {
						@Override
						public void onViewDestroy() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_WAITING_RECORD_DISTRIBUTION + "\"停止");
							WRDModel = null;
						}
						@Override
						public void onViewCreate() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_WAITING_RECORD_DISTRIBUTION + "\"打开");
						}
					});
				}
				else{
					addInfo("" + StringSet.VSL_WAITING_RECORD_DISTRIBUTION + "后台已经运行");
				}
				WRDModel.show();
				break;
			//打开等待排队状态图
			case StringSet.CMD_VSL_WAITING_RECORD_QUEUE_STATUS:
				if(QSModel == null){
					QSModel = new QueueStatusViewer(new QSModelListener() {
						@Override
						public void onViewDestroy() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_WAITING_RECORD_QUEUE_STATUS + "\"停止");
							QSModel = null;
						}
						@Override
						public void onViewCreate() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_WAITING_RECORD_QUEUE_STATUS + "\"打开");
						}
					});
				}
				else{
					addInfo("" + StringSet.VSL_WAITING_RECORD_QUEUE_STATUS + "后台已经运行");
				}
				QSModel.show();
				break;
			//打开等待人数分布图
			case StringSet.CMD_VSL_POPULATION_DISTRIBUTION:
				if(PDModel == null){
					PDModel = new PopulationDistributionViewer(new ModelListener() {
						@Override
						public void onViewDestroy() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_POPULATION_DISTRIBUTION + "\"停止");
							PDModel = null;
						}
						@Override
						public void onViewCreate() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_POPULATION_DISTRIBUTION + "\"打开");
						}
					});
				}
				else{
					addInfo("" + StringSet.VSL_POPULATION_DISTRIBUTION + "后台已经运行");
				}
				PDModel.show();
				break;
			//打开等待时间分布图
			case StringSet.CMD_VSL_WAITING_TIME_DISTRIBUTION:
				if(WTDModel == null){
					WTDModel = new WaitingTimeDistributionViewer(new ModelListener() {
						@Override
						public void onViewDestroy() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_POPULATION_DISTRIBUTION + "\"停止");
							WTDModel = null;
						}
						@Override
						public void onViewCreate() {
							// TODO Auto-generated method stub
							addInfo("\"" + StringSet.VSL_POPULATION_DISTRIBUTION + "\"打开");
						}
					});
				}
				else{
					addInfo("" + StringSet.VSL_POPULATION_DISTRIBUTION + "后台已经运行");
				}
				WTDModel.show();
				break;
			case StringSet.CMD_ANL_ATTRIBUTE_DESCRIPTION:
				new AttributeDescriptionViewer(getModelListener(StringSet.ANL_ATTRIBUTE_DESCRIPTION)).show();
				break;
			case StringSet.CMD_ANL_DOUBLE_ATTRIBUTES_DESCRIPTION:
				new DoubleAttributeDescriptionViewer(getModelListener(StringSet.ANL_DOUBLE_ATTRIBUTES_DESCRIPTION)).show();
				break;
			case StringSet.CMD_ANL_WAITING_TIME:
				new WaitingTimeAnalyzer(getModelListener(StringSet.ANL_WAITING_TIME)).show();
				break;
			case StringSet.CMD_IMPORT:
				popImportDialog();
				break;
			case StringSet.CMD_EXPORT:
				break;
			case StringSet.CMD_TRUNCATE:
				popTruncateDialog();
				break;
			default:break;
			}
			
		}
	}
	
	private ModelListener getModelListener(final String label){
		return new ModelListener() {
			@Override
			public void onViewDestroy() {
				// TODO Auto-generated method stub
				addInfo("\"" + label + "\"停止");
			}
			@Override
			public void onViewCreate() {
				// TODO Auto-generated method stub
				addInfo("\"" + label + "\"打开");
			}
		};
	}
	
	/**
	 * 弹出清空数据库对话框，以及对应响应
	 */
	private void popTruncateDialog(){
		int r = JOptionPane.showOptionDialog(null, "即将删除所有数据库数据", "警告", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"确定", "取消"}, JOptionPane.CANCEL_OPTION);
		if(r == JOptionPane.OK_OPTION){
			OutpatientLogDBO.truncate(new DBOpeListener() {
				@Override
				public void onTransactionStart() {
					// TODO Auto-generated method stub
					addInfo("正在处理");
				}
				
				@Override
				public void onTransactionCompletion(ResultSet rs) {
					// TODO Auto-generated method stub
					addInfo("完成");
					informCountToUser();
				}
			});
		}
		else {
			addInfo("用户取消操作");
		}
	}
	
	/**
	 * 弹出导入数据对话框，以及对话框响应
	 */
	private void popImportDialog(){
		//chooser
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(StringSet.MENU_DATA + StringSet.MENUITEM_IMPORT);
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
			addInfo("" + fileList.length + StringSet.MENU_DATA + ":");
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
					addInfo("数据库导入文件结束，导入结束");
					informCountToUser();
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
