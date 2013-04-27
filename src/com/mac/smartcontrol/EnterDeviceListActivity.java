package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.adapter.EnterDeviceListAdapter;
import com.mac.smartcontrol.broadcast.CmdBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class EnterDeviceListActivity extends Activity {

	public List<Appl_S> deviceList = null;
	public Map<Short, Rgn_S> areaMap = null;
	public EnterDeviceListAdapter deviceListAdapter = null;
	public ListView deviceListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	CmdBroadcastReceiver cmdBroadcastReceiver;
	TextView device_title_tv;
	public Appl_S appl_S = null;
	public Rgn_S rgn_S;
	public int msgId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		device_title_tv = (TextView) findViewById(R.id.userlist_title);
		device_title_tv.setText(R.string.device_list);
		appl_S = new Appl_S();
		areaMap = new HashMap<Short, Rgn_S>();
		deviceListView = (ListView) findViewById(R.id.userlist);
		deviceList = new ArrayList<Appl_S>();
		deviceListAdapter = new EnterDeviceListAdapter(
				EnterDeviceListActivity.this, deviceList, areaMap);
		deviceListView.setAdapter(deviceListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setVisibility(View.INVISIBLE);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			rgn_S = new Rgn_S();
			byte[] b = bundle.getByteArray("area");
			msgId = bundle.getInt("msgId");
			rgn_S.setRgn_S(b);
		}
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cmdBroadcastReceiver = new CmdBroadcastReceiver(
				EnterDeviceListActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("3_4");
		filter.addAction("4_4");
		filter.addAction("4_5");
		filter.addAction("IOException");
		registerReceiver(cmdBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
			if (rgn_S != null) {
				WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_MAX.getVal(), (short) 2,
						new MsgQryReq_S(rgn_S.getUsIdx()).getMsgQryReq_S());

			} else {
				WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_QRY.getVal(), (short) 2,
						new MsgQryReq_S((short) 0).getMsgQryReq_S());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(EnterDeviceListActivity.this, "获取列表失败",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(cmdBroadcastReceiver);
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
