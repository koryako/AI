package com.mac.smartcontrol.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mac.smartcontrol.R;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.SensLog_S;
import define.type.SensType_E;

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
			MarqueeText sense_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.log_name);
			MarqueeText area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);
			MarqueeText log_content_Tv = (MarqueeText) convertView
					.findViewById(R.id.log_content);
			MarqueeText log_time_Tv = (MarqueeText) convertView
					.findViewById(R.id.log_time);
			final SensLog_S sensLog_S = senseLogList.get(position);

			sense_name_Tv.setText(sensLog_S.getSzName());
			area_name_Tv.setText(sensLog_S.getSzRgnName());
			if (sensLog_S.getUcType() == SensType_E.SENS_TYPE_GAS.getVal()) {
				log_content_Tv.setText("ú����Ӧ");
			} else if (sensLog_S.getUcType() == SensType_E.SENS_TYPE_SMOKE
					.getVal()) {
				log_content_Tv.setText("�����Ӧ");
			}
			log_time_Tv.setText(sensLog_S.getUiTime() + "");
		}
		return convertView;
	}
}
