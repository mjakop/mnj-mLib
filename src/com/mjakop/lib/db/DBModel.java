package com.mjakop.lib.db;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;



public abstract class DBModel {
	
	private static HashMap<String, TableSchema> cachedTableSchemaH = new HashMap<String, TableSchema>();
	private DBOpenHelper dbHelper = null;
	
	public abstract String getTableName();
	
	public abstract TableSchema getTableSchemaImpl();
	
	
	public static DBItem jsonObjectToDBItem(JSONObject jobject) throws Exception {
		DBItem item = new DBItem();
		Iterator<String> keys = jobject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			Object value = jobject.get(key);
			item.put(key, value);
		}
		return item;
	}
	
	public static DBArray jsonArrayToDBArray(JSONArray jarray) throws Exception {
		DBArray result = new DBArray();
		
		for(int i=0;i<jarray.length();i++){
			JSONObject jobject = jarray.getJSONObject(i);
			DBItem dbItem = jsonObjectToDBItem(jobject);
			result.put(dbItem);
		}
		
		return result;
	}
	
	public DBModel(DBOpenHelper dbh) {
		this.dbHelper = dbh;
	}	
	
	public DBOpenHelper getDBHelper() {
		return dbHelper;
	}
	
	public SQLDatabase getDatabase(){
		return getDBHelper().getDatabase();
	}
	
	public void queryNoResult(String SQL) {
		getDatabase().queryNoResult(SQL);
	}
	
	public DBItem getByPK(String fields, Object pkVal){
		return getDatabase().getByPK(this, fields, pkVal);
	}
	
	public DBItem get(String fields, String otherConditions){
		return getDatabase().get(this, fields, otherConditions);
	}
	
	public DBArray select(String fields, String otherConditions){
		return getDatabase().select(this, fields, otherConditions);
	}
	
	public void update(DBItem data, String where) throws Exception{
		getDatabase().update(this, data, where);
	}
	
	public void updateByPK(DBItem data, Object pkVal) throws Exception{
		getDatabase().updateByPK(this, data, pkVal);
	}
	
	public int count(String otherConditions){
		return getDatabase().count(this, otherConditions);
	}
	
	public int count(){
		return count("");
	}
	
	public DBArray select(){
		return select("*", "");
	}
	
	public DBArray select(String SQL){
		return getDatabase().select(this, SQL);
	}	
	
	public void insert(DBItem data) throws Exception{
		getDatabase().insert(this, data);
	}
	
	public void insertAll(DBArray data) throws Exception{
		getDatabase().insertAll(this, data);
	}
	
	public void insertOrUpdateAll(DBArray data) throws Exception{
		getDatabase().insertOrUpdateAll(this, data);
	}
	
	public void insertOrUpdate(DBItem data) throws Exception{
		getDatabase().insertOrUpdate(this, data);
	}
	
	public void insertOrUpdateByCondition(DBItem data, String whereCondition) throws Exception {
		getDatabase().insertOrUpdateByCondition(this, data, whereCondition);
	}
	
	public void insertOrUpdateAllByCondition(DBArray data, String whereCondition) throws Exception{
		getDatabase().insertOrUpdateAllByCondition(this, data, whereCondition);
	}
	
	public void remove(String otherConditions) throws Exception{
		getDatabase().remove(this, otherConditions);
	}
	
	public void clear() throws Exception {
		remove("");
	}
	
	public void removeByPK(Object pkVal) throws Exception{
		getDatabase().removeByPK(this, pkVal);
	}
	
	public long lastInsertId() throws Exception {
		return getDatabase().lastInsertId(this);
	}
	
	public boolean exists(Object pkVal) throws Exception {
		String pf = getTableSchema().getPrimaryField().getName();
		DBItem i = getByPK(pf, pkVal);
		return (i != null);
	}
	
	public TableSchema getTableSchema(){
		TableSchema cachedTableSchema = cachedTableSchemaH.get(getTableName());
		if (cachedTableSchema == null){
			cachedTableSchema = getTableSchemaImpl();
			cachedTableSchemaH.put(getTableName(), cachedTableSchema);
		}
		return cachedTableSchema;
	}
	
	public String createTableSQL() {
		TableSchema schema = getTableSchema();
		String tableName = getTableName();
		return schema.getCreateSQL(tableName);
	}
	

}
