package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.CameraListActivity;
import com.mac.smartcontrol.EnterAreaActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Cama_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class CameraBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public CameraBroadcastReceiver(Activity activity) {
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
		CameraListActivity cameraListActivity = null;
		if (msgId == MsgId_E.MSGID_RGN.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
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
				}
			} else {
				ErrCode_E.showError(context, msgQryAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_CAMA.getVal()
				&& msgOper == MsgOper_E.MSGOPER_DEL.getVal()) {
			MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
			msgDelAck_S.setMsgDelAck_S(body);
			cameraListActivity = (CameraListActivity) activity;
			if (msgDelAck_S.getUsError() == 0) {
				for (int i = 0; i < cameraListActivity.cameraList.size(); i++) {
					Cama_S cama_S = cameraListActivity.cameraList.get(i);
					if (cama_S.getUsIdx() == msgDelAck_S.getUsIdx()) {
						cameraListActivity.cameraList.remove(i);
						cameraListActivity.cameraListAdapter
								.notifyDataSetChanged();
					}
				}
				Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgDelAck_S.getUsError());
			}
		}

		if ((msgId == MsgId_E.MSGID_CAMA.getVal() && msgOper == MsgOper_E.MSGOPER_QRY
				.getVal())
				|| (msgId == MsgId_E.MSGID_CAMA.getVal() && msgOper == MsgOper_E.MSGOPER_MAX
						.getVal())) {
			parseToList(msgId, msgOper, body);
		}

	}

	public void parseToList(short msgId, byte msgOper, byte[] body) {
		MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
		msgQryAck_S.setMsgQryAck_S(body);
		CameraListActivity cameraListActivity = (CameraListActivity) activity;
		if (msgQryAck_S.getUsCnt() > 0) {
			if (msgQryAck_S.getUsError() == 0) {
				for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
					byte[] cama_S_Byte = Arrays.copyOfRange(
							msgQryAck_S.getPucData(), i * Cama_S.getSize(),
							(i + 1) * Cama_S.getSize());
					Cama_S cama_S = new Cama_S();
					cama_S.setCama_S(cama_S_Byte);
					cameraListActivity.cameraList.add(cama_S);
				}
				cameraListActivity.cameraListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgQryAck_S.getUsError());
			}
		}
	}
}
