package com.zhulin.contactcopy.activity.receiver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.library.http.RequestCall;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.activity.login.LoginActivity;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.app.CApplication;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.view.MyDialog;

public class RefershDataActivity  extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(RefershDataActivity.this,R.layout.dialog_tip_message, null);
		MyDialog tipDialog = new MyDialog(RefershDataActivity.this,R.style.loading_dialog);
		final TextView tipTextView = (TextView) view.findViewById(R.id.tv_tip);// 提示文字
		tipTextView.setText("正在刷新个人下载数据......");// 设置加载信息
		final Button sure=(Button) view.findViewById(R.id.btn_sure);
		sure.setText("登陆");
		sure.setVisibility(View.GONE);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width / 4 * 3,LinearLayout.LayoutParams.WRAP_CONTENT);
		tipDialog.addContentView(view, dialoglp);
		tipDialog.setCancelable(true);
		tipDialog.show();
		String name = getSharedPreferences("NameAndPsw", MODE_PRIVATE).getString("username", "");
		if (name != null && name.length() >= 1) {
			String psw=getSharedPreferences("NameAndPsw", MODE_PRIVATE).getString("psw", "");
		
			RequestManger.Login(RefershDataActivity.this,name, psw,new Listener<RequestCall>() {
	
				@Override
				public void onResponse(RequestCall response) {
					dismissloading();
					if (response.getParser().getResultSuccess()) {
						tipTextView.setText("刷新个人数据成功！");
					}else {
						tipTextView.setText("刷新个人数据失败，请重新登陆刷新！");
						sure.setVisibility(View.VISIBLE);
						sure.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent i = new Intent(RefershDataActivity.this, LoginActivity.class);
					      	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					      	    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					      	    startActivity(i);
							}
						});
					}
				}
			}, errorListener);
		}
	}

	
	
}
