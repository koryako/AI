package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mac.smartcontrol.AddModeActivity;
import com.mac.smartcontrol.EnterModeListActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Mode_S;
import define.entity.Rgn_S;
import define.oper.body.ack.MsgAddAck_S;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgModeQryByRgnAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class EnterModeBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public EnterModeBroadcastReceiver(Activity activity) {
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
		EnterModeListActivity modeListActivity = null;
		AddModeActivity addModeActivity = null;
		if (msgId == 3 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			addModeActivity = (AddModeActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] rgn_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Rgn_S.getSize(),
								(i + 1) * Rgn_S.getSize());
						Rgn_S rgn_S = new Rgn_S();
						rgn_S.setRgn_S(rgn_S_Byte);
						addModeActivity.areaList.add(rgn_S);
						addModeActivity.areaNameList.add(rgn_S.getSzName());
					}
					addModeActivity.area_adapter.notifyDataSetChanged();
				}
			}
		}

		if (msgId == MsgId_E.MSGID_MODE.getVal()) {
			switch (msgOper) {
			case 1:
				MsgAddAck_S msgAddAck_S = new MsgAddAck_S();
				msgAddAck_S.setMsgAddAck_S(body);
				if (msgAddAck_S.getUsError() == 0) {
					Toast.makeText(activity, "添加成功", Toast.LENGTH_LONG).show();
					addModeActivity = (AddModeActivity) activity;
					addModeActivity.mode_S.setUsIdx(msgAddAck_S.getUsIdx());
					Bundle bundle = new Bundle();
					bundle.putByteArray("mode",
							addModeActivity.mode_S.getMode_S());
					Intent i = new Intent();
					i.putExtras(bundle);
					// 返回intent
					addModeActivity.setResult(Activity.RESULT_OK, i);
					addModeActivity.finish();
				} else {
					ErrCode_E.showError(context, msgAddAck_S.getUsError());
				}
				break;
			case 2:
				MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
				msgDelAck_S.setMsgDelAck_S(body);
				modeListActivity = (EnterModeListActivity) activity;
				if (msgDelAck_S.getUsError() == 0) {
					for (int i = 0; i < modeListActivity.modeList.size(); i++) {

						Mode_S mode_S = modeListActivity.modeList.get(i);
						if (mode_S.getUsIdx() == msgDelAck_S.getUsIdx()) {
							modeListActivity.modeList.remove(i);
							modeListActivity.modeListAdapter
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
				parseToList(body);
				break;
			default:
				break;
			}

		}
	}

	public void parseToList(byte[] body) {
		MsgModeQryByRgnAck_S msgModeQryByRgnAck_S = new MsgModeQryByRgnAck_S();
		msgModeQryByRgnAck_S.setMsgModeQryByRgnAck_S(body);
		EnterModeListActivity modeListActivity = (EnterModeListActivity) activity;
		if (msgModeQryByRgnAck_S.getUsCnt() > 0) {
			if (msgModeQryByRgnAck_S.getUcErr() == 0) {
				for (int i = 0; i < msgModeQryByRgnAck_S.getUsCnt(); i++) {
					byte[] cmd_S_Byte = Arrays.copyOfRange(
							msgModeQryByRgnAck_S.getPucData(),
							i * Mode_S.getSize(), (i + 1) * Mode_S.getSize());
					Mode_S mode_S = new Mode_S();
					mode_S.setMode_S(cmd_S_Byte);
					modeListActivity.modeList.add(mode_S);
				}
				modeListActivity.modeListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgModeQryByRgnAck_S.getUcErr());
			}
		}
	}
}
