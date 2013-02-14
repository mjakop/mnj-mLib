package com.mjakop.lib.db;

public class TableSchemaEncryptedStringField extends TableSchemaField {
	
	public enum EncryptionType {AES};
	
	private EncryptionType encType;
	
	public TableSchemaEncryptedStringField(String name) {
		super(name, Type.ENCRYPTED_STRING);
		this.encType = EncryptionType.AES;
	}
	
	public TableSchemaEncryptedStringField(String name, EncryptionType encType) {
		super(name, Type.ENCRYPTED_STRING);
		this.encType = encType;
	}
	
	public EncryptionType getEncType() {
		return encType;
	}
	
	
	
}
