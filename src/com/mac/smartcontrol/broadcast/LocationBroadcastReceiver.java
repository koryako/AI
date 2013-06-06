package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.LocationActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Mode_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgModeQryByRgnAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class LocationBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public LocationBroadcastReceiver(Activity activity) {
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
			Toast.makeText(activity, "ÇëÈ·ÈÏÍøÂçÊÇ·ñ¿ªÆô,²Ù×÷Ê§°Ü,ÇëÖØÐÂµÇÂ¼", Toast.LENGTH_LONG)
					.show();
			return;
		}

		short msgId = Short.parseShort(action.split("_")[0]);
		byte msgOper = Byte.parseByte(action.split("_")[1]);
		byte[] body = intent.getExtras().getByteArray("data");
		LocationActivity locationActivity = (LocationActivity) activity;

		if (msgId == MsgId_E.MSGID_MODE.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgModeQryByRgnAck_S msgModeQryByRgnAck_S = new MsgModeQryByRgnAck_S();
			msgModeQryByRgnAck_S.setMsgModeQryByRgnAck_S(body);
			if (msgModeQryByRgnAck_S.getUcErr() == 0) {
				if (msgModeQryByRgnAck_S.getUsCnt() > 0) {
					int mode_Idx = -1;
					for (int i = 0; i < msgModeQryByRgnAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgModeQryByRgnAck_S.getPucData(),
								i * Mode_S.getSize(),
								(i + 1) * Mode_S.getSize());
						Mode_S mode_S = new Mode_S();
						mode_S.setMode_S(cmd_S_Byte);
						if (mode_S.getUsIdx() == locationActivity.mode_Id) {
							mode_Idx = i;
						}
						locationActivity.modelist.add(mode_S);
						locationActivity.modelistStr.add(mode_S.getSzName());
					}
					locationActivity.mode_adapter.notifyDataSetChanged();
					locationActivity.mode_Sp.setSelection(mode_Idx);
				}
			} else {
				ErrCode_E.showError(activity, msgModeQryByRgnAck_S.getUcErr());
			}
		}
	}

}
