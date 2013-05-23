package com.mac.smartcontrol;

import java.io.IOException;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.adapter.CameraListAdapter;
import com.mac.smartcontrol.broadcast.CameraBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Cama_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class CameraListActivity extends Activity {

	public List<Cama_S> cameraList = null;
	public CameraListAdapter cameraListAdapter = null;
	public ListView deviceListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	CameraBroadcastReceiver cameraBroadcastReceiver;
	TextView camera_title_tv;
	public Cama_S cama_S = null;
	public Rgn_S rgn_S = null;
	public String areaName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			rgn_S = new Rgn_S();
			rgn_S.setRgn_S(bundle.getByteArray("area"));
			areaName = bundle.getString("areaName");
		}

		camera_title_tv = (TextView) findViewById(R.id.userlist_title);
		camera_title_tv.setText(R.string.camera_list);
		cama_S = new Cama_S();

		deviceListView = (ListView) findViewById(R.id.userlist);
		cameraList = new ArrayList<Cama_S>();
		cameraListAdapter = new CameraListAdapter(CameraListActivity.this,
				cameraList);
		deviceListView.setAdapter(cameraListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(CameraListActivity.this,
						AddCameraActivity.class);
				// 开始一个新的 Activity等候返回结果
				startActivityForResult(newIntent, 0);
			}
		});
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cameraBroadcastReceiver = new CameraBroadcastReceiver(
				CameraListActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CAMA.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_CAMA.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_CAMA.getVal() + "_"
				+ MsgOper_E.MSGOPER_MAX.getVal());

		filter.addAction("IOException");
		registerReceiver(cameraBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_CAMA.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_MAX
							.getVal(), (short) 2,
					new MsgQryReq_S(rgn_S.getUsIdx()).getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(CameraListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
					.show();
			DisconnectionUtil.restart(CameraListActivity.this);
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(cameraBroadcastReceiver);
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
					byte[] b = extras.getByteArray("camera");
					Cama_S c_S = new Cama_S();
					c_S.setCama_S(b);
					if (c_S.getUsRgnIdx() == rgn_S.getUsIdx()) {
						this.cameraList.add(c_S);
						this.deviceListView.setAdapter(cameraListAdapter);
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] d_b = extras.getByteArray("camera");
					Cama_S c_S = new Cama_S();
					c_S.setCama_S(d_b);
					if (c_S.getUsRgnIdx() == rgn_S.getUsIdx()) {
						cameraList.set(mod_Idx, c_S);
					} else {
						cameraList.remove(mod_Idx);
					}
					this.deviceListView.setAdapter(cameraListAdapter);
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
