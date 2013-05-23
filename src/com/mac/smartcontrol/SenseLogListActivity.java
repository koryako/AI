package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.mac.smartcontrol.adapter.SenseLogListAdapter;
import com.mac.smartcontrol.broadcast.SenseBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.SensLog_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class SenseLogListActivity extends Activity {

	public List<SensLog_S> senseLogList = null;
	public SenseLogListAdapter senseLogListAdapter = null;
	public ListView senseLogListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	SenseBroadcastReceiver senseBroadcastReceiver;
	TextView sense_title_tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		sense_title_tv = (TextView) findViewById(R.id.userlist_title);
		sense_title_tv.setText(R.string.senselog_list);

		senseLogListView = (ListView) findViewById(R.id.userlist);
		senseLogList = new ArrayList<SensLog_S>();
		senseLogListAdapter = new SenseLogListAdapter(
				SenseLogListActivity.this, senseLogList);
		senseLogListView.setAdapter(senseLogListAdapter);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setVisibility(View.INVISIBLE);

		senseBroadcastReceiver = new SenseBroadcastReceiver(
				SenseLogListActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_SENSLOG.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction("IOException");
		registerReceiver(senseBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_SENSLOG.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(SenseLogListActivity.this, "获取列表失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(SenseLogListActivity.this);

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

}
