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

import com.mac.smartcontrol.EnterModeListActivity;
import com.mac.smartcontrol.ModeCmdListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Mode_S;

public class EnterModeListAdapter extends BaseAdapter {
	private Context context;
	private List<Mode_S> modeList;

	public EnterModeListAdapter(Context context, List<Mode_S> modeList) {
		super();
		this.context = context;
		this.modeList = modeList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modeList.get(position);
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
			convertView = localinflater.inflate(R.layout.enter_mode_list_item,
					null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.mode_name_Tv = (TextView) convertView
					.findViewById(R.id.mode_name_tv);
			holder.voice_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.voice_name_tv);
			holder.area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name_tv);
			holder.enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);

		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		final Mode_S mode_S = modeList.get(position);
		holder.mode_name_Tv.setText(mode_S.getSzName());
		holder.voice_name_Tv.setText(mode_S.getSzVoice());
		holder.area_name_Tv.setText(((EnterModeListActivity) context).rgn_S
				.getSzName());
		holder.enter_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("mode", mode_S.getMode_S());
				intent.setClass(context, ModeCmdListActivity.class);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {

		TextView mode_name_Tv;
		MarqueeText voice_name_Tv;
		MarqueeText area_name_Tv;
		ImageView enter_Iv;

	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.mode_name_Tv.setText(null);
		viewHolder.voice_name_Tv.setText(null);
		viewHolder.area_name_Tv.setText(null);
	}
}
