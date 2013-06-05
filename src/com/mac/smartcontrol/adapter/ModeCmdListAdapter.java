package com.mac.smartcontrol.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.ModeCmdListActivity;
import com.mac.smartcontrol.ModifyModeCmdActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.entity.ModeCmd_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModeCmdListAdapter extends BaseAdapter {
	private Context context;
	private Map<Short, Cmd_S> cmdMap;
	private List<ModeCmd_S> modecmdList;
	private Map<Short, Appl_S> deviceMap;
	private Map<Short, Rgn_S> areaMap;

	public ModeCmdListAdapter(Context context, List<ModeCmd_S> modecmdList,
			Map<Short, Cmd_S> cmdMap, Map<Short, Appl_S> deviceMap,
			Map<Short, Rgn_S> areaMap) {
		super();
		this.context = context;
		this.cmdMap = cmdMap;
		this.modecmdList = modecmdList;
		this.deviceMap = deviceMap;
		this.areaMap = areaMap;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modecmdList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modecmdList.get(position);
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
			convertView = localinflater.inflate(R.layout.mode_cmd_list_item,
					null);
			MarqueeText cmd_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.cmd_name_tv);

			TextView device_name_Tv = (TextView) convertView
					.findViewById(R.id.device_name_tv);

			TextView area_name_Tv = (TextView) convertView
					.findViewById(R.id.area_name_tv);

			ImageView delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);

			ImageView modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);
			final ModeCmd_S modeCmd_S = modecmdList.get(position);
			final Cmd_S cmd_S = cmdMap.get(modeCmd_S.getUsCmdIdx());
			if (cmd_S != null) {
				Appl_S appl_S = deviceMap.get(cmd_S.getUsDevIdx());
				cmd_name_Tv.setText(cmd_S.getSzName());
				if (appl_S != null) {
					Rgn_S rgn_S = areaMap.get(appl_S.getUsRgnIdx());
					device_name_Tv.setText(appl_S.getSzName());
					if (rgn_S != null)
						area_name_Tv.setText(rgn_S.getSzName());
				}
			}
			delete_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						WriteUtil.write(MsgId_E.MSGID_MODECMD.getVal(), 0,
								MsgType_E.MSGTYPE_REQ.getVal(),
								MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
										.getSize(),
								new MsgDelReq_S(modeCmd_S.getUsIdx())
										.getMsgDelReq_S());
						((CmdListActivity) context).del_Idx = position;
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
					intent.setClass(context, ModifyModeCmdActivity.class);
					intent.putExtra("modecmd", modeCmd_S.getModeCmd_S());
					intent.putExtra("mode",
							((ModeCmdListActivity) context).mode_S.getMode_S());
					((ModeCmdListActivity) context).mod_Idx = position;
					// 开始一个新的 Activity等候返回结果
					((Activity) context).startActivityForResult(intent, 1);
				}
			});

		}
		return convertView;
	}

}
