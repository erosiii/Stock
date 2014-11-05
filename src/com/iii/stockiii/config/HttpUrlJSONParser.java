package com.iii.stockiii.config;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpUrlJSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	private HttpURLConnection conn;

	public HttpUrlJSONParser() {

	}

	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {
		try {
			URL urlPath = null;
			String paramString = URLEncodedUtils.format(params, "utf-8");

			if (method == "GET") {
				urlPath = new URL(url + "?" + paramString);
			}
			if (method == "POST") {
				urlPath = new URL(url);
			}
			conn = (HttpURLConnection) urlPath.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (method == "GET") {
				conn.setRequestMethod("GET");
			}
			if (method == "POST") {
				conn.setRequestMethod("POST");

			}
			// send the data to the server using post
			OutputStream dos = conn.getOutputStream();
			dos.write(paramString.getBytes("UTF-8"));
			dos.close();
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			is = conn.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}
