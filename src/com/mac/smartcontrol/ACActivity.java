package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.ControlBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.oper.MsgOperCmd_E;
import define.oper.body.req.MsgCmdQryByDevReq_S;
import define.type.ApplType_E;
import define.type.CmdDevType_E;
import define.type.CmdType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ACActivity extends Activity {
	ImageView back_Iv;
	ImageView user_defined_Iv;
	ImageView switch_iv;

	ImageView time_iv;
	ImageView wind_d_iv;
	ImageView wind_t_add_iv;
	ImageView wind_t_sub_iv;
	ImageView air_out_iv;
	ImageView heating_iv;
	String[] names = new String[] { "开/关", "定时", "风向", "温度+", "温度-", "通风", "制热" };
	private Map<Integer, String> btn_Name;
	Appl_S appl_S = new Appl_S();
	public List<Cmd_S> cmd_List;
	ControlBroadcastReceiver controlBroadcastReceiver;

	public boolean b = false;
	IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_ac);
		cmd_List = new ArrayList<Cmd_S>();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("device");
			appl_S.setAppl_S(b);
		}
		controlBroadcastReceiver = new ControlBroadcastReceiver(this,
				ApplType_E.APPL_TYPE_AIRCOND.getVal());
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal());
		registerReceiver(controlBroadcastReceiver, filter);

		init_Btn_Name();

		try {
			WriteUtil.write(
					MsgId_E.MSGID_CMD.getVal(),
					1,
					MsgType_E.MSGTYPE_REQ.getVal(),
					MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
					MsgCmdQryByDevReq_S.getSize(),
					new MsgCmdQryByDevReq_S(CmdDevType_E.CMD_DEV_APPL.getVal(),
							appl_S.getUsIdx(), CmdType_E.CMD_TYPE_PREDEF
									.getVal()).getMsgCmdQryByDevReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ACActivity.this, "请确认网络是否开启,连接失败", Toast.LENGTH_LONG)
					.show();
			DisconnectionUtil.restart(ACActivity.this);
		}

		back_Iv = (ImageView) findViewById(R.id.back_iv);
		user_defined_Iv = (ImageView) findViewById(R.id.user_defined_iv);
		user_defined_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.setClass(ACActivity.this, UserDefinedActivity.class);
				startActivity(intent);
				unregisterReceiver(controlBroadcastReceiver);
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

	void init_Btn_Name() {

		btn_Name = new HashMap<Integer, String>();
		switch_iv = (ImageView) findViewById(R.id.switch_iv);
		time_iv = (ImageView) findViewById(R.id.time_iv);
		wind_d_iv = (ImageView) findViewById(R.id.wind_d_iv);
		wind_t_add_iv = (ImageView) findViewById(R.id.wind_t_add_iv);
		wind_t_sub_iv = (ImageView) findViewById(R.id.wind_t_sub_iv);
		air_out_iv = (ImageView) findViewById(R.id.air_out_iv);
		heating_iv = (ImageView) findViewById(R.id.heating_iv);

		switch_iv.setOnClickListener(new BtnListener(R.id.switch_iv));
		time_iv.setOnClickListener(new BtnListener(R.id.time_iv));
		wind_d_iv.setOnClickListener(new BtnListener(R.id.wind_d_iv));
		wind_t_add_iv.setOnClickListener(new BtnListener(R.id.wind_t_add_iv));
		wind_t_sub_iv.setOnClickListener(new BtnListener(R.id.wind_t_sub_iv));
		air_out_iv.setOnClickListener(new BtnListener(R.id.air_out_iv));
		heating_iv.setOnClickListener(new BtnListener(R.id.heating_iv));

		btn_Name.put(R.id.switch_iv, names[0]);
		btn_Name.put(R.id.time_iv, names[1]);
		btn_Name.put(R.id.wind_d_iv, names[2]);
		btn_Name.put(R.id.wind_t_add_iv, names[3]);
		btn_Name.put(R.id.wind_t_sub_iv, names[4]);
		btn_Name.put(R.id.air_out_iv, names[5]);
		btn_Name.put(R.id.heating_iv, names[6]);
	}

	private int checkIsExist(String name) {
		for (int i = 0; i < cmd_List.size(); i++) {
			if (cmd_List.get(i).getSzName().equals(name)) {
				return i;
			}
		}

		return -1;
	}

	class BtnListener implements OnClickListener {
		int id;

		public BtnListener(int id) {
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!b) {
				Toast.makeText(ACActivity.this, "正在获取信息", Toast.LENGTH_LONG)
						.show();
				return;
			}
			int idx = checkIsExist(btn_Name.get(id));
			if (idx == -1) {
				Toast.makeText(ACActivity.this, "请添加此按钮的指令", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.putExtra("type", 0);
				intent.putExtra("btn_Name", btn_Name.get(id));
				intent.setClass(ACActivity.this, AddCmdActivity.class);
				startActivityForResult(intent, 0);
			} else {
				Cmd_S cmd_S = cmd_List.get(idx);
				try {
					WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOperCmd_E.MSGOPER_CMD_EXC.getVal(),
							Cmd_S.getSize(), cmd_S.getCmd_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ACActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(ACActivity.this);
				}
			}
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
					byte[] c_b = extras.getByteArray("cmd");
					Cmd_S cmd_S = new Cmd_S();
					cmd_S.setCmd_S(c_b);
					if (cmd_S.getUsDevIdx() == appl_S.getUsIdx()) {
						this.cmd_List.add(cmd_S);
					}
				}
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(controlBroadcastReceiver);
		super.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(controlBroadcastReceiver, filter);
		super.onResume();
	}
}
