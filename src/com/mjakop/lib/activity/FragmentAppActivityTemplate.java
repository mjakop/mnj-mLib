package com.mjakop.lib.activity;

import com.mjakop.lib.db.DBModel;
import com.mjakop.lib.db.DBOpenHelper;
import com.mjakop.lib.db.models.AppSettings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public abstract class FragmentAppActivityTemplate extends FragmentActivity {

	
	private MActivityImpl impl;	
	private DBOpenHelper helper;
	private AppSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		onSetUI();
	}
	
	public MActivityImpl getImpl() {
		if (impl == null) {
			impl = new MActivityImpl(this);
		}
		return impl;
	}
	
	public DBOpenHelper getDBHelper() {
		if (helper == null){
			Class[] modelsAll = getDBSettingsListener().allModelsForDBHelper();
			helper = new DBOpenHelper(this, modelsAll);
		}
		return helper;
	}
	
	public DBModel getModel(Class modelClass){
		return AppActivityTemplate.getModel(modelClass, getDBHelper());
	}
	
	public void displayToastShort(String text) {
		getImpl().displayToastShort(text);
	}

	public void displayToastLong( String text) {
		getImpl().displayToastLong(text);
	}

	public void displayToast(String text, int duration) {
		getImpl().displayToast(text, duration);
	}
	
	public void displayAlert(String text,String close_button) {
		getImpl().displayAlert(text, close_button, null);
	}

	public void displayAlert(String text, String close_button, DialogInterface.OnClickListener customListener) {
		getImpl().displayAlert(text, close_button, customListener);
	}
	
	public void switchActivity(Class<?> cls, Intent i){
		startActivity(i);
	}
	
	public void switchActivity(Class<?> cls){
		getImpl().switchActivity(cls);
	}
	
	public AppSettings getSettings() {
		if (settings == null) {
			settings = new AppSettings(getDBHelper());
		}
		return settings;
	}
	
	public abstract DBSettingsListener getDBSettingsListener();	
	protected abstract void onSetUI();
	
}
