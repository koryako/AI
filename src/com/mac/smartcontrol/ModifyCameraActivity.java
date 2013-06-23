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

import com.mac.smartcontrol.broadcast.ModifyCameraBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Cama_S;
import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModifyCameraActivity extends Activity {
	public Cama_S cama_S;
	public Spinner area_sp;
	ModifyCameraBroadcastReceiver modifyCameraBroadcastReceiver;
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
		setContentView(R.layout.activity_modify_camera);
		camera_name_Et = (EditText) findViewById(R.id.camera_name_et);
		camera_uid_Et = (EditText) findViewById(R.id.uid_et);
		camera_user_Et = (EditText) findViewById(R.id.user_et);
		camera_pass_Et = (EditText) findViewById(R.id.pass_et);

		Bundle bundle = this.getIntent().getExtras();
		cama_S = new Cama_S();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("camera");
			cama_S.setCama_S(b);
		}
		camera_name_Et.setText(cama_S.getSzName());
		// byte[] ip_b = FormatTransfer.toLH(cama_S.getUiIpAddr());
		// String ip_Str = "";
		// for (int i = ip_b.length - 1; i >= 0; i--) {
		// if (ip_b[i] > 0) {
		// ip_Str += ip_b[i] + ".";
		// } else {
		// ip_Str += (ip_b[i] + 256) + ".";
		// }
		//
		// }
		camera_uid_Et.setText(cama_S.getSzUid());
		camera_user_Et.setText(cama_S.getSzUser());
		camera_pass_Et.setText(cama_S.getSzPass());
		area_sp = (Spinner) findViewById(R.id.area_sp);
		modifyCameraBroadcastReceiver = new ModifyCameraBroadcastReceiver(
				ModifyCameraActivity.this);
		IntentFilter filter = new IntentFilter();

		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_CAMA.getVal() + "_"
				+ MsgOper_E.MSGOPER_MOD.getVal());
		filter.addAction("IOException");
		registerReceiver(modifyCameraBroadcastReceiver, filter);

		// try {
		WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
				MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY.getVal(),
				(short) 2, new MsgQryReq_S((short) 0).getMsgQryReq_S(), this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(ModifyCameraActivity.this, "��ȷ�������Ƿ���,����ʧ��",
		// Toast.LENGTH_LONG).show();
		// DisconnectionUtil.restart(ModifyCameraActivity.this);
		// }

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
				String camera_name = camera_name_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_name, "���Ʋ���Ϊ��", "����̫��"))
					return;
				String camera_uid = camera_uid_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_uid, "��Ų���Ϊ��", "���̫��"))
					return;

				String camera_user = camera_user_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_user, "�˺Ų���Ϊ��", "�˺�̫��"))
					return;

				String camera_pass = camera_pass_Et.getText().toString().trim();
				if (!checkEditTextStr(camera_pass, "���벻��Ϊ��", "����̫��"))
					return;

				// String[] ip_Str = ip.split("\\.");
				// byte[] ip_b = new byte[4];
				// for (int i = 0; i < ip_Str.length; i++) {
				// int b_p = Integer.parseInt(ip_Str[i]);
				// if (b_p >= 128) {
				// b_p -= 255;
				// }
				// ip_b[3 - i] = (byte) b_p;
				// }
				// cama_S.setUiIpAddr(FormatTransfer.lBytesToInt(ip_b));
				// short port = 0;
				// try {
				// port = Short.parseShort(port_Str);
				// if (port <= 0) {
				// Toast.makeText(ModifyCameraActivity.this, "�˿ڲ���ȷ",
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				// } catch (NumberFormatException e1) {
				// // TODO Auto-generated catch block
				// Toast.makeText(ModifyCameraActivity.this, "�˿ڷ�ΧΪ��0��32767",
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				// cama_S.setUsPort(port);
				cama_S.setSzName(camera_name);
				cama_S.setSzUid(camera_uid);
				cama_S.setSzName(camera_user);
				cama_S.setSzPass(camera_pass);
				cama_S.setUsRgnIdx(areaList.get(
						area_sp.getSelectedItemPosition()).getUsIdx());
				// try {
				WriteUtil.write(MsgId_E.MSGID_CAMA.getVal(), 0,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_MOD.getVal(), Cama_S.getSize(),
						cama_S.getCama_S(), ModifyCameraActivity.this);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Toast.makeText(ModifyCameraActivity.this, "��ȷ�������Ƿ���,����ʧ��",
				// Toast.LENGTH_LONG).show();
				// DisconnectionUtil.restart(ModifyCameraActivity.this);
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
			Toast.makeText(ModifyCameraActivity.this, msg1, Toast.LENGTH_LONG)
					.show();
			return false;
		}

		if (text.getBytes().length > 32) {
			Toast.makeText(ModifyCameraActivity.this, msg2, Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(modifyCameraBroadcastReceiver);
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
