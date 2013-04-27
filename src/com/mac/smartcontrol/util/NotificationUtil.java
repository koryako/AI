package com.mac.smartcontrol.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.mac.smartcontrol.MainActivity;

public class NotificationUtil {
	static NotificationManager notificationManager;
	static int notifyID = 0;
	static Context notifyContext;

	public static void notify(Context context, int id, int currentPage) {
		if (notificationManager == null)
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		notifyID = id;
		notifyContext = context;
		Notification notification = new Notification();
		notification.icon = com.mac.smartcontrol.R.drawable.ac_icon;

		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.putExtra("currentPage", currentPage);
		intent.setClass(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(new ComponentName(context.getPackageName(), context
				.getPackageName()
				+ "."
				+ ((Activity) context).getLocalClassName()));
		PendingIntent contextIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, "AI-智能家居", "AI正在后台运行中",
				contextIntent);
		notificationManager.notify(id, notification);
	}

	public static void cancelNotify(int id) {
		if (notifyContext != null) {
			NotificationManager notificationManager = (NotificationManager) notifyContext
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		}
	}
}
