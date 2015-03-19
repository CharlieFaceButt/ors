package com.lsc.ors.debug;

/**
 * consoleÊä³ödebugÄÚÈİ
 * @author charlieliu
 *
 */
public class ConsoleOutput {

	private static boolean debug = true;
	
	public static void pop(String tag, String description){
		if(debug)
			System.out.println(tag + "\t\t\t: " + description);
	}
	public static void suspendDebug(){
		debug = false;
	}
	public static void reopenDebug(){
		debug = true;
	}
}
