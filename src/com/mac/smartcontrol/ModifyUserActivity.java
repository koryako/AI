package com.mac.smartcontrol;

import java.io.IOException;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.UserBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.User_S;
import define.oper.MsgOper_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModifyUserActivity extends Activity {
	public User_S user_S;
	String[] types = new String[] { "普通", "系统" };
	User_S userS;
	UserBroadcastReceiver userBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user);
		final TextView username_Et = (TextView) findViewById(R.id.username_et);
		final EditText password_Et = (EditText) findViewById(R.id.password_et);
		final Spinner usertype_sp = (Spinner) findViewById(R.id.usertype_sp);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		usertype_sp.setAdapter(adapter);
		user_S = new User_S();
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("user");
			user_S.setUser_S(b);
		}
		username_Et.setText(user_S.getSzName());
		password_Et.setText(user_S.getSzPass());
		usertype_sp.setSelection(user_S.getUcType() - 1);
		userBroadcastReceiver = new UserBroadcastReceiver(
				ModifyUserActivity.this);
		IntentFilter filter = new IntentFilter();

		filter.addAction(MsgId_E.MSGID_USER.getVal() + "_"
				+ MsgOper_E.MSGOPER_MOD.getVal());

		filter.addAction("UnknownHostException");
		filter.addAction("IOException");
		registerReceiver(userBroadcastReceiver, filter);

		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userName = username_Et.getText().toString().trim();
				if (userName == null || userName.equals("")) {
					Toast.makeText(ModifyUserActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (userName.getBytes().length > 32) {
					Toast.makeText(ModifyUserActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				String password = password_Et.getText().toString().trim();
				if (password == null || password.equals("")) {
					Toast.makeText(ModifyUserActivity.this, "密码不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (password.getBytes().length > 32) {
					Toast.makeText(ModifyUserActivity.this, "密码太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				user_S.setSzName(userName);
				user_S.setSzPass(password);
				user_S.setUcType((byte) (usertype_sp.getSelectedItemPosition() + 1));
				try {
					WriteUtil.write(MsgId_E.MSGID_USER.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MOD.getVal(), User_S.getSize(),
							user_S.getUser_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyUserActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(ModifyUserActivity.this);
				}
			}
		});

		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(userBroadcastReceiver);
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return true;
	}
}
