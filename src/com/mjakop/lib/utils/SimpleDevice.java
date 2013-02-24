package com.mjakop.lib.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class SimpleDevice {

	public String niceOSVersion(){
		return "Android "+Build.VERSION.RELEASE;
	}
	
	public static String getWiFiMac(Context c){
		try {
			WifiManager wifiMan = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			return wifiInf.getMacAddress().toUpperCase();
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	public static boolean isWiFiEnabled(Context c){
		WifiManager wifiMan = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		return wifiMan.isWifiEnabled();
	}
	
	public static String getBluetoothAddress(){
		try {
			BluetoothAdapter btAdapt = BluetoothAdapter.getDefaultAdapter();
			return btAdapt.getAddress();
		}catch (Exception e) {
			return e.toString();
		}
	}
	
	public static String getDeviceId(Context c) {
		try {
			TelephonyManager telMan = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
			return telMan.getDeviceId();
		}catch (Exception e) {
			return e.toString();
		}
	}
	
	public static String getUNIQDeviceId(Context c){
		StringBuilder tmp = new StringBuilder();
		tmp.append(getWiFiMac(c));	
		tmp.append(getBluetoothAddress());
		tmp.append(getDeviceId(c));
		String partID = "35" +
	        	Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
	        	Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + 
	        	Build.DISPLAY.length()%10 + Build.HOST.length()%10 + 
	        	Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
	        	Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + 
	        	Build.TAGS.length()%10 + Build.TYPE.length()%10 + 
	        	Build.USER.length()%10 ; 
		tmp.append(partID);		
		try{
			return SimpleHash.SHA1(tmp.toString());
		}catch (Exception e) {
			return e.toString();
		}
	}
	
}
