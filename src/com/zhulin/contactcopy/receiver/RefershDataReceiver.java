package com.zhulin.contactcopy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhulin.contactcopy.activity.receiver.RefershDataActivity;

public class RefershDataReceiver extends BroadcastReceiver{
	  @Override
	  public void onReceive(Context context, Intent intent){
	    Intent i = new Intent(context, RefershDataActivity.class);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    context.startActivity(i);
	  }
}

