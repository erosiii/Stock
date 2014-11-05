package com.iii.stockiii.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

public class HttpUrlConfigurationWS {

	int TIMEOUT_MILLISEC = 10000;
	Context context;
	JSONArray jarr = null;

	public HttpUrlConfigurationWS(Context context) {
		this.context = context;
	}

	public JSONArray connectWSPut_Get_Data(String url, JSONObject json,
			String jsonName) {

		try {
			byte[] bytes = json.toString().getBytes("UTF-8");
			HttpURLConnection conn = null;
			URL url1 = new URL(url);
			conn = (HttpURLConnection) url1.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			conn.connect();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			} else {
				InputStream is = conn.getInputStream();
				String a = ConfigurationWSRestClient.convertStreamToString(is);
				JSONObject obj = new JSONObject(a);
				jarr = obj.getJSONArray(jsonName);
			}
		} catch (Exception t) {
		}
		return jarr;
		/*
		 * HttpParams httpParams = new BasicHttpParams();
		 * HttpConnectionParams.setConnectionTimeout(httpParams,
		 * TIMEOUT_MILLISEC); HttpConnectionParams.setSoTimeout(httpParams,
		 * TIMEOUT_MILLISEC); HttpClient client = new
		 * DefaultHttpClient(httpParams);
		 * 
		 * HttpPost request = new HttpPost(url); request.setEntity(new
		 * ByteArrayEntity(json.toString().getBytes( "UTF-8")));
		 * request.setHeader("json", json.toString()); HttpResponse response =
		 * client.execute(request); HttpEntity entity = response.getEntity();
		 * 
		 * InputStream instream = entity.getContent();
		 * 
		 * String result = ConfigurationWSRestClient
		 * .convertStreamToString(instream); JSONObject jobj = new
		 * JSONObject(result); jarr = jobj.getJSONArray(jsonName);
		 * 
		 * } catch (Exception t) { } return jarr;
		 */
	}

	public JSONArray connectWS_Get_Data(String url, String jsonName) {
		JSONArray jarr = null;
		try {

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient client = new DefaultHttpClient(httpParams);

			HttpPost request = new HttpPost(url);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			InputStream instream = entity.getContent();

			String result = ConfigurationWSRestClient
					.convertStreamToString(instream);
			JSONObject jobj = new JSONObject(result);
			jarr = jobj.getJSONArray(jsonName);

		} catch (Exception t) {
		}
		return jarr;
	}

	public void connectWS_Put_Data(String url, JSONObject json) {
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost request = new HttpPost(url);
			request.setEntity(new ByteArrayEntity(json.toString().getBytes(
					"UTF8")));
			request.setHeader("json", json.toString());
			client.execute(request);

		} catch (Exception t) {
		}
	}

}
