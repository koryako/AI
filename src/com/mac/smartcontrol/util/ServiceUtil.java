package com.mac.smartcontrol.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtil {
	 public static boolean isServiceRunning(Context mContext,String className) {
         boolean isRunning = false;
ActivityManager activityManager = (ActivityManager)
mContext.getSystemService(Context.ACTIVITY_SERVICE); 
         List<ActivityManager.RunningServiceInfo> serviceList 
         = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
             return false;
         }
         for (int i=0; i<serviceList.size(); i++) {
        	 System.out.println(serviceList.get(i).service.getClassName());
             if (serviceList.get(i).service.getClassName().equals(className) == true) {
                 isRunning = true;
                 break;
             }
         }
         return isRunning;
     }
}
