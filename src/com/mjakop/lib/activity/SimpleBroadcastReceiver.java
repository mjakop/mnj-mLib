package com.mjakop.lib.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SimpleBroadcastReceiver  extends BroadcastReceiver{
	private SimpleBroadcastReceiverListener listener;
	private Context context;
	IntentFilter intentFilter = new IntentFilter();
	
	public SimpleBroadcastReceiver(Context context, SimpleBroadcastReceiverListener listener) {
		this.context = context;
		this.listener = listener;
	}
	
	public void listenToAction(String action){
		intentFilter.addAction(action);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (listener != null){
			listener.onAction(action, context, intent);
		}
	}
	
	public void start(){
		context.registerReceiver(this, intentFilter);
	}
	
	public void stop(){
		context.unregisterReceiver(this);
	}

	
}
