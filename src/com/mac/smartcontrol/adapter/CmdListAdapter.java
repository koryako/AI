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
import com.mac.smartcontrol.ModifyCmdActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;
import com.mac.smartcontrol.widget.MarqueeText;

import define.entity.Cmd_S;
import define.entity.Ctrl_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.CmdDevType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class CmdListAdapter extends BaseAdapter {
	private Context context;
	private List<Cmd_S> cmdList;
	private Map<Short, Ctrl_S> ctrlMap;

	private byte cmdType;

	public CmdListAdapter(Context context, List<Cmd_S> cmdList,
			Map<Short, Ctrl_S> ctrlMap, byte cmdType) {
		super();
		this.context = context;
		this.cmdList = cmdList;
		this.ctrlMap = ctrlMap;
		this.cmdType = cmdType;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cmdList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cmdList.get(position);
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
			convertView = localinflater.inflate(R.layout.cmd_list_item, null);
			ImageView cmd_icon_Iv = (ImageView) convertView
					.findViewById(R.id.cmd_icon);
			TextView cmd_name_Tv = (TextView) convertView
					.findViewById(R.id.cmd_name_tv);
			MarqueeText voice_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.voice_name_tv);
			MarqueeText ctrl_name_Tv = (MarqueeText) convertView
					.findViewById(R.id.ctrl_name_tv);

			ImageView delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);

			ImageView modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);
			final Cmd_S cmd_S = cmdList.get(position);
			Ctrl_S ctrl_S = ctrlMap.get(cmd_S.getUsCtrlIdx());
			if (ctrl_S != null)
				ctrl_name_Tv.setText(ctrl_S.getSzName());
			cmd_name_Tv.setText(cmd_S.getSzName());
			voice_name_Tv.setText(cmd_S.getSzVoice());
			if (cmd_S.getUcDevType() == 1) {
				cmd_icon_Iv.setImageResource(R.drawable.light_icon);
			} else if (cmd_S.getUcDevType() == 2) {
				cmd_icon_Iv.setImageResource(R.drawable.tv_icon);
			} else if (cmd_S.getUcDevType() == 3) {
				cmd_icon_Iv.setImageResource(R.drawable.light_icon);
			}

			delete_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 0,
								MsgType_E.MSGTYPE_REQ.getVal(),
								MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S
										.getSize(),
								new MsgDelReq_S(cmd_S.getUsIdx())
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
					intent.setClass(context, ModifyCmdActivity.class);
					intent.putExtra("cmd", cmd_S.getCmd_S());
					if (cmdType == CmdDevType_E.CMD_DEV_APPL.getVal()) {
						intent.putExtra("device",
								((CmdListActivity) context).appl_S.getAppl_S());
					} else if (cmdType == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
						intent.putExtra("camare",
								((CmdListActivity) context).cama_S.getCama_S());
					} else if (cmdType == CmdDevType_E.CMD_DEV_SENS.getVal()) {
						intent.putExtra("sense",
								((CmdListActivity) context).sens_S.getSens_S());
					}

					intent.putExtra("cmdType", cmdType);
					((CmdListActivity) context).mod_Idx = position;
					// ��ʼһ���µ� Activity�Ⱥ򷵻ؽ��
					((Activity) context).startActivityForResult(intent, 1);
					((CmdListActivity) context).unreg_receiver();
				}
			});

		}
		return convertView;
	}

}
