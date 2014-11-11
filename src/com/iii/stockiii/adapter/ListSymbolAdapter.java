package com.iii.stockiii.adapter;

import java.util.List;

import com.iii.stockiii.R;
import com.iii.stockiii.model.Symbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListSymbolAdapter extends ArrayAdapter<Symbol> {
	private Context context;
	private List<Symbol> arraylist;

	public ListSymbolAdapter(Context context, int resource, List<Symbol> objects) {
		super(context, resource, objects);
		this.context = context;
		this.arraylist = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inlater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View item = inlater.inflate(R.layout.item_configution_layout, null);
		CheckBox cbsymbol = (CheckBox) item.findViewById(R.id.cbsymbol);
		Symbol sb = arraylist.get(position);
		cbsymbol.setText(sb.getCode());
		cbsymbol.setChecked(sb.isSelected());
		return item;
	}

}
