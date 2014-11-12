package com.iii.stockiii.ws;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.bool;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iii.stockiii.MainActivity;
import com.iii.stockiii.R;
import com.iii.stockiii.config.ConfigurationServer;
import com.iii.stockiii.config.ConfigurationWS;
import com.iii.stockiii.helper.MyShareprefer;

public class WSCheckLogin extends AsyncTask<Void, Void, Boolean> {

	// ------------------default floor1-----------------------------//
	private ConfigurationWS mWs;
	private ProgressDialog mProgress;
	private Context context;
	private String phone, email;
	private boolean checkAcc = false;
	private Dialog dl;
	private MyShareprefer myShare;
	private int showForm;

	public WSCheckLogin(Context mContext, String phone, String email,
			boolean checkAcc, Dialog dl, MyShareprefer myShare, int showForm) {
		super();
		context = mContext;
		mWs = new ConfigurationWS(mContext);
		mProgress = new ProgressDialog(mContext);
		this.phone = phone;
		this.email = email;
		this.checkAcc = checkAcc;
		this.dl = dl;
		this.myShare = myShare;
		this.showForm = showForm;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgress
				.setMessage(context.getResources().getString(R.string.loading));
		mProgress.setCancelable(false);
		mProgress.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		String messgae = "fail";
		boolean check = false;
		try {
			// ---------------get String ------------------------//
			String UrlGetDataDo = ConfigurationServer.getURLServer()
					+ "wsGetUser.php";
			JSONObject json = new JSONObject();
			json.put("phone", this.phone);
			json.put("email", this.email);

			JSONArray jarr = mWs.connectWSPut_Get_Data(UrlGetDataDo, json,
					"posts");

			for (int i = 0; i < jarr.length(); i++) {
				JSONObject element = jarr.getJSONObject(i);
				messgae = element.getString("result");
			}
		} catch (Exception e) {
		}

		if (messgae.equals("success")) {
			checkAcc = true;
			showForm = 1;
		}

		return checkAcc;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (mProgress.isShowing())
			mProgress.dismiss();
		checkAcc = result;
		if (!checkAcc) {
			showForm = 0;
			Log.d("AAA", "This account not exist!");
			Toast.makeText(context, "This account not exist!", 1).show();
		} else {
			dl.dismiss();
		}

	}

}
