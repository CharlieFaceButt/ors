package com.lsc.ors.applications;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

import javax.swing.JFrame;

import com.lsc.ors.applications.listener.ModelListener;
import com.lsc.ors.db.dbo.OutpatientLogDBO;

/**
 * ����������࣬��ɹ��ܲ����Ͷ�Ӧ��ʾ
 * @author charlieliu
 *
 */
public abstract class ModelObject extends JFrame{
	
	/**
	 * version id
	 */
	private static final long serialVersionUID = -2732432365752898831L;
	
	//data
	protected Date firstDate = null;
	protected Date lastDate = null;

	protected ModelListener modelListener = null;

	public void setModelListener(ModelListener listener){
		modelListener = listener;
	}
	
	public ModelObject(ModelListener listener) {
		super();
		initDate();
		modelListener = listener;
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				if(modelListener != null) modelListener.onViewCreate();
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
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(modelListener != null) modelListener.onViewDestroy();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * ��ʼ������
	 */
	private void initDate(){
		getLastDate();
		getFirstDate();
	}

	
	/**
	 * ��ȡ����¼����
	 * @return
	 */
	private Date getLastDate() {
		// TODO Auto-generated method stub
		if(lastDate == null){
			Object obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_LAST_DATE, null);
			if(obj != null) lastDate = (Date)obj;
		}
		return lastDate;
	}

	/**
	 * ��ȡ�����¼����
	 * @return
	 */
	private Date getFirstDate() {
		// TODO Auto-generated method stub
		if(firstDate == null){
			Object obj = OutpatientLogDBO.getData(OutpatientLogDBO.TIME_FIRST_DATE, null);
			if(obj != firstDate) firstDate = (Date)obj;
		}
		return firstDate;
	}
}
