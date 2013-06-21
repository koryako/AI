package com.mac.smartcontrol.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mac.smartcontrol.MainActivity;

public class LocationAddressBroadcastReceiver extends BroadcastReceiver {

	Activity activity;

	public LocationAddressBroadcastReceiver(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if ("location_ok".equals(action)) {
			((MainActivity) context).request_Weather();
		}
	}

}
