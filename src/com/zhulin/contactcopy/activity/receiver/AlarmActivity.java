package com.zhulin.contactcopy.activity.receiver;

import android.R.integer;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.activity.MainActivity;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.app.CApplication;
import com.zhulin.contactcopy.view.MyDialog;

public class AlarmActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean mdr = getSharedPreferences("SYSTEMSET", MODE_PRIVATE).getBoolean("mdr", true);
		if (mdr) {
			checkTime();
		} else {
			showAlarm();
		}
	}

	private void checkTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// 这里时区需要设置一下，不然会有8个小时的时间差
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String sh = String.valueOf(hour);
		if (hour < 10) {
			sh = "0" + String.valueOf(hour);
		} else {
			sh = String.valueOf(hour);
		}
		String sm = String.valueOf(minute);
		if (minute < 10) {
			sm = "0" + String.valueOf(minute);
		} else {
			sm = String.valueOf(minute);
		}
		int time=Integer.valueOf(sh+sm);
		if (time>1210 && time<1400) {
			AlarmActivity.this.finish();
		}else {
			showAlarm();
		}
	}

	private void showAlarm() {
		final MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
			mp.prepare();
			mp.start();
		} catch (Exception e) {
		}
		View view = View.inflate(AlarmActivity.this, R.layout.dialog_tip_message, null);
		MyDialog tipDialog = new MyDialog(AlarmActivity.this, R.style.loading_dialog);
		TextView tipTextView = (TextView) view.findViewById(R.id.tv_tip);// 提示文字
		tipTextView.setText("下载时间到了");// 设置加载信息
		Button sure = (Button) view.findViewById(R.id.btn_sure);
		sure.setVisibility(View.VISIBLE);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AlarmActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
				if (mp != null && mp.isPlaying()) {
					mp.stop();
				}
			}
		});
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width / 4 * 3,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tipDialog.addContentView(view, dialoglp);
		tipDialog.setCancelable(true);
		tipDialog.show();
	}
}
