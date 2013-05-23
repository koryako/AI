package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.adapter.EnterModeListAdapter;
import com.mac.smartcontrol.broadcast.EnterModeBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Mode_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class EnterModeListActivity extends Activity {

	public List<Mode_S> modeList = null;
	public EnterModeListAdapter modeListAdapter = null;
	public ListView modeListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	EnterModeBroadcastReceiver modeBroadcastReceiver;
	TextView mode_title_tv;
	public Rgn_S rgn_S = null;
	public Mode_S mode_S = null;
	IntentFilter filter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			rgn_S = new Rgn_S();
			rgn_S.setRgn_S(bundle.getByteArray("area"));
		}

		mode_title_tv = (TextView) findViewById(R.id.userlist_title);
		mode_title_tv.setText(R.string.mode_list);

		modeListView = (ListView) findViewById(R.id.userlist);
		modeList = new ArrayList<Mode_S>();
		modeListAdapter = new EnterModeListAdapter(EnterModeListActivity.this,
				modeList);
		modeListView.setAdapter(modeListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(EnterModeListActivity.this,
						AddModeActivity.class);
				// 开始一个新的 Activity等候返回结果
				startActivityForResult(newIntent, 0);
			}
		});

		modeListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				mode_S = modeList.get(arg2);
				new AlertDialog.Builder(EnterModeListActivity.this)
						.setTitle("请选择")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(new String[] { "修改", "删除" }, 0,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											Intent intent = new Intent();
											intent.setClass(
													EnterModeListActivity.this,
													ModifyModeActivity.class);
											intent.putExtra("mode",
													mode_S.getMode_S());
											mod_Idx = arg2;
											// 开始一个新的 Activity等候返回结果
											startActivityForResult(intent, 1);
										} else if (which == 1) {

											try {
												WriteUtil.write(
														MsgId_E.MSGID_MODE
																.getVal(),
														0,
														MsgType_E.MSGTYPE_REQ
																.getVal(),
														MsgOper_E.MSGOPER_DEL
																.getVal(),
														MsgDelReq_S.getSize(),
														new MsgDelReq_S(mode_S
																.getUsIdx())
																.getMsgDelReq_S());
												del_Idx = arg2;
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												Intent i = new Intent(
														"IOException");
												sendBroadcast(i);
												DisconnectionUtil
														.restart(EnterModeListActivity.this);
											}
										}
										dialog.dismiss();
									}
								}).setNegativeButton("取消", null).show();
				return false;
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
		modeBroadcastReceiver = new EnterModeBroadcastReceiver(
				EnterModeListActivity.this);
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_MODE.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());
		filter.addAction(MsgId_E.MSGID_MODE.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_MODE.getVal() + "_"
				+ MsgOper_E.MSGOPER_MAX.getVal());
		filter.addAction("IOException");
		registerReceiver(modeBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_MODE.getVal(), 1,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_MAX
							.getVal(), (short) 2,
					new MsgQryReq_S(rgn_S.getUsIdx()).getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(EnterModeListActivity.this, "获取列表失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(EnterModeListActivity.this);

		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(modeBroadcastReceiver);
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
					byte[] m_b = extras.getByteArray("mode");
					Mode_S m = new Mode_S();
					m.setMode_S(m_b);
					if (m.getUsRgnIdx() == rgn_S.getUsIdx()) {
						this.modeList.add(m);
						this.modeListView.setAdapter(modeListAdapter);
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] m_b = extras.getByteArray("mode");
					Mode_S m = new Mode_S();
					m.setMode_S(m_b);
					m.setMode_S(m_b);
					if (m.getUsRgnIdx() == rgn_S.getUsIdx()) {
						this.modeList.set(mod_Idx, m);
						this.modeListView.setAdapter(modeListAdapter);
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
		registerReceiver(modeBroadcastReceiver, filter);
		super.onResume();
	}
}
