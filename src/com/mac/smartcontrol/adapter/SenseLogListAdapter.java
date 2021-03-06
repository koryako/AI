package com.mac.smartcontrol.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
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
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater.inflate(R.layout.sense_log_list_item,
					null);
			holder = new ViewHolder();
			convertView.setTag(holder);

			holder.sense_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.log_name);
			holder.area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);
			holder.log_content_Tv = (MarqueeText) convertView
					.findViewById(R.id.log_content);
			holder.log_time_Tv = (MarqueeText) convertView
					.findViewById(R.id.log_time);

		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		final SensLog_S sensLog_S = senseLogList.get(position);

		holder.sense_name_Tv.setText(sensLog_S.getSzName());
		holder.area_name_Tv.setText(sensLog_S.getSzRgnName());
		if (sensLog_S.getUcType() == SensType_E.SENS_TYPE_GAS.getVal()) {
			holder.log_content_Tv.setText("煤气感应");
		} else if (sensLog_S.getUcType() == SensType_E.SENS_TYPE_SMOKE.getVal()) {
			holder.log_content_Tv.setText("烟雾感应");
		}
		holder.log_time_Tv.setText(paserTime(sensLog_S.getUiTime()));
		return convertView;
	}

	class ViewHolder {

		MarqueeText sense_name_Tv;
		MarqueeText area_name_Tv;
		MarqueeText log_content_Tv;
		MarqueeText log_time_Tv;

	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.sense_name_Tv.setText(null);
		viewHolder.area_name_Tv.setText(null);
		viewHolder.log_content_Tv.setText(null);
		viewHolder.log_time_Tv.setText(null);
	}

	public String paserTime(int time) {
		// System.setProperty("user.timezone", "Asia/Shanghai");
		// TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		// TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String times = format.format(new Date(time * 1000L));
		return times;
	}
}
