package com.mjakop.lib.db;

public class TableSchemaField {
	
	public enum Type {INTEGER, REAL, TEXT, DATETIME, BOOLEAN, FOREIGN_KEY, ENCRYPTED_STRING};
	
	private String name;
	private Type type;
	private Object defaultValue = null;
	
	public TableSchemaField(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public TableSchemaField(String name, Type type, Object defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}	
	
	public Type getType() {
		return type;
	}
	
	public String getSQLType(){
		if (type == Type.INTEGER){
			return "INTEGER";
		} else if (type == Type.REAL){
			return "REAL";
		} else if (type == Type.TEXT){
			return "TEXT";
		} else if (type == Type.DATETIME){
			return "LONG";
		} else if (type == Type.BOOLEAN){
			return "INTEGER";
		} else if (type == Type.FOREIGN_KEY){
			return "INTEGER";
		} else if (type == Type.ENCRYPTED_STRING){
			return "BLOB";
		}
		return "TEXT";
	}
	
	public String getName() {
		return name;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}

}
