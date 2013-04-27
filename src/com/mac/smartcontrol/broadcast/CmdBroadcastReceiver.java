package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.CmdListActivity;
import com.mac.smartcontrol.EnterDeviceListActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.entity.Ctrl_S;
import define.entity.Rgn_S;
import define.oper.body.ack.MsgCmdQryByDevAck_S;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;

public class CmdBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public CmdBroadcastReceiver(Activity activity) {
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
		CmdListActivity cmdListActivity = null;
		EnterDeviceListActivity enterDeviceListActivity;
		if (msgId == 2 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			cmdListActivity = (CmdListActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] ctrl_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Ctrl_S.getSize(),
								(i + 1) * Ctrl_S.getSize());
						Ctrl_S ctrl_S = new Ctrl_S();
						ctrl_S.setCtrl_S(ctrl_S_Byte);
						cmdListActivity.ctrlMap.put(ctrl_S.getUsIdx(), ctrl_S);
					}
					// addSenseActivity.areaListAdapter.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}
		if (msgId == 3 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			enterDeviceListActivity = (EnterDeviceListActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] rgn_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Rgn_S.getSize(),
								(i + 1) * Rgn_S.getSize());
						Rgn_S rgn_S = new Rgn_S();
						rgn_S.setRgn_S(rgn_S_Byte);
						enterDeviceListActivity.areaMap.put(rgn_S.getUsIdx(),
								rgn_S);
					}
					// enterDeviceListActivity.deviceListAdapter.notifyDataSetChanged();
				}
			}
		}

		if (msgId == 4 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			enterDeviceListActivity = (EnterDeviceListActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] appl_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Appl_S.getSize(),
								(i + 1) * Appl_S.getSize());
						Appl_S appl_S = new Appl_S();
						appl_S.setAppl_S(appl_S_Byte);
						enterDeviceListActivity.deviceList.add(appl_S);
					}
					enterDeviceListActivity.deviceListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == 4 && msgOper == 5) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			enterDeviceListActivity = (EnterDeviceListActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] appl_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Appl_S.getSize(),
								(i + 1) * Appl_S.getSize());
						Appl_S appl_S = new Appl_S();
						appl_S.setAppl_S(appl_S_Byte);
						enterDeviceListActivity.deviceList.add(appl_S);
					}
					enterDeviceListActivity.deviceListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == 5) {
			switch (msgOper) {
			case 2:
				MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
				msgDelAck_S.setMsgDelAck_S(body);
				cmdListActivity = (CmdListActivity) activity;
				if (msgDelAck_S.getUsError() == 0) {
					for (int i = 0; i < cmdListActivity.cmdList.size(); i++) {
						Cmd_S cmd_S = cmdListActivity.cmdList.get(i);
						if (cmd_S.getUsIdx() == msgDelAck_S.getUsIdx()) {
							cmdListActivity.cmdList.remove(i);
							cmdListActivity.cmdListAdapter
									.notifyDataSetChanged();
						}
					}
					Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
				} else {
					ErrCode_E.showError(context, msgDelAck_S.getUsError());
				}
				break;
			case 4:

				break;
			case 5:
				parseToList(body);
				break;
			default:
				break;
			}

		}

	}

	public void parseToList(byte[] body) {
		MsgCmdQryByDevAck_S msgCmdQryByDevAck_S = new MsgCmdQryByDevAck_S();
		msgCmdQryByDevAck_S.setMsgCmdQryByDevAck_S(body);
		CmdListActivity cmdListActivity = (CmdListActivity) activity;
		if (msgCmdQryByDevAck_S.getUsCnt() > 0) {
			if (msgCmdQryByDevAck_S.getUcErr() == 0) {
				for (int i = 0; i < msgCmdQryByDevAck_S.getUsCnt(); i++) {
					byte[] cmd_S_Byte = Arrays.copyOfRange(
							msgCmdQryByDevAck_S.getPucData(),
							i * Cmd_S.getSize(), (i + 1) * Cmd_S.getSize());
					Cmd_S cmd_S = new Cmd_S();
					cmd_S.setCmd_S(cmd_S_Byte);
					cmdListActivity.cmdList.add(cmd_S);
				}
				cmdListActivity.cmdListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgCmdQryByDevAck_S.getUcErr());
			}
		}
	}
}
