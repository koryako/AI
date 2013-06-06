package com.mac.smartcontrol.broadcast;

import java.util.Arrays;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mac.smartcontrol.AddUserActivity;
import com.mac.smartcontrol.LoginActivity;
import com.mac.smartcontrol.MenuActivity;
import com.mac.smartcontrol.ModifyUserActivity;
import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.UserListActivity;

import define.entity.User_S;
import define.oper.MsgOper_E;
import define.oper.body.ack.MsgAddAck_S;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgModAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.oper.body.ack.MsgUserLoginAck_S;
import define.type.ErrCode_E;
import define.type.MsgId_E;

public class UserBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public UserBroadcastReceiver(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals("UnknownHostException")) {
			LoginActivity loginActivity = (LoginActivity) activity;
			loginActivity.progressDialog.dismiss();
			Intent i = new Intent();
			intent.setClass(activity, SocketService.class);
			activity.stopService(i);
			Toast.makeText(activity, "请确认Ip地址,连接失败", Toast.LENGTH_LONG).show();
			return;
		}
		if (action.equals("IOException")) {
			Intent it = new Intent();
			LoginActivity loginActivity = (LoginActivity) activity;
			it.setClass(activity, SocketService.class);
			loginActivity.progressDialog.dismiss();
			activity.stopService(it);
			Toast.makeText(activity, "请确认网络是否开启,操作失败,请重新登录", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// if(action.equals("LoginSuccess")){
		// progressDialog=ProgressDialog.show(activity, "登陆",
		// "正在登陆…", true,true);
		// return;
		// }
		byte msgId = Byte.parseByte(action.split("_")[0]);
		byte msgOper = Byte.parseByte(action.split("_")[1]);
		byte[] body = intent.getExtras().getByteArray("data");
		UserListActivity userListActivity = null;
		AddUserActivity addUserActivity = null;
		ModifyUserActivity modifyUserActivity = null;
		LoginActivity loginActivity = null;
		if (msgId == MsgId_E.MSGID_USER.getVal()
				&& msgOper == MsgOper_E.MSGOPER_ADD.getVal()) {
			MsgAddAck_S msgAddAck_S = new MsgAddAck_S();
			msgAddAck_S.setMsgAddAck_S(body);
			if (msgAddAck_S.getUsError() == 0) {
				Toast.makeText(activity, "添加成功", Toast.LENGTH_LONG).show();
				addUserActivity = (AddUserActivity) activity;
				addUserActivity.user_S.setUsIdx(msgAddAck_S.getUsIdx());
				Bundle bundle = new Bundle();
				bundle.putByteArray("user", addUserActivity.user_S.getUser_S());
				Intent i = new Intent();
				i.putExtras(bundle);
				// 返回intent
				addUserActivity.setResult(Activity.RESULT_OK, i);
				addUserActivity.finish();
			} else {
				ErrCode_E.showError(context, msgAddAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_USER.getVal()
				&& msgOper == MsgOper_E.MSGOPER_DEL.getVal()) {
			MsgDelAck_S msgDelAck_S = new MsgDelAck_S();
			msgDelAck_S.setMsgDelAck_S(body);
			userListActivity = (UserListActivity) activity;
			if (msgDelAck_S.getUsError() == 0) {
				userListActivity.userList.remove(userListActivity.del_Idx);
				// userListActivity.userListAdapter.notifyDataSetChanged();
				userListActivity.userListView
						.setAdapter(userListActivity.userListAdapter);
				Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				ErrCode_E.showError(context, msgDelAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_USER.getVal()
				&& msgOper == MsgOper_E.MSGOPER_MOD.getVal()) {
			MsgModAck_S msgModAck_S = new MsgModAck_S();
			msgModAck_S.setMsgModAck_S(body);
			if (msgModAck_S.getUsError() == 0) {
				Toast.makeText(activity, "修改成功", Toast.LENGTH_LONG).show();
				// Intent i=new Intent();
				// i.setClass(activity, UserListActivity.class);
				// activity.startActivity(i);
				// activity.finish();

				modifyUserActivity = (ModifyUserActivity) activity;
				modifyUserActivity.user_S.setUsIdx(msgModAck_S.getUsIdx());
				Bundle bundle = new Bundle();
				bundle.putByteArray("user",
						modifyUserActivity.user_S.getUser_S());
				Intent i = new Intent();
				i.putExtras(bundle);
				// 返回intent
				modifyUserActivity.setResult(Activity.RESULT_OK, i);
				modifyUserActivity.finish();
			} else {
				ErrCode_E.showError(context, msgModAck_S.getUsError());
			}
		}

		if (msgId == MsgId_E.MSGID_USER.getVal()
				&& msgOper == MsgOper_E.MSGOPER_QRY.getVal()) {
			parseToList(body);
		}

		if (msgId == MsgId_E.MSGID_USER.getVal()
				&& msgOper == MsgOper_E.MSGOPER_MAX.getVal()) {
			MsgUserLoginAck_S msgUserLoginAck_S = new MsgUserLoginAck_S();
			msgUserLoginAck_S.setMsgUserLoginAck_S(body);
			loginActivity = (LoginActivity) activity;
			if (loginActivity.progressDialog != null) {
				loginActivity.progressDialog.dismiss();
			}
			if (msgUserLoginAck_S.getUsError() == 0) {
				if (loginActivity.isRemeber) {
					loginActivity.editor.putString("ip", loginActivity.ip_et
							.getText().toString());
					loginActivity.editor.putString("username",
							loginActivity.username_et.getText().toString());
					loginActivity.editor.putString("password",
							loginActivity.password_et.getText().toString());
				} else {
					loginActivity.editor.clear();
				}
				loginActivity.editor.commit();
				if (!loginActivity.isRepeat) {
					Intent i = new Intent();
					i.setClass(loginActivity, MenuActivity.class);
					loginActivity.startActivity(i);
				} else {
					Toast.makeText(loginActivity, "重新连接成功,请刷新数据",
							Toast.LENGTH_LONG).show();
				}
				loginActivity.finish();
			} else {
				ErrCode_E.showError(context, msgUserLoginAck_S.getUsError());
			}
		}

	}

	public void parseToList(byte[] body) {
		MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
		msgQryAck_S.setMsgQryAck_S(body);
		UserListActivity userListActivity = (UserListActivity) activity;
		if (msgQryAck_S.getUsError() == 0) {
			if (msgQryAck_S.getUsCnt() > 0) {
				for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
					byte[] user_S_Byte = Arrays.copyOfRange(
							msgQryAck_S.getPucData(), i * User_S.getSize(),
							(i + 1) * User_S.getSize());
					User_S user_S = new User_S();
					user_S.setUser_S(user_S_Byte);
					userListActivity.userList.add(user_S);
				}
				userListActivity.userListAdapter.notifyDataSetChanged();
			}
		} else {
			ErrCode_E.showError(activity, msgQryAck_S.getUsError());
		}
	}
}
