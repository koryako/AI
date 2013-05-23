package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.EnterAreaActivity;
import com.mac.smartcontrol.SenseListActivity;
import com.mac.smartcontrol.SenseLogListActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Rgn_S;
import define.entity.SensLog_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class SenseBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public SenseBroadcastReceiver(Activity activity) {
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
		SenseListActivity senseListActivity = null;
		EnterAreaActivity enterAreaActivity = null;
		SenseLogListActivity senseLogListActivity = null;
		if (msgId == MsgId_E.MSGID_SENSLOG.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			senseLogListActivity = (SenseLogListActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] sensLog_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(),
								i * SensLog_S.getSize(),
								(i + 1) * SensLog_S.getSize());
						SensLog_S sensLog_S = new SensLog_S();
						sensLog_S.setSensLog_S(sensLog_S_Byte);
						senseLogListActivity.senseLogList.add(sensLog_S);
					}
					senseLogListActivity.senseLogListAdapter
							.notifyDataSetChanged();
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

		if (msgId == MsgId_E.MSGID_RGN.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			enterAreaActivity = (EnterAreaActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					enterAreaActivity.areaList.clear();
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
		if (msgId == MsgId_E.MSGID_SENS.getVal()
				&& msgOper == MsgOper_E.MSGOPER_DEL.getVal()) {
			MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
			msgDelAck_S.setMsgDelAck_S(body);
			senseListActivity = (SenseListActivity) activity;
			if (msgDelAck_S.getUsError() == 0) {
				senseListActivity.senseList.remove(senseListActivity.del_Idx);
				senseListActivity.senseListView
						.setAdapter(senseListActivity.senseListAdapter);
				Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgDelAck_S.getUsError());
			}
		}

		if ((msgId == MsgId_E.MSGID_SENS.getVal() && msgOper == MsgOper_E.MSGOPER_QRY
				.getVal())
				|| (msgId == MsgId_E.MSGID_SENS.getVal() && msgOper == MsgOper_E.MSGOPER_MAX
						.getVal())) {
			parseToList(body);
		}

	}

	public void parseToList(byte[] body) {
		MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
		msgQryAck_S.setMsgQryAck_S(body);
		SenseListActivity senseListActivity = (SenseListActivity) activity;
		if (msgQryAck_S.getUsCnt() > 0) {
			if (msgQryAck_S.getUsError() == 0) {
				for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
					byte[] sens_S_Byte = Arrays.copyOfRange(
							msgQryAck_S.getPucData(), i * Sens_S.getSize(),
							(i + 1) * Sens_S.getSize());
					Sens_S sens_S = new Sens_S();
					sens_S.setSens_S(sens_S_Byte);
					senseListActivity.senseList.add(sens_S);
				}
				senseListActivity.senseListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgQryAck_S.getUsError());
			}
		}
	}
}
