package com.mac.smartcontrol.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mac.smartcontrol.LoginActivity;
import com.mac.smartcontrol.broadcast.UserBroadcastReceiver;
import com.mac.smartcontrol.listener.MyLocationListener;

public class SmartControlApplication extends Application {
	public LocationClient mLocationClient = null;
	public MyLocationListener myListener =null;
	BroadcastReceiver BroadcastReceiver;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myListener=new MyLocationListener();
		mLocationClient=new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
	}
	
	//������ز���
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//��gps
		option.setCoorType("bd09ll");		//������������
		option.setScanSpan(3000);
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);	
		option.setAddrType("all");
		option.setPriority(LocationClientOption.GpsFirst);        //�����ã�Ĭ����gps����
		option.setPoiNumber(3);
		option.disableCache(true);		
		mLocationClient.setLocOption(option);
	}

	
}
