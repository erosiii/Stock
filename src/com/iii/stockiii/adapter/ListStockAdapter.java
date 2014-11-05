package com.iii.stockiii.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iii.stockiii.R;
import com.iii.stockiii.model.Stock;

public class ListStockAdapter extends ArrayAdapter<Stock> {
	private Context context;
	private List<Stock> arrayList;

	public ListStockAdapter(Context context, int resource, List<Stock> list) {
		super(context, resource, list);
		this.context = context;
		this.arrayList = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflate = (LayoutInflater) this.context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflate.inflate(R.layout.item_do_layout,null);
				/*-----------------------------------------------------------*/
				holder.tvchange = (TextView) convertView .findViewById(R.id.tvChange);
				holder.tvresultpri = (TextView) convertView.findViewById(R.id.tvPri);
				holder.tvresultvol = (TextView) convertView.findViewById(R.id.tvVol);
				holder.tvresultotalvol = (TextView) convertView.findViewById(R.id.tvTotalVol);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Stock stockmodel = arrayList.get(position);
			holder.tvchange.setText(stockmodel.getChange());
			holder.tvresultpri.setText(stockmodel.getResult_pri());
			holder.tvresultvol.setText(stockmodel.getResult_vol());
			holder.tvresultotalvol.setText(stockmodel.getResult_total_vol());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvchange;
		TextView tvresultpri;
		TextView tvresultvol;
		TextView tvresultotalvol;
	}

}
