package com.iii.stockiii.helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class VoiceMangamentHelp {
	
	public static void makeVoice(Context mContext, MediaPlayer media, long time, String symbol,  String asc_desc,  int number_value_left,
			int number_value_right, int number_price_left, int number_price_right,
			String dot, String price) {
		
		callSymBold(mContext, media, symbol, time);
		callDescAsc(mContext, media, time, asc_desc);
		if(number_value_left == 0){
			callNumber(mContext, media, time, 101);
		}else{ callNumber(mContext, media, time, number_value_left);}
		
		if(number_value_right != 0){
			callDot(mContext, media, dot, time);
			callNumber(mContext, media, time, number_value_right);
		}
		
		callPrice(mContext, media, time, price);
		
		if(number_price_left == 0){
			callNumber(mContext, media, time, 101);
		}else{ callNumber(mContext, media, time, number_price_left);}
		
		if(number_price_right != 0){
			callDot(mContext, media, dot, time);
			callNumber(mContext, media, time, number_price_right);
		}
	}

		public static void callNumber(Context mContext, MediaPlayer media, long time, int quantity) {
			try {
				if (quantity != 0) {
					media = new MediaPlayer();
					String quantityvoice = Environment.getExternalStorageDirectory() + "/POS/Media/number/" + quantity + ".mp3";
					Uri uiquantity = Uri.parse(quantityvoice);
					media.setDataSource(mContext, uiquantity);
					media.prepare();
					media.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}
					});
					media.start();
					time = media.getDuration();
					Thread.sleep(time);
				} else {
					Toast.makeText(mContext, "Not voice", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void callDot(Context mContext, MediaPlayer media, String word, long time) {
			try {
				if (word != null) {
					media = new MediaPlayer();
					String messvoice = Environment.getExternalStorageDirectory() + "/POS/Media/word_bufer/" + word + ".mp3";
					Uri uimessage = Uri.parse(messvoice);
					media.setDataSource(mContext, uimessage);
					media.prepare();
					media.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}
					});
					media.start();
					time = media.getDuration();
					Thread.sleep(time);
				} else {
					Toast.makeText(mContext, "Not voice", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void callSymBold(Context mContext, MediaPlayer media, String symbol, long time) {
			try {
				if (symbol != null) {
					media = new MediaPlayer();
					String messvoice = Environment.getExternalStorageDirectory() + "/POS/Media/symbols/" + symbol + ".mp3";
					Uri uimessage = Uri.parse(messvoice);
					media.setDataSource(mContext, uimessage);
					media.prepare();
					media.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}
					});
					media.start();
					time = media.getDuration();
					Thread.sleep(time);
				} else {
					Toast.makeText(mContext, "Not voice", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public static void callDescAsc(Context mContext, MediaPlayer media, long time, String message) {
			try {
				media = new MediaPlayer();
				if (message != null) {
					media = new MediaPlayer();
					String messvoice = Environment.getExternalStorageDirectory() + "/POS/Media/action/" + message + ".mp3";
					Uri uimessage = Uri.parse(messvoice);
					media.setDataSource(mContext, uimessage);
					media.prepare();
					media.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}
					});
					media.start();
					time = media.getDuration();
					Thread.sleep(time);

				} else {
					Toast.makeText(mContext,"not voice", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void callPrice(Context mContext, MediaPlayer media, long time, String message) {
			try {
				media = new MediaPlayer();
				if (message != null) {
					media = new MediaPlayer();
					String messvoice = Environment.getExternalStorageDirectory() + "/POS/Media/action/" + message + ".mp3";
					Uri uimessage = Uri.parse(messvoice);
					media.setDataSource(mContext, uimessage);
					media.prepare();
					media.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}
					});
					media.start();
					time = media.getDuration();
					Thread.sleep(time);

				} else {
					Toast.makeText(mContext,"not voice", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	


}
