package com.iii.stockiii;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import com.iii.stockiii.adapter.ListStockAdapter;
import com.iii.stockiii.config.ConfigurationServer;
import com.iii.stockiii.config.ConfigurationWS;
import com.iii.stockiii.model.Stock;


public class MainActivity extends ActionBarActivity {
	private ListView lsvStock;
	private ArrayList<Stock> lstStock;
	private ListStockAdapter adapter;
	private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
        setData();
    }

	private void setUI() {
		lsvStock = (ListView)findViewById(R.id.lsvStock);
		
		lstStock = new ArrayList<Stock>();
		handler = new Handler();
		
	}
	
	private void setData() {
		new WSGetDataDo(MainActivity.this, lstStock).execute();
		final Runnable r = new Runnable(){

			@Override
			public void run() {
				new WSGetDataDo(MainActivity.this, lstStock).execute();
				
			}
			
		};
		/*Thread thread = new Thread()
		{
		    @Override
		    public void run() {
		        try {
		            while(true) {
		                sleep(1000);
		                handler.post(r);
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		};

		thread.start();*/
		
		
	}
	
	public class WSGetDataDo extends AsyncTask<Void, Void, ArrayList<Stock>> {
		// ------------------default floor1-----------------------------//
			private ConfigurationWS mWs;
			private ProgressDialog mProgress;
			private ArrayList<Stock> lstStock ;
			private Context context;
			
			public WSGetDataDo(Context mContext,  ArrayList<Stock> lstStock) {
				super();
				context = mContext;
				mWs = new ConfigurationWS(mContext);
				mProgress = new ProgressDialog(mContext);
				this.lstStock = lstStock;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected ArrayList<Stock> doInBackground(Void... params) {
				if( lstStock.size() > 0 ) lstStock.clear();
				try {
					// ---------------get String ------------------------//
					String UrlGetDataDo = ConfigurationServer.getURLServer() + "wsgetchangestock.php";
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
						lstStock.add(stock);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return lstStock;
			}

			@Override
			protected void onPostExecute( ArrayList<Stock> result) {
				setAdapter();
			}
			
			private void setAdapter(){
				adapter = new ListStockAdapter(context, R.layout.item_do_layout, lstStock);
				lsvStock.setAdapter(adapter);
			}
	}

}
