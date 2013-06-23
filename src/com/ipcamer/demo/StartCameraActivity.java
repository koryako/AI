package com.ipcamer.demo;

import java.util.Date;

import object.p2pipcam.nativecaller.NativeCaller;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ipcamer.demo.BridgeService.AddCameraInterface;
import com.ipcamer.demo.BridgeService.IpcamClientInterface;
import com.mac.smartcontrol.R;

import define.entity.Cama_S;

public class StartCameraActivity extends Activity implements
		AddCameraInterface, OnItemSelectedListener, IpcamClientInterface {
	private int option = ContentCommon.INVALID_OPTION;
	private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
	private ProgressDialog progressdlg = null;
	private MyBroadCast receiver;
	private ProgressBar progressBar = null;
	private static final String STR_DID = "did";
	private static final String STR_MSG_PARAM = "msgparam";
	private Intent intentbrod = null;
	private WifiInfo info = null;
	boolean bthread = true;
	Cama_S cama_S;

	private class MyBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			StartCameraActivity.this.finish();
			Log.d("tag", "AddCameraActivity.this.finish()");
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			BridgeService.setAddCameraInterface(StartCameraActivity.this);
			receiver = new MyBroadCast();
			IntentFilter filter = new IntentFilter();
			filter.addAction("finish");
			registerReceiver(receiver, filter);
			intentbrod = new Intent("drop");
			done(cama_S);
		}
	};

	class StartPPPPThread implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				StartCameraPPPP();
			} catch (Exception e) {

			}
		}
	}

	private void StartCameraPPPP() {
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
		int result = NativeCaller.StartPPPP(SystemValue.deviceId,
				SystemValue.deviceName, SystemValue.devicePass);
		System.out.println("StartPPPP result:" + result);

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_camera);
		progressdlg = new ProgressDialog(this);
		progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdlg.setMessage(getString(R.string.searching_tip));
		progressBar = (ProgressBar) findViewById(R.id.main_model_progressBar1);

		Bundle bundle = getIntent().getExtras();
		byte[] b = bundle.getByteArray("camera");
		cama_S = new Cama_S();
		cama_S.setCama_S(b);

		Intent intent = new Intent();
		intent.setClass(StartCameraActivity.this, BridgeService.class);
		startService(intent);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					NativeCaller
							.PPPPInitial("EFGBFFBJKDJBGNJBEBGMFOEIHPNFHGNOGHFBBOCPAJJOLDLNDBAHCOOPGJLMJGLKAOMPLMDIOLMFAFCJJPNEIGAM");
					long lStartTime = new Date().getTime();
					int nRes = NativeCaller.PPPPNetworkDetect();
					long lEndTime = new Date().getTime();
					if (lEndTime - lStartTime <= 1000) {
						Thread.sleep(3000);
					}
					Message msg = new Message();
					mHandler.sendMessage(msg);
				} catch (Exception e) {

				}
			}
		}).start();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// if (myWifiThread != null) {
		// blagg = false;
		// }
		progressdlg.dismiss();
		NativeCaller.StopSearch();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		NativeCaller.Free();
		Intent intent = new Intent();
		intent.setClass(this, BridgeService.class);
		stopService(intent);
	}

	public static String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	private void done(Cama_S cama_S) {
		Intent in = new Intent();
		// in.setAction(ContentCommon.STR_CAMERA_INFO_RECEIVER);
		if (option == ContentCommon.INVALID_OPTION) {
			option = ContentCommon.ADD_CAMERA;
		}
		in.putExtra(ContentCommon.CAMERA_OPTION, option);
		in.putExtra(ContentCommon.STR_CAMERA_ID, cama_S.getSzUid());
		in.putExtra(ContentCommon.STR_CAMERA_USER, cama_S.getSzUser());
		in.putExtra(ContentCommon.STR_CAMERA_PWD, cama_S.getSzPass());
		in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);
		progressBar.setVisibility(View.VISIBLE);
		// sendBroadcast(in);
		SystemValue.deviceName = cama_S.getSzUser();
		SystemValue.deviceId = cama_S.getSzUid();
		SystemValue.devicePass = cama_S.getSzPass();
		BridgeService.setIpcamClientInterface(this);
		NativeCaller.Init();
		new Thread(new StartPPPPThread()).start();
		// overridePendingTransition(R.anim.in_from_right,
		// R.anim.out_to_left);// Ω¯»Î∂Øª≠
		// finish();
	}

	public String getInfoSSID() {

		String ssid = info.getSSID();
		return ssid;
	}

	public int getInfoIp() {

		int ip = info.getIpAddress();
		return ip;
	}

	private Handler PPPPMsgHandler = new Handler() {
		public void handleMessage(Message msg) {

			Bundle bd = msg.getData();
			int msgParam = bd.getInt(STR_MSG_PARAM);
			int msgType = msg.what;
			String did = bd.getString(STR_DID);
			Log.i("info", "ppp" + msgType);
			switch (msgType) {
			case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
				switch (msgParam) {
				case ContentCommon.PPPP_STATUS_CONNECTING:
					progressBar.setVisibility(View.VISIBLE);
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_FAILED:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_connect_failed,
							Toast.LENGTH_SHORT).show();
					finish();
				case ContentCommon.PPPP_STATUS_DISCONNECT:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_disconnect, Toast.LENGTH_SHORT)
							.show();
					finish();
					break;
				case ContentCommon.PPPP_STATUS_INITIALING:
					progressBar.setVisibility(View.VISIBLE);
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_initialing, Toast.LENGTH_SHORT)
							.show();
					finish();
					break;
				case ContentCommon.PPPP_STATUS_INVALID_ID:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_invalid_id, Toast.LENGTH_SHORT)
							.show();
					finish();
					break;
				case ContentCommon.PPPP_STATUS_ON_LINE:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_online, Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(StartCameraActivity.this,
							PlayActivity.class);
					startActivity(intent);
					break;
				case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(StartCameraActivity.this,
							R.string.device_not_on_line, Toast.LENGTH_SHORT)
							.show();
					finish();
					break;
				case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_connect_timeout,
							Toast.LENGTH_SHORT).show();
					finish();
					break;
				default:
					Toast.makeText(StartCameraActivity.this,
							R.string.pppp_status_unknown, Toast.LENGTH_SHORT)
							.show();
					finish();
				}
				if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
					NativeCaller.PPPPGetSystemParams(did,
							ContentCommon.MSG_TYPE_GET_PARAMS);
				}
				if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_FAILED
						|| msgParam == ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE
						|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT) {
					NativeCaller.StopPPPP(did);
				}
				break;
			case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
				break;

			}

		}
	};

	@Override
	public void BSMsgNotifyData(String did, int type, int param) {
		Log.d("≤‚ ‘", "type:" + type + " param:" + param);
		Bundle bd = new Bundle();
		Message msg = PPPPMsgHandler.obtainMessage();
		msg.what = type;
		bd.putInt(STR_MSG_PARAM, param);
		bd.putString(STR_DID, did);
		msg.setData(bd);
		PPPPMsgHandler.sendMessage(msg);
		if (type == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
			intentbrod.putExtra("ifdrop", param);
			sendBroadcast(intentbrod);
		}

	}

	@Override
	public void BSSnapshotNotify(String did, byte[] bImage, int len) {
		// TODO Auto-generated method stub

	}

	@Override
	public void callBackUserParams(String did, String user1, String pwd1,
			String user2, String pwd2, String user3, String pwd3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void CameraStatus(String did, int status) {

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void callBackSearchResultData(int cameraType, String strMac,
			String strName, String strDeviceID, String strIpAddr, int port) {
		// TODO Auto-generated method stub

	}

}
