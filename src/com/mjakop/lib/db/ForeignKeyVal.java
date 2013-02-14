package com.mjakop.lib.db;

import org.json.JSONObject;

public class ForeignKeyVal {

	private int val;
	private DBModel model;
	
	public ForeignKeyVal(DBModel model, int val) {
		this.val = val;
		this.model = model;
	}
	
	public int getVal() {
		return val;
	}
	
	public DBItem getData(){
		return getData("*");
	}
	
	public DBItem getData(String fields){
		return model.getByPK(fields, val);
	}
	
}
