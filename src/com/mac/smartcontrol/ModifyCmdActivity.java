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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.ModifyCmdBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cmd_S;
import define.entity.Ctrl_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ModifyCmdActivity extends Activity {
	public Cmd_S cmd_S;
	ModifyCmdBroadcastReceiver modifyCmdBroadcastReceiver;

	public List<String> ctrlNameList = new ArrayList<String>();
	public List<String> codeNameList = new ArrayList<String>();
	public List<String> paraNameList = new ArrayList<String>();
	public Spinner ctrl_sp;
	public ArrayAdapter<String> ctrl_adapter;
	public ArrayAdapter<String> code_adapter;
	public ArrayAdapter<String> para_adapter;

	public List<Ctrl_S> ctrlList = null;
	private String[] para = { "打开", "关闭" };
	private String[] code = { "电源", "照明", "红外" };
	private Appl_S appl_S;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_cmd);

		Bundle bundle = getIntent().getExtras();
		appl_S = new Appl_S();
		cmd_S = new Cmd_S();
		if (bundle != null) {
			appl_S.setAppl_S(bundle.getByteArray("device"));
			cmd_S.setCmd_S(bundle.getByteArray("cmd"));
		}
		ctrlList = new ArrayList<Ctrl_S>();

		final EditText cmd_name_Et = (EditText) findViewById(R.id.cmdname_et);
		cmd_name_Et.setText(cmd_S.getSzName());
		final EditText voice_name_Et = (EditText) findViewById(R.id.voicename_et);
		voice_name_Et.setText(cmd_S.getSzVoice());
		final LinearLayout para_Layout = (LinearLayout) findViewById(R.id.param_ll);
		ctrl_sp = (Spinner) findViewById(R.id.ctrl_sp);
		final Spinner code_sp = (Spinner) findViewById(R.id.cmdcode_sp);

		final Spinner para_sp = (Spinner) findViewById(R.id.param_sp);
		code_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 2) {
					para_Layout.setVisibility(View.INVISIBLE);
				} else {
					para_Layout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		modifyCmdBroadcastReceiver = new ModifyCmdBroadcastReceiver(
				ModifyCmdActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("5_3");
		filter.addAction("2_4");
		filter.addAction("IOException");
		registerReceiver(modifyCmdBroadcastReceiver, filter);

		try {
			WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ModifyCmdActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();
		}

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// R.layout.simple_spinner_item,types);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// sense_type_sp.setAdapter(adapter);

		ctrl_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, ctrlNameList);
		ctrl_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		ctrl_sp.setAdapter(ctrl_adapter);

		code_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, code);
		code_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		code_sp.setAdapter(code_adapter);

		para_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, para);
		para_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		para_sp.setAdapter(para_adapter);

		code_sp.setSelection(cmd_S.getUcCode() - 1);
		para_sp.setSelection(cmd_S.getUiPara() - 1);

		ImageView modify_Iv = (ImageView) findViewById(R.id.modify);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		modify_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cmdName = cmd_name_Et.getText().toString().trim();
				if (cmdName == null || cmdName.equals("")) {
					Toast.makeText(ModifyCmdActivity.this, "名称不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (cmdName.getBytes().length > 32) {
					Toast.makeText(ModifyCmdActivity.this, "名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				String voiceName = voice_name_Et.getText().toString().trim();
				if (voiceName == null || voiceName.equals("")) {
					Toast.makeText(ModifyCmdActivity.this, "语音文本不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (voiceName.getBytes().length > 64) {
					Toast.makeText(ModifyCmdActivity.this, "语音文本名称太长",
							Toast.LENGTH_LONG).show();
					return;
				}
				cmd_S.setSzName(cmdName);
				cmd_S.setSzVoice(voiceName);
				cmd_S.setUsCtrlIdx(ctrlList.get(
						ctrl_sp.getSelectedItemPosition()).getUsIdx());
				cmd_S.setUcCode((byte) (code_sp.getSelectedItemPosition() + 1));
				cmd_S.setUiPara(para_sp.getSelectedItemPosition() + 1);
				cmd_S.setUsDevIdx(appl_S.getUsIdx());
				cmd_S.setUcDevType(appl_S.getUcType());
				try {
					WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MOD.getVal(), Cmd_S.getSize(),
							cmd_S.getCmd_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyCmdActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
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
		unregisterReceiver(modifyCmdBroadcastReceiver);
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
