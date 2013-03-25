package com.mjakop.lib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.mjakop.lib.api.ExceptionServerNotRunning;

public class SimpleHttpClient {
	
	private static byte[] readContentFromInputStream(InputStream is) throws Exception {
		BufferedInputStream bus = new BufferedInputStream(is);
		byte[] buff = new byte[8192];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while(true){
			int n = bus.read(buff);
			if (n == -1){
				break;
			}
			baos.write(buff, 0, n);
		}
		return baos.toByteArray();
	}
	
	/*public static byte[] requestPOST(String url, byte[] postBody) throws Exception {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
		HttpConnectionParams.setSoTimeout(httpParameters, 30000);
		HttpClient client = new DefaultHttpClient(httpParameters);
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new ByteArrayEntity(postBody));
			HttpResponse response = client.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream ips = response.getEntity().getContent();
				return readContentFromInputStream(ips);
			} else {
				throw new ExceptionServerNotRunning();
			}
		} catch (Exception e) {
			throw e;
		}
	}*/
	
	public static byte[] requestPOST(String url, byte[] postBody) throws Exception {
		System.setProperty("http.keepAlive", "false"); //to prevent EOFException
		int tries = 3; //number of retries in case of exception
		while (tries > 0) {
			tries--;
			try {
				URL u = new URL(url);
				HttpURLConnection urlConnection = (HttpURLConnection)u.openConnection();
				try {
		
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					urlConnection.setUseCaches(false);
					urlConnection.setConnectTimeout(5000);
					urlConnection.setReadTimeout(7000);
					urlConnection.setFixedLengthStreamingMode(postBody.length);
					
					urlConnection.connect();
					
					BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
					out.write(postBody);
					out.flush();
					out.close();
					
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					if (urlConnection.getResponseCode() == 200) {
						return readContentFromInputStream(in);
					} else {
						throw new ExceptionServerNotRunning();
					}
					
				}catch (Exception e) {
					throw e;
				} finally {
					urlConnection.disconnect();
				}
			}catch (Exception e) {
				if (tries == 0) {
					throw e;
				}
			}
		}
		return new byte[0];
	}
	
	public static byte[] requestGET(String url) throws Exception {
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			conn.setInstanceFollowRedirects(true);
			conn.setDoInput(true);
			conn.connect();
			//if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			byte[] data = readContentFromInputStream(is);
			return data;
			//} else {
			//	throw new ExceptionServerNotRunning();
			//}
		} catch (Exception e) {
			throw e;
		}
	}

	public static byte[] requestPUT(String url, byte[] content) throws Exception {
		throw new MethodNotSupportedException("Still have to be developed.");
	}
	
	public static byte[] requestDELETE(String url, byte[] content) throws Exception {
		throw new MethodNotSupportedException("Still have to be developed.");
	}
}
