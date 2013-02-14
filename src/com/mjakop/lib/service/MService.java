package com.mjakop.lib.service;


import com.mjakop.lib.db.DBOpenHelper;

import android.app.Service;
import android.content.Intent;

public abstract class MService extends Service {
	

	public ServiceNotification getNotification(Intent intent, String tickerText, String title, String content, int icon){
		return new ServiceNotification(getApplicationContext(), intent, tickerText, title, content, icon, -1);
	}
	
	public ServiceNotification getNotification(Intent intent, String tickerText, String title, String content, int icon, int id){
		return new ServiceNotification(getApplicationContext(), intent, tickerText, title, content, icon, id);
	}	
	
	public abstract DBOpenHelper getDBHelper();
}
