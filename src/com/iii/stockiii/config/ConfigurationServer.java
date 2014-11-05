package com.iii.stockiii.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConfigurationServer {

	private static final String FILENAME = "URLFile.txt";

	// ---------------fields------------------------------//
	private static String URLServer = "http://117.6.131.222:6789/pos/wspos/STOCK/";
//	private static String URLServer = "http://117.6.131.222:88/pos/wspos/";
//	private static String URLServer = "http://192.168.1.154/pos/wspos/";
	// private static String URLServer = "http://tamminhfurniture.com/wspos/";
	 //private static String URLServer = "http://117.6.131.222:8090/pos/wspos/";
	/*----------------language----------------------------*/

	private static String Lng = "";

	public static int body = 0;

	public static int status = 0;
	public static String table_code = "";
	public static int itable_id = 0;
	public static int user_id = 0;
	public static int language_code = 2;
	public static int floor = -1;
	private Context context;

	public ConfigurationServer(Context context) {
		this.context = context;
	}

	public void writeToFile(String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (Exception e) {
			Log.e("Error", "File write failed: " + e.toString());
		}
	}

	public String getURLServerFromFile() {
		String ret = "";
		try {
			try {
				InputStream inputStream = context.openFileInput(FILENAME);
				if (inputStream != null) {
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String receiveString = "";
					StringBuilder stringBuilder = new StringBuilder();

					while ((receiveString = bufferedReader.readLine()) != null) {
						stringBuilder.append(receiveString);
					}
					inputStream.close();
					ret = stringBuilder.toString();
				}
			} catch (FileNotFoundException e) {
				Log.e("Error", "File not found: " + e.toString());
			} catch (IOException e) {
				Log.e("Error", "Can not read file: " + e.toString());
			}
		} catch (Exception e) {
		}
		if (ret.equals(""))
			//ret = "http://117.6.131.222:8090/POS/wspos/";
			ret = "http://117.6.131.222:6789/pos/wspos/STOCK";
		return ret;
	}

	public static String getURLServer() {
		return URLServer;
	}

	public static void setURLServer(String uRLServer) {
		URLServer = uRLServer;
	}

	public String getLng() {
		return Lng;
	}

	public void setLng(String lng) {
		Lng = lng;
	}

	// ---------check the network available-------------------//
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	
}


