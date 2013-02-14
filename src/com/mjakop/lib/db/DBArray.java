package com.mjakop.lib.db;

import org.json.JSONArray;
import org.json.JSONException;

public class DBArray extends JSONArray {

	
	public DBItem getDBItem(int index) throws JSONException {
		return (DBItem)super.get(index);
	}
	
}
