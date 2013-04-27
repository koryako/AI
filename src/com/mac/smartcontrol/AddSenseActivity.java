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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.AddSenseBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Rgn_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class AddSenseActivity extends Activity {
	public Sens_S sens_S;
	String[] types = new String[] { "煤气", "烟雾" };
	public Spinner area_sp;
	AddSenseBroadcastReceiver addSenseBroadcastReceiver;
	public List<Rgn_S> areaList = new ArrayList<Rgn_S>();
	public List<String> areaNameList = new ArrayList<String>();
	public ArrayAdapter<String> area_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_sense);
		final EditText sense_name_Et = (EditText) findViewById(R.id.sensename_et);
		final Spinner sense_type_sp = (Spinner) findViewById(R.id.sensetype_sp);
		area_sp = (Spinner) findViewById(R.id.area_sp);
		addSenseBroadcastReceiver = new AddSenseBroadcastReceiver(
				AddSenseActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("6_1");
		filter.addAction("3_4");
		filter.addAction("UnknownHostException");
		filter.addAction("IOException");
		registerReceiver(addSenseBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(AddSenseActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sense_type_sp.setAdapter(adapter);

		area_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, areaNameList);
		area_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		area_sp.setAdapter(area_adapter);

		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		sens_S = new Sens_S();
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String senseName = sense_name_Et.getText().toString().trim();
				if (senseName == null || senseName.equals("")) {
					Toast.makeText(AddSenseActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (senseName.getBytes().length > 32) {
					Toast.makeText(AddSenseActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				sens_S.setSzName(senseName);
				sens_S.setUcType((byte) (sense_type_sp
						.getSelectedItemPosition() + 1));
				sens_S.setUsRgnIdx(areaList.get(
						area_sp.getSelectedItemPosition()).getUsIdx());
				try {
					WriteUtil.write(MsgId_E.MSGID_SENS.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_ADD.getVal(), Sens_S.getSize(),
							sens_S.getSens_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(AddSenseActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(addSenseBroadcastReceiver);
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
