package com.iii.stockiii.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
				holder.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
				holder.imgDesc = (ImageView) convertView.findViewById(R.id.imgvArrDESC);
				holder.imgvAsc = (ImageView) convertView.findViewById(R.id.imgvArrASC);
				holder.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Stock stockmodel = arrayList.get(position);
			double count ;
			holder.tvchange.setText(stockmodel.getChange());
			holder.tvresultpri.setText(stockmodel.getResult_pri());
			holder.tvresultvol.setText(stockmodel.getResult_vol());
			holder.tvresultotalvol.setText(stockmodel.getResult_total_vol());
			DecimalFormat df = new DecimalFormat("##.##");
			if(Float.parseFloat(stockmodel.getResult_pri()) >  Float.parseFloat(stockmodel.getRef())){
				if(!stockmodel.getResult_pri().equals("0")){
					holder.tvchange.setTextColor(Color.parseColor("#32C617"));
					holder.tvresultpri.setTextColor(Color.parseColor("#32C617"));
					holder.tvresultvol.setTextColor(Color.parseColor("#32C617"));
					holder.tvresultotalvol.setTextColor(Color.parseColor("#32C617"));
					count = Double.parseDouble(stockmodel.getResult_pri()) - Double.parseDouble(stockmodel.getRef());
					holder.tvCount.setText("+" + String.valueOf(df.format(count)));
					holder.tvCount.setTextColor(Color.parseColor("#32C617"));
					holder.imgvAsc.setVisibility(ImageView.VISIBLE);
					holder.imgDesc.setVisibility(ImageView.GONE);
				}else {
					count = 0;
					holder.tvchange.setTextColor(Color.parseColor("#EEFB00"));
					holder.tvCount.setVisibility(TextView.GONE);
					holder.imgvAsc.setVisibility(ImageView.GONE);
					holder.imgDesc.setVisibility(ImageView.GONE);
				}
				
			}else if(Float.parseFloat(stockmodel.getResult_pri()) <  Float.parseFloat(stockmodel.getRef())){
				
				if(!stockmodel.getResult_pri().equals("0")){
					holder.tvchange.setTextColor(Color.parseColor("#A32A36"));
					holder.tvresultpri.setTextColor(Color.parseColor("#A32A36"));
					holder.tvresultvol.setTextColor(Color.parseColor("#A32A36"));
					holder.tvresultotalvol.setTextColor(Color.parseColor("#A32A36"));
					holder.imgvAsc.setVisibility(ImageView.GONE);
					holder.imgDesc.setVisibility(ImageView.VISIBLE);
					count = Double.parseDouble(stockmodel.getRef()) - Double.parseDouble(stockmodel.getResult_pri());
					holder.tvCount.setText("-" + String.valueOf(df.format(count)));
					holder.tvCount.setTextColor(Color.parseColor("#A32A36"));
				}else {
					count = 0;
					holder.tvchange.setTextColor(Color.parseColor("#EEFB00"));
					holder.tvCount.setVisibility(TextView.GONE);
					holder.imgvAsc.setVisibility(ImageView.GONE);
					holder.imgDesc.setVisibility(ImageView.GONE);
				}
				
			}else{
				holder.tvchange.setTextColor(Color.parseColor("#EEFB00"));
				holder.tvresultpri.setTextColor(Color.parseColor("#EEFB00"));
				holder.tvresultvol.setTextColor(Color.parseColor("#EEFB00"));
				holder.tvresultotalvol.setTextColor(Color.parseColor("#EEFB00"));
				holder.tvCount.setVisibility(TextView.GONE);
				holder.imgvAsc.setVisibility(ImageView.GONE);
				holder.imgDesc.setVisibility(ImageView.GONE);
				
			}
			
			

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
		TextView tvCount;
		ImageView imgvAsc;
		ImageView imgDesc;
	}

}
