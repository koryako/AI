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
import define.oper.body.ack.MsgAddAck_S;
import define.oper.body.ack.MsgDelAck_S;
import define.oper.body.ack.MsgModAck_S;
import define.oper.body.ack.MsgQryAck_S;
import define.oper.body.ack.MsgUserLoginAck_S;
import define.type.ErrCode_E;

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
			it.setClass(activity, SocketService.class);
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
		byte msgOper = Byte.parseByte(action.split("_")[1]);
		byte[] body = intent.getExtras().getByteArray("data");
		UserListActivity userListActivity = null;
		AddUserActivity addUserActivity = null;
		ModifyUserActivity modifyUserActivity = null;
		LoginActivity loginActivity = null;
		switch (msgOper) {
		case 1:
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
			break;
		case 2:
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
			break;

		case 3:
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
			break;
		case 4:
			parseToList(body);
			break;
		case 5:
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

			break;
		default:
			break;
		}

	}

	public void parseToList(byte[] body) {
		MsgQryAck_S msgQryAck_S = new MsgQryAck_S();
		msgQryAck_S.setMsgQryAck_S(body);
		UserListActivity userListActivity = (UserListActivity) activity;
		// switch (msgId) {
		// case 1:
		if (msgQryAck_S.getUsCnt() > 0) {
			if (msgQryAck_S.getUsError() == 0) {
				for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
					byte[] user_S_Byte = Arrays.copyOfRange(
							msgQryAck_S.getPucData(), i * User_S.getSize(),
							(i + 1) * User_S.getSize());
					User_S user_S = new User_S();
					user_S.setUser_S(user_S_Byte);
					userListActivity.userList.add(user_S);
				}
				userListActivity.userListAdapter.notifyDataSetChanged();
			} else {
				ErrCode_E.showError(activity, msgQryAck_S.getUsError());
			}
		}
		// break;
		// case 2:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Ctrl_S>list = new ArrayList<Ctrl_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] ctrl_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Ctrl_S.getSize(), (i+1)*Ctrl_S.getSize());
		// Ctrl_S ctrl_S=new Ctrl_S();
		// ctrl_S.setCtrl_S(ctrl_S_Byte);
		// list.add(ctrl_S);
		// }
		//
		// }
		// }
		// break;
		//
		// case 3:
		// if(msgQryAck_S.getUsError()==0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Rgn_S>list = new ArrayList<Rgn_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] rgn_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Rgn_S.getSize(), (i+1)*Rgn_S.getSize());
		// Rgn_S rgn_S=new Rgn_S();
		// rgn_S.setRgn_S(rgn_S_Byte);
		// list.add(rgn_S);
		// }
		// }
		// }
		// break;
		//
		// case 4:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Appl_S>list = new ArrayList<Appl_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] appl_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Appl_S.getSize(), (i+1)*Appl_S.getSize());
		// Appl_S appl_S=new Appl_S();
		// appl_S.setAppl_S(appl_S_Byte);
		// list.add(appl_S);
		// }
		// }
		// }
		// break;
		//
		// case 5:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Cmd_S>list = new ArrayList<Cmd_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] cmd_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Cmd_S.getSize(), (i+1)*Cmd_S.getSize());
		// Cmd_S cmd_S=new Cmd_S();
		// cmd_S.setCmd_S(cmd_S_Byte);
		// list.add(cmd_S);
		// }
		// }
		// }
		// break;
		//
		// case 6:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Sens_S>list = new ArrayList<Sens_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] sens_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Sens_S.getSize(), (i+1)*Sens_S.getSize());
		// Sens_S sens_S=new Sens_S();
		// sens_S.setSens_S(sens_S_Byte);
		// list.add(sens_S);
		// }
		// }
		// }
		// break;
		//
		// case 7:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Cama_S>list = new ArrayList<Cama_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] cama_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Cama_S.getSize(), (i+1)*Cama_S.getSize());
		// Cama_S cama_S=new Cama_S();
		// cama_S.setCama_S(cama_S_Byte);
		// list.add(cama_S);
		// }
		// }
		// }
		// break;
		//
		// case 8:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Mode_S>list = new ArrayList<Mode_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] mode_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*Mode_S.getSize(), (i+1)*Mode_S.getSize());
		// Mode_S mode_S=new Mode_S();
		// mode_S.setMode_S(mode_S_Byte);
		// list.add(mode_S);
		// }
		// }
		// }
		// break;
		//
		// case 9:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<ModeCmd_S>list = new ArrayList<ModeCmd_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] modeCmd_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*ModeCmd_S.getSize(), (i+1)*ModeCmd_S.getSize());
		// ModeCmd_S modeCmd_S=new ModeCmd_S();
		// modeCmd_S.setMode_S(modeCmd_S_Byte);
		// list.add(modeCmd_S);
		// }
		// }
		// }
		// break;
		//
		// case 10:
		// if(msgQryAck_S.getUsCnt()>0){
		// if(msgQryAck_S.getUsError()==0){
		// List<Appl_S>list = new ArrayList<Appl_S>();
		// for (int i = 0; i < msgQryAck_S.getUsCnt(); i++) {
		// byte[] user_S_Byte=Arrays.copyOfRange(msgQryAck_S.getPucData(),
		// i*User_S.getSize(), (i+1)*User_S.getSize());
		// User_S user_S=new User_S();
		// user_S.setUser_S(user_S_Byte);
		// }
		// }
		// }
		// break;
		//
		// default:
		// break;
		// }
	}
}
