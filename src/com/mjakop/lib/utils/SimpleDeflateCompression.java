package com.mjakop.lib.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class SimpleDeflateCompression {

	public static byte[] compress(byte[] bytes) throws IOException {

		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
		deflater.setInput(bytes);
		deflater.finish();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		while (!deflater.finished()) {
			int byteCount = deflater.deflate(buf);
			baos.write(buf, 0, byteCount);
	    }
		deflater.end();
		return baos.toByteArray();
	}

	public static byte[] decompress(byte[] compressed) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(compressed, 0, compressed.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
	    while(!inflater.finished()){
	    	int byteCount = inflater.inflate(buf);
	    	baos.write(buf, 0, byteCount);
	    }
	    inflater.end();
	    return baos.toByteArray();
	}
	
}
