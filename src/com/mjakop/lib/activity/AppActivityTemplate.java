package com.mjakop.lib.activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;

import com.mjakop.lib.activity.MActivity;
import com.mjakop.lib.db.DBModel;
import com.mjakop.lib.db.DBOpenHelper;
import com.mjakop.lib.db.models.AppSettings;


public abstract class AppActivityTemplate extends MActivity {
	
	private DBOpenHelper helper = null;
	private static AppSettings settings;
	private static Hashtable<String, DBModel> loadedModels = new Hashtable<String, DBModel>();
	
	private static Constructor findModelConstructor(Constructor[] constructors){
		for (int i=0;i<constructors.length;i++){
			if (constructors[i].getParameterTypes().length == 1) {
				return constructors[i];
			}
		}
		return null;
	}
	
	public static DBModel getModel(Class modelClass, DBOpenHelper helper){
		String className = modelClass.getName();
		DBModel mLoaded = loadedModels.get(className);
		if (mLoaded == null){
			try {
				Constructor constructor = findModelConstructor(modelClass.getConstructors());
				mLoaded = (DBModel)constructor.newInstance(helper);
				loadedModels.put(className, mLoaded);
			}catch (IllegalAccessException e) {
			}catch (InstantiationException e) {
			}catch (InvocationTargetException e) {
			}
		}
		return mLoaded;
	}
	
	public DBModel getModel(Class modelClass){
		return getModel(modelClass, getDBHelper());
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (helper != null) {
			 helper.close();
			 helper = null;
		}
	}
	
	@Override
	public DBOpenHelper getDBHelper() {
		if (helper == null){
			Class[] modelsAll = getDBSettingsListener().allModelsForDBHelper();
			helper = new DBOpenHelper(this, modelsAll);
		}
		return helper;
	}
	
	public AppSettings getSettings() {
		if (settings == null) {
			settings = new AppSettings(getDBHelper());
		}
		return settings;
	}
	
	public void sendBrodcastAction(String action){
		Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		writeActivityLog("onPause");
		if (helper != null){
			helper.close();
			helper = null;
		}
		if (settings != null){
			settings = null;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		writeActivityLog("onResume");
	}
	
	@Override
	public void onBackPressed() {
		writeActivityLog("onBackPressed");
		super.onBackPressed();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		writeActivityLog("onLowMemory");
	}

	@Override
	public void onBackPressedAnimate() {
		writeActivityLog("onBackPressed");
		super.onBackPressedAnimate();
	}

	public abstract DBSettingsListener getDBSettingsListener();
	
}
