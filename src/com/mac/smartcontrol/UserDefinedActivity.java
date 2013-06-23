package com.mac.smartcontrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mac.smartcontrol.broadcast.UserDefinedBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.oper.MsgOperCmd_E;
import define.oper.body.req.MsgCmdQryByDevReq_S;
import define.type.CmdDevType_E;
import define.type.CmdType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class UserDefinedActivity extends Activity {
	public List<Cmd_S> cmd_List;
	Appl_S appl_S = new Appl_S();
	ImageView back_Iv;
	UserDefinedBroadcastReceiver userDefinedBroadcastReceiver;
	LinearLayout btn_Container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_user_defined);
		cmd_List = new ArrayList<Cmd_S>();
		btn_Container = (LinearLayout) findViewById(R.id.btn_container);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			byte[] b = bundle.getByteArray("device");
			appl_S.setAppl_S(b);
		}

		back_Iv = (ImageView) findViewById(R.id.back_iv);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		userDefinedBroadcastReceiver = new UserDefinedBroadcastReceiver(
				UserDefinedActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal());

		filter.addAction("IOException");
		registerReceiver(userDefinedBroadcastReceiver, filter);
		// try {
		WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1, MsgType_E.MSGTYPE_REQ
				.getVal(), MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
				MsgCmdQryByDevReq_S.getSize(), new MsgCmdQryByDevReq_S(
						CmdDevType_E.CMD_DEV_APPL.getVal(), appl_S.getUsIdx(),
						CmdType_E.CMD_TYPE_CUSTOM.getVal())
						.getMsgCmdQryByDevReq_S(), this);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Toast.makeText(UserDefinedActivity.this, "请确认网络是否开启,连接失败",
		// Toast.LENGTH_LONG).show();
		// DisconnectionUtil.restart(UserDefinedActivity.this);
		// }
	}

	public void init_Btn() {
		int row = 0;
		int res = cmd_List.size() / 3;
		int remainder = cmd_List.size() % 3;
		if (cmd_List.size() != 0) {
			if (remainder != 0) {
				row = cmd_List.size() / 3 + 1;
			} else {
				row = cmd_List.size() / 3;
			}
		}
		btn_Container.removeAllViews();
		for (int i = 0; i < row; i++) {
			LinearLayout layout = new LinearLayout(UserDefinedActivity.this);
			for (int j = 0; j < 3; j++) {
				if ((i * 3 + j) >= cmd_List.size()) {
					break;
				}
				Button b1 = new Button(UserDefinedActivity.this);
				b1.setBackgroundResource(R.drawable.control_user_defined_btn_selector);
				final Cmd_S c = cmd_List.get(i * 3 + j);
				b1.setText(c.getSzName());
				b1.setOnClickListener(new OnClickListener() {
					;
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// try {
						WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
								MsgType_E.MSGTYPE_REQ.getVal(),
								MsgOperCmd_E.MSGOPER_CMD_EXC.getVal(),
								Cmd_S.getSize(), c.getCmd_S(),
								UserDefinedActivity.this);
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// Toast.makeText(UserDefinedActivity.this,
						// "请确认网络是否开启,连接失败", Toast.LENGTH_LONG).show();
						// DisconnectionUtil.restart(UserDefinedActivity.this);
						// }
					}
				});
				LayoutParams params = null;
				if (res == i) {
					if (remainder == 0) {
						params = new LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT, 1);
					} else {
						params = new LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(10, 0, 0, 0);
					}
				} else {
					params = new LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 0, 0, 0);
				}

				b1.setLayoutParams(params);

				layout.addView(b1);

				LayoutParams l_params = new LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1);
				l_params.setMargins(0, 10, 0, 0);

				layout.setLayoutParams(l_params);
			}
			btn_Container.addView(layout);

		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		unregisterReceiver(userDefinedBroadcastReceiver);
	}
}
