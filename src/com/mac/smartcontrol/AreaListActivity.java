package com.mac.smartcontrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.adapter.AreaListAdapter;
import com.mac.smartcontrol.broadcast.AreaBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class AreaListActivity extends Activity {

	public List<Rgn_S> areaList = null;
	public AreaListAdapter areaListAdapter = null;
	public ListView areaListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	AreaBroadcastReceiver areaBroadcastReceiver;
	TextView area_title_tv;
	public Rgn_S rgn_S = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		area_title_tv = (TextView) findViewById(R.id.userlist_title);
		area_title_tv.setText(R.string.area_list);
		rgn_S = new Rgn_S();
		areaListView = (ListView) findViewById(R.id.userlist);
		areaList = new ArrayList<Rgn_S>();
		areaListAdapter = new AreaListAdapter(AreaListActivity.this, areaList);
		areaListView.setAdapter(areaListAdapter);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(AreaListActivity.this,
						R.style.MyDialog);
				// 设置它的ContentView
				dialog.setContentView(R.layout.area_add_dialog);

				TextView dialog_title_tv = (TextView) dialog
						.findViewById(R.id.dialog_title);
				dialog_title_tv.setText(R.string.add_area);
				dialog.show();

				ImageView submit_Iv = (ImageView) dialog
						.findViewById(R.id.submit_iv);
				ImageView cancel_Iv = (ImageView) dialog
						.findViewById(R.id.cancel_iv);
				final EditText name_et = (EditText) dialog
						.findViewById(R.id.name_et);

				cancel_Iv
						.setOnClickListener(new android.view.View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				submit_Iv
						.setOnClickListener(new android.view.View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String name = name_et.getText().toString()
										.trim();
								if (name == null || "".equals(name)) {
									Toast.makeText(AreaListActivity.this,
											"名字不能为空", Toast.LENGTH_LONG).show();
									return;
								}
								if (name.getBytes().length > 32) {
									Toast.makeText(AreaListActivity.this,
											"名字太长了", Toast.LENGTH_LONG).show();
									return;
								}
								// try {
								AreaListActivity.this.rgn_S.setSzName(name);
								WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
										MsgType_E.MSGTYPE_REQ.getVal(),
										MsgOper_E.MSGOPER_ADD.getVal(),
										Rgn_S.getSize(), rgn_S.getRgn_S(),
										AreaListActivity.this);
								dialog.dismiss();
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// Toast.makeText(AreaListActivity.this,
								// "添加失败", Toast.LENGTH_LONG).show();
								// DisconnectionUtil
								// .restart(AreaListActivity.this);
								// }
							}
						});
			}
		});
		areaBroadcastReceiver = new AreaBroadcastReceiver(AreaListActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_ADD.getVal());

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_MOD.getVal());

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction("IOException");
		registerReceiver(areaBroadcastReceiver, filter);

		// try {
		WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
				MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY.getVal(),
				(short) 2, new MsgQryReq_S((short) 0).getMsgQryReq_S(), this);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(AreaListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
		// .show();
		// DisconnectionUtil.restart(AreaListActivity.this);
		//
		// }

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(areaBroadcastReceiver);
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
