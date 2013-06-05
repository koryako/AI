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

import com.mac.smartcontrol.adapter.SenseListAdapter;
import com.mac.smartcontrol.broadcast.SenseBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Rgn_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class SenseListActivity extends Activity {

	public List<Sens_S> senseList = null;
	public SenseListAdapter senseListAdapter = null;
	public ListView senseListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	SenseBroadcastReceiver senseBroadcastReceiver;
	TextView sense_title_tv;
	public Sens_S sens_S = null;
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

		sense_title_tv = (TextView) findViewById(R.id.userlist_title);
		sense_title_tv.setText(R.string.sense_list);
		sens_S = new Sens_S();

		senseListView = (ListView) findViewById(R.id.userlist);
		senseList = new ArrayList<Sens_S>();
		senseListAdapter = new SenseListAdapter(SenseListActivity.this,
				senseList);
		senseListView.setAdapter(senseListAdapter);
		senseListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				sens_S = senseList.get(arg2);
				new AlertDialog.Builder(SenseListActivity.this)
						.setTitle("请选择")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(new String[] { "修改", "删除" }, 0,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											Intent intent = new Intent();
											intent.setClass(
													SenseListActivity.this,
													ModifySenseActivity.class);
											intent.putExtra("sense",
													sens_S.getSens_S());
											mod_Idx = arg2;
											// 开始一个新的 Activity等候返回结果
											startActivityForResult(intent, 1);
										} else if (which == 1) {
											try {
												WriteUtil.write(
														MsgId_E.MSGID_SENS
																.getVal(),
														0,
														MsgType_E.MSGTYPE_REQ
																.getVal(),
														MsgOper_E.MSGOPER_DEL
																.getVal(),
														MsgDelReq_S.getSize(),
														new MsgDelReq_S(sens_S
																.getUsIdx())
																.getMsgDelReq_S());
												del_Idx = arg2;
											} catch (IOException e) {
												// TODO Auto-generated
												// catch block
												Intent i = new Intent(
														"IOException");
												sendBroadcast(i);
												DisconnectionUtil
														.restart(SenseListActivity.this);
											}
										}
										dialog.dismiss();
									}
								}).setNegativeButton("取消", null).show();
				return false;
			}
		});

		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(SenseListActivity.this,
						AddSenseActivity.class);
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
		senseBroadcastReceiver = new SenseBroadcastReceiver(
				SenseListActivity.this);
		IntentFilter filter = new IntentFilter();

		filter.addAction(MsgId_E.MSGID_SENS.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_SENS.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_SENS.getVal() + "_"
				+ MsgOper_E.MSGOPER_MAX.getVal());

		filter.addAction("IOException");
		registerReceiver(senseBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_SENS.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_MAX
							.getVal(), (short) 2,
					new MsgQryReq_S(rgn_S.getUsIdx()).getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(SenseListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
					.show();
			DisconnectionUtil.restart(SenseListActivity.this);
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(senseBroadcastReceiver);
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
					byte[] b = extras.getByteArray("sense");
					Sens_S sens_S = new Sens_S();
					sens_S.setSens_S(b);
					if (sens_S.getUsRgnIdx() == rgn_S.getUsIdx()) {
						this.senseList.add(sens_S);
						this.senseListView.setAdapter(senseListAdapter);
					}
					// this.senseListAdapter.notifyDataSetChanged();
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] s_b = extras.getByteArray("sense");
					Sens_S sens_S = new Sens_S();
					sens_S.setSens_S(s_b);
					if (sens_S.getUsRgnIdx() == rgn_S.getUsIdx()) {
						Sens_S s = senseList.get(mod_Idx);
						if (s.getUsIdx() == sens_S.getUsIdx()) {
							senseList.set(mod_Idx, sens_S);
						}
					} else {
						senseList.remove(mod_Idx);
					}
					this.senseListView.setAdapter(senseListAdapter);
					// this.senseListAdapter.notifyDataSetChanged();
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
