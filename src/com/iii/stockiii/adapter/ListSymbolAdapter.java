package com.iii.stockiii.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iii.stockiii.R;
import com.iii.stockiii.model.Symbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

public class ListSymbolAdapter extends ArrayAdapter<Symbol> implements
		Filterable {
	private Context context;
	private List<Symbol> arraylist;
	private List<Symbol> orig;

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

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				arraylist = (ArrayList<Symbol>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults oReturn = new FilterResults();
				final List<Symbol> results = new ArrayList<Symbol>();
				if (orig == null)
					orig = arraylist;
				if (constraint != null) {
					if (orig != null && orig.size() > 0) {
						for (final Symbol g : orig) {
							if (g.getCode().contains(constraint.toString()))
								results.add(g);
						}
					}
					oReturn.values = results;
				}
				return oReturn;
			}
		};
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public Symbol getItem(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
