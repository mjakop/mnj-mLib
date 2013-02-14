package com.mjakop.lib.api;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public abstract class AsyncAPICallListener {

	public void onStart(){
		
	}
	
	//in working thread
	public void onEachSuccess(APIResponse response){
		
	}
	
	//inside gui thread
	public abstract void onSuccess(APIResponse response);
	
	//inside GUI thread
	public void onSuccessAll(ArrayList<APIResponse> responses){
		for(int i=0;i<responses.size();i++) {
			onEachSuccess(responses.get(i));
		}
	}
	
	//if true, break other calls
	public boolean onFailure(APIResponseError response){
		return true;
	}
	
	public void onFinishWithErrors(ArrayList<APIResponse> okResponses, ArrayList<APIResponse> errorResponses) {
		
	}
	
	public void onFinishWithoutErrors(ArrayList<APIResponse> okResponses) {
		if (okResponses.size() > 0) {
			onSuccess(okResponses.get(0));
		}
	}
	
	public APIResponseError onException(APICall call,Exception ex) { 
		return new APIResponseError(call, ex);
	}
	
	public void onFinishAll(){
		
	}
	
	public Dialog buildDialog(Context context){
		return ProgressDialog.show(context, "", "Working. Please wait...", true);
	}
}
