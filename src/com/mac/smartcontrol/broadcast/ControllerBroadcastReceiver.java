package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.ControllerListActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Ctrl_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgModAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class ControllerBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public ControllerBroadcastReceiver(Activity activity) {
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
		ControllerListActivity controllerListActivity = (ControllerListActivity) activity;

		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOper_E.MSGOPER_DEL.getVal()) {
			MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
			msgDelAck_S.setMsgDelAck_S(body);
			if (msgDelAck_S.getUsError() == 0) {
				controllerListActivity.ctrlList
						.remove(controllerListActivity.del_Idx);
				// controllerListActivity.controllerListAdapter.notifyDataSetChanged();
				controllerListActivity.ctrlListView
						.setAdapter(controllerListActivity.controllerListAdapter);
				Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgDelAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOper_E.MSGOPER_MOD.getVal()) {

			MsgModAck_S msgModAck_S = new MsgModAck_S();
			msgModAck_S.setMsgModAck_S(body);
			if (msgModAck_S.getUsError() == 0) {
				controllerListActivity.ctrlList.set(
						controllerListActivity.mod_Idx,
						controllerListActivity.ctrl_S);
				controllerListActivity.ctrlListView
						.setAdapter(controllerListActivity.controllerListAdapter);
				// controllerListActivity.controllerListAdapter.notifyDataSetChanged();
				Toast.makeText(activity, "修改成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgModAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			parseToList(body);
		}
	}

	public void parseToList(byte[] body) {
		MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
		msgQryAck_S.setMsgQryAck_S(body);
		ControllerListActivity controllerListActivity = (ControllerListActivity) activity;
		if (msgQryAck_S.getUsCnt() > 0) {
			if (msgQryAck_S.getUsError() == 0) {
				for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
					byte[] ctrl_S_Byte = Arrays.copyOfRange(
							msgQryAck_S.getPucData(), i * Ctrl_S.getSize(),
							(i + 1) * Ctrl_S.getSize());
					Ctrl_S ctrl_S = new Ctrl_S();
					ctrl_S.setCtrl_S(ctrl_S_Byte);
					controllerListActivity.ctrlList.add(ctrl_S);
				}
				controllerListActivity.controllerListAdapter
						.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgQryAck_S.getUsError());
			}
		}
	}
}
