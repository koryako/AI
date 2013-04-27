package com.mac.smartcontrol.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {
	public static boolean isIp(String ipAddress) {
		Pattern p = Pattern
				.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\."
						+ "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
		Matcher m = p.matcher("192.168.168.1");
		return m.matches();
	}
}
