package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.DeviceListActivity;
import com.mac.smartcontrol.EnterAreaActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Appl_S;
import define.entity.Rgn_S;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;

public class DeviceBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public DeviceBroadcastReceiver(Activity activity) {
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
		EnterAreaActivity enterAreaActivity = null;
		DeviceListActivity deviceListActivity = null;
		if (msgId == 3 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			enterAreaActivity = (EnterAreaActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] rgn_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Rgn_S.getSize(),
								(i + 1) * Rgn_S.getSize());
						Rgn_S rgn_S = new Rgn_S();
						rgn_S.setRgn_S(rgn_S_Byte);
						enterAreaActivity.areaList.add(rgn_S);
					}
					enterAreaActivity.enterAreaListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}
		switch (msgOper) {
		case 2:
			MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
			msgDelAck_S.setMsgDelAck_S(body);
			deviceListActivity = (DeviceListActivity) activity;
			if (msgDelAck_S.getUsError() == 0) {
				for (int i = 0; i < deviceListActivity.deviceList.size(); i++) {
					Appl_S appl_S = deviceListActivity.deviceList.get(i);
					if (appl_S.getUsIdx() == msgDelAck_S.getUsIdx()) {
						deviceListActivity.deviceList.remove(i);
						deviceListActivity.deviceListAdapter
								.notifyDataSetChanged();
					}
				}
				Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgDelAck_S.getUsError());
			}
			break;
		case 4:
		case 5:
			parseToList(msgId, msgOper, body);
			break;
		default:
			break;
		}

	}

	public void parseToList(short msgId, byte msgOper, byte[] body) {
		MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
		msgQryAck_S.setMsgQryAck_S(body);
		DeviceListActivity deviceListActivity = (DeviceListActivity) activity;
		if (msgQryAck_S.getUsCnt() > 0) {
			if (msgQryAck_S.getUsError() == 0) {
				for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
					byte[] appl_S_Byte = Arrays.copyOfRange(
							msgQryAck_S.getPucData(), i * Appl_S.getSize(),
							(i + 1) * Appl_S.getSize());
					Appl_S appl_S = new Appl_S();
					appl_S.setAppl_S(appl_S_Byte);
					deviceListActivity.deviceList.add(appl_S);
				}
				deviceListActivity.deviceListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgQryAck_S.getUsError());
			}
		}
	}
}
