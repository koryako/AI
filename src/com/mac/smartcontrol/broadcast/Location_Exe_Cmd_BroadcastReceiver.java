package com.mac.smartcontrol.broadcast;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mac.smartcontrol.SocketService;
import com.mac.smartcontrol.util.WriteUtil;

import define.oper.MsgOperMode_E;
import define.oper.body.req.MsgModeExcReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class Location_Exe_Cmd_BroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals("IOException")) {
			Intent it = new Intent();
			it.setClass(context, SocketService.class);
			context.stopService(it);
			Toast.makeText(context, "请确认网络是否开启,操作失败,请重新登录", Toast.LENGTH_LONG)
					.show();
			return;
		}
		short mode_Id = intent.getExtras().getShort("mode_Id");
		if (action.equals("exe_mode")) {
			try {
				WriteUtil.write(MsgId_E.MSGID_MODE.getVal(), 0,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOperMode_E.MSGOPER_MODE_EXC.getVal(),
						MsgModeExcReq_S.getSize(),
						new MsgModeExcReq_S(mode_Id).getMsgModeExcReq_S());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(context, "请确认网络是否开启,连接失败", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
}
