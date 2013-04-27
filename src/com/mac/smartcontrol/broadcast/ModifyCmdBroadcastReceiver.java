package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mac.smartcontrol.ModifyCmdActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Ctrl_S;
import define.oper.body.ack.MsgModAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;

public class ModifyCmdBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public ModifyCmdBroadcastReceiver(Activity activity) {
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
		ModifyCmdActivity modifyCmdActivity = (ModifyCmdActivity) activity;
		if (msgId == 2 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					int select_idx = 0;
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] ctrl_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Ctrl_S.getSize(),
								(i + 1) * Ctrl_S.getSize());
						Ctrl_S ctrl_S = new Ctrl_S();
						ctrl_S.setCtrl_S(ctrl_S_Byte);
						modifyCmdActivity.ctrlList.add(ctrl_S);
						modifyCmdActivity.ctrlNameList.add(ctrl_S.getSzName());
						if (ctrl_S.getUsIdx() == modifyCmdActivity.cmd_S
								.getUsCtrlIdx()) {
							select_idx = i;
						}
					}
					modifyCmdActivity.ctrl_adapter.notifyDataSetChanged();
					modifyCmdActivity.ctrl_sp.setSelection(select_idx);
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}
		if (msgOper == 3) {
			MsgModAck_S msgModAck_S = new MsgModAck_S();
			msgModAck_S.setMsgModAck_S(body);
			if (msgModAck_S.getUsError() == 0) {
				Toast.makeText(activity, "修改成功", Toast.LENGTH_LONG).show();
				Bundle bundle = new Bundle();
				bundle.putByteArray("cmd", modifyCmdActivity.cmd_S.getCmd_S());
				Intent i = new Intent();
				i.putExtras(bundle);
				// // 返回intent
				modifyCmdActivity.setResult(Activity.RESULT_OK, i);
				modifyCmdActivity.finish();
			} else {
				ErrCode_E.showError(context, msgModAck_S.getUsError());
			}
		}

	}

}
