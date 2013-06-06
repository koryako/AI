package com.mac.smartcontrol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import com.mac.smartcontrol.application.SmartControlApplication;
import com.mac.smartcontrol.broadcast.Location_Exe_Cmd_BroadcastReceiver;
import com.mac.smartcontrol.thread.MessageQueueRunnable;
import com.mac.smartcontrol.thread.ReadRunnable;
import com.mac.smartcontrol.util.WriteUtil;

import define.oper.MsgOper_E;
import define.oper.body.req.MsgUserLoginReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class SocketService extends Service {

	public Socket socket = null;
	public static InputStream is;
	public static OutputStream os;
	public static String ip = null;
	public static int port = 12345;
	Thread readThread = null;
	Thread messageQueueThread = null;
	MessageQueueRunnable messageQueueRunnable = null;
	ReadRunnable readRunnable = null;
	Location_Exe_Cmd_BroadcastReceiver cmd_BroadcastReceiver;
	IntentFilter intentFilter;

	// SharedPreferences sharedPreferences;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		((SmartControlApplication) getApplication()).mLocationClient.start();
		((SmartControlApplication) getApplication()).mLocationClient
				.requestLocation();
		// sharedPreferences = getSharedPreferences("location", 0);
		// if (sharedPreferences != null) {
		// boolean location_isOpen = sharedPreferences.getBoolean(
		// "location_isOpen", false);
		// short mode_Id = (short) sharedPreferences.getInt(
		// "location_mode_ID", -1);
		//
		// if (location_isOpen && mode_Id != -1) {
		cmd_BroadcastReceiver = new Location_Exe_Cmd_BroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction("exe_mode");
		registerReceiver(cmd_BroadcastReceiver, intentFilter);
		// }
		// }
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (socket == null) {
						socket = new Socket(ip, port);
						is = socket.getInputStream();
						os = socket.getOutputStream();
						readRunnable = new ReadRunnable();
						readThread = new Thread(readRunnable);
						readThread.start();
						messageQueueRunnable = new MessageQueueRunnable(
								SocketService.this);
						messageQueueThread = new Thread(messageQueueRunnable);
						messageQueueThread.start();
					}
					Bundle bundle = intent.getExtras();
					byte b[] = bundle.getByteArray("data");
					WriteUtil.write(MsgId_E.MSGID_USER.getVal(), 0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_MAX.getVal(),
							MsgUserLoginReq_S.getSize(), b);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					Intent i = new Intent("UnknownHostException");
					SocketService.this.sendBroadcast(i);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Intent i = new Intent("IOException");
					SocketService.this.sendBroadcast(i);
				}
			}
		}).start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		if (messageQueueRunnable != null) {
			messageQueueRunnable.isRun = false;
		}
		if (readRunnable != null) {
			readRunnable.isRun = false;
		}
		WriteUtil.usSsnId = 0;
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmd_BroadcastReceiver != null)
			unregisterReceiver(cmd_BroadcastReceiver);
		((SmartControlApplication) getApplication()).mLocationClient.stop();
		return super.stopService(name);
	}

}
