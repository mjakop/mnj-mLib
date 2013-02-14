package com.mjakop.lib.db;

import com.mjakop.lib.utils.SimpleCrypto;

public class EncryptedStringFieldVal {

	private String value;
	private String key;
	private byte[] bData;
	
	public EncryptedStringFieldVal(String value, String key) {
		this.value = value;
		this.key = key;
	}
	
	public EncryptedStringFieldVal(byte[] data) {
		this.bData = data;
	}
	
	public String decrypt(String key) throws Exception {
		bData = SimpleCrypto.decryptByte(key, bData);
		return new String(bData, "UTF-8");
	}
	
	public byte[] encrypt() {
		bData = value.getBytes();
		try {
			bData = SimpleCrypto.encryptByte(key, bData);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bData;
	}
	
	
	public String getValue() {
		return value;
	}
	
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return "Call decrypt!";
	}
	
}
