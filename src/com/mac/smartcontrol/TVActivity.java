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

public class TVActivity extends Activity {
	ImageView back_Iv;
	ImageView user_defined_Iv;
	ImageView power_Iv;

	ImageView btn0_Iv;
	ImageView btn1_Iv;
	ImageView btn2_Iv;
	ImageView btn3_Iv;
	ImageView btn4_Iv;
	ImageView btn5_Iv;
	ImageView btn6_Iv;
	ImageView btn7_Iv;
	ImageView btn8_Iv;
	ImageView btn9_Iv;

	ImageView up_Iv;
	ImageView down_Iv;
	ImageView left_Iv;
	ImageView right_Iv;
	ImageView enter_Iv;

	ImageView slience_Iv;
	ImageView msg_Iv;

	ImageView tv_power_Iv;
	ImageView v_add_Iv;
	ImageView v_sub_Iv;
	ImageView tvav_Iv;

	String[] names = new String[] { "POWER", "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "静音", "信息", "电视电源", "音量+", "音量-", "TV/AV", "左", "右",
			"上", "下", "确定" };
	private Map<Integer, String> btn_Name;
	Appl_S appl_S;
	public List<Cmd_S> cmd_List;
	ControlBroadcastReceiver controlBroadcastReceiver;

	public boolean b = false;
	IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_tv);
		cmd_List = new ArrayList<Cmd_S>();
		appl_S = new Appl_S();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("device");
			appl_S.setAppl_S(b);
		}
		controlBroadcastReceiver = new ControlBroadcastReceiver(this,
				ApplType_E.APPL_TYPE_TVSET.getVal());
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
			Toast.makeText(TVActivity.this, "请确认网络是否开启,连接失败", Toast.LENGTH_LONG)
					.show();
			DisconnectionUtil.restart(TVActivity.this);
		}
		back_Iv = (ImageView) findViewById(R.id.back_iv);
		user_defined_Iv = (ImageView) findViewById(R.id.user_defined_iv);
		user_defined_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TVActivity.this, UserDefinedActivity.class);
				intent.putExtra("device", appl_S.getAppl_S());
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
		power_Iv = (ImageView) findViewById(R.id.power_iv);
		btn0_Iv = (ImageView) findViewById(R.id.btn0_iv);
		btn1_Iv = (ImageView) findViewById(R.id.btn1_iv);
		btn2_Iv = (ImageView) findViewById(R.id.btn2_iv);
		btn3_Iv = (ImageView) findViewById(R.id.btn3_iv);
		btn4_Iv = (ImageView) findViewById(R.id.btn4_iv);
		btn5_Iv = (ImageView) findViewById(R.id.btn5_iv);
		btn6_Iv = (ImageView) findViewById(R.id.btn6_iv);
		btn7_Iv = (ImageView) findViewById(R.id.btn7_iv);
		btn8_Iv = (ImageView) findViewById(R.id.btn8_iv);
		btn9_Iv = (ImageView) findViewById(R.id.btn9_iv);
		slience_Iv = (ImageView) findViewById(R.id.slience_iv);
		msg_Iv = (ImageView) findViewById(R.id.msg_iv);

		tv_power_Iv = (ImageView) findViewById(R.id.tv_power_iv);
		v_add_Iv = (ImageView) findViewById(R.id.v_add_iv);
		v_sub_Iv = (ImageView) findViewById(R.id.v_sub_iv);
		tvav_Iv = (ImageView) findViewById(R.id.tvav_iv);

		left_Iv = (ImageView) findViewById(R.id.left_iv);
		right_Iv = (ImageView) findViewById(R.id.right_iv);
		up_Iv = (ImageView) findViewById(R.id.up_iv);
		down_Iv = (ImageView) findViewById(R.id.down_iv);
		enter_Iv = (ImageView) findViewById(R.id.enter_iv);

		power_Iv.setOnClickListener(new BtnListener(R.id.power_iv));
		btn0_Iv.setOnClickListener(new BtnListener(R.id.btn0_iv));
		btn1_Iv.setOnClickListener(new BtnListener(R.id.btn1_iv));
		btn2_Iv.setOnClickListener(new BtnListener(R.id.btn2_iv));
		btn3_Iv.setOnClickListener(new BtnListener(R.id.btn3_iv));
		btn4_Iv.setOnClickListener(new BtnListener(R.id.btn4_iv));
		btn5_Iv.setOnClickListener(new BtnListener(R.id.btn5_iv));
		btn6_Iv.setOnClickListener(new BtnListener(R.id.btn6_iv));
		btn7_Iv.setOnClickListener(new BtnListener(R.id.btn7_iv));
		btn8_Iv.setOnClickListener(new BtnListener(R.id.btn8_iv));
		btn9_Iv.setOnClickListener(new BtnListener(R.id.btn9_iv));
		slience_Iv.setOnClickListener(new BtnListener(R.id.slience_iv));
		msg_Iv.setOnClickListener(new BtnListener(R.id.msg_iv));

		tv_power_Iv.setOnClickListener(new BtnListener(R.id.tv_power_iv));
		v_add_Iv.setOnClickListener(new BtnListener(R.id.v_add_iv));
		v_sub_Iv.setOnClickListener(new BtnListener(R.id.v_sub_iv));
		tvav_Iv.setOnClickListener(new BtnListener(R.id.tvav_iv));

		left_Iv.setOnClickListener(new BtnListener(R.id.left_iv));
		right_Iv.setOnClickListener(new BtnListener(R.id.right_iv));
		up_Iv.setOnClickListener(new BtnListener(R.id.up_iv));
		down_Iv.setOnClickListener(new BtnListener(R.id.down_iv));
		enter_Iv.setOnClickListener(new BtnListener(R.id.enter_iv));

		btn_Name.put(R.id.power_iv, names[0]);
		btn_Name.put(R.id.btn0_iv, names[1]);
		btn_Name.put(R.id.btn1_iv, names[2]);
		btn_Name.put(R.id.btn2_iv, names[3]);
		btn_Name.put(R.id.btn3_iv, names[4]);
		btn_Name.put(R.id.btn4_iv, names[5]);
		btn_Name.put(R.id.btn5_iv, names[6]);
		btn_Name.put(R.id.btn6_iv, names[7]);
		btn_Name.put(R.id.btn7_iv, names[8]);
		btn_Name.put(R.id.btn8_iv, names[9]);
		btn_Name.put(R.id.btn9_iv, names[10]);
		btn_Name.put(R.id.slience_iv, names[11]);
		btn_Name.put(R.id.msg_iv, names[12]);

		btn_Name.put(R.id.tv_power_iv, names[13]);
		btn_Name.put(R.id.v_add_iv, names[14]);
		btn_Name.put(R.id.v_sub_iv, names[15]);
		btn_Name.put(R.id.tvav_iv, names[16]);

		btn_Name.put(R.id.left_iv, names[17]);
		btn_Name.put(R.id.right_iv, names[18]);
		btn_Name.put(R.id.up_iv, names[19]);
		btn_Name.put(R.id.down_iv, names[20]);
		btn_Name.put(R.id.enter_btn, names[21]);
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
				Toast.makeText(TVActivity.this, "正在获取信息", Toast.LENGTH_LONG)
						.show();
				return;
			}
			int idx = checkIsExist(btn_Name.get(id));
			if (idx == -1) {
				Toast.makeText(TVActivity.this, "请添加此按钮的指令", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.putExtra("cmdType", CmdDevType_E.CMD_DEV_APPL.getVal());
				intent.putExtra("type", 0);
				intent.putExtra("btn_Name", btn_Name.get(id));
				intent.setClass(TVActivity.this, AddCmdActivity.class);
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
					Toast.makeText(TVActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(TVActivity.this);
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
