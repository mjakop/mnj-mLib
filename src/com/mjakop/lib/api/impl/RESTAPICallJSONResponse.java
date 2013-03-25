package com.mjakop.lib.api.impl;

import org.json.JSONObject;

import com.mjakop.lib.api.APICall;
import com.mjakop.lib.api.APIResponse;

public class RESTAPICallJSONResponse extends APIResponse {

	private JSONObject data;
	
	public RESTAPICallJSONResponse(RESTAPICall call) {
		super(call);
	}
	
	public RESTAPICallJSONResponse(RESTAPICall call, JSONObject data) {
		super(call);
		this.data = data;
	}
	
	public JSONObject getData() {
		return data;
	}
	
	@Override
	public APIResponse parseResponseMsg(APICall call, byte[] responseMsg) throws Exception {
		JSONObject obj = new JSONObject(new String(responseMsg, "utf-8"));
		return new RESTAPICallJSONResponse((RESTAPICall)call, obj);
	}
	
}
