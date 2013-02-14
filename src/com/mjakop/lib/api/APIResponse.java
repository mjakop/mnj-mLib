package com.mjakop.lib.api;


public abstract class APIResponse {

	
	private APICall apiCall;
	
	public APIResponse(APICall call) {
		this.apiCall = call;
	}
	
	public APICall getApiCall() {
		return apiCall;
	}
	
	public static APIResponse createDummyInstance(APICall call){
		return new APIResponse(call) {
			@Override
			public APIResponse parseResponseMsg(APICall call, byte[] responseMsg) throws Exception {
				return null;
			}
		};
	}
	
	public abstract APIResponse parseResponseMsg(APICall call, byte[] responseMsg) throws Exception;

}
