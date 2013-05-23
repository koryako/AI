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

import com.mac.smartcontrol.adapter.DeviceListAdapter;
import com.mac.smartcontrol.broadcast.DeviceBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class DeviceListActivity extends Activity {

	public List<Appl_S> deviceList = null;
	public DeviceListAdapter deviceListAdapter = null;
	public ListView deviceListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	DeviceBroadcastReceiver deviceBroadcastReceiver;
	TextView device_title_tv;
	public Appl_S appl_S = null;
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

		device_title_tv = (TextView) findViewById(R.id.userlist_title);
		device_title_tv.setText(R.string.device_list);
		appl_S = new Appl_S();

		deviceListView = (ListView) findViewById(R.id.userlist);
		deviceList = new ArrayList<Appl_S>();
		deviceListAdapter = new DeviceListAdapter(DeviceListActivity.this,
				deviceList);
		deviceListView.setAdapter(deviceListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(DeviceListActivity.this,
						AddDeviceActivity.class);
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
		deviceBroadcastReceiver = new DeviceBroadcastReceiver(
				DeviceListActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_MAX.getVal());
		filter.addAction("IOException");
		registerReceiver(deviceBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_MAX
							.getVal(), (short) 2,
					new MsgQryReq_S(rgn_S.getUsIdx()).getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(DeviceListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
					.show();

			DisconnectionUtil.restart(DeviceListActivity.this);
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(deviceBroadcastReceiver);
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
					byte[] b = extras.getByteArray("device");
					Appl_S appl_S = new Appl_S();
					appl_S.setAppl_S(b);
					if (appl_S.getUsRgnIdx() == rgn_S.getUsIdx()) {
						this.deviceList.add(appl_S);
						this.deviceListView.setAdapter(deviceListAdapter);
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] d_b = extras.getByteArray("device");
					Appl_S appl_S = new Appl_S();
					appl_S.setAppl_S(d_b);
					if (appl_S.getUsRgnIdx() == rgn_S.getUsIdx()) {
						Appl_S s = deviceList.get(mod_Idx);
						if (s.getUsIdx() == appl_S.getUsIdx()) {
							deviceList.set(mod_Idx, appl_S);
						}
					} else {
						deviceList.remove(mod_Idx);
					}
					this.deviceListView.setAdapter(deviceListAdapter);
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
