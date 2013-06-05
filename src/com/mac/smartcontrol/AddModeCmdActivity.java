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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.AddModeCmdBroadcastReceiver;
import com.mac.smartcontrol.util.DisconnectionUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Appl_S;
import define.entity.Cama_S;
import define.entity.Cmd_S;
import define.entity.ModeCmd_S;
import define.entity.Mode_S;
import define.entity.Rgn_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.CmdDevType_E;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class AddModeCmdActivity extends Activity {
	public ModeCmd_S modeCmd_S;
	AddModeCmdBroadcastReceiver addCmdBroadcastReceiver;

	public List<Rgn_S> areaList = null;
	public List<Appl_S> deviceList = null;
	public List<Cmd_S> cmdList = null;

	public List<Sens_S> senseList = null;
	public List<Cama_S> cameraList = null;

	public List<String> areaListStr = null;
	public List<String> deviceListStr = null;
	public List<String> cmdListStr = null;

	public List<Short> deviceIDList;
	public List<Short> cmdIDList;

	public ArrayAdapter<String> device_type_adapter;
	public ArrayAdapter<String> area_adapter;
	public ArrayAdapter<String> device_adapter;
	public ArrayAdapter<String> cmd_adapter;

	private String[] device_Type = { "家用电器", "感应器", "摄像头" };
	Mode_S mode_S = null;
	short typeId = 1;
	short areaId = -1;
	short deviceId = -1;
	short cmdId = -1;
	public Spinner area_sp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_mode_cmd);
		areaList = new ArrayList<Rgn_S>();
		cmdList = new ArrayList<Cmd_S>();
		deviceList = new ArrayList<Appl_S>();
		areaListStr = new ArrayList<String>();
		deviceListStr = new ArrayList<String>();
		cmdListStr = new ArrayList<String>();
		cmdIDList = new ArrayList<Short>();
		deviceIDList = new ArrayList<Short>();
		cameraList = new ArrayList<Cama_S>();
		senseList = new ArrayList<Sens_S>();
		Bundle bundle = getIntent().getExtras();
		mode_S = new Mode_S();
		if (bundle != null) {
			mode_S.setMode_S(bundle.getByteArray("mode"));
		}
		modeCmd_S = new ModeCmd_S();
		Spinner device_type_sp = (Spinner) findViewById(R.id.device_type_sp);
		area_sp = (Spinner) findViewById(R.id.area_sp);
		final Spinner device_sp = (Spinner) findViewById(R.id.device_sp);
		final Spinner cmd_sp = (Spinner) findViewById(R.id.cmd_sp);

		device_type_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, device_Type);
		device_type_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		device_type_sp.setAdapter(device_type_adapter);

		area_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, areaListStr);
		area_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		area_sp.setAdapter(area_adapter);
		device_type_sp.setSelection(0, true);
		area_sp.setSelection(0, true);
		cmd_sp.setSelection(0, true);
		device_sp.setSelection(0, true);
		device_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, deviceListStr);
		device_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		device_sp.setAdapter(device_adapter);

		cmd_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, cmdListStr);
		cmd_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		cmd_sp.setAdapter(cmd_adapter);

		// ImageView study_Iv = (ImageView) findViewById(R.id.study_iv);
		// study_Iv.setVisibility(View.INVISIBLE);

		addCmdBroadcastReceiver = new AddModeCmdBroadcastReceiver(
				AddModeCmdActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_APPL.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_SENS.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_CAMA.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		filter.addAction(MsgId_E.MSGID_MODECMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_ADD.getVal());
		filter.addAction("IOException");
		registerReceiver(addCmdBroadcastReceiver, filter);

		device_type_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				typeId = (short) (arg2 + 1);
				areaListStr.clear();
				deviceListStr.clear();
				deviceIDList.clear();
				cmdIDList.clear();
				cmdListStr.clear();
				area_adapter.notifyDataSetChanged();
				device_adapter.notifyDataSetChanged();
				cmd_adapter.notifyDataSetChanged();
				areaId = -1;
				deviceId = -1;
				cmdId = -1;
				// if (areaId != -1) {
				// if (typeId == CmdDevType_E.CMD_DEV_APPL.getVal()) {
				// for (int i = 0; i < deviceList.size(); i++) {
				// Appl_S appl_S = deviceList.get(i);
				// if (areaId == appl_S.getUsRgnIdx()) {
				// deviceIDList.add(appl_S.getUsIdx());
				// deviceListStr.add(appl_S.getSzName());
				// }
				// }
				// } else if (typeId == CmdDevType_E.CMD_DEV_SENS.getVal()) {
				// for (int i = 0; i < senseList.size(); i++) {
				// Sens_S sens_S = senseList.get(i);
				// if (areaId == sens_S.getUsRgnIdx()) {
				// deviceIDList.add(sens_S.getUsIdx());
				// deviceListStr.add(sens_S.getSzName());
				// }
				// }
				// } else if (typeId == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
				// for (int i = 0; i < cameraList.size(); i++) {
				// Cama_S cama_S = cameraList.get(i);
				// if (areaId == cama_S.getUsRgnIdx()) {
				// deviceIDList.add(cama_S.getUsIdx());
				// deviceListStr.add(cama_S.getSzName());
				// }
				// }
				// }
				//
				// }

				for (int i = 0; i < areaList.size(); i++) {
					areaListStr.add(areaList.get(i).getSzName());
				}
				area_adapter.notifyDataSetChanged();
				device_adapter.notifyDataSetChanged();
				cmd_adapter.notifyDataSetChanged();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		area_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				areaId = areaList.get(arg2).getUsIdx();
				deviceListStr.clear();
				deviceIDList.clear();

				cmdIDList.clear();
				cmdListStr.clear();

				device_adapter.notifyDataSetChanged();
				cmd_adapter.notifyDataSetChanged();
				deviceId = -1;
				cmdId = -1;
				if (typeId == CmdDevType_E.CMD_DEV_APPL.getVal()) {
					for (int i = 0; i < deviceList.size(); i++) {
						Appl_S appl_S = deviceList.get(i);
						if (areaId == appl_S.getUsRgnIdx()) {
							deviceIDList.add(appl_S.getUsIdx());
							deviceListStr.add(appl_S.getSzName());
						}
					}
				} else if (typeId == CmdDevType_E.CMD_DEV_SENS.getVal()) {
					for (int i = 0; i < senseList.size(); i++) {
						Sens_S sens_S = senseList.get(i);
						if (areaId == sens_S.getUsRgnIdx()) {
							deviceIDList.add(sens_S.getUsIdx());
							deviceListStr.add(sens_S.getSzName());
						}
					}
				} else if (typeId == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
					for (int i = 0; i < cameraList.size(); i++) {
						Cama_S cama_S = cameraList.get(i);
						if (areaId == cama_S.getUsRgnIdx()) {
							deviceIDList.add(cama_S.getUsIdx());
							deviceListStr.add(cama_S.getSzName());
						}
					}
				}
				if (deviceIDList.size() > 0) {
					deviceId = deviceIDList.get(0);
				}
				device_adapter.notifyDataSetChanged();
				cmd_adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		device_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				deviceId = deviceIDList.get(arg2);
				cmdListStr.clear();
				cmdIDList.clear();
				cmd_adapter.notifyDataSetChanged();
				cmdId = -1;
				for (int i = 0; i < cmdList.size(); i++) {
					Cmd_S cmd_S = cmdList.get(i);
					if (deviceId == cmd_S.getUsDevIdx()) {
						cmdIDList.add(cmd_S.getUsIdx());
						cmdListStr.add(cmd_S.getSzName());
					}
				}
				if (cmdIDList.size() > 0) {
					cmdId = cmdIDList.get(0);
				}
				cmd_adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		cmd_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				cmdId = cmdIDList.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		try {
			WriteUtil.write(MsgId_E.MSGID_RGN.getVal(), 0,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());
			WriteUtil.write(MsgId_E.MSGID_APPL.getVal(), 1,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());

			WriteUtil.write(MsgId_E.MSGID_SENS.getVal(), 2,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());

			WriteUtil.write(MsgId_E.MSGID_CAMA.getVal(), 3,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());

			WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 4,
					MsgType_E.MSGTYPE_REQ.getVal(), MsgOper_E.MSGOPER_QRY
							.getVal(), (short) 2, new MsgQryReq_S((short) 0)
							.getMsgQryReq_S());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(AddModeCmdActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(AddModeCmdActivity.this);
		}

		ImageView add_Iv = (ImageView) findViewById(R.id.add);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		add_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (typeId == -1) {
					Toast.makeText(AddModeCmdActivity.this, "请选择设备类型",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (areaId == -1) {
					Toast.makeText(AddModeCmdActivity.this, "请选择区域",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (deviceId == -1) {
					Toast.makeText(AddModeCmdActivity.this, "请选择设备",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (cmdId == -1) {
					Toast.makeText(AddModeCmdActivity.this, "请选择设备指令",
							Toast.LENGTH_LONG).show();
					return;
				}
				modeCmd_S.setUsCmdIdx(cmdId);
				modeCmd_S.setUsModeIdx(mode_S.getUsIdx());
				try {
					WriteUtil.write(MsgId_E.MSGID_MODECMD.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_ADD.getVal(),
							ModeCmd_S.getSize(), modeCmd_S.getModeCmd_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(AddModeCmdActivity.this, "请确认网络是否开启,连接失败",
							Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(AddModeCmdActivity.this);
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
		unregisterReceiver(addCmdBroadcastReceiver);
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
