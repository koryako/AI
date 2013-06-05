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

import com.mac.smartcontrol.broadcast.ModifyModeCmdBroadcastReceiver;
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

public class ModifyModeCmdActivity extends Activity {
	public ModeCmd_S modeCmd_S;
	ModifyModeCmdBroadcastReceiver modifyModeCmdBroadcastReceiver;

	public List<Rgn_S> areaList = null;
	public List<Appl_S> deviceList = null;
	public List<Cmd_S> cmdList = null;

	public List<String> areaListStr = null;
	public List<String> deviceListStr = null;
	public List<String> cmdListStr = null;

	public List<Sens_S> senseList = null;
	public List<Cama_S> cameraList = null;

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
	Spinner device_sp;
	Spinner cmd_sp;
	Spinner device_type_sp;
	Cmd_S cmd_S;
	Appl_S appl_S;
	Sens_S sens_S;
	Cama_S cama_S;
	Rgn_S rgn_S;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_mode_cmd);
		mode_S = new Mode_S();
		modeCmd_S = new ModeCmd_S();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			modeCmd_S.setModeCmd_S(bundle.getByteArray("modecmd"));
			mode_S.setMode_S(bundle.getByteArray("mode"));
		}
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

		device_type_sp = (Spinner) findViewById(R.id.device_type_sp);
		area_sp = (Spinner) findViewById(R.id.area_sp);
		device_sp = (Spinner) findViewById(R.id.device_sp);
		cmd_sp = (Spinner) findViewById(R.id.cmd_sp);

		device_type_adapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_item, device_Type);
		device_type_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		device_type_sp.setAdapter(device_type_adapter);

		// ImageView study_Iv = (ImageView) findViewById(R.id.study_iv);
		// study_Iv.setVisibility(View.INVISIBLE);

		modifyModeCmdBroadcastReceiver = new ModifyModeCmdBroadcastReceiver(
				ModifyModeCmdActivity.this);
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
				+ MsgOper_E.MSGOPER_MOD.getVal());
		filter.addAction("IOException");
		registerReceiver(modifyModeCmdBroadcastReceiver, filter);

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
				if (cmd_adapter != null) {
					cmd_adapter.notifyDataSetChanged();
				}
				if (device_type_adapter != null) {
					device_type_adapter.notifyDataSetChanged();
				}

				if (area_adapter != null) {
					area_adapter.notifyDataSetChanged();
				}
				areaId = -1;
				deviceId = -1;
				cmdId = -1;

				for (int i = 0; i < areaList.size(); i++) {
					areaListStr.add(areaList.get(i).getSzName());
				}

				area_adapter = new ArrayAdapter<String>(
						ModifyModeCmdActivity.this,
						R.layout.simple_spinner_item, areaListStr);
				area_adapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				area_sp.setAdapter(area_adapter);

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
						if (cmd_adapter != null) {
							cmd_adapter.notifyDataSetChanged();
						}
						if (device_type_adapter != null) {
							device_type_adapter.notifyDataSetChanged();
						}

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

						device_adapter = new ArrayAdapter<String>(
								ModifyModeCmdActivity.this,
								R.layout.simple_spinner_item, deviceListStr);
						device_adapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						device_sp.setAdapter(device_adapter);

						device_sp
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										if (deviceIDList.size() <= arg2)
											return;
										deviceId = deviceIDList.get(arg2);
										cmdListStr.clear();
										cmdIDList.clear();
										if (cmd_adapter != null) {
											cmd_adapter.notifyDataSetChanged();
										}
										cmdId = -1;

										for (int i = 0; i < cmdList.size(); i++) {
											Cmd_S cmd_S = cmdList.get(i);
											if (typeId == cmd_S.getUcDevType()) {
												if (deviceId == cmd_S
														.getUsDevIdx()) {
													cmdIDList.add(cmd_S
															.getUsIdx());
													cmdListStr.add(cmd_S
															.getSzName());
												}
											}
										}

										cmd_adapter = new ArrayAdapter<String>(
												ModifyModeCmdActivity.this,
												R.layout.simple_spinner_item,
												cmdListStr);
										cmd_adapter
												.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
										cmd_sp.setAdapter(cmd_adapter);

										cmd_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(
													AdapterView<?> arg0,
													View arg1, int arg2,
													long arg3) {
												// TODO Auto-generated method
												// stub
												if (cmdIDList.size() <= arg2)
													return;
												cmdId = cmdIDList.get(arg2);
											}

											@Override
											public void onNothingSelected(
													AdapterView<?> arg0) {
												// TODO Auto-generated method
												// stub

											}
										});

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO Auto-generated method stub

									}
								});
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
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
			Toast.makeText(ModifyModeCmdActivity.this, "请确认网络是否开启,连接失败",
					Toast.LENGTH_LONG).show();
			DisconnectionUtil.restart(ModifyModeCmdActivity.this);
		}

		ImageView modify_Iv = (ImageView) findViewById(R.id.modify);
		ImageView back_Iv = (ImageView) findViewById(R.id.back);
		modify_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (typeId == -1) {
					Toast.makeText(ModifyModeCmdActivity.this, "请选择设备类型",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (areaId == -1) {
					Toast.makeText(ModifyModeCmdActivity.this, "请选择区域",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (deviceId == -1) {
					Toast.makeText(ModifyModeCmdActivity.this, "请选择设备",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (cmdId == -1) {
					Toast.makeText(ModifyModeCmdActivity.this, "请选择设备指令",
							Toast.LENGTH_LONG).show();
					return;
				}
				modeCmd_S.setUsCmdIdx(cmdId);
				modeCmd_S.setUsModeIdx(mode_S.getUsIdx());
				try {
					WriteUtil.write(MsgId_E.MSGID_MODECMD.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MOD.getVal(),
							ModeCmd_S.getSize(), modeCmd_S.getModeCmd_S());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ModifyModeCmdActivity.this,
							"请确认网络是否开启,连接失败", Toast.LENGTH_LONG).show();
					DisconnectionUtil.restart(ModifyModeCmdActivity.this);
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

	public void init_Info() {
		for (int i = 0; i < cmdList.size(); i++) {
			Cmd_S c = cmdList.get(i);
			if (c.getUsIdx() == modeCmd_S.getUsCmdIdx()) {
				cmd_S = c;
				break;
			}
		}

		if (cmd_S != null) {
			typeId = cmd_S.getUcDevType();
			device_type_sp.setSelection(typeId - 1);
			int area_Idx = -1;
			if (typeId == CmdDevType_E.CMD_DEV_APPL.getVal()) {
				for (int i = 0; i < deviceList.size(); i++) {
					Appl_S a = deviceList.get(i);
					if (a.getUsIdx() == cmd_S.getUsDevIdx()) {
						appl_S = a;
						break;
					}
				}
				for (int i = 0; i < areaList.size(); i++) {
					Rgn_S r = areaList.get(i);
					if (r.getUsIdx() == appl_S.getUsRgnIdx()) {
						area_Idx = i;
						rgn_S = r;
						break;
					}
				}
			} else if (typeId == CmdDevType_E.CMD_DEV_SENS.getVal()) {
				for (int i = 0; i < deviceList.size(); i++) {
					Sens_S s = senseList.get(i);
					if (s.getUsIdx() == cmd_S.getUsDevIdx()) {
						sens_S = s;
						break;
					}
				}
				for (int i = 0; i < areaList.size(); i++) {
					Rgn_S r = areaList.get(i);
					if (r.getUsIdx() == sens_S.getUsRgnIdx()) {
						area_Idx = i;
						rgn_S = r;
						break;
					}
				}
			} else if (typeId == CmdDevType_E.CMD_DEV_CAMA.getVal()) {
				for (int i = 0; i < cameraList.size(); i++) {
					Cama_S c = cameraList.get(i);
					if (c.getUsIdx() == cmd_S.getUsDevIdx()) {
						cama_S = c;
						break;
					}
				}
				for (int i = 0; i < areaList.size(); i++) {
					Rgn_S r = areaList.get(i);
					if (r.getUsIdx() == cama_S.getUsRgnIdx()) {
						area_Idx = i;
						rgn_S = r;
						break;
					}
				}

			}
			area_sp.setSelection(area_Idx);

			for (int i = 0; i < deviceIDList.size(); i++) {
				if (deviceIDList.get(i) == cmd_S.getUsDevIdx()) {
					device_sp.setSelection(i);
					break;
				}
			}

			for (int i = 0; i < cmdIDList.size(); i++) {
				if (cmdIDList.get(i) == cmd_S.getUsIdx()) {
					cmd_sp.setSelection(i);
					break;
				}
			}
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		unregisterReceiver(modifyModeCmdBroadcastReceiver);
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
