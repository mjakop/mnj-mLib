package com.mjakop.lib.api;

public class APIResponseError extends APIResponse {

	private Exception exception;
	
	public APIResponseError(APICall call, Exception ex) {
		super(call);
		ex.printStackTrace();
		System.out.println("Exception: "+ex);
		this.exception = ex;
	}
	
	public Exception getException() {
		return exception;
	}

	@Override
	public APIResponse parseResponseMsg(APICall call, byte[] responseMsg){
		return null;
	}
}
