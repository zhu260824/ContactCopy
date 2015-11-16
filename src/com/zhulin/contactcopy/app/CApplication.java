package com.zhulin.contactcopy.app;

import android.content.Context;
import android.view.WindowManager;

import com.library.app.ZLApplication;

public class CApplication extends ZLApplication{
	public static int Width = 720;
	public static int Height = 1280;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initScreenSize();
	}
	
	@SuppressWarnings("deprecation")
	private void initScreenSize() {
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		Width = wm.getDefaultDisplay().getWidth();
		Height = wm.getDefaultDisplay().getHeight();
	}
}
