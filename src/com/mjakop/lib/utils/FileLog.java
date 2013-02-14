package com.mjakop.lib.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import android.os.Environment;

public class FileLog {

	private static final String path = "/logs/";
	private static Object syncObj = new Object();
	
	protected FileLog() {
	}

	private static File getStorageFilePath(String packageName){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)){
			File storage = Environment.getExternalStorageDirectory();
			Calendar now = Calendar.getInstance();
			String fileName = now.get(Calendar.YEAR)+""+now.get(Calendar.MONTH)+""+now.get(Calendar.DAY_OF_MONTH);
			String basicPath = storage.getPath()+"/"+packageName+path;
			new File(basicPath).mkdirs(); //create dirs
			return new File(basicPath+fileName+".log");
		} else {
			return null;
		} 
	}
	
	public static void writeLog(String packageName, String text){
		synchronized (syncObj) {
			try {
				File f = getStorageFilePath(packageName);
				BufferedWriter w = new BufferedWriter(new FileWriter(f, true));
				Calendar now = Calendar.getInstance();
				w.write(SimpleDateTime.formatDateTime(now));
				w.write(" - ");
				w.write(text);
				w.newLine();
				w.flush();
				w.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
