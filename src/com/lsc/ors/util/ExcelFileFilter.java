package com.lsc.ors.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

public class ExcelFileFilter extends FileFilter{

	private String extension = "xls";
	private String extension_new = "xlsx";
	
	public static final String CONFIG = "config.txt"; 
	
	private String lastFileChooser = null;
	
	public ExcelFileFilter() {
		super();
		File file = getDefaultDictionary();
		if(file != null) lastFileChooser = file.getPath();
	}
	
	/**
	 * 获得config文件中存储的上次文件打开路径
	 * @return
	 */
	public File getDefaultDictionary(){
		File file = new File(CONFIG);
		File config = null;
		if(file.exists()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				config = new File(br.readLine());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else config = FileSystemView.getFileSystemView().getHomeDirectory();
		///debug
		System.out.println("default directory:" + config.getPath());
		return config;
	}

	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		if (f != null) {
			if (f.isDirectory()) return true;
			String extension = getExtension(f);
			if (extension != null){
				if(extension.equalsIgnoreCase(this.extension_new) || 
						extension.equalsIgnoreCase(this.extension))
					return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 设置config文件中的默认文件打开路径
	 * 使用时用来作为FileChooser的setCurrentDirectory方法的参数
	 * @param path
	 */
	public void setChooserPathInConfig(String path){
		lastFileChooser = path;
		File file = new File(CONFIG);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		try {
			PrintStream ps = new PrintStream(file);
			ps.print(lastFileChooser);
			///debug
			System.out.println("新默认地址：" + lastFileChooser);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "xls,xlsx";
	}
	
	private String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1)
				return filename.substring(i + 1).toLowerCase();
		}
		return null;
	}

}
