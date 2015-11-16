package com.zhulin.contactcopy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownAlarmService extends BroadcastReceiver
{
	  @Override
	  public void onReceive(Context context, Intent intent)
	  {
	    Intent i = new Intent(context, AlarmActivity.class);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    context.startActivity(i);
	  }
	}
