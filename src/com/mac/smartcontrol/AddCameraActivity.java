package com.mac.smartcontrol;

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

import com.mac.smartcontrol.broadcast.AddCameraBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Cama_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class AddCameraActivity extends Activity {
	public Cama_S cama_S;
	public Spinner area_sp;
	AddCameraBroadcastReceiver addCameraBroadcastReceiver;
	public List<Rgn_S> areaList = new ArrayList<Rgn_S>();
	public List<String> areaNameList = new ArrayList<String>();
	public ArrayAdapter<String> area_adapter;
	EditText camera_name_Et;
	EditText camera_uid_Et;
	EditText camera_user_Et;
	EditText camera_pass_Et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_camera);
		camera_name_Et = (EditText) findViewById(R.id.camera_name_et);
		camera_uid_Et = (EditText) findViewById(R.id.uid_et);
		camera_user_Et = (EditText) findViewById(R.id.user_et);
		camera_pass_Et = (EditText) findViewById(R.id.pass_et);
		area_sp = (Spinner) findViewById(R.id.area_sp);
		addCameraBroadcastReceiver = new AddCameraBroadcastReceiver(
				AddCameraActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CAMA.getVal() + "_"
				+ MsgOper_E.MSGOPER_ADD.getVal());
		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction("IOException");
		registerReceiver(addCameraBroadcastReceiver, filter);

		// try {
		WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
				MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY.getVal(),
				(short) 2, new MsgQryReq_S((short) 0).getMsgQryReq_S(), this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(AddCameraActivity.this, "请确认网络是否开启,连接失败",
		// Toast.LENGTH_LONG).show();
		// DisconnectionUtil.restart(AddCameraActivity.this);
		// }

		area_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, areaNameList);
		area_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		area_sp.setAdapter(area_adapter);

		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		cama_S = new Cama_S();
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String camera_name = camera_name_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_name, "名称不能为空", "名称太长"))
					return;
				String camera_uid = camera_uid_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_uid, "编号不能为空", "编号太长"))
					return;

				String camera_user = camera_user_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_user, "账号不能为空", "账号太长"))
					return;

				String camera_pass = camera_pass_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_pass, "密码不能为空", "密码太长"))
					return;
				// String ip = ip_Et.getText().toString().trim();
				// if (!RegularUtil.isIp(ip)) {
				// Toast.makeText(AddCameraActivity.this, "Ip格式错误,请重新输入",
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				// String port = port_Et.getText().toString().trim();
				// cama_S.setSzName(camera_name);
				// String[] ip_Str = ip.split("\\.");
				// byte[] ip_b = new byte[4];
				// for (int i = 0; i < ip_Str.length; i++) {
				// int b_p = Integer.parseInt(ip_Str[i]);
				// if (b_p >= 128) {
				// b_p -= 255;
				// }
				// ip_b[3 - i] = (byte) b_p;
				// }
				cama_S.setSzName(camera_name);
				cama_S.setSzUid(camera_uid);
				cama_S.setSzName(camera_user);
				cama_S.setSzPass(camera_pass);
				cama_S.setUsRgnIdx(areaList.get(
						area_sp.getSelectedItemPosition()).getUsIdx());
				// try {
				WriteUtil.write(MsgId_E.MSGID_CAMA.getVal(), 0,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_ADD.getVal(), Cama_S.getSize(),
						cama_S.getCama_S(), AddCameraActivity.this);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Toast.makeText(AddCameraActivity.this, "请确认网络是否开启,连接失败",
				// Toast.LENGTH_LONG).show();
				// DisconnectionUtil.restart(AddCameraActivity.this);
				// }
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

	private boolean checkEditTextStr(String text, String msg1, String msg2) {

		if (text == null || text.equals("")) {
			Toast.makeText(AddCameraActivity.this, msg1, Toast.LENGTH_LONG)
					.show();
			return false;
		}

		if (text.getBytes().length > 32) {
			Toast.makeText(AddCameraActivity.this, msg2, Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(addCameraBroadcastReceiver);
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
