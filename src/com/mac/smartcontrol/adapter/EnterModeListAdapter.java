package com.mac.smartcontrol.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.smartcontrol.EnterModeListActivity;
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
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater.inflate(R.layout.enter_mode_list_item,
					null);
			TextView mode_name_Tv = (TextView) convertView
					.findViewById(R.id.mode_name_tv);
			MarqueeText voice_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.voice_name_tv);
			MarqueeText area_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.area_name_tv);
			ImageView enter_Iv = (ImageView) convertView
					.findViewById(R.id.enter_btn);
			final Mode_S mode_S = modeList.get(position);
			mode_name_Tv.setText(mode_S.getSzName());
			voice_name_Tv.setText(mode_S.getSzVoice());
			area_name_Tv.setText(((EnterModeListActivity) context).rgn_S
					.getSzName());
			System.out.println(enter_Iv + "---->>>>");
			enter_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Intent intent = new Intent();
					// intent.putExtra("mode", mode_S.getMode_S());
					// context.startActivity(intent);
				}
			});

			// delete_Iv.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// try {
			// WriteUtil.write(MsgId_E.MSGID_MODE.getVal(), 0,
			// MsgType_E.MSGTYPE_REQ.getVal(),
			// MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
			// .getSize(),
			// new MsgDelReq_S(mode_S.getUsIdx())
			// .getMsgDelReq_S());
			// ((ModeListActivity) context).del_Idx = position;
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
			// intent.setClass(context, ModifyModeActivity.class);
			// intent.putExtra("mode", mode_S.getMode_S());
			// ((ModeListActivity) context).mod_Idx = position;
			// // 开始一个新的 Activity等候返回结果
			// ((Activity) context).startActivityForResult(intent, 1);
			// }
			// });

		}
		return convertView;
	}

}
