package com.mac.smartcontrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.mac.smartcontrol.broadcast.LocationBroadcastReceiver;
import com.mac.smartcontrol.util.SaveLocationUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Mode_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class LocationActivity extends Activity {

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	int location_distance = 0;
	String location_longitude = "0";
	String location_latitude = "0";
	String location_address = "";
	BDLocation bdLocation = null;
	boolean location_isOpen = false;
	public short mode_Id = -1;
	LocationBroadcastReceiver broadcastReceiver;
	public List<Mode_S> modelist;
	public List<String> modelistStr;
	public ArrayAdapter<String> mode_adapter;
	public Spinner mode_Sp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		ImageView postion_Iv = (ImageView) findViewById(R.id.position_iv);
		ImageView save_Iv = (ImageView) findViewById(R.id.save_iv);
		final ImageView remeber_Iv = (ImageView) findViewById(R.id.remeber_iv);
		LinearLayout remeber_ll = (LinearLayout) findViewById(R.id.remeber_ll);
		modelist = new ArrayList<Mode_S>();
		modelistStr = new ArrayList<String>();
		mode_Sp = (Spinner) findViewById(R.id.mode_sp);
		mode_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, modelistStr);
		mode_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		mode_Sp.setAdapter(mode_adapter);
		final TextView position_name_Tv = (TextView) findViewById(R.id.position_name_et);
		final TextView position_distance_Et = (TextView) findViewById(R.id.distance_et);
		sharedPreferences = getSharedPreferences("location", 0);
		if (sharedPreferences != null) {
			editor = sharedPreferences.edit();
			location_distance = sharedPreferences.getInt("location_distance",
					1000);
			location_longitude = sharedPreferences.getString(
					"location_longitude", "0");
			location_latitude = sharedPreferences.getString(
					"location_latitude", "0");
			location_address = sharedPreferences.getString("location_address",
					"");
			location_isOpen = sharedPreferences.getBoolean("location_isOpen",
					false);
			mode_Id = (short) sharedPreferences.getInt("location_mode_ID", -1);

			position_name_Tv.setText(location_address);
			position_distance_Et.setText(location_distance + "");
			if (location_isOpen)
				remeber_Iv.setImageResource(R.drawable.checkbox_bg_focus);

		}
		broadcastReceiver = new LocationBroadcastReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_MODE.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		registerReceiver(broadcastReceiver, filter);
		// try {
		WriteUtil.write(MsgId_E.MSGID_MODE.getVal(), 0,
				MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY.getVal(),
				(short) 2, new MsgQryReq_S((short) 0).getMsgQryReq_S(),
				LocationActivity.this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(LocationActivity.this, "请确认网络是否开启,连接失败",
		// Toast.LENGTH_LONG).show();
		// DisconnectionUtil.restart(LocationActivity.this);
		// }

		remeber_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				location_isOpen = (!location_isOpen);
				if (location_isOpen) {
					remeber_Iv.setImageResource(R.drawable.checkbox_bg_focus);
				} else {
					remeber_Iv.setImageResource(R.drawable.checkbox_bg);
				}
			}
		});

		postion_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bdLocation = SaveLocationUtil.getBdLocation();
				if (bdLocation == null) {
					position_name_Tv.setText("正在获取定位信息……");
				} else {
					position_name_Tv.setText(bdLocation.getAddrStr());
					location_longitude = bdLocation.getLongitude() + "";
					location_latitude = bdLocation.getLatitude() + "";
					location_address = bdLocation.getAddrStr();
				}
			}
		});

		save_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int idx = mode_Sp.getSelectedItemPosition();
				if (idx < 0) {
					Toast.makeText(LocationActivity.this, "请选择要执行的智能模式",
							Toast.LENGTH_LONG).show();
					return;
				}
				mode_Id = modelist.get(idx).getUsIdx();
				editor.putInt("location_distance", location_distance);
				editor.putString("location_longitude", location_longitude);
				editor.putString("location_latitude", location_latitude);
				editor.putString("location_address", location_address);
				editor.putBoolean("location_isOpen", location_isOpen);
				editor.putInt("location_mode_ID", mode_Id);
				editor.commit();
				Toast.makeText(LocationActivity.this, "保存成功", Toast.LENGTH_LONG)
						.show();
			}
		});

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(broadcastReceiver);
		super.finish();
	}
}
