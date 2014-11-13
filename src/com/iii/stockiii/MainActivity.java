package com.iii.stockiii;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.iii.stockiii.adapter.ListStockAdapter;
import com.iii.stockiii.config.ConfigurationServer;
import com.iii.stockiii.config.ConfigurationWS;
import com.iii.stockiii.helper.VoiceMangamentHelp;
import com.iii.stockiii.model.Stock;
import com.iii.stockiii.model.Voices;

public class MainActivity extends Activity implements OnClickListener {
	private ListView lsvStock;
	private Button btnStartService, btnStopService;
	private Button btnOk, btnCancel, btnConfiguration;
	private EditText txtName, txtPhone, txtEmail;
	private CheckBox cbRemember;
	private ArrayList<Stock> lstStock;
	private ArrayList<Voices> lstVoices;
	private ListStockAdapter adapter;
	private Handler handler;
	private int index;
	private int top;
	private MediaPlayer media;
	private long time;
	private String left_value;
	private String right_value;
	private String left_price;
	private String right_price;
	private Thread thread, thread2;
	private SharedPreferences isshare;
	private Dialog dl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		policy();
		setUI();
		setData();
		isshare = getSharedPreferences("saveinfo", this.MODE_PRIVATE);
		if (TextUtils.isEmpty(isshare.getString("username", null))
				&& TextUtils.isEmpty(isshare.getString("phone", null))
				&& TextUtils.isEmpty(isshare.getString("email", null)))
			showDialogLogin();
	}

	private void setUI() {
		lsvStock = (ListView) findViewById(R.id.lsvStock);
		btnStartService = (Button) findViewById(R.id.btnStartService);
		btnStopService = (Button) findViewById(R.id.btnStopService);
		btnConfiguration = (Button) findViewById(R.id.btnConfig);

		lstStock = new ArrayList<Stock>();
		lstVoices = new ArrayList<Voices>();
		handler = new Handler();

		btnStartService.setOnClickListener(this);
		btnStopService.setOnClickListener(this);
		btnConfiguration.setOnClickListener(this);

	}

	private void setData() {
		index = lsvStock.getFirstVisiblePosition();
		View v = lsvStock.getChildAt(0);
		top = (v == null) ? 0 : v.getTop();
		new WSGetDataDo(MainActivity.this).execute();

		final Runnable r = new Runnable() {

			@Override
			public void run() {
				index = lsvStock.getFirstVisiblePosition();
				View v = lsvStock.getChildAt(0);
				top = (v == null) ? 0 : v.getTop();
				new WSGetDataDo(MainActivity.this).execute();
				// adapter.notifyDataSetChanged();

			}

		};

		final Runnable r2 = new Runnable() {

			@Override
			public void run() {

				new WSGetStockConfig(MainActivity.this).execute();
			}

		};
		thread = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						sleep(2000);
						handler.post(r);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		// thread.start();

		thread2 = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						sleep(3000);
						handler.post(r2);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		// thread2.start();

	}

	// ================== Policy ===================//
	private void policy() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	public class WSGetDataDo extends AsyncTask<Void, Void, ArrayList<Stock>> {
		// ------------------default floor1-----------------------------//
		private ConfigurationWS mWs;
		private ProgressDialog mProgress;
		private Context context;

		public WSGetDataDo(Context mContext) {
			super();
			context = mContext;
			mWs = new ConfigurationWS(mContext);
			mProgress = new ProgressDialog(mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Stock> doInBackground(Void... params) {
			if (lstStock.size() > 0)
				lstStock.clear();
			try {
				// ---------------get String ------------------------//
				String UrlGetDataDo = ConfigurationServer.getURLServer()
						+ "wsgetchangestock.php";
				JSONObject json = new JSONObject();

				JSONArray jarr = mWs.connectWSPut_Get_Data(UrlGetDataDo, json,
						"product");
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject element = jarr.getJSONObject(i);
					Stock stock = new Stock();
					stock.setChange(element.getString("symbol"));
					stock.setResult_pri(element.getString("result_pri"));
					stock.setResult_vol(element.getString("result_vol"));
					stock.setResult_total_vol(element.getString("result_total"));
					stock.setResult_pri_old(element.getString("result_pri_old"));
					stock.setRef(element.getString("ref"));
					lstStock.add(stock);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lstStock;
		}

		@Override
		protected void onPostExecute(ArrayList<Stock> result) {
			try {
				setAdapter();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		public void setAdapter() {
			adapter = new ListStockAdapter(MainActivity.this,
					R.layout.item_do_layout, lstStock);
			lsvStock.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			lsvStock.setSelectionFromTop(index, top);
		}

	}

	public class WSGetStockConfig extends
			AsyncTask<Void, Void, ArrayList<Voices>> {
		// ------------------default floor1-----------------------------//
		private ConfigurationWS mWs;
		private ProgressDialog mProgress;
		private Context context;

		public WSGetStockConfig(Context mContext) {
			super();
			context = mContext;
			mWs = new ConfigurationWS(mContext);
			mProgress = new ProgressDialog(mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Voices> doInBackground(Void... params) {
			if (lstVoices.size() > 0)
				lstVoices.clear();
			try {
				// ---------------get String ------------------------//
				String UrlGetDataDo = ConfigurationServer.getURLServer()
						+ "wsgetStockConfig.php";
				JSONObject json = new JSONObject();

				JSONArray jarr = mWs.connectWSPut_Get_Data(UrlGetDataDo, json,
						"product");
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject element = jarr.getJSONObject(i);
					Voices voices = new Voices();
					voices.setStrchange(element.getString("result_change"));
					voices.setDifferent_value(element
							.getDouble("difference_value"));
					voices.setPrice(element.getDouble("price"));
					voices.setInc_des(element.getInt("inc_des"));
					lstVoices.add(voices);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lstVoices;
		}

		@Override
		protected void onPostExecute(ArrayList<Voices> result) {
			try {
				for (Voices voices : lstVoices) {
					double difrrent_value = voices.getDifferent_value();
					double price = voices.getPrice();
					String strdifrrent_value = String.valueOf(difrrent_value);
					String strprice = String.valueOf(price);
					String[] split_value = strdifrrent_value.split("\\.");
					left_value = split_value[0];
					try {
						right_value = split_value[1];
					} catch (Exception e) {
						// TODO: handle exception
					}
					String[] split_price = strprice.split("\\.");
					left_price = split_price[0];
					right_price = split_price[1];
					Time now = new Time();
					now.setToNow();
					String lsNow = now.format("%H:%M");
					if (lsNow.equals("09:00")) {
						setVoice(voices);
					} else if (lsNow.equals("11:30")) {
						setVoice(voices);
					} else if (lsNow.equals("13:00")) {
						setVoice(voices);
					} else if (lsNow.equals("17:20")) {
						setVoice(voices);
					} else if (lsNow.equals("10:56")) {
						setVoice(voices);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void setVoice(Voices voices) {
		if (voices.getInc_des() == 0) {
			VoiceMangamentHelp.callVibrate(MainActivity.this, time);
			VoiceMangamentHelp.makeVoice(MainActivity.this, media, time,
					voices.getStrchange(), "giam",
					Integer.parseInt(left_value),
					Integer.parseInt(right_value),
					Integer.parseInt(left_price),
					Integer.parseInt(right_price), "cham", "gia");
			new WSUpdateFlag(MainActivity.this, voices.getStrchange())
					.execute();
		} else {
			VoiceMangamentHelp.callVibrate(MainActivity.this, time);
			VoiceMangamentHelp.makeVoice(MainActivity.this, media, time,
					voices.getStrchange(), "tang",
					Integer.parseInt(left_value),
					Integer.parseInt(right_value),
					Integer.parseInt(left_price),
					Integer.parseInt(right_price), "cham", "gia");
			new WSUpdateFlag(MainActivity.this, voices.getStrchange())
					.execute();
		}
	}

	public class WSUpdateFlag extends AsyncTask<Void, Void, Void> {
		private ConfigurationWS mWS;
		private ProgressDialog dialog;
		private Context mContext;
		private String symbol;

		// --------constructor-----------------------------//
		public WSUpdateFlag(Context mContext, String symbol) {
			this.mContext = mContext;
			mWS = new ConfigurationWS(mContext);
			this.symbol = symbol;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// --------background method-------------------//
		@Override
		protected synchronized Void doInBackground(Void... params) {
			try {

				String URLAddClient = ConfigurationServer.getURLServer()
						+ "wsUpdateFlag.php";
				JSONObject json = new JSONObject();
				json.put("symbol", symbol);

				String jsonData = mWS
						.getDataJson(URLAddClient, json, "product");
				JSONObject jsonObject = new JSONObject(jsonData);
				String message = jsonObject.getString("message");
				Log.i("Log : ", "" + message);
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartService:
			if (!thread.isAlive()) {
				thread.start();
			}
			if (thread2.isAlive()) {
				thread2.start();
			}
			Toast.makeText(MainActivity.this,
					"Starting services to get the data", Toast.LENGTH_LONG)
					.show();
			break;

		case R.id.btnStopService:
			if (!thread.isAlive()) {
				thread.interrupt();
			}
			if (thread2.isAlive()) {
				thread2.interrupt();
			}
			Toast.makeText(MainActivity.this,
					"Stoped services to get the data", Toast.LENGTH_LONG)
					.show();
			break;

		case R.id.btnConfig:
			startActivity(new Intent(MainActivity.this,
					ConfigurationActivity.class));
			break;

		default:
			break;
		}

	}

	public void showDialogLogin() {
		// khởi tạo dialog
		dl = new Dialog(this);
		dl.setCanceledOnTouchOutside(false);
		// set loại giao diện hay còn gọi là theme: cái này là ko có title
		// dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dl.setTitle("Viet Stock Tracking");
		// set layout
		dl.setContentView(R.layout.login_dialog);

		// muốn gọi 1 view trong dialog phải gọi qua dl, ví dụ:

		// rồi làm gì nó thì làm
		// cuối cùng là show dialog

		btnOk = (Button) dl.findViewById(R.id.btnLogin);
		btnCancel = (Button) dl.findViewById(R.id.btnCancel);
		txtName = (EditText) dl.findViewById(R.id.txtName);
		txtPhone = (EditText) dl.findViewById(R.id.txtPhone);
		txtEmail = (EditText) dl.findViewById(R.id.txtEmail);
		cbRemember = (CheckBox) dl.findViewById(R.id.cbSaveUser);

		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (CheckLogin(txtName.getText().toString(), txtPhone.getText()
						.toString(), txtEmail.getText().toString())) {

					if (cbRemember.isChecked()) {
						isshare = getSharedPreferences("saveinfo",
								MainActivity.this.MODE_PRIVATE);
						Editor editor = isshare.edit();
						editor.putString("username", txtName.getText()
								.toString());
						editor.putString("phone", txtPhone.getText().toString());
						editor.putString("email", txtEmail.getText().toString());
						editor.commit();
					}
					dl.dismiss();
				}

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dl.dismiss();
				MainActivity.this.finish();
			}
		});

		dl.show();
	}

	private boolean CheckLogin(String name, String phone, String email) {
		try {

			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ConfigurationServer.getURLServer()
					+ "wsGetUser.php");

			// add your data
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("username", name.trim()));
			nameValuePairs.add(new BasicNameValuePair("phone", phone.trim()));
			nameValuePairs.add(new BasicNameValuePair("email", email.trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String stringresponse = httpclient.execute(httppost,
					responseHandler);
			System.out.println("Response : " + response);

			if (stringresponse.equalsIgnoreCase("1")) {
				return true;
			} else {
				showAlert();
			}

		} catch (Exception e) {
			dl.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
		return false;
	}

	private void showAlert() {
		MainActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("Login Error.");
				builder.setMessage("User not Found.")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

}
