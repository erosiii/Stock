package com.iii.stockiii.model;

public class Voices {

	private String strchange;
	private double different_value;
	private double price;
	private int inc_des;
	private int flag;
	private String ref;
	public Voices(String strchange, double different_value, double price, int inc_des,
			int flag, String ref) {
		super();
		this.strchange = strchange;
		this.different_value = different_value;
		this.price = price;
		this.inc_des = inc_des;
		this.flag = flag;
		this.ref = ref;
	}
	
	public Voices() {
		// TODO Auto-generated constructor stub
	}

	public String getStrchange() {
		return strchange;
	}

	public void setStrchange(String strchange) {
		this.strchange = strchange;
	}

	public double getDifferent_value() {
		return different_value;
	}

	public void setDifferent_value(double different_value) {
		this.different_value = different_value;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getInc_des() {
		return inc_des;
	}

	public void setInc_des(int inc_des) {
		this.inc_des = inc_des;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	

}
