package com.zhulin.contactcopy.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;

public class AlarmActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
          }
        }) .show();
	}

}
