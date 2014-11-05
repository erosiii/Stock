package com.iii.stockiii.config; 

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ConfigurationWS {

	int TIMEOUT_MILLISEC = 10000;
	Context context;

	public ConfigurationWS(Context context) {
		this.context = context;
	}

	public JSONArray connectWSPut_Get_Data(String url, JSONObject json, String jsonName) {
		JSONArray jarr = null;
		try {

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost request = new HttpPost(url);
			// cach khac de thay vao entity __________________________________
			StringEntity se = new StringEntity(json.toString(), "UTF-8");
			InputStreamEntity ise = new InputStreamEntity(new ByteArrayInputStream(json.toString().getBytes(
					"UTF-8")), -1);
			//---------------------------------------------------------------------
			request.setEntity(se);
		 	request.setHeader("json", json.toString());
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			InputStream instream = entity.getContent();

			String result = ConfigurationWSRestClient.convertStreamToString(instream);
			JSONObject jobj = new JSONObject(result);
			jarr = jobj.getJSONArray(jsonName);

		} catch (Exception t) {
		}
		return jarr;
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
	
	/**
	 * 
	 * @param url
	 * @param json
	 * @param jsonName
	 * @return
	 * 
	 * lay du lieu kieu jsonObject ve
	 */
	public String getDataJson(String url, JSONObject json, String jsonName){
		String result = null;
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost request = new HttpPost(url);
			// cach khac de thay vao entity __________________________________
			StringEntity se = new StringEntity(json.toString(), "UTF-8");
			InputStreamEntity ise = new InputStreamEntity(new ByteArrayInputStream(json.toString().getBytes("UTF-8")), -1);
			//---------------------------------------------------------------------
			request.setEntity(se);
			//Log.d("fuck", "fuck");
		 	request.setHeader("json", json.toString());
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream instream = entity.getContent();
			result = ConfigurationWSRestClient.convertStreamToString(instream);
		} catch (Exception t) {
		}
		return result;
	}

	public void connectWS_Put_Data(String url, JSONObject json) {
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost request = new HttpPost(url);
			request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
			request.setHeader("json", json.toString());
			client.execute(request);
		} catch (Exception t) {
		}
	}

}
