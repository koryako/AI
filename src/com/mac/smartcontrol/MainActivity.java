package com.mac.smartcontrol;

import java.io.IOException;
import java.util.List;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mac.smartcontrol.broadcast.VoiceControlBroadcastReceiver;
import com.mac.smartcontrol.util.NotificationUtil;
import com.mac.smartcontrol.util.WriteUtil;

import define.oper.MsgOperSql_E;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class MainActivity extends ActivityGroup {
	private LinearLayout container = null;
	private int currentPage = 0;
	LinearLayout manage_ll = null;
	LinearLayout control_ll = null;
	LinearLayout camera_ll = null;
	LinearLayout location_ll = null;
	ImageView voice_Iv = null;
	private final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	VoiceControlBroadcastReceiver broadcastReceiver;
	public List<String> voiceName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		setContentView(R.layout.activity_main);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			currentPage = bundle.getInt("currentPage");
		// ������ͼ
		container = (LinearLayout) findViewById(R.id.containerBody);
		// ģ��1
		manage_ll = (LinearLayout) findViewById(R.id.menu_manage_ll);
		control_ll = (LinearLayout) findViewById(R.id.menu_control_ll);
		camera_ll = (LinearLayout) findViewById(R.id.menu_camera_ll);
		location_ll = (LinearLayout) findViewById(R.id.menu_location_ll);
		voice_Iv = (ImageView) findViewById(R.id.menu_voice_iv);
		broadcastReceiver = new VoiceControlBroadcastReceiver(MainActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgId_E.MSGID_SQL.getVal() + "_"
				+ MsgOperSql_E.MSGOPER_MAX.getVal());
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		registerReceiver(broadcastReceiver, filter);
		voice_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try { // ͨ��Intent��������ʶ���ģʽ����������
					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					// ����ģʽ������ģʽ������ʶ��
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
							RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // ��ʾ������ʼ
					intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "��ʼ����");
					// ��ʼ����ʶ��
					startActivityForResult(intent,
							VOICE_RECOGNITION_REQUEST_CODE);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"�Ҳ��������豸,�밲װGoogle Voice", 1).show();
				}

				/*
				 * String sql =
				 * "select idx,dev_type,dev_idx,name,voice,ctrl_idx,type,code,para from tbl_cmd where voice=\'fgh\'"
				 * ; MsgSqlExcReq_S msgSqlExcReq_S = new MsgSqlExcReq_S(); try {
				 * msgSqlExcReq_S.setUsLen((short) sql.getBytes("gbk").length);
				 * } catch (UnsupportedEncodingException e1) { //
				 * TODOAuto-generated catch block e1.printStackTrace(); }
				 * msgSqlExcReq_S.setSzSql(sql); try {
				 * WriteUtil.write(MsgId_E.MSGID_SQL.getVal(), 0,
				 * MsgType_E.MSGTYPE_REQ.getVal(),
				 * MsgOperSql_E.MSGOPER_MAX.getVal(), (short)
				 * (msgSqlExcReq_S.getUsLen() + 2),
				 * msgSqlExcReq_S.getMsgSqlExcReq_S()); } catch (IOException e)
				 * { // TODO Auto-generated catch block
				 * Toast.makeText(MainActivity.this, "��ȷ�������Ƿ���,����ʧ��",
				 * Toast.LENGTH_LONG).show(); }
				 */

			}
		});

		manage_ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initFocus();
				currentPage = 0;
				container.removeAllViews();
				container.addView(getLocalActivityManager().startActivity(
						"manage",
						new Intent(MainActivity.this, ManageActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				manage_ll
						.setBackgroundResource(R.drawable.menu_manage_btn_focus);
			}
		});

		// ģ��2

		control_ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initFocus();
				currentPage = 1;
				Intent intent = new Intent(MainActivity.this,
						EnterAreaActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("msgId", (short) 34);
				container.removeAllViews();
				container.addView(getLocalActivityManager().startActivity(
						"control", intent).getDecorView());
				control_ll
						.setBackgroundResource(R.drawable.menu_device_btn_focus);
			}
		});

		// ģ��3

		camera_ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initFocus();
				currentPage = 2;
				// container.removeAllViews();
				// container.addView(getLocalActivityManager().startActivity(
				// "camera",
				// new Intent(MainActivity.this, CameraActivity.class)
				// .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				// .getDecorView());
				camera_ll
						.setBackgroundResource(R.drawable.menu_camera_btn_focus);
			}
		});

		// ģ��4

		location_ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initFocus();
				currentPage = 3;
				container.removeAllViews();
				container.addView(getLocalActivityManager().startActivity(
						"location",
						new Intent(MainActivity.this, LocationActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				location_ll
						.setBackgroundResource(R.drawable.menu_location_btn_focus);
			}
		});

		switch (currentPage) {
		case 0:
			manage_ll.performClick();
			break;
		case 1:
			control_ll.performClick();
			break;
		case 2:
			camera_ll.performClick();
			break;
		case 3:
			location_ll.performClick();
			break;
		default:
			break;
		}
	}

	private void initFocus() {
		switch (currentPage) {
		case 0:
			manage_ll.setBackgroundResource(R.drawable.menu_manage_btn);
			break;
		case 1:
			control_ll.setBackgroundResource(R.drawable.menu_device_btn);
			break;
		case 2:
			camera_ll.setBackgroundResource(R.drawable.menu_camera_btn);
			break;
		case 3:
			location_ll.setBackgroundResource(R.drawable.menu_location_btn);
			break;
		default:
			break;
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// return getCurrentActivity().onKeyDown(keyCode, event);
	// }
	// return false;
	// }

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("ȷ��Ҫ�˳���?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		builder.setNegativeButton("ȡ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.setNeutralButton("����",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						NotificationUtil.notify(MainActivity.this,
								R.string.app_name, currentPage);
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addCategory(Intent.CATEGORY_HOME);
						MainActivity.this.startActivity(intent);
					}
				});

		builder.create().show();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			dialog();
		}
		return false;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SocketService.class);
		stopService(intent);
		unregisterReceiver(broadcastReceiver);
		android.os.Process.killProcess(android.os.Process.myPid());
		super.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		NotificationUtil.cancelNotify(R.string.app_name);
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// �ص���ȡ�ӹȸ�õ�������
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// ȡ���������ַ�
			voiceName = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			try {
				WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_QRY.getVal(), (short) 2,
						new MsgQryReq_S((short) 0).getMsgQryReq_S());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(MainActivity.this, "��ȷ�������Ƿ���,����ʧ��",
						Toast.LENGTH_LONG).show();
			}
			/*
			 * for (int i = 0; i < results.size(); i++) { String sql =
			 * "select idx,dev_type,dev_idx,name,voice,ctrl_idx,type,code,para from tbl_cmd where name="
			 * + results.get(i); MsgSqlExcReq_S msgSqlExcReq_S = new
			 * MsgSqlExcReq_S(); msgSqlExcReq_S.setUsLen((short)
			 * sql.getBytes().length); msgSqlExcReq_S.setSzSql(sql); try {
			 * WriteUtil.write(MsgId_E.MSGID_SQL.getVal(), 0,
			 * MsgType_E.MSGTYPE_REQ.getVal(),
			 * MsgOperSql_E.MSGOPER_MAX.getVal(), msgSqlExcReq_S.getUsLen(),
			 * msgSqlExcReq_S.getMsgSqlExcReq_S()); } catch (IOException e) { //
			 * TODO Auto-generated catch block Toast.makeText(MainActivity.this,
			 * "��ȷ�������Ƿ���,����ʧ��", Toast.LENGTH_LONG).show(); } }
			 */

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
