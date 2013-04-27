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
import android.widget.TextView;

import com.mac.smartcontrol.ModifySenseActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.SenseListActivity;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class SenseListAdapter extends BaseAdapter {
	private Context context;
	private List<Sens_S> senseList;

	public SenseListAdapter(Context context, List<Sens_S> senseList) {
		super();
		this.context = context;
		this.senseList = senseList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return senseList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return senseList.get(position);
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
			convertView = localinflater.inflate(R.layout.sense_list_item, null);
			TextView sense_name_Tv = (TextView) convertView
					.findViewById(R.id.sense_name);
			TextView area_name_Tv = (TextView) convertView
					.findViewById(R.id.area_name);
			TextView sense_type_Tv = (TextView) convertView
					.findViewById(R.id.sense_type);
			ImageView delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);
			area_name_Tv.setText(((SenseListActivity) context).areaName);
			ImageView modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);
			final Sens_S sens_S = senseList.get(position);
			sense_name_Tv.setText(sens_S.getSzName());
			if (sens_S.getUcType() == 1) {
				sense_type_Tv.setText("煤气");
			} else {
				sense_type_Tv.setText("烟雾");
			}

			delete_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						WriteUtil.write(MsgId_E.MSGID_SENS.getVal(), 0,
								MsgType_E.MSGTYPE_REQ.getVal(),
								MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
										.getSize(),
								new MsgDelReq_S(sens_S.getUsIdx())
										.getMsgDelReq_S());
						((SenseListActivity) context).del_Idx = position;
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
					intent.setClass(context, ModifySenseActivity.class);
					intent.putExtra("sense", sens_S.getSens_S());
					((SenseListActivity) context).mod_Idx = position;
					// 开始一个新的 Activity等候返回结果
					((Activity) context).startActivityForResult(intent, 1);
				}
			});

		}
		return convertView;
	}

}
