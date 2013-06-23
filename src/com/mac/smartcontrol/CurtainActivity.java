package com.mac.smartcontrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.ControlBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.oper.MsgOperCmd_E;
import define.oper.MsgOperCtrl_E;
import define.oper.body.req.MsgCmdQryByDevReq_S;
import define.type.ApplType_E;
import define.type.CmdDevType_E;
import define.type.CmdType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class CurtainActivity extends Activity {
	ImageView switch_Iv;
	ImageView pause_Iv;
	ImageView user_defined_Iv;
	ImageView back_Iv;
	public List<Cmd_S> cmd_List;
	ControlBroadcastReceiver controlBroadcastReceiver;
	Appl_S appl_S;
	public boolean b = false;
	String[] names = new String[] { "开", "关", "暂停" };

	IntentFilter filter;
	public ImageView switch_Icon;
	public boolean state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_curtain);
		switch_Icon = (ImageView) findViewById(R.id.switch_state_iv);
		cmd_List = new ArrayList<Cmd_S>();
		appl_S = new Appl_S();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("device");
			appl_S.setAppl_S(b);
		}
		// try {
		WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1, MsgType_E.MSGTYPE_REQ
				.getVal(), MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
				MsgCmdQryByDevReq_S.getSize(), new MsgCmdQryByDevReq_S(
						CmdDevType_E.CMD_DEV_APPL.getVal(), appl_S.getUsIdx(),
						CmdType_E.CMD_TYPE_PREDEF.getVal())
						.getMsgCmdQryByDevReq_S(), this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(CurtainActivity.this, "请确认网络是否开启,连接失败",
		// Toast.LENGTH_LONG).show();
		// DisconnectionUtil.restart(CurtainActivity.this);
		// }

		back_Iv = (ImageView) findViewById(R.id.back_iv);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		user_defined_Iv = (ImageView) findViewById(R.id.user_defined_iv);
		user_defined_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.setClass(CurtainActivity.this, UserDefinedActivity.class);
				startActivity(intent);
				unregisterReceiver(controlBroadcastReceiver);
			}
		});

		pause_Iv = (ImageView) findViewById(R.id.pause_iv);
		pause_Iv.setOnClickListener(new BtnListener());
		switch_Iv = (ImageView) findViewById(R.id.switch_iv);
		switch_Iv.setOnClickListener(new BtnListener());

		controlBroadcastReceiver = new ControlBroadcastReceiver(
				CurtainActivity.this, ApplType_E.APPL_TYPE_CURTAIN.getVal());
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal());
		filter.addAction(MsgId_E.MSGID_CTRL.getVal() + "_"
				+ MsgOperCtrl_E.MSGOPER_CTRL_STATUS.getVal());
		filter.addAction("IOException");
		registerReceiver(controlBroadcastReceiver, filter);

	}

	private int checkIsExist(String name) {
		for (int i = 0; i < cmd_List.size(); i++) {
			if (cmd_List.get(i).getSzName().equals(name)) {
				return i;
			}
		}
		return -1;
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

	class BtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!b) {
				Toast.makeText(CurtainActivity.this, "正在获取信息",
						Toast.LENGTH_LONG).show();
				return;
			}
			int idx = -1;
			if (v.getId() == switch_Iv.getId()) {
				if (state) {
					idx = checkIsExist(names[1]);
				} else {
					idx = checkIsExist(names[0]);
				}
			} else {
				idx = checkIsExist(names[2]);
			}
			if (idx == -1) {
				Toast.makeText(CurtainActivity.this, "请添加此按钮的指令",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.putExtra("cmdType", CmdDevType_E.CMD_DEV_APPL.getVal());
				intent.putExtra("type", 1);
				if (v.getId() == switch_Iv.getId()) {
					if (state) {
						intent.putExtra("btn_Name", names[1]);
						intent.putExtra("uiparam", 2);
					} else {
						intent.putExtra("btn_Name", names[0]);
						intent.putExtra("uiparam", 1);
					}

				} else {
					intent.putExtra("btn_Name", names[2]);
					intent.putExtra("uiparam", 3);
				}
				intent.setClass(CurtainActivity.this, AddCmdActivity.class);
				startActivityForResult(intent, 0);
			} else {
				Cmd_S cmd_S = cmd_List.get(idx);
				// try {
				WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOperCmd_E.MSGOPER_CMD_EXC.getVal(), Cmd_S.getSize(),
						cmd_S.getCmd_S(), CurtainActivity.this);
				if (v.getId() == switch_Iv.getId()) {
					if (state) {
						switch_Iv.setImageResource(R.drawable.switch_btn_close);
						switch_Icon
								.setImageResource(R.drawable.switch_state_close);
					} else {
						switch_Iv.setImageResource(R.drawable.switch_btn_open);
						switch_Icon
								.setImageResource(R.drawable.switch_state_open);
					}
					state = !state;
				}
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Toast.makeText(CurtainActivity.this, "请确认网络是否开启,连接失败",
				// Toast.LENGTH_LONG).show();
				// DisconnectionUtil.restart(CurtainActivity.this);
				// }
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		unregisterReceiver(controlBroadcastReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(controlBroadcastReceiver, filter);
		super.onResume();
	}
}
