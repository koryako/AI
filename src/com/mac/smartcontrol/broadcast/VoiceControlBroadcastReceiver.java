package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.MainActivity;
import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Cmd_S;
import define.entity.Mode_S;
import define.oper.MsgOperCmd_E;
import define.oper.MsgOperMode_E;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class VoiceControlBroadcastReceiver extends BroadcastReceiver {
	Activity activity;

	public VoiceControlBroadcastReceiver(Activity activity) {
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
		// if (msgId == MsgId_E.MSGID_SQL.getVal()
		// && msgOper == MsgOperSql_E.MSGOPER_MAX.getVal()) {
		// MsgSqlExcAck_S msgSqlExcAck_S = new MsgSqlExcAck_S();
		// msgSqlExcAck_S.setMsgSqlExcAck_S(body);
		// if (msgSqlExcAck_S.getUsError() == 0) {
		// byte[] b = new byte[Cmd_S.getSize()];
		// System.arraycopy(msgSqlExcAck_S.getPucData(), 0, b, 0,
		// Cmd_S.getSize());
		// Cmd_S cmd_S = new Cmd_S();
		// int start = 0;
		// for (int j = 0; j < msgSqlExcAck_S.getUsCol(); j++) {
		// byte[] len_b = new byte[2];
		// System.arraycopy(b, start, len_b, 0, 2);
		// short len = FormatTransfer.lBytesToShort(len_b);
		//
		// byte[] str_b = new byte[len];
		// System.arraycopy(b, start + 2 * (j + 1), len_b, 0, len);
		// String field = new String(str_b);
		// switch (j) {
		// case 0:
		// cmd_S.setUsIdx(Short.parseShort(field));
		// break;
		// case 1:
		// cmd_S.setUcDevType(Byte.parseByte(field));
		// break;
		// case 2:
		// cmd_S.setUsDevIdx(Short.parseShort(field));
		// break;
		// case 3:
		// cmd_S.setSzName(field);
		// break;
		// case 4:
		// cmd_S.setSzVoice(field);
		// break;
		// case 5:
		// cmd_S.setUsCtrlIdx(Short.parseShort(field));
		// break;
		// case 6:
		// cmd_S.setUcType(Byte.parseByte(field));
		// break;
		// case 7:
		// cmd_S.setUcCode(Byte.parseByte(field));
		// break;
		// case 8:
		// cmd_S.setUiPara(Integer.parseInt(field));
		// break;
		// default:
		// break;
		// }
		// }
		//
		// try {
		// WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
		// MsgType_E.MSGTYPE_REQ.getVal(),
		// MsgOperCmd_E.MSGOPER_CMD_EXC.getVal(),
		// Cmd_S.getSize(), cmd_S.getCmd_S());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(context, "请确认网络是否开启,连接失败", Toast.LENGTH_LONG)
		// .show();
		// }
		// } else {
		// ErrCode_E.showError(activity, msgSqlExcAck_S.getUsError());
		// }
		//
		// }

		if (msgId == MsgId_E.MSGID_CMD.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MainActivity mainActivity = (MainActivity) activity;
			if (mainActivity.voiceName == null) {
				return;
			}
			if (mainActivity.voiceName.size() > 0) {
				return;
			}
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			int tag = -1;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Cmd_S.getSize(),
								(i + 1) * Cmd_S.getSize());
						Cmd_S cmd_S = new Cmd_S();
						cmd_S.setCmd_S(cmd_S_Byte);
						for (int j = 0; j < mainActivity.voiceName.size(); j++) {
							if (cmd_S.getSzVoice().equals(
									mainActivity.voiceName.get(j))) {
								tag = j;
								// try {
								WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
										MsgType_E.MSGTYPE_REQ.getVal(),
										MsgOperCmd_E.MSGOPER_CMD_EXC.getVal(),
										Cmd_S.getSize(), cmd_S.getCmd_S(),
										context);
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// Toast.makeText(context, "请确认网络是否开启,连接失败",
								// Toast.LENGTH_LONG).show();
								// }
								mainActivity.voiceName.clear();
								Toast.makeText(
										context,
										"已执行："
												+ mainActivity.voiceName
														.get(tag),
										Toast.LENGTH_LONG).show();
								return;
							}
						}
					}
				} else {
					ErrCode_E.showError(activity, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == MsgId_E.MSGID_MODE.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MainActivity mainActivity = (MainActivity) activity;
			if (mainActivity.voiceName == null) {
				return;
			}
			if (mainActivity.voiceName.size() > 0) {
				return;
			}
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			int tag = -1;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Mode_S.getSize(),
								(i + 1) * Mode_S.getSize());
						Mode_S mode_S = new Mode_S();
						mode_S.setMode_S(cmd_S_Byte);
						for (int j = 0; j < mainActivity.voiceName.size(); j++) {
							if (mode_S.getSzVoice().equals(
									mainActivity.voiceName.get(j))) {
								tag = j;
								// try {
								WriteUtil
										.write(MsgId_E.MSGID_MODE.getVal(), 1,
												MsgType_E.MSGTYPE_REQ.getVal(),
												MsgOperMode_E.MSGOPER_MODE_EXC
														.getVal(), Cmd_S
														.getSize(), mode_S
														.getMode_S(), context);
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// Toast.makeText(context, "请确认网络是否开启,连接失败",
								// Toast.LENGTH_LONG).show();
								// }
								mainActivity.voiceName.clear();
								Toast.makeText(
										context,
										"已执行："
												+ mainActivity.voiceName
														.get(tag),
										Toast.LENGTH_LONG).show();
								return;
							}
						}
					}
				} else {
					ErrCode_E.showError(activity, msgQryAck_S.getUsError());
				}
			}
		}

	}
}
