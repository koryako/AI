package com.mac.smartcontrol.adapter;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mac.smartcontrol.DeviceListActivity;
import com.mac.smartcontrol.ModifyDeviceActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Appl_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class DeviceListAdapter extends BaseAdapter {
	private Context context;
	private List<Appl_S> deviceList;

	public DeviceListAdapter(Context context, List<Appl_S> deviceList) {
		super();
		this.context = context;
		this.deviceList = deviceList;
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
			convertView = localinflater
					.inflate(R.layout.device_list_item, null);
			MarqueeText device_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.device_name_tv);
			MarqueeText area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);
			ImageView device_icon_Iv = (ImageView) convertView
					.findViewById(R.id.device_icon_iv);
			ImageView delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);
			area_name_Tv.setText(((DeviceListActivity) context).areaName);
			ImageView modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);
			final Appl_S appl_S = deviceList.get(position);
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

			delete_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 0,
								MsgType_E.MSGTYPE_REQ.getVal(),
								MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
										.getSize(),
								new MsgDelReq_S(appl_S.getUsIdx())
										.getMsgDelReq_S());
						((DeviceListActivity) context).del_Idx = position;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Intent i = new Intent("IOException");
						context.sendBroadcast(i);
					}
				}
			});
			modify_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, ModifyDeviceActivity.class);
					intent.putExtra("device", appl_S.getAppl_S());
					((DeviceListActivity) context).mod_Idx = position;
					// 开始一个新的 Activity等候返回结果
					((Activity) context).startActivityForResult(intent, 1);
				}
			});

		}
		return convertView;
	}

}
