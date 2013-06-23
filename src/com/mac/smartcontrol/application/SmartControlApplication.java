package com.mac.smartcontrol.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.IntentFilter;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mac.smartcontrol.broadcast.NtfBroadcastReceiver;
import com.mac.smartcontrol.listener.MyLocationListener;

import define.entity.Rgn_S;
import define.entity.Sens_S;
import define.oper.MsgOper_E;
import define.type.MsgId_E;
import define.type.SensType_E;

public class SmartControlApplication extends Application {
	public LocationClient mLocationClient = null;
	public MyLocationListener myListener = null;
	public Map<Short, Rgn_S> area_Map = new HashMap<Short, Rgn_S>();
	public List<Sens_S> sense_List = new ArrayList<Sens_S>();
	NtfBroadcastReceiver broadcastReceiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myListener = new MyLocationListener(this);
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
		broadcastReceiver = new NtfBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("ntf");
		filter.addAction("IO_Exception");
		filter.addAction(MsgId_E.MSGID_RGN.getVal() + "_"
				+ MsgOper_E.MSGOPER_QRY.getVal());
		registerReceiver(broadcastReceiver, filter);

	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(3000);
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		// option.setPriority(LocationClientOption.NetWorkFirst); //
		// 不设置，默认是gps优先
		option.setPoiNumber(3);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	public void showNTFToast() {

		for (int i = 0; i < sense_List.size(); i++) {
			Sens_S sens_S = sense_List.get(i);
			Rgn_S rgn_S = area_Map.get(sens_S.getUsIdx());
			String msg = "";
			if (rgn_S != null) {
				msg += rgn_S.getSzName();
			}
			if (sens_S.getUcType() == SensType_E.SENS_TYPE_GAS.getVal()) {
				msg += ",煤气泄漏";
			} else if (sens_S.getUcType() == SensType_E.SENS_TYPE_SMOKE
					.getVal()) {
				msg += ",出现烟雾";
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
					.show();
		}
	}

	// public void unRegisterReceiver() {
	// unregisterReceiver(broadcastReceiver);
	// }
}
