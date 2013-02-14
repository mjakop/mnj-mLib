package com.mjakop.lib.activity;

import com.mjakop.lib.db.models.AppSettings;

public abstract class DBSettingsListener {

	
	public abstract Class[] allModels();
	
	public Class[] allModelsForDBHelper(){
		Class[] models = allModels();
		Class[] modelsAll = new Class[models.length+1];
		System.arraycopy(models, 0, modelsAll, 0, models.length);
		//list API DB models
		modelsAll[models.length] = AppSettings.class;
		return modelsAll;
	}
	
}
