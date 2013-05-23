package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.mac.smartcontrol.adapter.ModeCmdListAdapter;
import com.mac.smartcontrol.broadcast.ModeCmdBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.entity.ModeCmd_S;
import define.entity.Mode_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgCmdQryByDevReq_S;
import define.oper.body.req.MsgModeCmdQryByModeReq_S;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModeCmdListActivity extends Activity {

	public List<ModeCmd_S> modecmdList = null;
	public Map<Short, Cmd_S> cmdMap = null;
	public Map<Short, Rgn_S> areaMap = null;
	public Map<Short, Appl_S> deviceMap = null;
	public ModeCmdListAdapter modecmdListAdapter = null;
	public ListView modecmdListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	ModeCmdBroadcastReceiver modeCmdBroadcastReceiver;
	TextView modecmd_title_tv;
	Mode_S mode_S;
	IntentFilter filter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		mode_S = new Mode_S();
		cmdMap = new HashMap<Short, Cmd_S>();
		areaMap = new HashMap<Short, Rgn_S>();
		deviceMap = new HashMap<Short, Appl_S>();

		if (bundle != null) {
			mode_S.setMode_S(bundle.getByteArray("mode"));
		}

		modecmd_title_tv = (TextView) findViewById(R.id.userlist_title);
		modecmd_title_tv.setText(R.string.mode_cmd_list);

		modecmdListView = (ListView) findViewById(R.id.userlist);
		modecmdList = new ArrayList<ModeCmd_S>();
		modecmdListAdapter = new ModeCmdListAdapter(this, modecmdList, cmdMap,
				deviceMap, areaMap);
		modecmdListView.setAdapter(modecmdListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(ModeCmdListActivity.this,
						AddModeCmdActivity.class);
				newIntent.putExtra("mode", mode_S.getMode_S());
				// 开始一个新的 Activity等候返回结果
				startActivityForResult(newIntent, 0);
				unregisterReceiver(modeCmdBroadcastReceiver);
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
		modeCmdBroadcastReceiver = new ModeCmdBroadcastReceiver(
				ModeCmdListActivity.this);
		filter = new IntentFilter();

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_MODECMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_MODECMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_MAX.getVal());
		filter.addAction("IOException");
		registerReceiver(modeCmdBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_MODECMD.getVal(), 3,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_MAX
							.getVal(), MsgCmdQryByDevReq_S.getSize(),
					new MsgModeCmdQryByModeReq_S(mode_S.getUsIdx())
							.getMsgQryReq_S());
			WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 2,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
			WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 1,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ModeCmdListActivity.this, "获取列表失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(ModeCmdListActivity.this);
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(modeCmdBroadcastReceiver);
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
					byte[] c_b = extras.getByteArray("modecmd");
					ModeCmd_S modeCmd_S = new ModeCmd_S();
					modeCmd_S.setModeCmd_S(c_b);
					if (modeCmd_S.getUsModeIdx() == mode_S.getUsIdx()) {
						this.modecmdList.add(modeCmd_S);
						this.modecmdListView.setAdapter(modecmdListAdapter);
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] c_b = extras.getByteArray("modecmd");
					ModeCmd_S modeCmd_S = new ModeCmd_S();
					modeCmd_S.setModeCmd_S(c_b);
					if (modeCmd_S.getUsModeIdx() == mode_S.getUsIdx()) {
						modecmdList.set(mod_Idx, modeCmd_S);
						this.modecmdListView.setAdapter(modecmdListAdapter);
					}
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(modeCmdBroadcastReceiver, filter);
		super.onResume();
	}

}
