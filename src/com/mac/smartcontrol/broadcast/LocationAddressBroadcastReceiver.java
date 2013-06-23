package com.mac.smartcontrol.broadcast;

import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mac.smartcontrol.MainActivity;

public class LocationAddressBroadcastReceiver extends BroadcastReceiver {

	Activity activity;
	Date last_Date;
	long sub_time = 60 * 60 * 1000;

	public LocationAddressBroadcastReceiver(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		if (last_Date == null
				|| (last_Date != null && (new Date().getTime() - last_Date
						.getTime()) > sub_time))
			if ("location_ok".equals(action)) {
				((MainActivity) context).request_Weather();
			}
	}

}
