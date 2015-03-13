package com.lsc.ors.applications;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public abstract class ModelObject extends JFrame{
	
	/**
	 * version id
	 */
	private static final long serialVersionUID = -2732432365752898831L;

	protected ModelListener modelListener = null;

	public void setModelListener(ModelListener listener){
		modelListener = listener;
	}
	
	public ModelObject(ModelListener listener) {
		super();
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
	
	
}
