package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.ModeCmdListActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.entity.ModeCmd_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class ModeCmdBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public ModeCmdBroadcastReceiver(Activity activity) {
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
		ModeCmdListActivity modeCmdListActivity = (ModeCmdListActivity) activity;
		if (msgId == MsgId_E.MSGID_RGN.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] rgn_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Rgn_S.getSize(),
								(i + 1) * Rgn_S.getSize());
						Rgn_S rgn_S = new Rgn_S();
						rgn_S.setRgn_S(rgn_S_Byte);
						modeCmdListActivity.areaMap
								.put(rgn_S.getUsIdx(), rgn_S);
					}
					modeCmdListActivity.modecmdListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == MsgId_E.MSGID_APPL.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] appl_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Appl_S.getSize(),
								(i + 1) * Appl_S.getSize());
						Appl_S appl_S = new Appl_S();
						appl_S.setAppl_S(appl_S_Byte);
						modeCmdListActivity.deviceMap.put(appl_S.getUsIdx(),
								appl_S);
					}
					modeCmdListActivity.modecmdListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == MsgId_E.MSGID_CMD.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Cmd_S.getSize(),
								(i + 1) * Cmd_S.getSize());
						Cmd_S cmd_S = new Cmd_S();
						cmd_S.setCmd_S(cmd_S_Byte);
						modeCmdListActivity.cmdMap.put(cmd_S.getUsIdx(), cmd_S);
					}
					modeCmdListActivity.modecmdListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(activity, msgQryAck_S.getUsError());
				}
			}
		}
		if (msgId == MsgId_E.MSGID_MODECMD.getVal()
				&& msgOper == MsgOper_E.MSGOPER_DEL.getVal()) {
			MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
			msgDelAck_S.setMsgDelAck_S(body);
			if (msgDelAck_S.getUsError() == 0) {
				modeCmdListActivity.modecmdList
						.remove(modeCmdListActivity.del_Idx);
				modeCmdListActivity.modecmdListView
						.setAdapter(modeCmdListActivity.modecmdListAdapter);
				Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgDelAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_MODECMD.getVal()
				&& msgOper == MsgOper_E.MSGOPER_MAX.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);

			if (msgQryAck_S.getUsError() == 0) {
				if (msgQryAck_S.getUsCnt() > 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] modecmd_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(),
								i * ModeCmd_S.getSize(),
								(i + 1) * ModeCmd_S.getSize());
						ModeCmd_S modeCmd_S = new ModeCmd_S();
						modeCmd_S.setModeCmd_S(modecmd_Byte);
						modeCmdListActivity.modecmdList.add(modeCmd_S);
					}
					modeCmdListActivity.modecmdListAdapter
							.notifyDataSetChanged();
				}
			} else {
				ErrCode_E.showError(context, msgQryAck_S.getUsError());
			}
		}
	}
}
