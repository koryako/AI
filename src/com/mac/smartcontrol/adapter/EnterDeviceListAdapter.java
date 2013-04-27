package com.mac.smartcontrol.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.EnterDeviceListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Appl_S;
import define.entity.Rgn_S;

public class EnterDeviceListAdapter extends BaseAdapter {
	private Context context;
	private List<Appl_S> deviceList;
	private Map<Short, Rgn_S> areaMap;

	public EnterDeviceListAdapter(Context context, List<Appl_S> deviceList,
			Map<Short, Rgn_S> areaMap) {
		super();
		this.context = context;
		this.deviceList = deviceList;
		this.areaMap = areaMap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return deviceList.get(position);
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
			convertView = localinflater.inflate(
					R.layout.enter_device_list_item, null);

			ImageView device_icon_Iv = (ImageView) convertView
					.findViewById(R.id.device_icon_iv);
			MarqueeText device_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.device_name_tv);
			MarqueeText area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);

			ImageView enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);
			final Appl_S appl_S = deviceList.get(position);
			device_name_Tv.setText(appl_S.getSzName());
			Rgn_S rgn_S = areaMap.get(appl_S.getUsRgnIdx());
			if (rgn_S != null) {
				area_name_Tv.setText(rgn_S.getSzName());
			}
			device_name_Tv.setText(appl_S.getSzName());
			if (appl_S.getUcType() == 1) {
				device_icon_Iv.setImageResource(R.drawable.light_icon);
			} else if (appl_S.getUcType() == 2) {
				device_icon_Iv.setImageResource(R.drawable.tv_icon);
			} else if (appl_S.getUcType() == 3) {
				device_icon_Iv.setImageResource(R.drawable.tv_box_icon);
			} else if (appl_S.getUcType() == 4) {
				device_icon_Iv.setImageResource(R.drawable.ac_icon);
			} else if (appl_S.getUcType() == 5) {
				device_icon_Iv.setImageResource(R.drawable.curtain_icon);
			}
			enter_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("device", appl_S.getAppl_S());
					int msgId = ((EnterDeviceListActivity) context).msgId;
					if (msgId == -1) {
						intent.setClass(context, CmdListActivity.class);
					} else if (msgId == 34) {
						// intent.setClass(context, CmdListActivity.class);
					}
					context.startActivity(intent);
				}
			});

		}
		return convertView;
	}

}
