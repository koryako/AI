package com.mac.smartcontrol;

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

public class HDPlayActivity extends Activity {
	ImageView back_Iv;
	ImageView user_defined_Iv;
	ImageView power_iv;

	ImageView open_close_iv;
	ImageView submit_iv;
	ImageView previous_iv;
	ImageView next_iv;
	ImageView backwards_iv;
	ImageView forward_iv;
	ImageView pause_iv;
	ImageView play_iv;
	ImageView stop_iv;

	public ImageView switch_Icon;
	String[] names = new String[] { "开/关", "OPEN/CLOSE", "确认", "上一个", "下一个",
			"快退", "快进", "暂停", "播放", "停止" };
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
		setContentView(R.layout.control_hdplay);
		switch_Icon = (ImageView) findViewById(R.id.switch_state_iv);
		cmd_List = new ArrayList<Cmd_S>();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("device");
			appl_S.setAppl_S(b);
		}
		controlBroadcastReceiver = new ControlBroadcastReceiver(this,
				ApplType_E.APPL_TYPE_HDPLAY.getVal());
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal());
		filter.addAction(MsgId_E.MSGID_CTRL.getVal() + "_"
				+ MsgOperCtrl_E.MSGOPER_CTRL_STATUS.getVal());
		registerReceiver(controlBroadcastReceiver, filter);
		init_Btn_Name();
		// try {
		WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1, MsgType_E.MSGTYPE_REQ
				.getVal(), MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
				MsgCmdQryByDevReq_S.getSize(), new MsgCmdQryByDevReq_S(
						CmdDevType_E.CMD_DEV_APPL.getVal(), appl_S.getUsIdx(),
						CmdType_E.CMD_TYPE_PREDEF.getVal())
						.getMsgCmdQryByDevReq_S(), this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(HDPlayActivity.this, "请确认网络是否开启,连接失败",
		// Toast.LENGTH_LONG).show();
		// DisconnectionUtil.restart(HDPlayActivity.this);
		// }

		back_Iv = (ImageView) findViewById(R.id.back_iv);
		user_defined_Iv = (ImageView) findViewById(R.id.user_defined_iv);
		user_defined_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.setClass(HDPlayActivity.this, UserDefinedActivity.class);
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
		power_iv = (ImageView) findViewById(R.id.power_iv);
		open_close_iv = (ImageView) findViewById(R.id.open_close_iv);
		submit_iv = (ImageView) findViewById(R.id.submit_iv);
		previous_iv = (ImageView) findViewById(R.id.previous_iv);
		next_iv = (ImageView) findViewById(R.id.next_iv);
		backwards_iv = (ImageView) findViewById(R.id.backwards_iv);
		forward_iv = (ImageView) findViewById(R.id.forward_iv);
		pause_iv = (ImageView) findViewById(R.id.pause_iv);
		play_iv = (ImageView) findViewById(R.id.play_iv);
		stop_iv = (ImageView) findViewById(R.id.stop_iv);

		power_iv.setOnClickListener(new BtnListener(R.id.power_iv));
		open_close_iv.setOnClickListener(new BtnListener(R.id.open_close_iv));
		submit_iv.setOnClickListener(new BtnListener(R.id.submit_iv));
		previous_iv.setOnClickListener(new BtnListener(R.id.previous_iv));
		next_iv.setOnClickListener(new BtnListener(R.id.next_iv));
		backwards_iv.setOnClickListener(new BtnListener(R.id.backwards_iv));
		forward_iv.setOnClickListener(new BtnListener(R.id.forward_iv));
		pause_iv.setOnClickListener(new BtnListener(R.id.pause_iv));
		play_iv.setOnClickListener(new BtnListener(R.id.play_iv));
		stop_iv.setOnClickListener(new BtnListener(R.id.stop_iv));

		btn_Name.put(R.id.power_iv, names[0]);
		btn_Name.put(R.id.open_close_iv, names[1]);
		btn_Name.put(R.id.submit_iv, names[2]);
		btn_Name.put(R.id.previous_iv, names[3]);
		btn_Name.put(R.id.next_iv, names[4]);
		btn_Name.put(R.id.backwards_iv, names[5]);
		btn_Name.put(R.id.forward_iv, names[6]);
		btn_Name.put(R.id.pause_iv, names[7]);
		btn_Name.put(R.id.play_iv, names[8]);
		btn_Name.put(R.id.stop_iv, names[9]);
	}

	private int checkIsExist(String name) {
		for (int i = 0; i < cmd_List.size(); i++) {
			if (cmd_List.get(i).getSzName().equals(name)
					&& cmd_List.get(i).getUcType() == CmdType_E.CMD_TYPE_PREDEF
							.getVal()) {
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
				Toast.makeText(HDPlayActivity.this, "正在获取信息", Toast.LENGTH_LONG)
						.show();
				return;
			}
			int idx = checkIsExist(btn_Name.get(id));
			if (idx == -1) {
				Toast.makeText(HDPlayActivity.this, "请添加此按钮的指令",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("device", appl_S.getAppl_S());
				intent.putExtra("type", 1);
				intent.putExtra("cmdType", CmdDevType_E.CMD_DEV_APPL.getVal());
				intent.putExtra("btn_Name", btn_Name.get(id));
				intent.setClass(HDPlayActivity.this, AddCmdActivity.class);
				startActivityForResult(intent, 0);
			} else {
				Cmd_S cmd_S = cmd_List.get(idx);
				// try {
				WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOperCmd_E.MSGOPER_CMD_EXC.getVal(), Cmd_S.getSize(),
						cmd_S.getCmd_S(), HDPlayActivity.this);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Toast.makeText(HDPlayActivity.this, "请确认网络是否开启,连接失败",
				// Toast.LENGTH_LONG).show();
				// DisconnectionUtil.restart(HDPlayActivity.this);
				// }
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
