package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mac.smartcontrol.application.SmartControlApplication;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.MsgSensTrigger_S;
import define.entity.Rgn_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgQryAck_S;
import define.oper.body.req.MsgQryReq_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class NtfBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		byte[] body = intent.getExtras().getByteArray("data");
		SmartControlApplication smartControlApplication = (SmartControlApplication) context;
		if (action.equals("ntf")) {
			MsgSensTrigger_S sensTriggerNty_S = new MsgSensTrigger_S();
			sensTriggerNty_S.setMsgSensTrigger_S(body);
			if (sensTriggerNty_S.getUsCnt() > 0) {
				smartControlApplication.sense_List.clear();
				for (int i = 0; i < sensTriggerNty_S.getUsCnt(); i++) {
					byte[] sens_S_Byte = Arrays.copyOfRange(
							sensTriggerNty_S.getPucData(),
							i * Sens_S.getSize(), (i + 1) * Sens_S.getSize());
					Sens_S sens_S = new Sens_S();
					sens_S.setSens_S(sens_S_Byte);
					// smartControlApplication.showNTFToast(sens_S);
					smartControlApplication.sense_List.add(sens_S);
				}
				// try {
				WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_QRY.getVal(), (short) 2,
						new MsgQryReq_S((short) 0).getMsgQryReq_S(), context);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Toast.makeText(context, "获取信息失败", Toast.LENGTH_LONG).show();
				// }
			}

		} else if (action.equals("IO_Exception")) {
			DisconnectionUtil.restart(context);
			// smartControlApplication.unRegisterReceiver();
		} else {
			byte msgId = Byte.parseByte(action.split("_")[0]);
			byte msgOper = Byte.parseByte(action.split("_")[1]);
			if (msgId == MsgId_E.MSGID_RGN.getVal()
					&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
				MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
				msgQryAck_S.setMsgQryAck_S(body);
				if (msgQryAck_S.getUsError() == 0) {
					if (msgQryAck_S.getUsCnt() > 0) {
						smartControlApplication.area_Map.clear();
						for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
							byte[] rgn_S_Byte = Arrays.copyOfRange(
									msgQryAck_S.getPucData(),
									i * Rgn_S.getSize(),
									(i + 1) * Rgn_S.getSize());
							Rgn_S rgn_S = new Rgn_S();
							rgn_S.setRgn_S(rgn_S_Byte);
							smartControlApplication.area_Map.put(
									rgn_S.getUsIdx(), rgn_S);
						}
						smartControlApplication.showNTFToast();
					}
				} else {
					ErrCode_E.showError(context, msgQryAck_S.getUsError());
				}
			}
		}

	}
}
