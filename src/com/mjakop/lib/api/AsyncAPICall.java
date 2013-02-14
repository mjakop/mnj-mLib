package com.mjakop.lib.api;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.view.WindowManager;

public class AsyncAPICall extends AsyncTask<APICall, Void, Void> implements OnDismissListener {
	
	private AsyncAPICallListener listener;
	private Context context;
	private Dialog dialog;
	private ArrayList<APIResponse> okResponses;
	private ArrayList<APIResponse> errorResponses;
	private boolean interactWithUser = true;
	
	public AsyncAPICall(Context context, boolean interactWithUser, AsyncAPICallListener listener) {
		
	}
	
	public AsyncAPICall(AsyncAPICallListener listener) {
		this.listener = listener;
		this.context = null;
		this.interactWithUser = false;
	}
	
	public AsyncAPICall(Context context, AsyncAPICallListener listener) {
		this.listener = listener;
		this.context = context;
	}
	
    protected void onPreExecute() {
    	if (interactWithUser) {
    		dialog = listener.buildDialog(this.context);
    		dialog.setOnDismissListener(this);
    	}
    	if (listener != null) {
    		listener.onStart();
    	}
    }
    
    protected boolean isInteractWithUser(){
    	return this.interactWithUser;
    }

    @Override
    protected Void doInBackground(APICall ... calls) {
    	okResponses = new ArrayList<APIResponse>();
    	errorResponses = new ArrayList<APIResponse>();
    	for(int i=0;i<calls.length;i++){
			APICall call = calls[i];
    		try {
    			APIResponse response = call.execute();
    			listener.onEachSuccess(response);
    			okResponses.add(response);
    		}catch (Exception ex) {
				if (listener != null){
					APIResponseError response = listener.onException(call, ex);
					errorResponses.add(response);
					boolean break_others = listener.onFailure(response);
					if (break_others) {
						break;
					}
				}
			}
    	}
    	return null;
    }

    protected void onPostExecute(Void unused) {
    	if (interactWithUser) {
    		dialog.dismiss();
    	}
    	if (listener != null){
    		if (errorResponses.size() > 0) {
    			listener.onFinishWithErrors(okResponses, errorResponses);
    		} else {
    			listener.onFinishWithoutErrors(okResponses);
    		}
    		listener.onFinishAll();
    		
    	}
    }

	public void onDismiss(DialogInterface dialog) {
		this.cancel(true);
	}

	

}
