package com.mjakop.lib.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;

public class SimpleNFC {
	
	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Activity activity;
	
	public SimpleNFC(Activity activity) {
		this.activity = activity;
		init();
	}
	
	private void init(){
		mAdapter = NfcAdapter.getDefaultAdapter(this.activity);
        mPendingIntent = PendingIntent.getActivity(this.activity, 0, new Intent(this.activity, this.activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters = new IntentFilter[] { ndef,};
        mTechLists = new String[][] { new String[] { MifareUltralight.class.getName(), Ndef.class.getName(), NfcA.class.getName()}, new String[] { MifareClassic.class.getName(), Ndef.class.getName(), NfcA.class.getName()}};		
	}
	
	public void onActivityPause(){
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this.activity);
		}
	}
	
	public void onActivityResume(){
        if (mAdapter != null) {
        	mAdapter.enableForegroundDispatch(this.activity, mPendingIntent, mFilters, mTechLists);
        }
	}
	
	public Tag onAcivityNewIntentGetTag(Intent intent){
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		return tagFromIntent;
	}
	
	public byte[] onAcivityNewIntentGetTagId(Intent intent){
		byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		return tagId;
	}
}
