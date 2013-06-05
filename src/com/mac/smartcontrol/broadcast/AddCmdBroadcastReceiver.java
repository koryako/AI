package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mac.smartcontrol.AddCmdActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Cmd_S;
import define.entity.Ctrl_S;
import define.entity.Mode_S;
import define.oper.MsgOperCmd_E;
import define.oper.MsgOperCtrl_E;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgAddAck_S;
import define.oper.body.ack.MsgCmdQryByDevAck_S;
import define.oper.body.ack.MsgCtrlStudyAck_S;
import define.oper.body.ack.MsgCtrlTestAck_S;
import define.oper.body.ack.MsgModeQryByRgnAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class AddCmdBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public AddCmdBroadcastReceiver(Activity activity) {
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
		AddCmdActivity addCmdActivity = (AddCmdActivity) activity;
		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] ctrl_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Ctrl_S.getSize(),
								(i + 1) * Ctrl_S.getSize());
						Ctrl_S ctrl_S = new Ctrl_S();
						ctrl_S.setCtrl_S(ctrl_S_Byte);
						addCmdActivity.ctrlList.add(ctrl_S);
						addCmdActivity.ctrlNameList.add(ctrl_S.getSzName());
					}
					addCmdActivity.ctrl_adapter.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOperCtrl_E.MSGOPER_CTRL_TEST.getVal()) {
			MsgCtrlTestAck_S msgCtrlTestAck_S = new MsgCtrlTestAck_S();
			msgCtrlTestAck_S.setMsgCtrlTestAck_S(body);
			if (msgCtrlTestAck_S.getUsError() == 0) {
				Toast.makeText(context, "测试成功", Toast.LENGTH_SHORT).show();
			} else {
				ErrCode_E.showError(context, msgCtrlTestAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOperCtrl_E.MSGOPER_CTRL_STUDY.getVal()) {
			MsgCtrlStudyAck_S msgCtrlStudyAck_S = new MsgCtrlStudyAck_S();
			msgCtrlStudyAck_S.setMsgCtrlStudyAck_S(body);
			if (msgCtrlStudyAck_S.getUsError() == 0) {
				addCmdActivity.usCmdIdx = msgCtrlStudyAck_S.getUsCmdIdx();
				addCmdActivity.ucOffset = msgCtrlStudyAck_S.getUcOffset();
				addCmdActivity.infrared_code_tv.setText(msgCtrlStudyAck_S
						.getUcOffset() + "");
				Toast.makeText(context, "学习成功", Toast.LENGTH_SHORT).show();
			} else {
				ErrCode_E.showError(context, msgCtrlStudyAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_CMD.getVal()
				&& msgOper == MsgOper_E.MSGOPER_ADD.getVal()) {
			MsgAddAck_S msgAddAck_S = new MsgAddAck_S();
			msgAddAck_S.setMsgAddAck_S(body);
			if (msgAddAck_S.getUsError() == 0) {
				Toast.makeText(activity, "添加成功", Toast.LENGTH_LONG).show();
				addCmdActivity.cmd_S.setUsIdx(msgAddAck_S.getUsIdx());
				Bundle bundle = new Bundle();
				bundle.putByteArray("cmd", addCmdActivity.cmd_S.getCmd_S());
				Intent i = new Intent();
				i.putExtras(bundle);
				// // 返回intent
				addCmdActivity.setResult(Activity.RESULT_OK, i);
				addCmdActivity.finish();
			} else {
				ErrCode_E.showError(context, msgAddAck_S.getUsError());
			}
		}

		if ((msgId == MsgId_E.MSGID_CMD.getVal() && msgOper == MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV
				.getVal())
				|| (msgId == MsgId_E.MSGID_CMD.getVal() && msgOper == MsgOper_E.MSGOPER_MAX
						.getVal())) {
			MsgCmdQryByDevAck_S msgCmdQryByDevAck_S = new MsgCmdQryByDevAck_S();
			msgCmdQryByDevAck_S.setMsgCmdQryByDevAck_S(body);
			if (msgCmdQryByDevAck_S.getUsCnt() > 0) {
				if (msgCmdQryByDevAck_S.getUcErr() == 0) {
					for (int i = 0; i < msgCmdQryByDevAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgCmdQryByDevAck_S.getPucData(),
								i * Cmd_S.getSize(), (i + 1) * Cmd_S.getSize());
						Cmd_S cmd_S = new Cmd_S();
						cmd_S.setCmd_S(cmd_S_Byte);
						addCmdActivity.cmdList.add(cmd_S);
						addCmdActivity.cmdNameList.add(cmd_S.getSzName());
					}
					addCmdActivity.cmd_adapter.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(activity,
							msgCmdQryByDevAck_S.getUcErr());
				}
			}
		}

		if ((msgId == MsgId_E.MSGID_MODE.getVal() && msgOper == MsgOper_E.MSGOPER_QRY
				.getVal())
				|| (msgId == MsgId_E.MSGID_MODE.getVal() && msgOper == MsgOper_E.MSGOPER_MAX
						.getVal())) {
			MsgModeQryByRgnAck_S msgModeQryByRgnAck_S = new MsgModeQryByRgnAck_S();
			msgModeQryByRgnAck_S.setMsgModeQryByRgnAck_S(body);
			if (msgModeQryByRgnAck_S.getUsCnt() > 0) {
				if (msgModeQryByRgnAck_S.getUcErr() == 0) {
					for (int i = 0; i < msgModeQryByRgnAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgModeQryByRgnAck_S.getPucData(),
								i * Mode_S.getSize(),
								(i + 1) * Mode_S.getSize());
						Mode_S mode_S = new Mode_S();
						mode_S.setMode_S(cmd_S_Byte);
						addCmdActivity.modeList.add(mode_S);
						addCmdActivity.modeNameList.add(mode_S.getSzName());
					}
					addCmdActivity.mode_adapter.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(activity,
							msgModeQryByRgnAck_S.getUcErr());
				}
			}
		}

	}

}
