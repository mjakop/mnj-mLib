package com.mjakop.lib.db;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.util.Log;

public class TableSchema {
	
	private Hashtable<String, TableSchemaField> fields = new Hashtable<String, TableSchemaField>();
	private TableSchemaField primaryField = null;

	public TableSchema() {
		
	}
	
	public void addField(TableSchemaField field, boolean primaryField){
		if (primaryField){
			this.primaryField = field;
		}
		fields.put(field.getName(), field);
	}
	
	public void addField(TableSchemaField field){
		addField(field, false);
	}
	
	public TableSchemaField getPrimaryField() {
		return primaryField;
	}
	
	public boolean hasField(String name){
		return fields.containsKey(name);
	}
	
	public TableSchemaField getFieldByName(String name){
		return fields.get(name);
	}
	
	public Iterator<TableSchemaField> getFields() {
		return fields.values().iterator();
	}
	
	public String getCreateSQL(String tableName){
		StringBuilder builder = new StringBuilder();
		builder.append("create table ");
		builder.append(tableName);
		builder.append("(");
		Iterator<TableSchemaField> fs = getFields();
		int i = 0;
		while(fs.hasNext()){
			TableSchemaField field = fs.next();
			boolean isPrimary = field == getPrimaryField();
			String sqlType = field.getSQLType();
			String name = field.getName();
			if (i > 0){
				builder.append(",");
			}
			builder.append(name+" "+sqlType);
			if (isPrimary){
				builder.append(" PRIMARY KEY");
			}
			i++;
		}
		builder.append(");");	
		Log.d("CREATE TABLE", builder.toString());
		return builder.toString();
	}
	
	
}
