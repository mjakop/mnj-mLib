package com.mjakop.lib.activity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mjakop.lib.utils.FileLog;
import com.mjakop.lib.utils.SimpleDevice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MActivityImpl {
	
	
	private Activity activity;
	
	public MActivityImpl(Activity activity) {
		this.activity = activity;
	}
	
	public void switchActivityForResult(Class<?> cls, int requestCode){
		Intent i = new Intent(activity, cls);
		activity.startActivityForResult(i, requestCode);
	}
	
	public void switchActivity(Class<?> cls){
		Intent i = new Intent(activity, cls);
		activity.startActivity(i);
	}
	
	public void switchActivity(Class<?> cls, Hashtable<String, Object> params){
		switchActivity(cls, params, 0, false);
	}
	
	public void switchActivityForResult(Class<?> cls, Hashtable<String, Object> params, int requestCode){
		switchActivity(cls, params, requestCode, true);
	}
	
	public void switchActivity(Class<?> cls, Intent i){
		activity.startActivity(i);
	}
	
	public void switchActivityClear(Class<?> cls, Intent i){
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		switchActivity(cls, i);
	}
	
	public void switchActivityClear(Class<?> cls){
		Intent i = new Intent(activity, cls);
		switchActivityClear(cls, i);
	}
	
	public static Intent fillParamsToIntent(Hashtable<String, Object> params, Intent i) {
		Enumeration<String> keys = params.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object value = params.get(key);
			if (value instanceof Boolean){
				i.putExtra(key, ((Boolean)value).booleanValue());
			} else if (value instanceof Integer){
				i.putExtra(key, ((Integer)value).intValue());
			} else if (value instanceof Double){
				i.putExtra(key, ((Double)value).doubleValue());
			} else if (value instanceof String){
				i.putExtra(key, (String)value);
			} else if (value instanceof Parcelable) {
				i.putExtra(key, (Parcelable)value);
			}
		}
		return i;
	}
	
	public void switchActivity(Class<?> cls, Hashtable<String, Object> params, int requestCode, boolean useRequestCode){
		Intent i = new Intent(activity, cls);
		i = fillParamsToIntent(params, i);
		if (useRequestCode){
			activity.startActivityForResult(i, requestCode);
		} else {
			activity.startActivity(i);
		}
	}	
	
	public void displayToastShort(String text) {
		displayToast(text, Toast.LENGTH_SHORT);
	}

	public void displayToastLong( String text) {
		displayToast(text, Toast.LENGTH_LONG);
	}

	public void displayToast(String text, int duration) {
		Toast toast = Toast.makeText(activity, text, duration);
		toast.show();
	}
	
	public void displayAlert(String text,String close_button) {
		displayAlert(text, close_button, null);
	}

	public void displayAlert(String text, String close_button, DialogInterface.OnClickListener customListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		DialogInterface.OnClickListener listener;
		if (customListener == null) {
			listener = new DialogInterface.OnClickListener() {

				// click listener on the alert box
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
				
			};
		} else {
			listener = customListener;
		}
		builder.setMessage(text).setNeutralButton(close_button, listener);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void askYesNo(String text, String yesText, String noText, DialogInterface.OnClickListener customListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		DialogInterface.OnClickListener listener;
		if (customListener == null){
			listener = new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();					
				}
			};
		} else {
			listener = customListener;
		}
		builder.setMessage(text).setPositiveButton(yesText, listener).setNegativeButton(noText, listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void fullscreen(boolean state) {
		if (state) {
		    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);  
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		}
	}
	
	public String getUniqueDeviceId(){
		return SimpleDevice.getUNIQDeviceId(activity);
	}
	
	public TextView findTextView(int id) {
		return (TextView)activity.findViewById(id);
	}
	
	public EditText findEditText(int id) {
		return (EditText)activity.findViewById(id);
	}
	
	public void writeLog(String text){
		FileLog.writeLog(activity.getApplicationContext().getPackageName(), text);
	}
	
	public void writeActivityLog(String text){
		writeLog(getClass().getName()+": "+text);
	}
	
	public void writeLogException(String tag, Exception ex){
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		writeLog(tag+"\n"+sw.toString()+"\n");
	}
	
	public void platformAction(String uri){
		Intent i = new Intent(Intent.ACTION_VIEW);  
		i.setData(Uri.parse(uri));  
		activity.startActivity(i); 
	}
	
	public void showShareOptions(String mimeType, String title, String subject, String content){
		showShareOptions(mimeType, title, subject, content, null);
	}
	
	public void showShareOptions(String mimeType, String title, String subject, String message, Hashtable<String, Serializable> extraParams){
		
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);  
		shareIntent.setType("molorewards/sendqr");  
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  
		
		if (extraParams != null) {
			Enumeration<String> keys = extraParams.keys();
			while(keys.hasMoreElements()) {
				String key = keys.nextElement();
				shareIntent.putExtra(key, extraParams.get(key));
			}
		}  		  
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		activity.startActivity(Intent.createChooser(shareIntent, "Insert share chooser title here")); 
	}
	
}
