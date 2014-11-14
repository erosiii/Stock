package com.iii.stockiii;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ParseException;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.iii.stockiii.adapter.ListStockAdapter;
import com.iii.stockiii.config.ConfigurationServer;
import com.iii.stockiii.config.ConfigurationWS;
import com.iii.stockiii.helper.MyShareprefer;
import com.iii.stockiii.helper.VoiceMangamentHelp;
import com.iii.stockiii.model.Stock;
import com.iii.stockiii.model.Voices;
import com.iii.stockiii.ws.WSCheckLogin;


public class MainActivity extends Activity implements OnClickListener{
	 private ListView lsvStock;
	 private Button btnStartService, btnStopService;
	 private Button btnOk, btnCancel, btnConfiguration;
	 private EditText txtName, txtPhone, txtEmail;
	 private CheckBox cbRemember;
	 private ArrayList<Stock> lstStock;
	 private ArrayList<Voices> lstVoices;
	 private ArrayList<String> lstSymbolChecked;
	 private ListStockAdapter adapter;
	 private Handler handler;
	 int index;
	 int top;
	 private MediaPlayer media;
	 private long time;
	 private String left_value;
	 private String right_value;
	 private String left_price;
	 private String right_price;
	 private String lsNow;
	 private DecimalFormat df1 = new DecimalFormat("##.#");
	 private Thread thread, thread2;
	 private MyShareprefer myShare;
	 private Dialog dl;
	 private boolean check;
	 private int showFrom =0;
	 private SimpleDateFormat sdf;
	 private Date date09, date11, date1330, date15, datenow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        policy();
        setUI();
        setData();
        if(TextUtils.isEmpty(myShare.getUerPhone().toString().trim()) && TextUtils.isEmpty(myShare.getUserName().toString().trim())){
        	showFrom = 0;
        	if(showFrom==0){
        		showDialogLogin();
        	}
        }else{
//        	new WSCheckLogin(MainActivity.this, myShare.getUerPhone().toString().trim(), myShare.getUserEmail().toString().trim(),
//        			check, dl, myShare, showFrom).execute();
        }
       // VoiceMangamentHelp.makeVoice(MainActivity.this, media, time, "giam", 3, "cham", "gia");
      
    }

	private void setUI() {
		lsvStock = (ListView)findViewById(R.id.lsvStock);
		btnStartService = (Button) findViewById(R.id.btnStartService);
		btnStopService =  (Button) findViewById(R.id.btnStopService);
		btnConfiguration = (Button) findViewById(R.id.btnConfig);
		
		lstStock = new ArrayList<Stock>();
		lstVoices = new ArrayList<Voices>();
		lstSymbolChecked = new ArrayList<String>();
		handler = new Handler();
		 myShare = new MyShareprefer(getApplicationContext(), MyShareprefer.GET_INFOUSER_ITEM); 
		
		btnStartService.setOnClickListener(this);
		btnStopService.setOnClickListener(this);
		btnConfiguration.setOnClickListener(this);
		
		Time now = new Time();
		now.setToNow();
		lsNow = now.format("%H:%M");
		String pattern = "HH:mm";
        sdf = new SimpleDateFormat(pattern);
	}
	
	private void setData() {
		index  = lsvStock.getFirstVisiblePosition();
		View v = lsvStock.getChildAt(0);
		top = (v == null) ? 0 : v.getTop();
		lsvStock.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		new WSGetDataDo(MainActivity.this).execute();
		
		final Runnable r = new Runnable(){

			@Override
			public void run() {
				index  = lsvStock.getFirstVisiblePosition();
				View v = lsvStock.getChildAt(0);
				top = (v == null) ? 0 : v.getTop();
				new WSGetDataDo(MainActivity.this).execute();
				//adapter.notifyDataSetChanged();
				
			}
			
		};
		
		final Runnable r2 = new Runnable(){

			@Override
			public void run() {

				new WSGetStockConfig(MainActivity.this).execute();		
			}
			
		};
		thread = new Thread()
		{
		    @Override
		    public void run() {
		        try {
		            while(true) {
		                sleep(2500);
		                handler.post(r);
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		};

		//thread.start();
		
		thread2 = new Thread()
		{
		    @Override
		    public void run() {
		        try {
		            while(true) {
		                sleep(3000);
		                handler.post(r2);
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		};

		//thread2.start();
		
		
	}
	
	// ================== Policy ===================//
		private void policy() {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
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
			protected void onPostExecute( ArrayList<Stock> result) {
				try {
					setAdapter();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			
	}
	
	public class WSGetStockConfig extends AsyncTask<Void, Void, ArrayList<Voices>> {
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
				if( lstVoices.size() > 0 ) lstVoices.clear();
				try {
					// ---------------get String ------------------------//
					String UrlGetDataDo = ConfigurationServer.getURLServer() + "wsgetStockConfig.php";
					JSONObject json = new JSONObject();
					
					JSONArray jarr = mWs.connectWSPut_Get_Data(UrlGetDataDo, json,
							"product");
					for (int i = 0; i < jarr.length(); i++) {
						JSONObject element = jarr.getJSONObject(i);
						Voices voices = new Voices();
						voices.setStrchange(element.getString("result_change"));
						voices.setDifferent_value(element.getDouble("difference_value"));
						voices.setPrice(element.getDouble("price"));
						voices.setInc_des(element.getInt("inc_des"));
						lstVoices.add(voices);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if( lstSymbolChecked.size() > 0 ) lstSymbolChecked.clear();
				try {
					// ---------------get String ------------------------//
					String UrlGetDataDo = ConfigurationServer.getURLServer() + "wsgetCheckedInterest.php";
					JSONObject json = new JSONObject();
					
					JSONArray jarr = mWs.connectWSPut_Get_Data(UrlGetDataDo, json,
							"product");
					for (int i = 0; i < jarr.length(); i++) {
						JSONObject element = jarr.getJSONObject(i);
						String symbol = element.getString("symbol");
						lstSymbolChecked.add(symbol);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return lstVoices;
			}

			@Override
			protected void onPostExecute( ArrayList<Voices> result) {
				try {
					for (Voices voices : lstVoices) {
						double difrrent_value = voices.getDifferent_value();
						double price  = voices.getPrice();
						String strdifrrent_value = String.valueOf(difrrent_value);
						String strprice = String.valueOf(price);
						String[] split_value = strdifrrent_value.split("\\.");
						 left_value  = split_value[0];
						 try {
							 right_value  = split_value[1];
						} catch (Exception e) {
							// TODO: handle exception
						}
						 
						
						String[] split_price = strprice.split("\\.");
						 left_price  = split_price[0];
						 right_price  = split_price[1];
						 try {
		                     date09 = sdf.parse("09:00");
		                     date11 = sdf.parse("11:00");
		                     date1330 = sdf.parse("13:30");
		                     date15 = sdf.parse("15:00");
		                     datenow = sdf.parse(lsNow);
		                     if(date09.before(datenow) && date11.after(datenow)){
		                    	 setVoice(voices);
		                     }else if(date1330.before(datenow) && date15.after(datenow)){
		                    	 setVoice(voices);
		                     }else{
		                     	Log.d(">>>>CHECK TIME", "Khong nam trong khoang thoi gian");
		                     }
		          

		                 }catch (java.text.ParseException e) {
		         			// TODO Auto-generated catch block
		         			e.printStackTrace();
		         		}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	private void setVoice(Voices voices){
		if(voices.getInc_des() == 0){
				if(lstSymbolChecked.contains(voices.getStrchange())){
					try {
						VoiceMangamentHelp.callVibrate(MainActivity.this, time);
						VoiceMangamentHelp.makeVoice(MainActivity.this, media, time, voices.getStrchange(),"giam", Integer.parseInt(left_value),
						 Integer.parseInt(right_value), Integer.parseInt(left_price), Integer.parseInt(right_price),
						"cham", "gia");
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					new WSUpdateFlag(MainActivity.this, voices.getStrchange()).execute();
				}
		}else{
				if(lstSymbolChecked.contains(voices.getStrchange())){
					try {
						VoiceMangamentHelp.callVibrate(MainActivity.this, time);
						VoiceMangamentHelp.makeVoice(MainActivity.this, media, time, voices.getStrchange(), "tang", Integer.parseInt(left_value),
						 Integer.parseInt(right_value), Integer.parseInt(left_price), Integer.parseInt(right_price),
						"cham", "gia");
					} catch (Exception e) {
						// TODO: handle exception
					}
					new WSUpdateFlag(MainActivity.this, voices.getStrchange()).execute();
				}
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

				String URLAddClient = ConfigurationServer.getURLServer() + "wsUpdateFlag.php";
				JSONObject json = new JSONObject();
				json.put("symbol", symbol);
				
				String jsonData = mWS.getDataJson(URLAddClient, json, "product");
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

	
	public void setAdapter(){
		adapter = new ListStockAdapter(MainActivity.this, R.layout.item_do_layout, lstStock);
		lsvStock.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		lsvStock.setSelectionFromTop(index, top);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartService:
			try {
				if((thread.getState() == Thread.State.NEW)){
					thread.start();
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				if((thread2.getState() == Thread.State.NEW)){
					thread2.start();
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			Toast.makeText(MainActivity.this, "Starting services to get the data", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btnStopService:
			try {
				if((thread.getState() == Thread.State.NEW)){
					thread.interrupt();
					thread = null;
				}
				if((thread2.getState() == Thread.State.NEW)){
					thread2.interrupt();
					thread2 = null;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			Toast.makeText(MainActivity.this, "Stoped services to get the data", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.btnConfig:
			startActivity(new Intent(MainActivity.this, ConfigurationActivity.class));
			break;

		default:
			break;
		}
		
	}
	
	public void showDialogLogin() {
		// khởi tạo dialog
		dl = new Dialog(this);
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
		
		final String phone = myShare.getUerPhone();
		final String email = myShare.getUserEmail();
		final String name = myShare.getUserName();
		
		if( !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) ){
			txtName.setText(name);
			txtPhone.setText(phone);
			txtEmail.setText(email);
			cbRemember.setChecked(true);
		}
		
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
			check = false;
			new WSCheckLogin(MainActivity.this, txtPhone.getText().toString().trim(), 
					txtEmail.getText().toString().trim(), check, dl, myShare, showFrom).execute();
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
		
		/** Xử lý sự kiện click vào checkbox lưu lại thông tin user đăng nhập */
		cbRemember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ){
					myShare.setUserEmail( txtEmail.getText().toString().trim() );
					myShare.setUserName( txtName.getText().toString().trim() );
					myShare.setUserPhone( txtPhone.getText().toString().trim() );
				}else{
					myShare.logout();
				}
			}
		});
		dl.show();
		dl.setCancelable(false);
	}

}
