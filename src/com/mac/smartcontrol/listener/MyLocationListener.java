package com.mac.smartcontrol.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.mac.smartcontrol.util.SaveLocationUtil;

public class MyLocationListener implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return ;
		SaveLocationUtil.setBdLocation(location);
	}
	
	public void onReceivePoi(BDLocation location) {
			if (location == null){
				return ;
			}
			SaveLocationUtil.setBdLocation(location);
			}
}
