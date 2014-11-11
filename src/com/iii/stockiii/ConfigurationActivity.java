package com.iii.stockiii;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.iii.stockiii.adapter.ListSymbolAdapter;
import com.iii.stockiii.config.ConfigurationServer;
import com.iii.stockiii.config.ConfigurationWS;
import com.iii.stockiii.model.Symbol;

public class ConfigurationActivity extends ActionBarActivity implements
		OnItemClickListener, OnClickListener {

	private ListView lvconfig;
	private ProgressDialog mProgress;
	private ArrayAdapter<Symbol> adapter;
	private List<Symbol> listsymbol;
	private Button btnOK, btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration_layout);
		SetUI();
		SetData();
	}

	private void SetUI() {
		lvconfig = (ListView) findViewById(R.id.lvconfig);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		lvconfig.setItemsCanFocus(true);
		lvconfig.setOnItemClickListener(this);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	private void SetData() {
		mProgress = new ProgressDialog(ConfigurationActivity.this);
		mProgress.show();
		new WSGetDataDo(this).execute();
	}

	//when click on the listview
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Symbol sb = (Symbol) parent.getItemAtPosition(position);
		if (sb.isSelected())
			sb.setSelected(false);
		else {
			sb.setSelected(true);
		}
		listsymbol.set(position, sb);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// update data in listview to database
	private void Updatedatabase() {
		String json = Converttojson().toString();
		HttpClient httpclient = new DefaultHttpClient();
		String URL = ConfigurationServer.getURLServer() + "wsUpdateStockInterest.php";
		try {
			HttpPost httppost = new HttpPost(URL);

			List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
			nvp.add(new BasicNameValuePair("json", json));
			// httppost.setHeader("Content-type", "application/json");
			httppost.setEntity(new UrlEncodedFormEntity(nvp));
			HttpResponse response = httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String stringresponse = httpclient.execute(httppost,
					responseHandler);
			Log.e("dsfd", stringresponse);

			if (response != null) {
				InputStream is = response.getEntity().getContent();
				Log.e("sdfd", is.toString());
				finish();
			}

		} catch (Exception e) {
			// e.printStackTrace();
			Log.e("asdas", e.toString());
		}
	}

	//get data json to listview
	private JSONObject Converttojson() {
		JSONObject object = new JSONObject();
		int flag;
		for (int i = 0; i < listsymbol.size(); i++) {
			try {
				flag = 0;
				if (listsymbol.get(i).isSelected())
					flag = 1;
				object.put(listsymbol.get(i).getCode().toString(),
						String.valueOf(flag));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return object;
	}

	public class WSGetDataDo extends AsyncTask<Void, Void, Void> {
		// ------------------default floor1-----------------------------//
		private ConfigurationWS mWs;

		private Context context;

		public WSGetDataDo(Context mContext) {
			super();
			context = mContext;
			mWs = new ConfigurationWS(mContext);
		}

		@Override
		protected Void doInBackground(Void... params) {
			listsymbol = new ArrayList<Symbol>();
			if (listsymbol.size() > 0)
				listsymbol.clear();
			try {
				// ---------------get String ------------------------//
				String UrlGetDataDo = ConfigurationServer.getURLServer()
						+ "wsgetsymbol.php";
				JSONObject json = new JSONObject();
				boolean flag;
				JSONArray jarr = mWs.connectWSPut_Get_Data(UrlGetDataDo, json,
						"product");
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject element = jarr.getJSONObject(i);
					Symbol symbol = new Symbol();
					symbol.setCode(element.getString("symbol"));

					flag = false;
					if (Integer.parseInt(element.getString("checked")) == 1)
						flag = true;
					symbol.setSelected(flag);
					listsymbol.add(symbol);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setAdapter();
		}

		private void setAdapter() {
			mProgress.dismiss();
			adapter = new ListSymbolAdapter(context,
					R.layout.item_configution_layout, listsymbol);
			lvconfig.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOK:
			Updatedatabase();
			break;
		case R.id.btnCancel:
			ConfigurationActivity.this.finish();
			break;

		default:
			break;
		}
		
	}

}
