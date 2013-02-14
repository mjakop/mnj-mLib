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
		WifiManager wifiMan = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		return wifiInf.getMacAddress().toUpperCase();
	}
	
	public static boolean isWiFiEnabled(Context c){
		WifiManager wifiMan = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		return wifiMan.isWifiEnabled();
	}
	
	public static String getBluetoothAddress(){
		BluetoothAdapter btAdapt = BluetoothAdapter.getDefaultAdapter();
    	return btAdapt.getAddress();
	}
	
	public static String getDeviceId(Context c) {
		TelephonyManager telMan = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
		return telMan.getDeviceId();
	}
	
	public static String getUNIQDeviceId(Context c){
		StringBuilder tmp = new StringBuilder();
		tmp.append(getWiFiMac(c));	
		tmp.append(getBluetoothAddress());
		tmp.append(getDeviceId(c));
		try{
			return SimpleHash.SHA1(tmp.toString());
		}catch (Exception e) {
			return e.toString();
		}
	}
	
}
