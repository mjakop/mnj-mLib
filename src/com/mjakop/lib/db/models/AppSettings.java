package com.mjakop.lib.db.models;

import java.util.Calendar;

import com.mjakop.lib.db.DBModel;
import com.mjakop.lib.db.DBOpenHelper;
import com.mjakop.lib.db.SQLDatabase;
import com.mjakop.lib.db.TableSchema;
import com.mjakop.lib.db.TableSchemaField;
import com.mjakop.lib.db.TableSchemaField.Type;

import android.database.Cursor;

public class AppSettings extends DBModel {

	private static String TABLE_NAME = "app_settings";
		
	public AppSettings() {
		super(null);
	}
	
	public AppSettings(DBOpenHelper oph){
		super(oph);
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public TableSchema getTableSchemaImpl() {
		TableSchema schema = new TableSchema();
		schema.addField(new TableSchemaField("key",Type.TEXT));
		schema.addField(new TableSchemaField("value", Type.TEXT));
		return schema;
	}
	
	public String getString(String key, String defaultVal) {
		SQLDatabase db = getDatabase();
		String result = defaultVal;
		Cursor c = db.query("SELECT value FROM "+TABLE_NAME+" WHERE key='"+key+"'");
		if (c.moveToFirst()){
			result = c.getString(0);
		}
		db.close();
		return result;
	}
	
	public boolean hasKey(String key){
		return hasKey(key, true);
	}
	
	public boolean hasKey(String key, boolean dbClose){
		SQLDatabase db = getDatabase();
		Cursor c = db.query("SELECT key FROM "+TABLE_NAME+" WHERE key='"+key+"'");
		boolean result = false;
		if (c.moveToFirst()){
			result = true;
		}
		if (dbClose){
			db.close();
		}
		return result;
	}
	
	public void setString(String key, String value) {
		String SQL = "";
		if (hasKey(key)){
			if (value == null) {
				SQL = "DELETE FROM "+TABLE_NAME+" WHERE key='"+key+"'";
			} else {
				//update
				SQL = "UPDATE "+TABLE_NAME+" SET value='"+value+"' WHERE key='"+key+"'";
			}
		} else {
			//insert
			SQL = "INSERT INTO "+TABLE_NAME+" (key, value) VALUES('"+key+"','"+value+"');";
		}
		SQLDatabase db = getDatabase();
		db.queryNoResult(SQL);
		db.close();
	}
	
	public int getInt(String key, int defaultVal){
		try {
			String tmp = getString(key, "");
			return Integer.parseInt(tmp);
		}catch (Exception e) {
			return defaultVal;
		}
	}
	
	public void setInt(String key, int value){
		setString(key, ""+value);
	}
	
	public boolean getBoolean(String key, boolean defaultVal){
		int tmp = getInt(key, -1);
		if (tmp == -1) {
			return defaultVal;
		} else {
			if (tmp == 0){
				return false;
			} else {
				return true;
			}
		}
	}
	
	public void setBoolean(String key, boolean value) {
		int intVal = 0;
		if (value == true) {
			intVal = 1;
		}
		setInt(key, intVal);
	}
	
	public void setDateTime(String key, Calendar cal) {
		long dbDate = SQLDatabase.makeDBDate(cal);
		setString(key, ""+dbDate);
	}
	
	public Calendar getDateTime(String key) {
		long dt = Long.parseLong(getString(key, "0"));
		if (dt != 0) {
			Calendar result = SQLDatabase.parseDBDate(dt);
			return result;
		}
		return null;
	}
	
	public void setDouble(String key, double value) {
		setString(key, ""+value);
	}
	
	public double getDouble(String key, double defaultVal) {
		try {
			String value = getString(key, null).replace(",", ".");
			return Double.parseDouble(value);
		}catch (Exception e) {
			return defaultVal;
		}
	}

}
