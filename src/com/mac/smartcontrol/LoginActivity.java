package com.mac.smartcontrol;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.UserBroadcastReceiver;
import com.mac.smartcontrol.util.ConnectionDetector;
import com.mac.smartcontrol.util.WriteUtil;

import define.oper.MsgOperUser_E;
import define.oper.body.req.MsgUserLoginReq_S;
import define.type.MsgId_E;

public class LoginActivity extends Activity {
	UserBroadcastReceiver userBroadcastReceiver;
	public ProgressDialog progressDialog = null;
	public SharedPreferences sp = null;
	public SharedPreferences.Editor editor = null;
	public boolean isRemeber = false;
	public EditText username_et = null;
	public EditText password_et = null;
	public EditText ip_et = null;
	Intent intent = null;
	public boolean isRepeat = false;
	private boolean isExit = false;
	public ImageView login_Iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (WriteUtil.usSsnId != 0) {
			WriteUtil.usSsnId = 0;
			isRepeat = true;
		}
		ip_et = (EditText) findViewById(R.id.ip_et);
		username_et = (EditText) findViewById(R.id.username_et);
		password_et = (EditText) findViewById(R.id.password_et);
		login_Iv = (ImageView) findViewById(R.id.login_btn);
		final ImageView remeber_Iv = (ImageView) findViewById(R.id.remeber_iv);
		LinearLayout remeber_ll = (LinearLayout) findViewById(R.id.remeber_ll);
		remeber_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRemeber = (!isRemeber);
				if (isRemeber) {
					remeber_Iv.setImageResource(R.drawable.checkbox_bg_focus);
				} else {
					remeber_Iv.setImageResource(R.drawable.checkbox_bg);
				}
			}
		});
		sp = getSharedPreferences("user", 0);
		editor = sp.edit();
		String defip = sp.getString("ip", null);
		String defusername = sp.getString("username", null);
		String defpwd = sp.getString("password", null);
		if (defip != null && defusername != null && defpwd != null) {
			ip_et.setText(defip);
			username_et.setText(defusername);
			password_et.setText(defpwd);
			remeber_ll.performClick();
		}

		userBroadcastReceiver = new UserBroadcastReceiver(LoginActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_USER.getVal() + "_"
				+ MsgOperUser_E.MSGOPER_USER_LOGIN.getVal());

		filter.addAction("UnknownHostException");
		filter.addAction("IOException");
		registerReceiver(userBroadcastReceiver, filter);
		login_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!new ConnectionDetector(LoginActivity.this)
						.isConnectingToInternet()) {
					Toast.makeText(LoginActivity.this, "请开启网络",
							Toast.LENGTH_SHORT).show();
					return;
				}
				login_Iv.setEnabled(false);
				SocketService.ip = ip_et.getText().toString().trim();
				String username = username_et.getText().toString().trim();
				String password = password_et.getText().toString().trim();

				MsgUserLoginReq_S msgUserLoginReq_S = new MsgUserLoginReq_S();
				msgUserLoginReq_S.setSzName(username);
				msgUserLoginReq_S.setSzPass(password);

				intent = new Intent();
				intent.putExtra("data",
						msgUserLoginReq_S.getMsgUserLoginReq_S());
				intent.setClass(LoginActivity.this, SocketService.class);

				progressDialog = new ProgressDialog(LoginActivity.this);
				progressDialog.setTitle("登陆");
				progressDialog.setMessage("正在登陆…");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog
						.setOnKeyListener(new DialogInterface.OnKeyListener() {

							@Override
							public boolean onKey(DialogInterface dialog,
									int keyCode, KeyEvent event) {
								// TODO Auto-generated method stub
								if (keyCode == KeyEvent.KEYCODE_BACK) {
									stopService(intent);
									login_Iv.setEnabled(true);
								}
								return false;
							}
						});
				progressDialog.show();

				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				startService(intent);
				// }
				// }).start();
			}
		});
	}

	private Timer timer;
	private int currentFlag = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isExit) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (currentFlag == 3) {
							isExit = false;
							currentFlag = 0;
							timer.cancel();
						}
						currentFlag++;
					}
				};
				timer.schedule(task, 0, 1000);
			} else {
				timer.cancel();
				finish();
			}
		}
		return false;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(userBroadcastReceiver);
		super.finish();
	}
}
