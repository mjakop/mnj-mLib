package com.mjakop.lib.utils;

import java.util.HashMap;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SimpleFont {

	private static Typeface loadedFont = null;
	private static HashMap<View, Boolean> startingViews = new HashMap<View, Boolean>(); //list of view which childs have font
	
	
	public static Typeface getTypeFace(Activity activity, String fontFileName) {
		if (loadedFont == null) {
			loadedFont = Typeface.createFromAsset(activity.getAssets(), "fonts/"+fontFileName);
		}
		return loadedFont;
	}
	
	private static void applyFont(Activity activity, View v, String fontFileName) {
		if (v instanceof TextView) {
			TextView tv = (TextView)v;
			tv.setTypeface(getTypeFace(activity, fontFileName));
		} else if (v instanceof ViewGroup) {
			applyFontToChilds(activity, v, fontFileName);
		}
	}
	
	public static void applyFontToChilds(Activity activity, View v, String fontFileName) {
		try {
			if (startingViews.containsKey(v) == true) {
				//do nothing!
			} else {
				startingViews.put(v, true);
				if (v instanceof ListView) {
					ListView lv = (ListView)v;
					for(int i=0;i<lv.getChildCount();i++) {
						applyFont(activity, lv.getChildAt(i), fontFileName);
					}
				} else {
					ViewGroup vg = (ViewGroup)v;
					for (int i=0;i<vg.getChildCount(); i++) {
						View item = vg.getChildAt(i);
						applyFont(activity, item, fontFileName);
					}
				}
			}
		}catch (Exception e) {
		}
	}
	
}
