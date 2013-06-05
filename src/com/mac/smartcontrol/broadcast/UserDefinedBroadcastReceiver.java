package com.mac.smartcontrol.broadcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.UserDefinedActivity;

import define.entity.Cmd_S;
import define.oper.MsgOperCmd_E;
import define.oper.body.ack.MsgCmdQryByDevAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class UserDefinedBroadcastReceiver extends BroadcastReceiver {
	Activity activity;

	public UserDefinedBroadcastReceiver(Activity activity) {
		super();
		this.activity = activity;
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
		UserDefinedActivity userDefinedActivity = (UserDefinedActivity) activity;
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
				userDefinedActivity.cmd_List.addAll(list);
				userDefinedActivity.init_Btn();
			} else {
				ErrCode_E.showError(activity, msgCmdQryByDevAck_S.getUcErr());
			}
		}
	}
}
