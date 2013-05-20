package com.mac.smartcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ManageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		ImageView user_Iv = (ImageView) findViewById(R.id.manage_user_iv);
		user_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ManageActivity.this, UserListActivity.class);
				startActivity(intent);
			}
		});
		ImageView area_Iv = (ImageView) findViewById(R.id.manage_area_iv);
		area_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ManageActivity.this, AreaListActivity.class);
				startActivity(intent);
			}
		});

		ImageView controller_Iv = (ImageView) findViewById(R.id.manage_controller_iv);
		controller_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ManageActivity.this,
						ControllerListActivity.class);
				startActivity(intent);
			}
		});

		ImageView sense_Iv = (ImageView) findViewById(R.id.manage_sensor_iv);
		sense_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("msgId", 6);
				intent.setClass(ManageActivity.this, EnterAreaActivity.class);
				startActivity(intent);
			}
		});

		ImageView device_Iv = (ImageView) findViewById(R.id.manage_device_iv);
		device_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("msgId", 4);
				intent.setClass(ManageActivity.this, EnterAreaActivity.class);
				startActivity(intent);
			}
		});

		// ImageView cmd_Iv = (ImageView) findViewById(R.id.manage_cmd_iv);
		// cmd_Iv.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setClass(ManageActivity.this,
		// EnterDeviceListActivity.class);
		// startActivity(intent);
		// }
		// });

		ImageView camera_Iv = (ImageView) findViewById(R.id.manage_camera_iv);
		camera_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("msgId", 8);
				intent.setClass(ManageActivity.this, EnterAreaActivity.class);
				startActivity(intent);
			}
		});

		ImageView mode_Iv = (ImageView) findViewById(R.id.manage_mode_iv);
		mode_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("msgId", 9);
				intent.setClass(ManageActivity.this, EnterAreaActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return true;
	}
}
