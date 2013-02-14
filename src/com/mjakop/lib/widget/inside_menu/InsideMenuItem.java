package com.mjakop.lib.widget.inside_menu;

public class InsideMenuItem {

	private String key;
	private String title;
	
	public InsideMenuItem(String key, String title) {
		this.key = key;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getKey() {
		return key;
	}
	
}
