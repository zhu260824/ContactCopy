package com.zhulin.contactcopy.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;

public class AlarmActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
			mp.prepare();
			mp.start();
		} catch (Exception e) {
		}
		new AlertDialog.Builder(AlarmActivity.this)
        .setIcon(R.drawable.a_ico)
        .setTitle("提示")
        .setMessage("下载时间到了")
        .setPositiveButton("朕知道了",
         new DialogInterface.OnClickListener(){
          public void onClick(DialogInterface dialog, int whichButton){
        	Intent i = new Intent(AlarmActivity.this, MainActivity.class);
      	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      	    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      	    startActivity(i);
      	    if (mp!=null && mp.isPlaying()) {
				mp.stop();
			}
          }
        }) .show();
		
	}

}
