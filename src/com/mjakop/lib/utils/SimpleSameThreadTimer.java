package com.mjakop.lib.utils;
import android.os.Handler;

public class SimpleSameThreadTimer {

	private Runnable wrapperR;
	private final Runnable r;
	private final long delayMillis;
	private final Handler handler = new Handler();
	
	public SimpleSameThreadTimer(Runnable runnable, long delayInMillis){
		this.r = runnable;
		this.delayMillis = delayInMillis;
		this.wrapperR = new Runnable() {
			
			public void run() {
				r.run();
				handler.postDelayed(wrapperR, delayMillis);
			}
			
		}; 
	}
	
	public void start(){
		handler.removeCallbacks(wrapperR);
		handler.postDelayed(wrapperR, delayMillis);
	}
	
	public void stop(){
		handler.removeCallbacks(wrapperR);
	}
	
	
	
}
