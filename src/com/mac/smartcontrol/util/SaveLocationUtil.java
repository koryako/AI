package com.mac.smartcontrol.util;

import com.baidu.location.BDLocation;

public class SaveLocationUtil {
	private static BDLocation bdLocation;

	public static BDLocation getBdLocation() {
		return bdLocation;
	}

	public static void setBdLocation(BDLocation location) {
		bdLocation = location;
	}

}
