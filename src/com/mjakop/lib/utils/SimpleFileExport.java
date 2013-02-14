package com.mjakop.lib.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import org.json.JSONException;

import com.mjakop.lib.db.DBArray;
import com.mjakop.lib.db.DBItem;

public class SimpleFileExport {

	private static BufferedWriter getWriter(String path, String fileName) throws IOException {
		new File(path).mkdirs();
		if (path.endsWith("/")==false){
			path = path + "/";
		}
		path = path + fileName;
		return new BufferedWriter(new FileWriter(new File(path)));
	}
	
	public static boolean exportToCSV(String path, String fileName, DBArray data, String... fields) throws IOException, JSONException {
		return exportToCSV(path, fileName, data, ";", ".", fields);
	}
	
	public static boolean exportToCSV(String path, String fileName, DBArray data, String valueSeperator, String decimalSeperator, String... fields) throws IOException, JSONException{
		DecimalFormat dec = new DecimalFormat("#"+decimalSeperator+"######");
		BufferedWriter writer = getWriter(path, fileName);
		for(int i=0;i<fields.length;i++){
			if (i > 0){
				writer.append(valueSeperator);
			}
			writer.append(fields[i]);
		}
		writer.newLine();
		for(int i=0; i<data.length(); i++){
			DBItem item = data.getDBItem(i);
			for(int j=0; j<fields.length; j++) {
				if (j > 0){
					writer.append(valueSeperator);
				}
				Object obj = item.get(fields[j]);
				if (obj instanceof Double){
					Double dObj = (Double)obj;
					obj = dec.format(dObj.doubleValue());
				}
				writer.append(obj.toString());
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
		return true;
	}
	
	public static boolean exportToXML(String path, String fileName, DBArray data, String... fields) throws IOException, JSONException {
		DecimalFormat dec = new DecimalFormat("#.######");
		BufferedWriter writer = getWriter(path, fileName);
		writer.append("<data>");
		for(int i=0; i<data.length(); i++){
			DBItem item = data.getDBItem(i);
			writer.append("<item>");
			for(int j=0;j<fields.length;j++) {
				writer.append("<"+fields[j]+">");
				Object obj = item.get(fields[j]);
				if (obj instanceof Double){
					Double dObj = (Double)obj;
					obj = dec.format(dObj.doubleValue());
				}
				writer.append(obj.toString());
				writer.append("</"+fields[j]+">");
			}
			writer.append("</item>");
		}
		writer.append("</data>");
		writer.flush();
		writer.close();
		return true;
	}
	
}
