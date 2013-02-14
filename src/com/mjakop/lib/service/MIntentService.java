package com.mjakop.lib.service;

import android.app.IntentService;
import android.content.Intent;

public abstract class MIntentService extends IntentService {

	public MIntentService(String name) {
		super(name);
	}

	public ServiceNotification getNotification(Intent intent, String tickerText, String title, String content, int icon){
		return new ServiceNotification(getApplicationContext(), intent, tickerText, title, content, icon, -1);
	}
	
	public ServiceNotification getNotification(Intent intent, String tickerText, String title, String content, int icon, int id){
		return new ServiceNotification(getApplicationContext(), intent, tickerText, title, content, icon, id);
	}	
}
