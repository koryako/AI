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
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.ModifyDeviceBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModifyDeviceActivity extends Activity {
	public Appl_S appl_S;
	String[] types = new String[] { "灯", "电视", "机顶盒", "空调", "窗帘" };
	public Spinner area_sp;
	ModifyDeviceBroadcastReceiver modifyDeviceBroadcastReceiver;
	public List<Rgn_S> areaList = new ArrayList<Rgn_S>();
	public List<String> areaNameList = new ArrayList<String>();
	public ArrayAdapter<String> area_adapter;
	TextView list_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_sense);
		Bundle bundle = this.getIntent().getExtras();
		appl_S = new Appl_S();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("device");
			appl_S.setAppl_S(b);
		}
		final EditText sense_name_Et = (EditText) findViewById(R.id.sensename_et);
		final Spinner sense_type_sp = (Spinner) findViewById(R.id.sensetype_sp);
		list_tv = (TextView) findViewById(R.id.list_title);
		list_tv.setText(R.string.modify_device);
		area_sp = (Spinner) findViewById(R.id.area_sp);
		modifyDeviceBroadcastReceiver = new ModifyDeviceBroadcastReceiver(
				ModifyDeviceActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_MOD.getVal());

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction("IOException");
		registerReceiver(modifyDeviceBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ModifyDeviceActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(ModifyDeviceActivity.this);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sense_type_sp.setAdapter(adapter);

		sense_name_Et.setText(appl_S.getSzName());
		sense_type_sp.setSelection(appl_S.getUcType() - 1);

		area_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, areaNameList);
		area_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		area_sp.setAdapter(area_adapter);

		ImageView modify_Iv = (ImageView) findViewById(R.id.modify);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		modify_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String senseName = sense_name_Et.getText().toString().trim();
				if (senseName == null || senseName.equals("")) {
					Toast.makeText(ModifyDeviceActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (senseName.getBytes().length > 32) {
					Toast.makeText(ModifyDeviceActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				appl_S.setSzName(senseName);
				appl_S.setUcType((byte) (sense_type_sp
						.getSelectedItemPosition() + 1));
				appl_S.setUsRgnIdx(areaList.get(
						area_sp.getSelectedItemPosition()).getUsIdx());
				try {
					WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MOD.getVal(), Appl_S.getSize(),
							appl_S.getAppl_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyDeviceActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(ModifyDeviceActivity.this);
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
		unregisterReceiver(modifyDeviceBroadcastReceiver);
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
