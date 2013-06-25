package com.mac.smartcontrol.broadcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.ACActivity;
import com.mac.smartcontrol.CurtainActivity;
import com.mac.smartcontrol.FansActivity;
import com.mac.smartcontrol.HDPlayActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.SwitchActivity;
import com.mac.smartcontrol.TVActivity;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Cmd_S;
import define.entity.CtrlStatus_S;
import define.oper.Ctrl_StatType_E;
import define.oper.MsgOperCmd_E;
import define.oper.MsgOperCtrl_E;
import define.oper.body.ack.MsgCmdQryByDevAck_S;
import define.oper.body.ack.MsgCtrlStatusAck_S;
import define.oper.body.req.MsgCtrlStatusReq_S;
import define.type.ApplType_E;
import define.type.ErrCode_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ControlBroadcastReceiver extends BroadcastReceiver {
	Activity activity;
	byte device_type;

	public ControlBroadcastReceiver(Activity activity, byte device_type) {
		super();
		this.activity = activity;
		this.device_type = device_type;
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
		TVActivity tvActivity;
		ACActivity acActivity;
		SwitchActivity switchActivity;
		FansActivity fansActivity;
		HDPlayActivity hdPlayActivity;
		CurtainActivity curtainActivity;

		if (msgId == MsgId_E.MSGID_CMD.getVal()
				&& msgOper == MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal()) {
			MsgCmdQryByDevAck_S msgCmdQryByDevAck_S = new MsgCmdQryByDevAck_S();
			msgCmdQryByDevAck_S.setMsgCmdQryByDevAck_S(body);
			if (msgCmdQryByDevAck_S.getUcErr() == 0) {
				List<Cmd_S> list = new ArrayList<Cmd_S>();
				if (msgCmdQryByDevAck_S.getUsCnt() > 0) {
					for (int i = 0; i < msgCmdQryByDevAck_S.getUsCnt(); i++) {
						byte[] cmd_S_Byte = Arrays.copyOfRange(
								msgCmdQryByDevAck_S.getPucData(),
								i * Cmd_S.getSize(), (i + 1) * Cmd_S.getSize());
						Cmd_S cmd_S = new Cmd_S();
						cmd_S.setCmd_S(cmd_S_Byte);

						if (cmd_S.getSzName().equals("开")
								|| cmd_S.getSzName().equals("关")) {
							MsgCtrlStatusReq_S ctrlStatusReq_S = new MsgCtrlStatusReq_S();
							ctrlStatusReq_S.setUsIdx(cmd_S.getUsCtrlIdx());
							ctrlStatusReq_S.setUsCnt((short) 1);
							CtrlStatus_S ctrlStatus_S = new CtrlStatus_S();
							ctrlStatus_S
									.setUcType(Ctrl_StatType_E.CTRL_STATTYPE_SWITCH
											.getVal());
							ctrlStatusReq_S.pucData.add(ctrlStatus_S);
							WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(), 9,
									MsgType_E.MSGTYPE_REQ.getVal(),
									MsgOperCtrl_E.MSGOPER_CTRL_STATUS.getVal(),
									ctrlStatusReq_S.getSize(),
									ctrlStatusReq_S.getMsgCtrlStatusReq_S(),
									context);
						}
						list.add(cmd_S);
					}
					if (device_type == ApplType_E.APPL_TYPE_SWITCH.getVal()) {
						switchActivity = (SwitchActivity) activity;
						switchActivity.cmd_List.addAll(list);
						switchActivity.b = true;

					} else if (device_type == ApplType_E.APPL_TYPE_TVSET
							.getVal()) {
						tvActivity = (TVActivity) activity;
						tvActivity.cmd_List.addAll(list);
						tvActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_HDPLAY
							.getVal()) {
						hdPlayActivity = (HDPlayActivity) activity;
						hdPlayActivity.cmd_List.addAll(list);
						hdPlayActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_AIRCOND
							.getVal()) {
						acActivity = (ACActivity) activity;
						acActivity.cmd_List.addAll(list);
						acActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_CURTAIN
							.getVal()) {
						curtainActivity = (CurtainActivity) activity;
						curtainActivity.cmd_List.addAll(list);
						curtainActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_FAN.getVal()) {
						fansActivity = (FansActivity) activity;
						fansActivity.cmd_List.addAll(list);
						fansActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_WATER
							.getVal()) {
						switchActivity = (SwitchActivity) activity;
						switchActivity.cmd_List.addAll(list);
						switchActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_CUSTOM
							.getVal()) {
						// switchActivity.cmd_List.addAll(list);
						// switchActivity = (SwitchActivity) activity;
						// switchActivity.cmd_List.addAll(list);
					}
				} else {
					if (device_type == ApplType_E.APPL_TYPE_SWITCH.getVal()) {
						switchActivity = (SwitchActivity) activity;
						switchActivity.b = true;

					} else if (device_type == ApplType_E.APPL_TYPE_TVSET
							.getVal()) {
						tvActivity = (TVActivity) activity;
						tvActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_HDPLAY
							.getVal()) {
						hdPlayActivity = (HDPlayActivity) activity;
						hdPlayActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_AIRCOND
							.getVal()) {
						acActivity = (ACActivity) activity;
						acActivity.cmd_List.addAll(list);
						acActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_CURTAIN
							.getVal()) {
						curtainActivity = (CurtainActivity) activity;
						curtainActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_FAN.getVal()) {
						fansActivity = (FansActivity) activity;
						fansActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_WATER
							.getVal()) {
						switchActivity = (SwitchActivity) activity;
						switchActivity.b = true;
					} else if (device_type == ApplType_E.APPL_TYPE_CUSTOM
							.getVal()) {
					}
				}
			} else {
				ErrCode_E.showError(activity, msgCmdQryByDevAck_S.getUcErr());
			}
			// cmdListActivity.cmdListAdapter.notifyDataSetChanged();
		}
		if (msgId == MsgId_E.MSGID_CTRL.getVal()
				&& msgOper == MsgOperCtrl_E.MSGOPER_CTRL_STATUS.getVal()) {
			MsgCtrlStatusAck_S msgCtrlStatusAck_S = new MsgCtrlStatusAck_S();
			msgCtrlStatusAck_S.setMsgCtrlStatusAck_S(body);
			for (int i = 0; i < msgCtrlStatusAck_S.pucData.size(); i++) {
				CtrlStatus_S ctrlStatus_S = msgCtrlStatusAck_S.pucData.get(i);
				if (ctrlStatus_S.getUcType() == Ctrl_StatType_E.CTRL_STATTYPE_SWITCH
						.getVal()) {
					if (device_type == ApplType_E.APPL_TYPE_SWITCH.getVal()) {
						switchActivity = (SwitchActivity) activity;

						if (ctrlStatus_S.getUiValue() == 1) {
							switchActivity.switch_Icon
									.setImageResource(R.drawable.switch_state_open);
							switchActivity.switch_Iv
									.setImageResource(R.drawable.switch_btn_open);
							switchActivity.state = true;
						} else if (ctrlStatus_S.getUiValue() == 2) {
							switchActivity.switch_Icon
									.setImageResource(R.drawable.switch_state_close);
							switchActivity.switch_Iv
									.setImageResource(R.drawable.switch_btn_close);
							switchActivity.state = false;
						}

					} else if (device_type == ApplType_E.APPL_TYPE_TVSET
							.getVal()) {
						tvActivity = (TVActivity) activity;
						if (ctrlStatus_S.getUiValue() == 1) {
							tvActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_open);
						} else if (ctrlStatus_S.getUiValue() == 2) {
							tvActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_close);
						}
					} else if (device_type == ApplType_E.APPL_TYPE_HDPLAY
							.getVal()) {
						hdPlayActivity = (HDPlayActivity) activity;
						if (ctrlStatus_S.getUiValue() == 1) {
							hdPlayActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_open);
						} else if (ctrlStatus_S.getUiValue() == 2) {
							hdPlayActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_close);
						}
					} else if (device_type == ApplType_E.APPL_TYPE_AIRCOND
							.getVal()) {
						acActivity = (ACActivity) activity;
						if (ctrlStatus_S.getUiValue() == 1) {
							acActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_open);
						} else if (ctrlStatus_S.getUiValue() == 2) {
							acActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_close);
						}
					} else if (device_type == ApplType_E.APPL_TYPE_CURTAIN
							.getVal()) {
						curtainActivity = (CurtainActivity) activity;
						if (ctrlStatus_S.getUiValue() == 1) {
							curtainActivity.switch_Icon
									.setImageResource(R.drawable.switch_state_open);
							curtainActivity.switch_Iv
									.setImageResource(R.drawable.switch_btn_open);
							curtainActivity.state = true;
						} else if (ctrlStatus_S.getUiValue() == 2) {
							curtainActivity.switch_Icon
									.setImageResource(R.drawable.switch_state_close);
							curtainActivity.switch_Iv
									.setImageResource(R.drawable.switch_btn_close);
							curtainActivity.state = false;
						}
					} else if (device_type == ApplType_E.APPL_TYPE_FAN.getVal()) {
						fansActivity = (FansActivity) activity;
						if (ctrlStatus_S.getUiValue() == 1) {
							fansActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_open);
						} else if (ctrlStatus_S.getUiValue() == 2) {
							fansActivity.switch_Icon
									.setImageResource(R.drawable.switch_btn_close);
						}
					} else if (device_type == ApplType_E.APPL_TYPE_WATER
							.getVal()) {
						switchActivity = (SwitchActivity) activity;
						if (ctrlStatus_S.getUiValue() == 1) {
							switchActivity.switch_Icon
									.setImageResource(R.drawable.switch_state_open);
							switchActivity.switch_Iv
									.setImageResource(R.drawable.switch_btn_open);
							switchActivity.state = true;
						} else if (ctrlStatus_S.getUiValue() == 2) {
							switchActivity.switch_Icon
									.setImageResource(R.drawable.switch_state_close);
							switchActivity.switch_Iv
									.setImageResource(R.drawable.switch_btn_close);
							switchActivity.state = false;
						}
					} else if (device_type == ApplType_E.APPL_TYPE_CUSTOM
							.getVal()) {
						// switchActivity.cmd_List.addAll(list);
					}
				}
			}

		}
	}
}
