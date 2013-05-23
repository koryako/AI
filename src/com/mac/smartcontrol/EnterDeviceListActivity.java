package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.mac.smartcontrol.adapter.EnterDeviceListAdapter;
import com.mac.smartcontrol.broadcast.CmdBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
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
	IntentFilter filter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		device_title_tv = (TextView) findViewById(R.id.userlist_title);
		device_title_tv.setText(R.string.device_list);
		areaMap = new HashMap<Short, Rgn_S>();
		deviceListView = (ListView) findViewById(R.id.userlist);
		deviceList = new ArrayList<Appl_S>();
		deviceListAdapter = new EnterDeviceListAdapter(
				EnterDeviceListActivity.this, deviceList, areaMap);
		deviceListView.setAdapter(deviceListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);

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

		deviceListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						appl_S = deviceList.get(arg2);
						new AlertDialog.Builder(EnterDeviceListActivity.this)
								.setTitle("请选择")
								.setIcon(android.R.drawable.ic_dialog_info)
								.setSingleChoiceItems(
										new String[] { "修改", "删除" }, 0,
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												if (which == 0) {
													Intent intent = new Intent();
													intent.setClass(
															EnterDeviceListActivity.this,
															ModifyDeviceActivity.class);
													intent.putExtra("device",
															appl_S.getAppl_S());
													mod_Idx = arg2;
													// 开始一个新的 Activity等候返回结果
													startActivityForResult(
															intent, 1);
												} else if (which == 1) {
													try {
														WriteUtil
																.write(MsgId_E.MSGID_APPL
																		.getVal(),
																		0,
																		MsgType_E.MSGTYPE_REQ
																				.getVal(),
																		MsgOper_E.MSGOPER_DEL
																				.getVal(),
																		MsgDelReq_S
																				.getSize(),
																		new MsgDelReq_S(
																				appl_S.getUsIdx())
																				.getMsgDelReq_S());
														del_Idx = arg2;
													} catch (IOException e) {
														// TODO Auto-generated
														// catch block
														Intent i = new Intent(
																"IOException");
														sendBroadcast(i);
														DisconnectionUtil
																.restart(EnterDeviceListActivity.this);
													}
												}
												dialog.dismiss();
											}
										}).setNegativeButton("取消", null).show();
						return false;
					}
				});
		if (msgId == 4) {
			add_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent newIntent = new Intent(EnterDeviceListActivity.this,
							AddDeviceActivity.class);
					// 开始一个新的 Activity等候返回结果
					startActivityForResult(newIntent, 0);
				}
			});
		} else if (msgId == 34) {
			add_Iv.setVisibility(View.INVISIBLE);
		}
		cmdBroadcastReceiver = new CmdBroadcastReceiver(
				EnterDeviceListActivity.this);
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_MAX.getVal());
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
			DisconnectionUtil.restart(EnterDeviceListActivity.this);
		}

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(cmdBroadcastReceiver, filter);
		super.onResume();
	}
}
