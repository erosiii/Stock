package com.iii.stockiii.model;

public class Stock {

	private String change;
	private String result_pri;
	private String result_vol;
	private String result_total_vol;
	public Stock(String change, String result_pri, String result_vol,
			String result_total_vol) {
		super();
		this.change = change;
		this.result_pri = result_pri;
		this.result_vol = result_vol;
		this.result_total_vol = result_total_vol;
	}

	public Stock() {
		// TODO Auto-generated constructor stub
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getResult_pri() {
		return result_pri;
	}
	public void setResult_pri(String result_pri) {
		this.result_pri = result_pri;
	}
	public String getResult_vol() {
		return result_vol;
	}
	public void setResult_vol(String result_vol) {
		this.result_vol = result_vol;
	}
	public String getResult_total_vol() {
		return result_total_vol;
	}
	public void setResult_total_vol(String result_total_vol) {
		this.result_total_vol = result_total_vol;
	}
	
	

}
