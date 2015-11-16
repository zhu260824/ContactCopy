package com.library.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ZLApplication extends Application{
	private static ZLApplication instance;

	public static ZLApplication getInstance() {
		return instance;
	}

	private static List<Activity> activityList = new LinkedList<Activity>();

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public static void exit() {
		for (int i = activityList.size() - 1; i >= 0; i--) {
			activityList.get(i).finish();
		}
		System.exit(0);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

}
