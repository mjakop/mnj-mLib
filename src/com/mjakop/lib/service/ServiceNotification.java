package com.mjakop.lib.service;

import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public class ServiceNotification {

	private String tickerText;
	private String title;
	private String content;
	private Notification notification;
	private boolean onGoing = false;
	private int icon;
	private Intent intent;
	private Context context;
	private int notificationID = 1234;
	
	public ServiceNotification(Context context, Intent intent, String tickerText, String title, String content, int icon, int notificationID) {
		this.tickerText = tickerText;
		this.title = title;
		this.content = content;
		this.onGoing = false;
		this.icon = icon;
		this.context = context;
		this.intent = intent;
		//create unique id for each notification.
		if (notificationID == 0 || notificationID < 0){
			this.notificationID = (Calendar.getInstance().getTimeInMillis()+"_"+(Math.random()*100000)).hashCode();
		}
	}
	
	private void buildNotification(){
		long when = System.currentTimeMillis();
		Intent notificationIntent = intent;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Notification notification = new Notification(icon, tickerText, when);
		if (onGoing){
			notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
		}
		notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, title, content, contentIntent);		
		this.notification = notification;		
	}
	
	public void setOnGoing(boolean onGoing) {
		this.onGoing = onGoing;
	}
	
	public boolean isOnGoing() {
		return onGoing;
	}
	
	public void setContent(String content) {
		this.content = content;
		updateChanges();
	}
	
	private void updateChanges(){
		show();
	}
	
	public void show(){
		buildNotification();
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(ns);
		mNotificationManager.notify(notificationID, this.notification);		
	}
	
	public void cancel(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(ns);
		mNotificationManager.cancel(notificationID);	
	}
	
}
