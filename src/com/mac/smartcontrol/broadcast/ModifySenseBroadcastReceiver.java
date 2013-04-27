package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mac.smartcontrol.ModifySenseActivity;
import com.mac.smartcontrol.SocketService;

import define.entity.Rgn_S;
import define.oper.body.ack.MsgModAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.type.ErrCode_E;

public class ModifySenseBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public ModifySenseBroadcastReceiver(Activity activity) {
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
		ModifySenseActivity modifySenseActivity = (ModifySenseActivity) activity;
		if (msgId == 3 && msgOper == 4) {
			MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
			msgQryAck_S.setMsgQryAck_S(body);
			modifySenseActivity = (ModifySenseActivity) activity;
			if (msgQryAck_S.getUsCnt() > 0) {
				if (msgQryAck_S.getUsError() == 0) {
					int selectIdx = 0;
					for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
						byte[] rgn_S_Byte = Arrays.copyOfRange(
								msgQryAck_S.getPucData(), i * Rgn_S.getSize(),
								(i + 1) * Rgn_S.getSize());
						Rgn_S rgn_S = new Rgn_S();
						rgn_S.setRgn_S(rgn_S_Byte);
						if (modifySenseActivity.sens_S.getUsRgnIdx() == rgn_S
								.getUsIdx()) {
							selectIdx = i;
						}
						modifySenseActivity.areaList.add(rgn_S);
						modifySenseActivity.area_adapter.add(rgn_S.getSzName());
					}
					modifySenseActivity.area_adapter.notifyDataSetChanged();
					modifySenseActivity.area_sp.setSelection(selectIdx);
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}
		if (msgId == 6 && msgOper == 3) {

			MsgModAck_S msgModAck_S = new MsgModAck_S();
			msgModAck_S.setMsgModAck_S(body);
			if (msgModAck_S.getUsError() == 0) {
				Toast.makeText(activity, "修改成功", Toast.LENGTH_LONG).show();

				modifySenseActivity.sens_S.setUsIdx(msgModAck_S.getUsIdx());
				Bundle bundle = new Bundle();
				bundle.putByteArray("sense",
						modifySenseActivity.sens_S.getSens_S());
				Intent i = new Intent();
				i.putExtras(bundle);
				// 返回intent
				modifySenseActivity.setResult(Activity.RESULT_OK, i);
				modifySenseActivity.finish();
			} else {
				ErrCode_E.showError(context, msgModAck_S.getUsError());
			}
		}

	}

}
