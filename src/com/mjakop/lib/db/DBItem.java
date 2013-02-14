package com.mjakop.lib.db;

import org.json.JSONException;
import org.json.JSONObject;

public class DBItem extends JSONObject {

	public ForeignKeyVal getForeignKeyVal(String name) throws JSONException{
		return (ForeignKeyVal)get(name);
	}
	
}
