package com.mjakop.lib.db;

public class TableSchemaFieldForeignKey extends TableSchemaField {
	
	
	private Class foreignModel;
	
	public TableSchemaFieldForeignKey(String name, Class foreignModel) {
		super(name, TableSchemaField.Type.FOREIGN_KEY, 0);
		this.foreignModel = foreignModel;
	}
	
	public Class getForeignModel() {
		return foreignModel;
	}	
	
}
