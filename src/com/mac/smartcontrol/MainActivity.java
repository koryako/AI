package com.mac.smartcontrol;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.mac.smartcontrol.broadcast.LocationAddressBroadcastReceiver;
import com.mac.smartcontrol.broadcast.VoiceControlBroadcastReceiver;
import com.mac.smartcontrol.util.NotificationUtil;
import com.mac.smartcontrol.util.SQLiteHelper;
import com.mac.smartcontrol.util.SaveLocationUtil;
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
	LocationAddressBroadcastReceiver addressBroadcastReceiver;
	public List<String> voiceName;
	TextView weather_Tv;
	SQLiteHelper sqLiteHelper;
	// 默认北京
	String weather_URL = "http://www.weather.com.cn/data/cityinfo/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		setContentView(R.layout.activity_main);
		sqLiteHelper = new SQLiteHelper(MainActivity.this, "ai", null, 1);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			currentPage = bundle.getInt("currentPage");
		// 设置视图
		container = (LinearLayout) findViewById(R.id.containerBody);
		// 模块1
		manage_ll = (LinearLayout) findViewById(R.id.menu_manage_ll);
		control_ll = (LinearLayout) findViewById(R.id.menu_control_ll);
		camera_ll = (LinearLayout) findViewById(R.id.menu_camera_ll);
		location_ll = (LinearLayout) findViewById(R.id.menu_location_ll);
		voice_Iv = (ImageView) findViewById(R.id.menu_voice_iv);
		weather_Tv = (TextView) findViewById(R.id.weather_tv);
		broadcastReceiver = new VoiceControlBroadcastReceiver(MainActivity.this);
		IntentFilter filter = new IntentFilter();
		addressBroadcastReceiver = new LocationAddressBroadcastReceiver(
				MainActivity.this);
		registerReceiver(addressBroadcastReceiver, new IntentFilter(
				"location_ok"));
		filter.addAction(MsgId_E.MSGID_SQL.getVal() + "_"
				+ MsgOperSql_E.MSGOPER_MAX.getVal());
		filter.addAction(MsgId_E.MSGID_CMD.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		registerReceiver(broadcastReceiver, filter);
		voice_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try { // 通过Intent传递语音识别的模式，开启语音
					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					// 语言模式和自由模式的语音识别
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
							RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // 提示语音开始
					intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
					// 开始语音识别
					startActivityForResult(intent,
							VOICE_RECOGNITION_REQUEST_CODE);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"找不到语音设备,请安装Google Voice", 1).show();
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
				 * Toast.makeText(MainActivity.this, "请确认网络是否开启,连接失败",
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

		// 模块2

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

		// 模块3

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

		// 模块4

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

	public void request_Weather() {
		String city_num = null;
		BDLocation bdLocation = SaveLocationUtil.getBdLocation();
		if (bdLocation != null) {
			String address = bdLocation.getAddrStr();
			if (address != null && !address.equals("")) {
				int p_index = address.indexOf("省") + 1;
				int c_index = address.indexOf("市");
				String city = address.substring(p_index, c_index);
				city_num = sqLiteHelper.selectCity_Num_By_City_Name(city);
				if (city_num != null && !"".equals(city_num)) {
					String httpUrl = weather_URL + city_num + ".html";
					// HttpGet连接对象
					HttpGet httpRequest = new HttpGet(httpUrl);
					// 取得HttpClient对象
					HttpClient httpclient = new DefaultHttpClient();
					// 请求HttpClient，取得HttpResponse
					String strResult = null;
					try {
						HttpResponse httpResponse = httpclient
								.execute(httpRequest);
						// 请求成功
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							// 取得返回的字符串
							strResult = EntityUtils.toString(httpResponse
									.getEntity());
						} else {
							weather_Tv.setText("请求错误!");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
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
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.setNeutralButton("隐藏",
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
		unregisterReceiver(addressBroadcastReceiver);
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
		// 回调获取从谷歌得到的数据
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// 取得语音的字符
			voiceName = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			try {
				WriteUtil.write(MsgId_E.MSGID_CMD.getVal(), 1,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_QRY.getVal(), (short) 2,
						new MsgQryReq_S((short) 0).getMsgQryReq_S());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(MainActivity.this, "请确认网络是否开启,连接失败",
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
			 * "请确认网络是否开启,连接失败", Toast.LENGTH_LONG).show(); } }
			 */

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
