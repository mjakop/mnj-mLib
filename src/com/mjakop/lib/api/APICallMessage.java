package com.mjakop.lib.api;

public abstract class APICallMessage {

	
	public APICallMessage() {
	}
	
	public abstract byte[] getBytes() throws Exception;
	
	
}
