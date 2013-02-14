package com.mjakop.lib.utils;

import java.util.regex.Pattern;

public class Utils {
	

	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );
	
	public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	
	
	public static String truncateText(String text, int length) {
		if (text.length() > length) {
			String nText = text.substring(0, length-3);
			nText = nText + "...";
			return nText;
		} else {
			return text;
		}
	}
}
