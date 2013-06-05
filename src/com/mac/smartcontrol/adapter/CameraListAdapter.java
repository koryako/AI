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

import com.mac.smartcontrol.CameraListActivity;
import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.FormatTransfer;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Cama_S;
import define.type.CmdDevType_E;

public class CameraListAdapter extends BaseAdapter {
	private Context context;
	private List<Cama_S> cameraList;

	public CameraListAdapter(Context context, List<Cama_S> cameraList) {
		super();
		this.context = context;
		this.cameraList = cameraList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cameraList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cameraList.get(position);
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
					.inflate(R.layout.camera_list_item, null);

			MarqueeText camera_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.camera_name);
			MarqueeText area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);

			MarqueeText ip_Tv = (MarqueeText) convertView
					.findViewById(R.id.ip_tv);
			MarqueeText port_Tv = (MarqueeText) convertView
					.findViewById(R.id.port_tv);
			ImageView enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);
			// ImageView delete_Iv = (ImageView) convertView
			// .findViewById(R.id.delete_btn);
			area_name_Tv.setText(((CameraListActivity) context).areaName);
			// ImageView modify_Iv = (ImageView) convertView
			// .findViewById(R.id.modify_btn);
			final Cama_S cama_S = cameraList.get(position);
			camera_name_Tv.setText(cama_S.getSzName());
			byte[] ip_b = FormatTransfer.toLH(cama_S.getUiIpAddr());
			String ip_Str = "";
			for (int i = ip_b.length - 1; i >= 0; i--) {
				if (ip_b[i] > 0) {
					ip_Str += ip_b[i] + ".";
				} else {
					ip_Str += (ip_b[i] + 256) + ".";
				}
			}
			ip_Tv.setText(ip_Str.substring(0, ip_Str.length() - 1));
			port_Tv.setText(cama_S.getUsPort() + "");

			enter_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("cmdType",
							CmdDevType_E.CMD_DEV_CAMA.getVal());
					intent.putExtra("camare", cama_S.getCama_S());
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
			// WriteUtil.write(MsgId_E.MSGID_CAMA.getVal(), 0,
			// MsgType_E.MSGTYPE_REQ.getVal(),
			// MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
			// .getSize(),
			// new MsgDelReq_S(cama_S.getUsIdx())
			// .getMsgDelReq_S());
			// ((CameraListActivity) context).del_Idx = position;
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
			// intent.setClass(context, ModifyCameraActivity.class);
			// intent.putExtra("camera", cama_S.getCama_S());
			// ((CameraListActivity) context).mod_Idx = position;
			// // 开始一个新的 Activity等候返回结果
			// ((Activity) context).startActivityForResult(intent, 1);
			// }
			// });

		}
		return convertView;
	}
}
