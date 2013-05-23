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

import com.mac.smartcontrol.adapter.EnterAreaListAdapter;
import com.mac.smartcontrol.broadcast.SenseBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class EnterAreaActivity extends Activity {

	public List<Rgn_S> areaList = null;
	public EnterAreaListAdapter enterAreaListAdapter = null;
	public ListView areaListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	SenseBroadcastReceiver senseBroadcastReceiver;
	TextView area_title_tv;
	public Rgn_S rgn_S = null;
	private int msgId;
	IntentFilter filter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		area_title_tv = (TextView) findViewById(R.id.userlist_title);
		area_title_tv.setText(R.string.area_list);
		rgn_S = new Rgn_S();
		areaListView = (ListView) findViewById(R.id.userlist);
		areaList = new ArrayList<Rgn_S>();
		Bundle bd = getIntent().getExtras();
		msgId = bd.getInt("msgId");
		enterAreaListAdapter = new EnterAreaListAdapter(EnterAreaActivity.this,
				areaList, msgId);
		areaListView.setAdapter(enterAreaListAdapter);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		if (msgId == 34) {
			back_Iv.setVisibility(View.INVISIBLE);
		}
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		if (msgId == MsgId_E.MSGID_SENS.getVal()) {
			add_Iv.setImageResource(R.drawable.list_sense_log_iv_selector);
			add_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(EnterAreaActivity.this,
							SenseLogListActivity.class);
					startActivity(intent);
				}
			});

		} else {
			add_Iv.setVisibility(View.INVISIBLE);
		}
		senseBroadcastReceiver = new SenseBroadcastReceiver(
				EnterAreaActivity.this);
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction("IOException");
		registerReceiver(senseBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(EnterAreaActivity.this, "获取列表失败", Toast.LENGTH_LONG)
					.show();

			DisconnectionUtil.restart(EnterAreaActivity.this);
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(senseBroadcastReceiver);
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(senseBroadcastReceiver, filter);
		super.onResume();
	}
}
