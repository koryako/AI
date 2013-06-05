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

import com.mac.smartcontrol.ACActivity;
import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.EnterDeviceListActivity;
import com.mac.smartcontrol.HDPlayActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.SwitchActivity;
import com.mac.smartcontrol.TVActivity;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Appl_S;
import define.entity.Rgn_S;
import define.type.ApplType_E;
import define.type.CmdDevType_E;
import define.type.MsgId_E;

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
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater.inflate(
					R.layout.enter_device_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);

			holder.device_icon_Iv = (ImageView) convertView
					.findViewById(R.id.device_icon_iv);
			holder.device_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.device_name_tv);
			holder.area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);

			holder.enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		final Appl_S appl_S = deviceList.get(position);
		holder.device_name_Tv.setText(appl_S.getSzName());
		Rgn_S rgn_S = areaMap.get(appl_S.getUsRgnIdx());
		if (rgn_S != null) {
			holder.area_name_Tv.setText(rgn_S.getSzName());
		}
		final Intent intent = new Intent();
		holder.device_name_Tv.setText(appl_S.getSzName());
		if (appl_S.getUcType() == ApplType_E.APPL_TYPE_SWITCH.getVal()) {
			holder.device_icon_Iv.setImageResource(R.drawable.light_icon);
		} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_TVSET.getVal()) {
			holder.device_icon_Iv.setImageResource(R.drawable.tv_icon);
		} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_HDPLAY.getVal()) {
			holder.device_icon_Iv.setImageResource(R.drawable.tv_box_icon);
		} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_AIRCOND.getVal()) {
			holder.device_icon_Iv.setImageResource(R.drawable.ac_icon);
		} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_CURTAIN.getVal()) {
			holder.device_icon_Iv.setImageResource(R.drawable.curtain_icon);
		} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_CUSTOM.getVal()) {
			holder.device_icon_Iv
					.setImageResource(R.drawable.user_defined_icon);
		}
		intent.putExtra("device", appl_S.getAppl_S());
		intent.putExtra("cmdType", CmdDevType_E.CMD_DEV_APPL.getVal());
		holder.enter_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int msgId = ((EnterDeviceListActivity) context).msgId;
				if (msgId == MsgId_E.MSGID_APPL.getVal()) {
					intent.setClass(context, CmdListActivity.class);
				} else if (msgId == 34) {
					// intent.setClass(context, CmdListActivity.class);
					if (appl_S.getUcType() == ApplType_E.APPL_TYPE_SWITCH
							.getVal()) {
						intent.setClass(context, SwitchActivity.class);
					} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_TVSET
							.getVal()) {
						intent.setClass(context, TVActivity.class);
					} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_HDPLAY
							.getVal()) {
						intent.setClass(context, HDPlayActivity.class);
					} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_AIRCOND
							.getVal()) {
						intent.setClass(context, ACActivity.class);
					} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_CURTAIN
							.getVal()) {
						intent.setClass(context, SwitchActivity.class);
					} else if (appl_S.getUcType() == ApplType_E.APPL_TYPE_CUSTOM
							.getVal()) {
						intent.setClass(context, SwitchActivity.class);
					}
				}
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {

		ImageView device_icon_Iv;
		MarqueeText device_name_Tv;
		MarqueeText area_name_Tv;
		ImageView enter_Iv;
	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.area_name_Tv.setText(null);
		viewHolder.device_name_Tv.setText(null);
	}
}
