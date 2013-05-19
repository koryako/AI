package com.mac.smartcontrol.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.smartcontrol.CameraListActivity;
import com.mac.smartcontrol.EnterDeviceListActivity;
import com.mac.smartcontrol.EnterModeListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.SenseListActivity;

import define.entity.Rgn_S;
import define.type.MsgId_E;

public class EnterAreaListAdapter extends BaseAdapter {
	private Context context;
	private List<Rgn_S> areaList;
	private int msgId;

	public EnterAreaListAdapter(Context context, List<Rgn_S> areaList, int msgId) {
		super();
		this.context = context;
		this.areaList = areaList;
		this.msgId = msgId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return areaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return areaList.get(position);
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
			convertView = localinflater.inflate(R.layout.enter_area_list_item,
					null);
			TextView areaName_Tv = (TextView) convertView
					.findViewById(R.id.areaName);
			ImageView enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);
			final Rgn_S rgn_S = areaList.get(position);
			areaName_Tv.setText(rgn_S.getSzName());

			enter_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("area", rgn_S.getRgn_S());
					intent.putExtra("areaName", rgn_S.getSzName());

					if (msgId == MsgId_E.MSGID_APPL.getVal()) {
						intent.putExtra("msgId", 4);
						intent.setClass(context, EnterDeviceListActivity.class);
					} else if (msgId == MsgId_E.MSGID_SENS.getVal()) {
						intent.setClass(context, SenseListActivity.class);
					} else if (msgId == MsgId_E.MSGID_CAMA.getVal()) {
						intent.setClass(context, CameraListActivity.class);
					} else if (msgId == 34) {
						intent.putExtra("msgId", 34);
						intent.setClass(context, EnterDeviceListActivity.class);
					} else if (msgId == MsgId_E.MSGID_MODE.getVal()) {
						intent.setClass(context, EnterModeListActivity.class);
					}
					context.startActivity(intent);
				}
			});

		}
		return convertView;
	}
}
