package com.mac.smartcontrol.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.mac.smartcontrol.util.DistanceUtil;
import com.mac.smartcontrol.util.SaveLocationUtil;

public class MyLocationListener implements BDLocationListener {
	private Context context;
	SharedPreferences sharedPreferences;
	short mode_Id = -1;
	double location_distance = 0;
	double location_longitude = 0;
	double location_latitude = 0;
	boolean location_isOpen;

	public MyLocationListener(Context context) {
		super();
		this.context = context;
		sharedPreferences = context.getSharedPreferences("location", 0);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		SaveLocationUtil.setBdLocation(location);
		isExeMode(location);
		context.sendBroadcast(new Intent("location_ok"));
	}

	public void onReceivePoi(BDLocation location) {
		if (location == null)
			return;
		SaveLocationUtil.setBdLocation(location);
		isExeMode(location);
		context.sendBroadcast(new Intent("location_ok"));
	}

	public void isExeMode(BDLocation location) {
		if (sharedPreferences != null) {
			mode_Id = (short) sharedPreferences.getInt("location_mode_ID", -1);
			location_distance = sharedPreferences.getInt("location_distance",
					1000);
			location_longitude = Double.parseDouble(sharedPreferences
					.getString("location_longitude", "0"));
			location_latitude = Double.parseDouble(sharedPreferences.getString(
					"location_latitude", "0"));
			location_isOpen = sharedPreferences.getBoolean("location_isOpen",
					false);
			if (location_isOpen) {
				double lat = location.getLatitude();
				double lon = location.getLongitude();
				double dis = DistanceUtil.GetLongDistance(lon, lat,
						location_longitude, location_latitude);
				if (Math.abs(dis) > location_distance) {
					Intent intent = new Intent("exe_mode");
					intent.putExtra("mode_Id", mode_Id);
					context.sendBroadcast(intent);
				}
			}
		}
	}
}
