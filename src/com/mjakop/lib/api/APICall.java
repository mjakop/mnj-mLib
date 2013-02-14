package com.mjakop.lib.api;

import java.util.HashMap;

import org.json.JSONObject;

import com.mjakop.lib.utils.SimpleDeflateCompression;
import com.mjakop.lib.utils.SimpleHttpClient;

public abstract class APICall {

	private APICallMessage msg;
	private HashMap<String, Object> localVars = new HashMap<String, Object>();
	
	
	public APICall(APICallMessage msg) {
		this.msg = msg;
	}
	
	public HashMap<String, Object> getLocalVars() {
		return localVars;
	}
	
	public APICallMessage getMsg() {
		return msg;
	}
	
	public abstract String makeUrl() throws Exception;
	
	public abstract APIResponse getResponseParser();
	
	public APIResponse execute() throws Exception {
		byte[] bytesToSend = msg.getBytes();
		byte[] response = SimpleHttpClient.requestPOST(makeUrl(), bytesToSend);
		return getResponseParser().parseResponseMsg(this, response);
	}
}
