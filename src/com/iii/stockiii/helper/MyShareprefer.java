package com.iii.stockiii.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyShareprefer {

	private SharedPreferences iShare;

	public static final String GET_INFOUSER_ITEM = "MyInfoUser";

	public static final String PHONE = "PHONE";
	public static final String EMAIL = "EMAIL";
	public static final String NAME = "NAME";

	public MyShareprefer(Context context, String share_name) {
		iShare = context.getSharedPreferences(share_name, Context.MODE_PRIVATE);
	}

	public void logout() {
		iShare.edit().putString(PHONE, "").commit();
		iShare.edit().putString(EMAIL, "").commit();
		iShare.edit().putString(NAME, "").commit();
		//iShare.edit().putString(USER_COMPANY, "").commit();
	}
	
	

	public String getUerPhone() {
		return iShare.getString(PHONE, "");
	}

	public String getUserEmail() {
		return iShare.getString(EMAIL, "");
	}
	
	public void setUserEmail(String email) {
		iShare.edit().putString(EMAIL, email).commit();
	}
	
	public String getUserName() {
		return iShare.getString(NAME, "");
	}

	public void setUserPhone(String phone) {
		iShare.edit().putString(PHONE, phone).commit();
	}

	public void setUserName(String name) {
		iShare.edit().putString(NAME, name).commit();
	}

}
