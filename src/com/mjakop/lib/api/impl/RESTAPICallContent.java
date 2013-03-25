package com.mjakop.lib.api.impl;

import com.mjakop.lib.api.APICallMessage;

public class RESTAPICallContent extends APICallMessage {

	private byte[] content;
	
	public RESTAPICallContent(byte[] content) {
		this.content = content;
	}
	
	@Override
	public byte[] getBytes() throws Exception {
		return content;
	}

}
