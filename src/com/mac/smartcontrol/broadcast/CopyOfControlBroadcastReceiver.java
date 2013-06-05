package com.mac.smartcontrol.broadcast;

import java.util.ArrayList;
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
import com.mac.smartcontrol.util.FormatTransfer;

import define.entity.Cmd_S;
import define.oper.MsgOperSql_E;
import define.oper.body.ack.MsgSqlExcAck_S;
import define.type.ApplType_E;
import define.type.MsgId_E;

public class CopyOfControlBroadcastReceiver extends BroadcastReceiver {
	Activity activity;
	byte device_type;

	public CopyOfControlBroadcastReceiver(Activity activity, byte device_type) {
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
		if (msgId == MsgId_E.MSGID_SQL.getVal()
				&& msgOper == MsgOperSql_E.MSGOPER_MAX.getVal()) {
			MsgSqlExcAck_S msgSqlExcAck_S = new MsgSqlExcAck_S();
			msgSqlExcAck_S.setMsgSqlExcAck_S(body);
			if (msgSqlExcAck_S.getUsError() == 0) {
				List<Cmd_S> list = new ArrayList<Cmd_S>();
				for (int i = 0; i < msgSqlExcAck_S.getUsRow(); i++) {
					byte[] b = new byte[Cmd_S.getSize()];
					System.arraycopy(msgSqlExcAck_S.getPucData(),
							i * Cmd_S.getSize(), b, 0, Cmd_S.getSize());
					Cmd_S cmd_S = new Cmd_S();
					int start = 0;
					for (int j = 0; j < msgSqlExcAck_S.getUsCol(); j++) {
						byte[] len_b = new byte[2];
						System.arraycopy(b, start, len_b, 0, 2);
						short len = FormatTransfer.lBytesToShort(len_b);

						byte[] str_b = new byte[len];
						System.arraycopy(b, start + 2 * (j + 1), len_b, 0, len);
						String field = new String(str_b);
						switch (j) {
						case 0:
							cmd_S.setUsIdx(Short.parseShort(field));
							break;
						case 1:
							cmd_S.setUcDevType(Byte.parseByte(field));
							break;
						case 2:
							cmd_S.setUsDevIdx(Short.parseShort(field));
							break;
						case 3:
							cmd_S.setSzName(field);
							break;
						case 4:
							cmd_S.setSzVoice(field);
							break;
						case 5:
							cmd_S.setUsCtrlIdx(Short.parseShort(field));
							break;
						case 6:
							cmd_S.setUcType(Byte.parseByte(field));
							break;
						case 7:
							cmd_S.setUcCode(Byte.parseByte(field));
							break;
						case 8:
							cmd_S.setUiPara(Integer.parseInt(field));
							break;
						default:
							break;
						}
						start += len;
					}
					list.add(cmd_S);
				}
				if (device_type == ApplType_E.APPL_TYPE_SWITCH.getVal()) {
					switchActivity = (SwitchActivity) activity;
					switchActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_TVSET.getVal()) {
					tvActivity = (TVActivity) activity;
					// tvActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_HDPLAY.getVal()) {
					hdPlayActivity = (HDPlayActivity) activity;
					// hdPlayActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_AIRCOND.getVal()) {
					acActivity = (ACActivity) activity;
					// acActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_CURTAIN.getVal()) {
					switchActivity = (SwitchActivity) activity;
					switchActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_FAN.getVal()) {
					fansActivity = (FansActivity) activity;
					// fansActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_WATER.getVal()) {
					switchActivity = (SwitchActivity) activity;
					switchActivity.cmd_List.addAll(list);
				} else if (device_type == ApplType_E.APPL_TYPE_CUSTOM.getVal()) {
					// switchActivity.cmd_List.addAll(list);
				}

				// cmdListActivity.cmdListAdapter.notifyDataSetChanged();
			} else {
				// ErrCode_E.showError(activity,
				// msgCmdQryByDevAck_S.getUcErr());
			}
		}
	}
}
