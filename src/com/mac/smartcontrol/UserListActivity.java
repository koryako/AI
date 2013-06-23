package com.mac.smartcontrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.mac.smartcontrol.adapter.UserListAdapter;
import com.mac.smartcontrol.broadcast.UserBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.User_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class UserListActivity extends Activity {

	public List<User_S> userList = null;
	public UserListAdapter userListAdapter = null;
	public ListView userListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	UserBroadcastReceiver userBroadcastReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		userListView = (ListView) findViewById(R.id.userlist);
		userList = new ArrayList<User_S>();
		userListAdapter = new UserListAdapter(UserListActivity.this, userList);
		userListView.setAdapter(userListAdapter);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(UserListActivity.this,
						AddUserActivity.class);
				// 开始一个新的 Activity等候返回结果
				startActivityForResult(newIntent, 0);
			}
		});
		userBroadcastReceiver = new UserBroadcastReceiver(UserListActivity.this);
		IntentFilter filter = new IntentFilter();

		filter.addAction(MsgId_E.MSGID_USER.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_USER.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction("Connection_IOException");
		filter.addAction("Login_IOException");
		filter.addAction("UnknownHostException");
		filter.addAction("IOException");
		registerReceiver(userBroadcastReceiver, filter);

		// try {
		WriteUtil.write(MsgId_E.MSGID_USER.getVal(), 0,
				MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY.getVal(),
				(short) 2, new MsgQryReq_S((short) 0).getMsgQryReq_S(), this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(UserListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
		// .show();
		// DisconnectionUtil.restart(UserListActivity.this);
		// }

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(userBroadcastReceiver);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] b = extras.getByteArray("user");
					User_S user_S = new User_S();
					user_S.setUser_S(b);
					this.userList.add(user_S);
					userListView.setAdapter(userListAdapter);
					// this.userListAdapter.notifyDataSetChanged();
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] b = extras.getByteArray("user");
					User_S user_S = new User_S();
					user_S.setUser_S(b);
					User_S s = userList.get(mod_Idx);
					if (s.getUsIdx() == user_S.getUsIdx()) {
						userList.set(mod_Idx, user_S);
					}
					userListView.setAdapter(userListAdapter);
					// this.userListAdapter.notifyDataSetChanged();

				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return true;
	}

}
