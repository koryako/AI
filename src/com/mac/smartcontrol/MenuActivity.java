package com.mac.smartcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MenuActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		ImageView manage_Iv = (ImageView) findViewById(R.id.main_manage_iv);
		ImageView location_Iv = (ImageView) findViewById(R.id.main_location_iv);
		ImageView control_Iv = (ImageView) findViewById(R.id.main_control_iv);
		final Intent intent = new Intent();
		manage_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.putExtra("currentPage", 0);
				intent.setClass(MenuActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		location_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.putExtra("currentPage", 3);
				intent.setClass(MenuActivity.this, MainActivity.class);
				startActivity(intent);
				stopService(intent);
				finish();
			}
		});

		control_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.putExtra("currentPage", 2);
				intent.setClass(MenuActivity.this, MainActivity.class);
				startActivity(intent);
				stopService(intent);
				finish();
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(MenuActivity.this, SocketService.class);
			stopService(intent);
			dialog();
			return true;
		}
		return true;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MenuActivity.this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(MenuActivity.this, SocketService.class);
						stopService(intent);
						android.os.Process.killProcess(android.os.Process
								.myPid());
						finish();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
}
