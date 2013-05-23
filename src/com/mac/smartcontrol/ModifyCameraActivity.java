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

import com.mac.smartcontrol.broadcast.ModifyCameraBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.FormatTransfer;
import com.mac.smartcontrol.util.RegularUtil;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_camera);
		final EditText camera_name_Et = (EditText) findViewById(R.id.camera_name_et);
		final EditText ip_Et = (EditText) findViewById(R.id.ip_et);
		final EditText port_Et = (EditText) findViewById(R.id.port_et);

		Bundle bundle = this.getIntent().getExtras();
		cama_S = new Cama_S();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("camera");
			cama_S.setCama_S(b);
		}
		camera_name_Et.setText(cama_S.getSzName());
		byte[] ip_b = FormatTransfer.toLH(cama_S.getUiIpAddr());
		String ip_Str = "";
		for (int i = ip_b.length - 1; i >= 0; i--) {
			if (ip_b[i] > 0) {
				ip_Str += ip_b[i] + ".";
			} else {
				ip_Str += (ip_b[i] + 256) + ".";
			}

		}
		ip_Et.setText(ip_Str.substring(0, ip_Str.length() - 1));
		port_Et.setText(cama_S.getUsPort() + "");

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

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ModifyCameraActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(ModifyCameraActivity.this);
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
				String camera_name = camera_name_Et.getText().toString().trim();
				if (camera_name == null || camera_name.equals("")) {
					Toast.makeText(ModifyCameraActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (camera_name.getBytes().length > 32) {
					Toast.makeText(ModifyCameraActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				String ip = ip_Et.getText().toString().trim();
				if (!RegularUtil.isIp(ip)) {
					Toast.makeText(ModifyCameraActivity.this, "Ip格式错误,请重新输入",
							Toast.LENGTH_LONG).show();
					return;
				}
				String port_Str = port_Et.getText().toString().trim();
				cama_S.setSzName(camera_name);
				String[] ip_Str = ip.split("\\.");
				byte[] ip_b = new byte[4];
				for (int i = 0; i < ip_Str.length; i++) {
					int b_p = Integer.parseInt(ip_Str[i]);
					if (b_p >= 128) {
						b_p -= 255;
					}
					ip_b[3 - i] = (byte) b_p;
				}
				cama_S.setUiIpAddr(FormatTransfer.lBytesToInt(ip_b));
				short port = 0;
				try {
					port = Short.parseShort(port_Str);
					if (port <= 0) {
						Toast.makeText(ModifyCameraActivity.this, "端口不正确",
								Toast.LENGTH_LONG).show();
						return;
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyCameraActivity.this, "端口范围为：0—32767",
							Toast.LENGTH_LONG).show();
					return;
				}
				cama_S.setUsPort(port);
				cama_S.setUsRgnIdx(areaList.get(
						area_sp.getSelectedItemPosition()).getUsIdx());
				try {
					WriteUtil.write(MsgId_E.MSGID_CAMA.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MOD.getVal(), Cama_S.getSize(),
							cama_S.getCama_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyCameraActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(ModifyCameraActivity.this);
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
