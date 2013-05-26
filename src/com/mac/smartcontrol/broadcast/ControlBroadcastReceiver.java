package com.mac.smartcontrol.broadcast;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.SocketService;

import define.entity.Cmd_S;
import define.oper.MsgOperSql_E;
import define.oper.body.ack.MsgSqlExcAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class ControlBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public ControlBroadcastReceiver(Activity activity) {
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

		short msgId = Short.parseShort(action.split("_")[0]);
		byte msgOper = Byte.parseByte(action.split("_")[1]);
		byte[] body = intent.getExtras().getByteArray("data");
		if (msgId == MsgId_E.MSGID_SQL.getVal()
				&& msgOper == MsgOperSql_E.MSGOPER_MAX.getVal()) {
		}

	}

	public void parseToList(byte[] body) {
		MsgSqlExcAck_S msgSqlExcAck_S = new MsgSqlExcAck_S();
		msgSqlExcAck_S.setMsgSqlExcAck_S(body);
		List<Cmd_S> list = new ArrayList<Cmd_S>();
		if (msgSqlExcAck_S.getUsError() == 0) {
			for (int i = 0; i < msgSqlExcAck_S.getUsRow(); i++) {
				for (int j = 0; j < msgSqlExcAck_S.getUsCol(); j++) {

				}
			}
		} else {
			ErrCode_E.showError(activity, msgSqlExcAck_S.getUsError());
		}
	}
}
