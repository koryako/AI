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
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.entity.Ctrl_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgCmdQryByDevReq_S;
import define.oper.body.req.MsgQryReq_S;
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
	TextView cmd_title_tv;
	public Cmd_S cmd_S = null;
	public Appl_S appl_S = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		ctrlMap = new HashMap<Short, Ctrl_S>();
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			appl_S = new Appl_S();
			appl_S.setAppl_S(bundle.getByteArray("device"));
		}

		cmd_title_tv = (TextView) findViewById(R.id.userlist_title);
		cmd_title_tv.setText(R.string.cmd_list);
		cmd_S = new Cmd_S();

		cmdListView = (ListView) findViewById(R.id.userlist);
		cmdList = new ArrayList<Cmd_S>();
		cmdListAdapter = new CmdListAdapter(CmdListActivity.this, cmdList,
				ctrlMap, appl_S);
		cmdListView.setAdapter(cmdListAdapter);
		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent(CmdListActivity.this,
						AddCmdActivity.class);
				newIntent.putExtra("device", appl_S.getAppl_S());
				// 开始一个新的 Activity等候返回结果
				startActivityForResult(newIntent, 0);
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
		IntentFilter filter = new IntentFilter();
		filter.addAction("2_4");
		filter.addAction("5_2");
		filter.addAction("5_4");
		filter.addAction("5_5");
		filter.addAction("IOException");
		registerReceiver(cmdBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
			WriteUtil.write(
					MsgId_E.MSGID_CMD.getVal(),
					1,
					MsgType_E.MSGTYPE_REQ.getVal(),
					MsgOper_E.MSGOPER_MAX.getVal(),
					MsgCmdQryByDevReq_S.getSize(),
					new MsgCmdQryByDevReq_S(appl_S.getUcType(), appl_S
							.getUsIdx()).getMsgCmdQryByDevReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(CmdListActivity.this, "获取列表失败", Toast.LENGTH_LONG)
					.show();
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
					Cmd_S cmd_S = new Cmd_S();
					cmd_S.setCmd_S(c_b);
					if (cmd_S.getUsDevIdx() == appl_S.getUsIdx()) {
						this.cmdList.add(cmd_S);
						this.cmdListView.setAdapter(cmdListAdapter);
					}
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					byte[] c_b = extras.getByteArray("cmd");
					Cmd_S cmd_S = new Cmd_S();
					cmd_S.setCmd_S(c_b);
					cmdList.set(mod_Idx, cmd_S);
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

}
