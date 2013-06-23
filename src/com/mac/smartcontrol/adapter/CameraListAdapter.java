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
import android.widget.Toast;

import com.ipcamer.demo.StartCameraActivity;
import com.mac.smartcontrol.CameraListActivity;
import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Cama_S;
import define.type.CmdDevType_E;

public class CameraListAdapter extends BaseAdapter {
	private Context context;
	private List<Cama_S> cameraList;

	private int type = -1; // 0是进入管理 1是进入监控

	public CameraListAdapter(Context context, List<Cama_S> cameraList, int type) {
		super();
		this.context = context;
		this.cameraList = cameraList;
		this.type = type;
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
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater
					.inflate(R.layout.camera_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);

			holder.camera_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.camera_name);
			holder.area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name);

			holder.uid_Tv = (MarqueeText) convertView.findViewById(R.id.uid_tv);
			holder.enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);

		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		// ImageView delete_Iv = (ImageView) convertView
		// .findViewById(R.id.delete_btn);
		holder.area_name_Tv.setText(((CameraListActivity) context).areaName);
		// ImageView modify_Iv = (ImageView) convertView
		// .findViewById(R.id.modify_btn);
		final Cama_S cama_S = cameraList.get(position);
		holder.camera_name_Tv.setText(cama_S.getSzName());
		holder.uid_Tv.setText(cama_S.getSzUid());

		holder.enter_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("cmdType", CmdDevType_E.CMD_DEV_CAMA.getVal());
				intent.putExtra("camera", cama_S.getCama_S());
				if (type == 0) {
					intent.setClass(context, CmdListActivity.class);
				} else {
					Toast.makeText(context, R.string.connecting,
							Toast.LENGTH_LONG).show();
					intent.setClass(context, StartCameraActivity.class);
				}
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		MarqueeText camera_name_Tv;
		TextView area_name_Tv;

		MarqueeText uid_Tv;

		ImageView enter_Iv;
	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.camera_name_Tv.setText(null);
		viewHolder.area_name_Tv.setText(null);
		viewHolder.uid_Tv.setText(null);

	}
}
