package com.iii.stockiii.model;

public class Symbol {
	private String code;
	private boolean selected;
	//save postion in listview
	private int posinlist;

	public int getPosinlist() {
		return posinlist;
	}

	public void setPosinlist(int posinlist) {
		this.posinlist = posinlist;
	}

	public Symbol() {
		this.code = null;
		this.selected = false;
		this.posinlist = 0;
	}

	public Symbol(String code, boolean selected, int pos) {
		this.code = code;
		this.selected = selected;
		this.posinlist = pos;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
