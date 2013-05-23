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

import com.mac.smartcontrol.broadcast.ModifyModeBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Mode_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModifyModeActivity extends Activity {
	public Mode_S mode_S;
	public Spinner area_sp;
	ModifyModeBroadcastReceiver modifyModeBroadcastReceiver;
	public List<Rgn_S> areaList = new ArrayList<Rgn_S>();
	public List<String> areaNameList = new ArrayList<String>();
	public ArrayAdapter<String> area_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_mode);
		final EditText mode_name_Et = (EditText) findViewById(R.id.mode_name_et);
		final EditText voice_name_Et = (EditText) findViewById(R.id.voice_name_et);
		area_sp = (Spinner) findViewById(R.id.area_sp);
		mode_S = new Mode_S();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mode_S.setMode_S(bundle.getByteArray("mode"));
			mode_name_Et.setText(mode_S.getSzName());
			voice_name_Et.setText(mode_S.getSzVoice());
		}

		modifyModeBroadcastReceiver = new ModifyModeBroadcastReceiver(
				ModifyModeActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("9_3");
		filter.addAction("3_4");
		filter.addAction(MsgId_E.MSGID_MODE.getVal() + "_"
				+ MsgOper_E.MSGOPER_MOD.getVal());

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction("IOException");
		registerReceiver(modifyModeBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ModifyModeActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();

			DisconnectionUtil.restart(ModifyModeActivity.this);
		}

		area_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, areaNameList);
		area_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		area_sp.setAdapter(area_adapter);

		ImageView modify_Iv = (ImageView) findViewById(R.id.modify);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		modify_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mode_name = mode_name_Et.getText().toString().trim();
				if (mode_name == null || mode_name.equals("")) {
					Toast.makeText(ModifyModeActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (mode_name.getBytes().length > 32) {
					Toast.makeText(ModifyModeActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				String voice_name = voice_name_Et.getText().toString().trim();
				if (voice_name == null || voice_name.equals("")) {
					Toast.makeText(ModifyModeActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (voice_name.getBytes().length > 64) {
					Toast.makeText(ModifyModeActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				mode_S.setSzName(mode_name);
				mode_S.setSzVoice(voice_name);
				mode_S.setUsRgnIdx(areaList.get(
						area_sp.getSelectedItemPosition()).getUsIdx());
				try {
					WriteUtil.write(MsgId_E.MSGID_MODE.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MOD.getVal(), Mode_S.getSize(),
							mode_S.getMode_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyModeActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();

					DisconnectionUtil.restart(ModifyModeActivity.this);
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
		unregisterReceiver(modifyModeBroadcastReceiver);
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
