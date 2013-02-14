package com.mjakop.lib.activity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mjakop.lib.db.DBOpenHelper;
import com.mjakop.lib.utils.FileLog;
import com.mjakop.lib.utils.SimpleDevice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/*
 
 Required permissions to use mLib:
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
	<uses-permission android:name="android.permission.BLUETOOTH" />
	<uses-permission android:name="android.permission.READ_PHONE_STATE" />
 
 */
public abstract class MActivity extends Activity {

	private int activityAnimationOut = -1;
	private int activityAnimationIn = -1;
	private int activityBackPressedAnimationOut = -1;
	private int activityBackPressedAnimationIn = -1;
	
	private MActivityImpl impl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		impl = new MActivityImpl(this);
		onSetUI();
	}
	
	public void setAnimation(int animantionIn, int animationOut){
		activityAnimationIn = animantionIn;
		activityAnimationOut = animationOut;
	}
	
	public void setBackPressedAnimation(int animantionIn, int animationOut){
		activityBackPressedAnimationIn = animantionIn;
		activityBackPressedAnimationOut = animationOut;
	}
	
	public void onBackPressedAnimate(){
		if (activityBackPressedAnimationOut != -1 && activityBackPressedAnimationIn != -1){
			overridePendingTransition(activityBackPressedAnimationIn, activityBackPressedAnimationOut);
		} else {
			animateActivitySwitch();
		}
	}
	
	private void animateActivitySwitch(){
		if (activityAnimationIn != -1 && activityAnimationOut != -1) {
			overridePendingTransition(activityAnimationIn, activityAnimationOut);
		}
	}	
	
	public void switchActivityForResult(Class<?> cls, int requestCode){
		impl.switchActivityForResult(cls, requestCode);
	}
	
	public void switchActivity(Class<?> cls){
		impl.switchActivity(cls);
		animateActivitySwitch();
	}
	
	public void switchActivity(Class<?> cls, Hashtable<String, Object> params){
		impl.switchActivity(cls, params, 0, false);
	}
	
	public void switchActivityForResult(Class<?> cls, Hashtable<String, Object> params, int requestCode){
		switchActivity(cls, params, requestCode, true);
	}
	
	public void switchActivity(Class<?> cls, Intent i){
		startActivity(i);
	}
	
	public void switchActivity(Class<?> cls, Hashtable<String, Object> params, int requestCode, boolean useRequestCode){
		impl.switchActivity(cls, params, requestCode, useRequestCode);
	}	
	
	public void switchActivityClear(Class<?> cls, Intent i){
		impl.switchActivityClear(cls, i);
	}
	
	public void switchActivityClear(Class<?> cls){
		impl.switchActivityClear(cls);
	}
	
	public PendingIntent startMServicePeriodically(Class<?> serviceClass, long intervalMillis){
		Intent myIntent = new Intent(this, serviceClass);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);		
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intervalMillis, pendingIntent);
		return pendingIntent;
	}
	
	public void startMService(Class<?> service) {
		startService(new Intent(service.getName()));
	}
	
	public void stopMService(Class<?> service) {
		stopService(new Intent(service.getName()));
	}
	
	public Bundle getParams() {
		return getIntent().getExtras();
	}
	
	public void displayToastShort(String text) {
		impl.displayToastShort(text);
	}

	public void displayToastLong( String text) {
		impl.displayToastLong(text);
	}

	public void displayToast(String text, int duration) {
		impl.displayToast(text, duration);
	}
	
	public void displayAlert(String text,String close_button) {
		impl.displayAlert(text, close_button, null);
	}

	public void displayAlert(String text, String close_button, DialogInterface.OnClickListener customListener) {
		impl.displayAlert(text, close_button, customListener);
	}
	
	public void askYesNo(String text, String yesText, String noText, DialogInterface.OnClickListener customListener) {
		impl.askYesNo(text, yesText, noText, customListener);
	}
	
	public void fullscreen(boolean state) {
		impl.fullscreen(state);
	}
	
	public String getUniqueDeviceId(){
		return impl.getUniqueDeviceId();
	}
	
	public TextView findTextView(int id) {
		return impl.findTextView(id);
	}
	
	public EditText findEditText(int id) {
		return impl.findEditText(id);
	}
	
	public void writeLog(String text){
		impl.writeLog(text);
	}
	
	public void writeActivityLog(String text){
		writeLog(getClass().getName()+": "+text);
	}
	
	public void writeLogException(String tag, Exception ex){
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		writeLog(tag+"\n"+sw.toString()+"\n");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onBackPressedAnimate();
	}
	
	public void platformAction(String uri){
		impl.platformAction(uri);
	}
	
	public void showShareOptions(String mimeType, String title, String subject, String content){
		impl.showShareOptions(mimeType, title, subject, content);
	}
	
	public void showShareOptions(String mimeType, String title, String subject, String message, Hashtable<String, Serializable> extraParams){
		impl.showShareOptions(mimeType, title, subject, message, extraParams);
	}
	
	public abstract DBOpenHelper getDBHelper();
	protected abstract void onSetUI();
}
