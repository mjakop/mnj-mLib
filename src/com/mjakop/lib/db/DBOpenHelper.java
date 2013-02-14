package com.mjakop.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static Context contextCache = null;
	public static final String DEFAULT_DB_PREFIX = "NONE_";
	private static String DB_PREFIX = DEFAULT_DB_PREFIX;
	private static final String DB_NAME = "B01.db";
	private static final int DB_VERSION = 1;
	
	private Class[] models;
	
	private static String getDBName(){
		return DB_NAME;
	}
	
	public DBOpenHelper(Context context, Class[] models) {
		super(context, getDBName(), null, DB_VERSION);
		this.models = models;
	}
	
	public Class[] getModels() {
		return models;
	}
	
	
	public synchronized SQLDatabase getDatabase() {
		return new SQLDatabase(super.getWritableDatabase());
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Class[] models = getModels();
		for(int i=0;i<models.length;i++){
			try{
				Class model = models[i];
				Object obj = model.newInstance();
				if (obj instanceof DBModel){
					DBModel dbModel = (DBModel)obj;
					String SQL = dbModel.createTableSQL();
					System.out.println(SQL);
					db.execSQL(SQL);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Class[] models = getModels();
		for(int i=0;i<models.length;i++){
			try{
				Class model = models[i];
				Object obj = model.newInstance();
				if (obj instanceof DBModel){
					DBModel dbModel = (DBModel)obj;
					try{
						String tableName = dbModel.getTableName();
						db.execSQL("DROP TABLE IF EXISTS "+tableName);
					}catch (Exception e) {
					}
					String SQL = dbModel.createTableSQL();
					db.execSQL(SQL);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		onCreate(db);
	}
}
