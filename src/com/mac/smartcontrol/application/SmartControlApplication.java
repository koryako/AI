package com.mac.smartcontrol.application;

import android.app.Application;
import android.content.BroadcastReceiver;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mac.smartcontrol.listener.MyLocationListener;

public class SmartControlApplication extends Application {
	public LocationClient mLocationClient = null;
	public MyLocationListener myListener = null;
	BroadcastReceiver BroadcastReceiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myListener = new MyLocationListener(this);
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
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
		option.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先
		option.setPoiNumber(3);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

}
