package com.mac.smartcontrol.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mac.smartcontrol.R;

import define.entity.SensLog_S;

public class SenseLogListAdapter extends BaseAdapter {
	private Context context;
	private List<SensLog_S> senseLogList;

	public SenseLogListAdapter(Context context, List<SensLog_S> senseLogList) {
		super();
		this.context = context;
		this.senseLogList = senseLogList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return senseLogList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return senseLogList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater.inflate(R.layout.sense_log_list_item,
					null);
			TextView sense_name_Tv = (TextView) convertView
					.findViewById(R.id.log_name);
			TextView area_name_Tv = (TextView) convertView
					.findViewById(R.id.area_name);
			TextView log_content_Tv = (TextView) convertView
					.findViewById(R.id.log_content);
			TextView log_time_Tv = (TextView) convertView
					.findViewById(R.id.log_time);
			final SensLog_S sensLog_S = senseLogList.get(position);

			sense_name_Tv.setText(sensLog_S.getSzName());
			area_name_Tv.setText(sensLog_S.getSzRgnName());
			log_content_Tv.setText(sensLog_S.getUcType() + "");
			log_time_Tv.setText(sensLog_S.getUiTime() + "");
		}
		return convertView;
	}
}
