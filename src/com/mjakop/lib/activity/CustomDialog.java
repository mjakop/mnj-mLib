package com.mjakop.lib.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public abstract class CustomDialog {

	private int layout;
	private Dialog dialog;
	private Context context;
	private String title;
	
	public CustomDialog(Context context, int layout, String title) {
		this.layout = layout;
		this.context = context;
		this.title = title;
	}
	
	public void show() {
		if (dialog == null){
			dialog = new Dialog(context);
			if (title != null) {
				dialog.setTitle(title);
			} else {
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);	
			}
			dialog.setContentView(layout);
			prepareView(dialog);
		}
		dialog.show();
	}
	
	public Context getContext() {
		return context;
	}
	
	public Dialog getDialog() {
		return dialog;
	}
	
	public abstract void prepareView(Dialog dialog);
}
