package com.mac.smartcontrol;

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

import define.oper.body.req.MsgUserLoginReq_S;

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
		ImageView login_Iv = (ImageView) findViewById(R.id.login_btn);
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
		filter.addAction("1_5");
		filter.addAction("UnknownHostException");
		filter.addAction("IOException");
		registerReceiver(userBroadcastReceiver, filter);
		login_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!new ConnectionDetector(LoginActivity.this)
						.isConnectingToInternet()) {
					Toast.makeText(LoginActivity.this, "Çë¿ªÆôÍøÂç",
							Toast.LENGTH_SHORT).show();
					return;
				}
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
				progressDialog.setTitle("µÇÂ½");
				progressDialog.setMessage("ÕýÔÚµÇÂ½¡­");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog
						.setOnKeyListener(new DialogInterface.OnKeyListener() {

							@Override
							public boolean onKey(DialogInterface dialog,
									int keyCode, KeyEvent event) {
								// TODO Auto-generated method stub
								if (keyCode == KeyEvent.KEYCODE_BACK) {
									stopService(intent);
								}
								return false;
							}
						});
				progressDialog.show();

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						startService(intent);
					}
				}).start();
			}
		});
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(userBroadcastReceiver);
		super.finish();
	}

}
