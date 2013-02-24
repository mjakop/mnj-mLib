package com.mjakop.lib.utils;

import java.text.NumberFormat;

public class SimpleFormat {

	
	public static String formatDoubleToPlaces(double value, int places) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(places);
		nf.setMaximumFractionDigits(places);
		String output = nf.format(value);
		return output;
	}
	
	
}
