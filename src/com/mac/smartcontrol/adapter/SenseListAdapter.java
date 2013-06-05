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

import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.SenseListActivity;

import define.entity.Sens_S;
import define.type.CmdDevType_E;
import define.type.SensType_E;

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
			ImageView icon_Iv = (ImageView) convertView
					.findViewById(R.id.icon_iv);
			TextView sense_name_Tv = (TextView) convertView
					.findViewById(R.id.sense_name);
			TextView area_name_Tv = (TextView) convertView
					.findViewById(R.id.area_name);
			TextView sense_type_Tv = (TextView) convertView
					.findViewById(R.id.sense_type);
			ImageView enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);
			area_name_Tv.setText(((SenseListActivity) context).areaName);
			final Sens_S sens_S = senseList.get(position);
			sense_name_Tv.setText(sens_S.getSzName());
			if (sens_S.getUcType() == SensType_E.SENS_TYPE_GAS.getVal()) {
				sense_type_Tv.setText("煤气");
				// icon_Iv.setImageResource(R.drawable.)
			} else if (sens_S.getUcType() == SensType_E.SENS_TYPE_SMOKE
					.getVal()) {
				sense_type_Tv.setText("烟雾");
			} else if (sens_S.getUcType() == SensType_E.SENS_TYPE_TEMP.getVal()) {
				sense_type_Tv.setText("温度");
				icon_Iv.setImageResource(R.drawable.temp_icon);
			} else if (sens_S.getUcType() == SensType_E.SENS_TYPE_MOIS.getVal()) {
				sense_type_Tv.setText("湿度");
				icon_Iv.setImageResource(R.drawable.mois_icon);
			}

			enter_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("cmdType",
							CmdDevType_E.CMD_DEV_SENS.getVal());
					intent.putExtra("sense", sens_S.getSens_S());
					intent.setClass(context, CmdListActivity.class);
					context.startActivity(intent);
				}
			});
			// delete_Iv.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// try {
			// WriteUtil.write(MsgId_E.MSGID_SENS.getVal(), 0,
			// MsgType_E.MSGTYPE_REQ.getVal(),
			// MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
			// .getSize(),
			// new MsgDelReq_S(sens_S.getUsIdx())
			// .getMsgDelReq_S());
			// ((SenseListActivity) context).del_Idx = position;
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// Intent i = new Intent("IOException");
			// context.sendBroadcast(i);
			// }
			// }
			// });
			// modify_Iv.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// Intent intent = new Intent();
			// intent.setClass(context, ModifySenseActivity.class);
			// intent.putExtra("sense", sens_S.getSens_S());
			// ((SenseListActivity) context).mod_Idx = position;
			// // 开始一个新的 Activity等候返回结果
			// ((Activity) context).startActivityForResult(intent, 1);
			// }
			// });

		}
		return convertView;
	}

}
