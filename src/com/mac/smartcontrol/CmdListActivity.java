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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.adapter.CmdListAdapter;
import com.mac.smartcontrol.broadcast.CmdBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cama_S;
import define.entity.Cmd_S;
import define.entity.Ctrl_S;
import define.entity.Sens_S;
import define.oper.MsgOperCmd_E;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgCmdQryByDevReq_S;
import define.oper.body.req.MsgQryReq_S;
import define.type.CmdDevType_E;
import define.type.CmdType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class CmdListActivity extends Activity {

	public List<Cmd_S> cmdList = null;
	public Map<Short, Ctrl_S> ctrlMap = null;
	public CmdListAdapter cmdListAdapter = null;
	public ListView cmdListView = null;
	public int del_Idx = -1;
	public int mod_Idx = -1;
	CmdBroadcastReceiver cmdBroadcastReceiver;
	IntentFilter filter;
	TextView cmd_title_tv;
	public Cmd_S cmd_S = null;
	public Appl_S appl_S = null;
	public Cama_S cama_S = null;
	public Sens_S sens_S = null;
	byte cmdType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		ctrlMap = new HashMap<Short, Ctrl_S>();
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			cmdType = bundle.getByte("cmdType");
			if (cmdType == CmdDevType_E.CMD_DEV_APPL.getVal()) {
				appl_S = new Appl_S();
				appl_S.setAppl_S(bundle.getByteArray("device"));
			} else if (cmdType == CmdDevType_E.CMD_DEV_SENS.getVal()) {
				sens_S = new Sens_S();
				sens_S.setSens_S(bundle.getByteArray("sense"));
			} else if (cmdType == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
				cama_S = new Cama_S();
				cama_S.setCama_S(bundle.getByteArray("camare"));
			}
		}

		cmd_title_tv = (TextView) findViewById(R.id.userlist_title);
		cmd_title_tv.setText(R.string.cmd_list);
		cmd_S = new Cmd_S();

		cmdListView = (ListView) findViewById(R.id.userlist);
		cmdList = new ArrayList<Cmd_S>();
		cmdListAdapter = new CmdListAdapter(CmdListActivity.this, cmdList,
				ctrlMap, cmdType);
		cmdListView.setAdapter(cmdListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(CmdListActivity.this,
						AddCmdActivity.class);
				if (cmdType == CmdDevType_E.CMD_DEV_APPL.getVal()) {
					newIntent.putExtra("device", appl_S.getAppl_S());
				} else if (cmdType == CmdDevType_E.CMD_DEV_SENS.getVal()) {
					newIntent.putExtra("sense", sens_S.getSens_S());
				} else if (cmdType == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
					newIntent.putExtra("camare", cama_S.getCama_S());
				}
				newIntent.putExtra("cmdType", cmdType);
				// 开始一个新的 Activity等候返回结果
				startActivityForResult(newIntent, 0);
				unregisterReceiver(cmdBroadcastReceiver);
			}
		});
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		back_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cmdBroadcastReceiver = new CmdBroadcastReceiver(CmdListActivity.this);
		filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_CTRL.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_DEL.getVal());

		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());

		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal());

		filter.addAction("IOException");
		registerReceiver(cmdBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
			if (cmdType == CmdDevType_E.CMD_DEV_APPL.getVal()) {
				WriteUtil.write(
						MsgId_E.MSGID_CMD.getVal(),
						1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
						MsgCmdQryByDevReq_S.getSize(),
						new MsgCmdQryByDevReq_S(CmdDevType_E.CMD_DEV_APPL
								.getVal(), appl_S.getUsIdx(),
								CmdType_E.CMD_TYPE_NULL.getVal())
								.getMsgCmdQryByDevReq_S());
			} else if (cmdType == CmdDevType_E.CMD_DEV_SENS.getVal()) {
				WriteUtil.write(
						MsgId_E.MSGID_CMD.getVal(),
						1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
						MsgCmdQryByDevReq_S.getSize(),
						new MsgCmdQryByDevReq_S(CmdDevType_E.CMD_DEV_SENS
								.getVal(), sens_S.getUsIdx(),
								CmdType_E.CMD_TYPE_NULL.getVal())
								.getMsgCmdQryByDevReq_S());
			} else if (cmdType == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
				WriteUtil.write(
						MsgId_E.MSGID_CMD.getVal(),
						1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOperCmd_E.MSGOPER_CMD_QRY_BYDEV.getVal(),
						MsgCmdQryByDevReq_S.getSize(),
						new MsgCmdQryByDevReq_S(CmdDevType_E.CMD_DEV_CAMA
								.getVal(), cama_S.getUsIdx(),
								CmdType_E.CMD_TYPE_NULL.getVal())
								.getMsgCmdQryByDevReq_S());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(CmdListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
					.show();
			DisconnectionUtil.restart(CmdListActivity.this);

		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(cmdBroadcastReceiver);
		super.finish();
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
					Cmd_S c = new Cmd_S();
					c.setCmd_S(c_b);
					if (cmdType == CmdDevType_E.CMD_DEV_APPL.getVal()) {
						if (c.getUsDevIdx() == appl_S.getUsIdx()) {
							this.cmdList.add(c);
							this.cmdListView.setAdapter(cmdListAdapter);
						}
					} else if (cmdType == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
						if (c.getUsDevIdx() == cama_S.getUsIdx()) {
							this.cmdList.add(c);
							this.cmdListView.setAdapter(cmdListAdapter);
						}
					} else if (cmdType == CmdDevType_E.CMD_DEV_SENS.getVal()) {
						if (c.getUsDevIdx() == sens_S.getUsIdx()) {
							this.cmdList.add(c);
							this.cmdListView.setAdapter(cmdListAdapter);
						}
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] c_b = extras.getByteArray("cmd");
					Cmd_S c = new Cmd_S();
					c.setCmd_S(c_b);
					cmdList.set(mod_Idx, c);
					// this.cmdListAdapter.notifyDataSetChanged();
					this.cmdListView.setAdapter(cmdListAdapter);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return true;
	}

	public void unreg_receiver() {
		unregisterReceiver(cmdBroadcastReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(cmdBroadcastReceiver, filter);
		super.onResume();
	}

}
