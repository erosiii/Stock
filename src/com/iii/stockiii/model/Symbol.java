package com.iii.stockiii.model;

public class Symbol {
	private String code;
	private boolean selected;

	public Symbol() {
		this.code = null;
		this.selected = false;
	}

	public Symbol(String code, boolean selected) {
		this.code = code;
		this.selected = selected;
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
