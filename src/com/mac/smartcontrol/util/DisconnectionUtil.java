package com.mac.smartcontrol.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.LoginActivity;
import com.mac.smartcontrol.SocketService;

public class DisconnectionUtil {
	public static void restart(Activity context) {
		Toast.makeText(context, "��ȷ�������Ƿ���,����ʧ��,�����µ�¼", Toast.LENGTH_LONG)
				.show();
		Intent i_s = new Intent();
		i_s.setClass(context, SocketService.class);
		context.stopService(i_s);
		// WriteUtil.usSsnId = 0;

		Intent i = new Intent();
		i.setClass(context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
