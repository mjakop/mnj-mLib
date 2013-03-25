package com.mjakop.lib.api.impl;

import com.mjakop.lib.api.APICall;
import com.mjakop.lib.api.APIResponse;
import com.mjakop.lib.utils.SimpleHttpClient;

public class RESTAPICall extends APICall {
	
	public enum CallType {
		GET, POST, PUT, DELETE
	}
	
	public enum	 ResponseType {
		JSON
	}
	
	private String url;
	private CallType type;
	private ResponseType responseType;
	
	public RESTAPICall(){
		super(null); 
	}
	
	public RESTAPICall(String url, CallType type, ResponseType responseType){
		super(null);
		this.url = url;
		this.type = type;
		this.responseType = responseType;
	}
	
	public RESTAPICall(String url, CallType type, RESTAPICallContent content, ResponseType responseType) {
		super(content);
		this.url = url;
		this.type = type;
		this.responseType = responseType;
	}
	
	public String makeUrl() throws Exception {
		return url;
	}
	
	public APIResponse getResponseParser() {
		if (responseType == ResponseType.JSON) {
			return new RESTAPICallJSONResponse(this);
		}
		return null;
	}
	
	public APIResponse execute() throws Exception {
		if (type == CallType.GET) {
			byte[] response = SimpleHttpClient.requestGET(makeUrl());
			return getResponseParser().parseResponseMsg(this, response);
		} else if (type == CallType.POST) {
			byte[] bytesToSend = getMsg().getBytes();
			byte[] response = SimpleHttpClient.requestPOST(makeUrl(), bytesToSend);
			return getResponseParser().parseResponseMsg(this, response);
		}else if (type == CallType.PUT) {
			throw new UnsupportedOperationException();
		} else if (type == CallType.DELETE) {
			throw new UnsupportedOperationException();
		}
		return null;
	}

	
}
