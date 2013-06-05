package com.mac.smartcontrol.broadcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.ACActivity;
import com.mac.smartcontrol.FansActivity;
import com.mac.smartcontrol.HDPlayActivity;
import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.SwitchActivity;
import com.mac.smartcontrol.TVActivity;

import define.entity.Cmd_S;
import define.oper.MsgOperCmd_E;
import define.oper.body.ack.MsgCmdQryByDevAck_S;
import define.type.ApplType_E;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class ControlBroadcastReceiver extends BroadcastReceiver {
	Activity activity;
	byte device_type;

	public ControlBroadcastReceiver(Activity activity, byte device_type) {
		super();
		this.activity = activity;
		this.device_type = device_type;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals("IOException")) {
			Intent it = new Intent();
			it.setClass(activity, SocketService.class);
			activity.stopService(it);
			Toast.makeText(activity, "请确认网络是否开启,操作失败,请重新登录", Toast.LENGTH_LONG)
					.show();
			return;
		}
		byte msgId = Byte.parseByte(action.split("_")[0]);
		byte msgOper = Byte.parseByte(action.split("_")[1]);
		byte[] body = intent.getExtras().getByteArray("data");
		TVActivity tvActivity;
		ACActivity acActivity;
		SwitchActivity switchActivity;
		FansActivity fansActivity;
		HDPlayActivity hdPlayActivity;
		if (msgId == MsgId_E.MSGID_CMD.getVal()
				&& msgOper == MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal()) {
			MsgCmdQryByDevAck_S msgCmdQryByDevAck_S = new MsgCmdQryByDevAck_S();
			msgCmdQryByDevAck_S.setMsgCmdQryByDevAck_S(body);
			if (msgCmdQryByDevAck_S.getUcErr() == 0) {
				List<Cmd_S> list = new ArrayList<Cmd_S>();
				for (int i = 0; i < msgCmdQryByDevAck_S.getUsCnt(); i++) {
					byte[] cmd_S_Byte = Arrays.copyOfRange(
							msgCmdQryByDevAck_S.getPucData(),
							i * Cmd_S.getSize(), (i + 1) * Cmd_S.getSize());
					Cmd_S cmd_S = new Cmd_S();
					cmd_S.setCmd_S(cmd_S_Byte);
					list.add(cmd_S);
				}
				if (device_type == ApplType_E.APPL_TYPE_SWITCH.getVal()) {
					switchActivity = (SwitchActivity) activity;
					switchActivity.cmd_List.addAll(list);
					switchActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_TVSET.getVal()) {
					tvActivity = (TVActivity) activity;
					tvActivity.cmd_List.addAll(list);
					tvActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_HDPLAY.getVal()) {
					hdPlayActivity = (HDPlayActivity) activity;
					hdPlayActivity.cmd_List.addAll(list);
					hdPlayActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_AIRCOND.getVal()) {
					acActivity = (ACActivity) activity;
					acActivity.cmd_List.addAll(list);
					acActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_CURTAIN.getVal()) {
					switchActivity = (SwitchActivity) activity;
					switchActivity.cmd_List.addAll(list);
					switchActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_FAN.getVal()) {
					fansActivity = (FansActivity) activity;
					fansActivity.cmd_List.addAll(list);
					fansActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_WATER.getVal()) {
					switchActivity = (SwitchActivity) activity;
					switchActivity.cmd_List.addAll(list);
					switchActivity.b = true;
				} else if (device_type == ApplType_E.APPL_TYPE_CUSTOM.getVal()) {
					// switchActivity.cmd_List.addAll(list);
				}

				// cmdListActivity.cmdListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgCmdQryByDevAck_S.getUcErr());
			}
		}
	}
}
