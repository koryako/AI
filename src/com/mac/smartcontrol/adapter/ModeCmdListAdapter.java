package com.mac.smartcontrol.adapter;

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

import com.mac.smartcontrol.ModeCmdListActivity;
import com.mac.smartcontrol.ModifyModeCmdActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Appl_S;
import define.entity.Cama_S;
import define.entity.Cmd_S;
import define.entity.ModeCmd_S;
import define.entity.Rgn_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.CmdDevType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModeCmdListAdapter extends BaseAdapter {
	private Context context;
	private Map<Short, Cmd_S> cmdMap;
	private List<ModeCmd_S> modecmdList;
	private Map<Short, Appl_S> deviceMap;
	private Map<Short, Sens_S> senseMap;
	private Map<Short, Rgn_S> areaMap;
	private Map<Short, Cama_S> cameraMap;

	public ModeCmdListAdapter(Context context, List<ModeCmd_S> modecmdList,
			Map<Short, Cmd_S> cmdMap, Map<Short, Appl_S> deviceMap,
			Map<Short, Sens_S> senseMap, Map<Short, Cama_S> cameraMap,
			Map<Short, Rgn_S> areaMap) {
		super();
		this.context = context;
		this.cmdMap = cmdMap;
		this.modecmdList = modecmdList;
		this.deviceMap = deviceMap;
		this.areaMap = areaMap;
		this.cameraMap = cameraMap;
		this.senseMap = senseMap;
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
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater.inflate(R.layout.mode_cmd_list_item,
					null);
			holder = new ViewHolder();
			convertView.setTag(holder);

			holder.cmd_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.cmd_name_tv);

			holder.device_name_Tv = (TextView) convertView
					.findViewById(R.id.device_name_tv);

			holder.area_name_Tv = (TextView) convertView
					.findViewById(R.id.area_name_tv);

			holder.delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);

			holder.modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}
		final ModeCmd_S modeCmd_S = modecmdList.get(position);
		final Cmd_S cmd_S = cmdMap.get(modeCmd_S.getUsCmdIdx());
		if (cmd_S != null) {
			byte cmdDevType = cmd_S.getUcDevType();
			if (cmdDevType == CmdDevType_E.CMD_DEV_APPL.getVal()) {
				Appl_S appl_S = deviceMap.get(cmd_S.getUsDevIdx());
				if (appl_S != null) {
					holder.cmd_name_Tv.setText(cmd_S.getSzName());
					Rgn_S rgn_S = areaMap.get(appl_S.getUsRgnIdx());
					holder.device_name_Tv.setText(appl_S.getSzName());
					if (rgn_S != null)
						holder.area_name_Tv.setText(rgn_S.getSzName());
				}
			} else if (cmdDevType == CmdDevType_E.CMD_DEV_SENS.getVal()) {
				Sens_S sens_S = senseMap.get(cmd_S.getUsDevIdx());
				if (sens_S != null) {
					holder.cmd_name_Tv.setText(sens_S.getSzName());
					Rgn_S rgn_S = areaMap.get(sens_S.getUsRgnIdx());
					holder.device_name_Tv.setText(sens_S.getSzName());
					if (rgn_S != null)
						holder.area_name_Tv.setText(rgn_S.getSzName());
				}
			} else if (cmdDevType == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
				Cama_S cama_S = cameraMap.get(cmd_S.getUsDevIdx());
				if (cama_S != null) {
					holder.cmd_name_Tv.setText(cama_S.getSzName());
					Rgn_S rgn_S = areaMap.get(cama_S.getUsRgnIdx());
					holder.device_name_Tv.setText(cama_S.getSzName());
					if (rgn_S != null)
						holder.area_name_Tv.setText(rgn_S.getSzName());
				}
			}
		}
		holder.delete_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// try {
				WriteUtil.write(MsgId_E.MSGID_MODECMD.getVal(), 0,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S.getSize(),
						new MsgDelReq_S(modeCmd_S.getUsIdx()).getMsgDelReq_S(),
						context);
				((ModeCmdListActivity) context).del_Idx = position;
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Intent i = new Intent("IOException");
				// context.sendBroadcast(i);
				// }
			}
		});
		holder.modify_Iv.setOnClickListener(new OnClickListener() {

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
		return convertView;
	}

	class ViewHolder {
		MarqueeText cmd_name_Tv;

		TextView device_name_Tv;

		TextView area_name_Tv;

		ImageView delete_Iv;

		ImageView modify_Iv;
	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.cmd_name_Tv.setText(null);
		viewHolder.device_name_Tv.setText(null);
		viewHolder.area_name_Tv.setText(null);

	}
}
