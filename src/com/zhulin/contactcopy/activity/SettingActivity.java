package com.zhulin.contactcopy.activity;

import android.os.Bundle;

import com.nineoldandroids.SwitchView;
import com.nineoldandroids.SwitchView.OnStateChangeListener;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;

public class SettingActivity extends BaseActivity {
	private SwitchView switchView1,switchView2,switchView3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle(getString(R.string.system_setting));
		goBack();
		switchView1=(SwitchView) findViewById(R.id.switchView1);
		switchView2=(SwitchView) findViewById(R.id.switchView2);
		switchView3=(SwitchView) findViewById(R.id.switchView3);
		switchView1.setOnStateChangeListener(new OnStateChangeListener() {
			
			@Override
			public void onStateChange(int state) {
				if (state==SwitchView.STATE_LEFT_ON_TOP) {
					//左边ON按钮
					getSharedPreferences("SYSTEMSET", MODE_PRIVATE).edit().putBoolean("ONE", true).commit();
				}else {
					//右边OFF按钮
					getSharedPreferences("SYSTEMSET", MODE_PRIVATE).edit().putBoolean("ONE", false).commit();
				}
			}
		});
		switchView2.setOnStateChangeListener(new OnStateChangeListener() {
			
			@Override
			public void onStateChange(int state) {
				if (state==SwitchView.STATE_LEFT_ON_TOP) {
					//左边ON按钮
					getSharedPreferences("SYSTEMSET", MODE_PRIVATE).edit().putBoolean("Alarm", true).commit();
				}else {
					//右边OFF按钮
					getSharedPreferences("SYSTEMSET", MODE_PRIVATE).edit().putBoolean("Alarm", false).commit();
				}
			}
		});
		switchView2.setOnStateChangeListener(new OnStateChangeListener() {
			
			@Override
			public void onStateChange(int state) {
				if (state==SwitchView.STATE_LEFT_ON_TOP) {
					//左边ON按钮
					getSharedPreferences("SYSTEMSET", MODE_PRIVATE).edit().putBoolean("mdr", true).commit();
				}else {
					//右边OFF按钮
					getSharedPreferences("SYSTEMSET", MODE_PRIVATE).edit().putBoolean("mdr", false).commit();
				}
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			boolean one=getSharedPreferences("SYSTEMSET", MODE_PRIVATE).getBoolean("ONE", true);
			boolean isalarm=getSharedPreferences("SYSTEMSET", MODE_PRIVATE).getBoolean("Alarm", true);
			boolean mdr=getSharedPreferences("SYSTEMSET", MODE_PRIVATE).getBoolean("mdr", true);
			if (one) {
				switchView1.setState(SwitchView.STATE_LEFT_ON_TOP);
			}else {
				switchView1.setState(SwitchView.STATE_RIGHT_ON_TOP);
			}
			if (isalarm) {
				switchView2.setState(SwitchView.STATE_LEFT_ON_TOP);
			}else {
				switchView2.setState(SwitchView.STATE_RIGHT_ON_TOP);
			}
			if (mdr) {
				switchView3.setState(SwitchView.STATE_LEFT_ON_TOP);
			}else {
				switchView3.setState(SwitchView.STATE_RIGHT_ON_TOP);
			}
		}
	}
	
}
