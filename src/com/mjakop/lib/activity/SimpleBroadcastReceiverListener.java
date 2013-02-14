package com.mjakop.lib.activity;

import android.content.Context;
import android.content.Intent;

public interface SimpleBroadcastReceiverListener {

	public void onAction(String action, Context context, Intent intent);
	
}
