package com.app.ipinle.util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.app.ipinle.base.C;

public class AppClient {

	// compress strategy
	final private static int CS_NONE = 0;
	final private static int CS_GZIP = 1;
		
	// logic variables
	private String apiUrl;
	private HttpParams httpParams;
	private HttpClient httpClient;
	private int timeoutConnection = 10000;
	private int timeoutSocket = 10000;
	private int compress = CS_NONE;
	
	// charset default utf8
	private String charset = HTTP.UTF_8;
	
	public AppClient(String url){
		initClient(url);
	}
	public AppClient(String url,String charset, int compress){
		initClient(url);
		this.charset = charset;
		this.compress = compress;
	}
	
	public void initClient(String url){
		// initialize API URL
		this.apiUrl = C.api.base + url;
//		String apiSid = AppUtil.getSessionId();
//		if(apiSid != null && apiSid.length()>0){
//			this.apiUrl += "?sid=" + apiSid;
//			Log.i("test sid", apiSid);
//		}else{
//			Log.i("test sid","no sid here");
//		}
		// set client timeout
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		// init client
		httpClient = new DefaultHttpClient(httpParams);
		
	}
	
	public String post (HashMap urlParams) throws Exception {
		try {

			HttpPost httpPost = new HttpPost(this.apiUrl);
			//Log.i("test url", httpPost.toString());
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			// get post parameters
			Iterator it = urlParams.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				postParams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
			}
			String apiSid = AppUtil.getSessionId();
			if(apiSid != null && apiSid.length()>0){
				postParams.add(new BasicNameValuePair("sid", apiSid));
				Log.i("test sid", apiSid);
			}else{
				Log.i("test sid","no sid here");
			}
			// set data charset
			if (this.charset != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams, this.charset));
			} else {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams));
			}
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String httpResult = EntityUtils.toString(httpResponse.getEntity());
				Log.i("debug", httpResult);
				return httpResult;
			} else {
				Log.i("debug", "nothing received!");
				return null;
			}
		} catch (ConnectTimeoutException e) {
			throw new Exception(C.err.network+"connect out of time");
		} catch(UnknownHostException e){
			throw new Exception(C.err.network+"unknown host");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
