package com.mjakop.lib.db;

import java.util.Calendar;
import java.util.Iterator;

import org.json.JSONArray;

import com.mjakop.lib.db.TableSchemaField.Type;
import com.mjakop.lib.utils.SimpleConvert;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLDatabase {


	private SQLiteDatabase db;
	
	public SQLDatabase(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void close(){
		db.close();
	}
	
	public static int makeDBBool(boolean status){
		int result = 0;
		if (status) {
			result = 1;
		}
		return result;
	}
	
	public static boolean parseDBBool(int dbVal){
		if (dbVal == 1){
			return true;
		}
		return false;
	}
	
	public static long makeDBDate(Calendar c, boolean resetTime) {
		if (resetTime){
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);			
		}
		return makeDBDate(c);
	}
	
	public static long makeDBDate(Calendar c) {
		return makeDBDate(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}
	
	public static long makeDBDate(int year, int month, int day) {
		return makeDBDate(year, month, day, 0, 0, 0);
	}
	
	public static long makeDBDate(int year, int month, int day, int hour, int min, int sec) {
		long result = year;
		result = result * 100 + month;
		result = result * 100 + day;
		result = result * 100 + hour;		
		result = result * 100 + min;
		result = result * 100 + sec;
		return result;
	}
	
	public static Calendar parseDBDate(long date){
		Calendar c = Calendar.getInstance();
		long sec = date % 100;
		date = (long)(date / 100);
		
		long minute = date % 100;
		date = (long)(date / 100);
		
		long hour = date % 100;
		date = (long)(date / 100);	
		
		long day = date % 100;
		date = (long)(date / 100);
		
		long month = date % 100;
		date = (long)(date / 100);
		
		long year = date;
		
		c.set((int)year, (int)month, (int)day);
		c.set(Calendar.HOUR_OF_DAY, (int)hour);
		c.set(Calendar.MINUTE, (int)minute);
		c.set(Calendar.SECOND, (int)sec);
		return c;
	}	
	
	public Cursor query(String sql) {
		return db.rawQuery(sql, null);
	}
	
	public void queryNoResult(String sql){
		db.execSQL(sql);
	}
	
	private DBModel createModelInstance(Class modelClass, DBModel model){
		try {
			return (DBModel)modelClass.getConstructor(DBOpenHelper.class).newInstance(model.getDBHelper());
		}catch (Exception e) {
			return null;
		}
	}
	
	private DBItem getDBItemFromCursor(Cursor c, DBModel model) throws Exception{
		
		DBItem result = new DBItem();
		String[] columns = c.getColumnNames();
		for(int i=0;i<columns.length;i++){
			String columnName = columns[i];
			//int columnType = c.getType(i);
			TableSchemaField field = model.getTableSchema().getFieldByName(columnName);

			if (field.getType() == Type.TEXT) {
				result.put(columnName, c.getString(i));
			} else if (field.getType() == Type.INTEGER) {
				result.put(columnName, c.getInt(i));
			} else if (field.getType() == Type.BOOLEAN) {
				result.put(columnName, parseDBBool(c.getInt(i)));
			} else if (field.getType() == Type.DATETIME) {
				result.put(columnName, parseDBDate(c.getLong(i)));
			} else if (field.getType() == Type.FOREIGN_KEY) {
				TableSchemaFieldForeignKey fk = (TableSchemaFieldForeignKey)field;
				ForeignKeyVal fkVal = new ForeignKeyVal(createModelInstance(fk.getForeignModel(), model), c.getInt(i));
				result.put(columnName, fkVal);
			} else if (field.getType() == Type.REAL) {
				result.put(columnName, c.getDouble(i));
			} else if (field.getType() == Type.ENCRYPTED_STRING) {
				byte[] bytes = c.getBlob(i);
				//must call decrypt to get String value! 
				result.put(columnName, new EncryptedStringFieldVal(bytes));
			} else {
				result.put(columnName, null);
			}
			
			/*if (columnType == Cursor.FIELD_TYPE_STRING){
				result.put(columnName, c.getString(i));
			}else if (columnType == Cursor.FIELD_TYPE_NULL){
				result.put(columnName, null);
			}else if (columnType == Cursor.FIELD_TYPE_INTEGER){
				
				if (field.getType() == Type.BOOLEAN){
					result.put(columnName, parseDBBool(c.getInt(i)));
				}else if (field.getType() == Type.DATETIME){
					result.put(columnName, parseDBDate(c.getLong(i)));
				}else if (field.getType() == Type.FOREIGN_KEY){
					TableSchemaFieldForeignKey fk = (TableSchemaFieldForeignKey)field;
					ForeignKeyVal fkVal = new ForeignKeyVal(createModelInstance(fk.getForeignModel(), model), c.getInt(i));
					result.put(columnName, fkVal);
				}else {
					result.put(columnName, c.getInt(i));
				}
			}else if (columnType == Cursor.FIELD_TYPE_FLOAT){
				result.put(columnName, c.getDouble(i));
			}else if (columnType == Cursor.FIELD_TYPE_BLOB){
				if (field.getType() == Type.ENCRYPTED_STRING) {
					byte[] bytes = c.getBlob(i);
					//must call decrypt to get String value! 
					result.put(columnName, new EncryptedStringFieldVal(bytes));
				}else {
					result.put(columnName,	"Not implemented!");
				}
			}*/
		}
		return result;
	}
	
	public DBItem getByPK(DBModel model, String fields, Object pkVal){
		String otherConditions = " WHERE "+model.getTableSchema().getPrimaryField().getName()+"="+convertValueForDB(model.getTableSchema().getPrimaryField(), pkVal);
		return get(model, fields, otherConditions);
	}
	
	public DBItem get(DBModel model, String fields, String otherConditions){
		String SQL = "SELECT "+fields+" FROM "+model.getTableName()+" "+otherConditions;
		return get(model, SQL);
	}
	
	/**
	 * @param fields
	 * @param tableName
	 * @param otherConditions: SQL part after table name 
	 */
	public DBItem get(DBModel model, String SQL){
		Cursor c = query(SQL);
		if (c.moveToFirst()){
			try {
				DBItem result = getDBItemFromCursor(c, model);
				return result;
			}catch (Exception e) {
				Log.e("SQLDatabase", "Problem getting JSONObject from cursor.", e);
				return null;
			}
		} else {
			return null;
		}
	}
		
	public DBArray select(DBModel model, String fields, String otherConditions) {
		String SQL = "SELECT "+fields+" FROM "+model.getTableName()+" "+otherConditions;
		return select(model, SQL);
	}
	
	public DBArray select(DBModel model, String SQL) {
		DBArray result = new DBArray();
		Cursor c = query(SQL);
		if (c.moveToFirst()){
			do {
				try{
					result.put(getDBItemFromCursor(c, model));
				}catch (Exception e) {
					Log.e("SQLDatabase", "Problem getting JSONObject from cursor.", e);
				}
			}while(c.moveToNext());
		}
		return result;
	}
	
	private Object convertValueForDB(TableSchemaField field, Object value){
		try {
			if (field.getType() == Type.TEXT){
				value = DatabaseUtils.sqlEscapeString(value.toString());
			}else if (field.getType() == Type.REAL){
				value = Double.parseDouble(value.toString());
			}else if (field.getType() == Type.INTEGER){
				value = Integer.parseInt(value.toString());
			}else if (field.getType() ==  Type.DATETIME){
				value = makeDBDate((Calendar)value);
			}else if (field.getType() == Type.BOOLEAN){
				value = makeDBBool((Boolean)value);
			}else if (field.getType() == Type.FOREIGN_KEY) {
				if (value instanceof Integer) {
					value = Integer.parseInt(value.toString());
				} else if (value instanceof String){
					value = Integer.parseInt(value.toString());
				} else if (value instanceof ForeignKeyVal){
					ForeignKeyVal fkVal = (ForeignKeyVal)value;
					value = fkVal.getVal();
				} else {
					throw new Exception("Type not supported for foreign key.");
				}
			} else if (field.getType() == Type.ENCRYPTED_STRING) {
				if (value instanceof EncryptedStringFieldVal) {
					EncryptedStringFieldVal esfv = (EncryptedStringFieldVal)value;
					byte[] data = esfv.encrypt();
					value = "X'"+SimpleConvert.toHex(data)+"'";
				} else {
					value = "";
				}

			}
			return value;
		}catch (Exception e) {
			try {
				return field.getDefaultValue();
			}catch (Exception t) {
				return null;
			}
		}
	}
	
	public void insert(DBModel model, DBItem data) throws Exception{
		String columns = "(";
		String values = "(";
		int i = 0;
		Iterator<TableSchemaField> fields = model.getTableSchema().getFields();
		while(fields.hasNext()){
			TableSchemaField field = fields.next();
			if (field == model.getTableSchema().getPrimaryField() && data.has(field.getName()) == false){
				//leave empty for autoincrement
				continue;
			}
			Object value = field.getDefaultValue();
			//replace with passing value
			if (data.has(field.getName())){
				Object tmpValue = data.get(field.getName());
				//if not null
				if (tmpValue.toString().equals("null") == false){
					value = tmpValue;
				}
			}
			//convert to appropriate form
			value = convertValueForDB(field, value);
			//add to sql
			if (i > 0){
				columns = columns + ", ";
				values = values + ", ";
			}
			if (value == null){
				value = "null";
			}
			columns = columns + field.getName();
			values = values + value.toString();
			i++;
		}
		String SQL = "INSERT INTO "+model.getTableName()+" "+columns+") VALUES"+values+");";
		queryNoResult(SQL);
	}
	
	public void insertAll(DBModel model, DBArray data) throws Exception{
		db.beginTransaction();
		try {
			for(int i=0;i<data.length();i++){
				DBItem obj = data.getDBItem(i);
				insert(model, obj);
			}
			db.setTransactionSuccessful();
		}catch (Exception e) {
			throw e;
		}finally {
			db.endTransaction();
		}
	}
	
	public void update(DBModel model, DBItem data, String where) throws Exception{
		try {
			JSONArray fields = data.names();
			String setFields = "";
			for(int i=0;i<fields.length();i++){
				String name = fields.getString(i);
				if (model.getTableSchema().hasField(name) == false) {
					continue;
				}
				TableSchemaField field = model.getTableSchema().getFieldByName(name);
				Object value = convertValueForDB(field, data.get(name));
				if (i > 0){
					setFields = setFields + ", ";	
				}
				setFields = setFields + name+"="+value;
			}
			String SQL = "UPDATE "+model.getTableName()+" SET "+setFields+" "+where;
			queryNoResult(SQL);
		}catch (Exception e) {
			throw e;
		}
	}
	
	public void updateByPK(DBModel model, DBItem data, Object pkVal) throws Exception{
		String otherConditions = " WHERE "+model.getTableSchema().getPrimaryField().getName()+"="+convertValueForDB(model.getTableSchema().getPrimaryField(), pkVal);
		update(model, data, otherConditions);
	}	
	
	public void insertOrUpdateByCondition(DBModel model, DBItem data, String whereCondition) throws Exception{
		if (exists(model, whereCondition) == true){
			update(model, data, whereCondition);
		} else {
			insert(model, data);
		}
	}
	
	public void insertOrUpdate(DBModel model, DBItem data) throws Exception{
		TableSchemaField primaryField = model.getTableSchema().getPrimaryField();
		Object primaryFieldValue = data.get(primaryField.getName());
		String whereCondition = "WHERE "+primaryField.getName()+"="+convertValueForDB(primaryField, primaryFieldValue);
		insertOrUpdateByCondition(model, data, whereCondition);
	}
	
	public void insertOrUpdateAll(DBModel model, DBArray data) throws Exception{
		db.beginTransaction();
		try {
			for(int i=0;i<data.length();i++){
				DBItem obj = data.getDBItem(i);
				insertOrUpdate(model, obj);
			}
			db.setTransactionSuccessful();
		}catch (Exception e) {
			throw e;
		}finally {
			db.endTransaction();
		}
	}
	
	public void insertOrUpdateAllByCondition(DBModel model, DBArray data, String whereCondition) throws Exception{
		db.beginTransaction();
		try {
			for(int i=0;i<data.length();i++){
				DBItem obj = data.getDBItem(i);
				insertOrUpdateByCondition(model, obj, whereCondition);
			}
			db.setTransactionSuccessful();
		}catch (Exception e) {
			throw e;
		}finally {
			db.endTransaction();
		}	
	}
	
	public boolean exists(DBModel model, String otherConditions){
		int c = count(model, otherConditions);
		if (c > 0){
			return true;
		}
		return false;
	}
	
	public int count(DBModel model, String otherConditions){
		try{
			String SQL = "SELECT count(*) as count FROM "+model.getTableName()+" "+otherConditions;
			Cursor c = query(SQL);
			if (c.moveToFirst()){
				return c.getInt(0);
			}
			return 0;
		}catch (Exception e) {
			return 0;
		}
	}
	
	public void remove(DBModel model, String otherConditions) throws Exception{
		String SQL = "DELETE FROM "+model.getTableName()+" "+otherConditions;
		queryNoResult(SQL);
	}
	
	public void removeByPK(DBModel model, Object pkVal) throws Exception{
		TableSchemaField primaryField = model.getTableSchema().getPrimaryField();
		String whereCondition = "WHERE "+primaryField.getName()+"="+convertValueForDB(primaryField, pkVal);
		remove(model, whereCondition);
	}
	
	
	public long lastInsertId(DBModel model) throws Exception {
		String SQL = "SELECT last_insert_rowid() as id";
		Cursor c = query(SQL);
		if (c.moveToFirst()){
			return c.getLong(0);
		} else {
			throw new Exception("Error with last_insert_rowid.");
		}
	}
}
