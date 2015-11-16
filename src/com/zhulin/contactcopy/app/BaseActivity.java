package com.zhulin.contactcopy.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.view.MyDialog;

public class BaseActivity extends Activity {
	protected MyDialog loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CApplication.getInstance().addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(this.getClass().getName()); 
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(this.getClass().getName()); 
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CApplication.getInstance().removeActivity(this);
	}
	
	protected ErrorListener errorListener = new ErrorListener() {
		public void onErrorResponse(VolleyError error) {
			dismissloading();
			Toast.makeText(BaseActivity.this, "请检查网络连接！",Toast.LENGTH_SHORT).show();
		}
	};
	
	protected void dismissloading(){
		if (loadingDialog!=null && loadingDialog.isShowing()) 
			loadingDialog.dismiss();
	}
	protected void setTitle(String Title){
		TextView tv_title=(TextView) findViewById(R.id.title);
		tv_title.setText(Title);
	}
	protected void goBack(){
		ImageView iv_goback=(ImageView) findViewById(R.id.iv_goback);
		iv_goback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaseActivity.this.finish();
			}
		});;
	}
	
	protected  void showLoadingDialog(Context context, String msg) {
		View view = View.inflate(context, R.layout.loading_dialog,null);
		loadingDialog = new MyDialog(context,R.style.loading_dialog);
		ImageView spaceshipImage = (ImageView) view.findViewById(R.id.img);
		TextView tipTextView = (TextView) view.findViewById(R.id.tv_tip);// 提示文字
		AnimationDrawable animationDrawable= (AnimationDrawable) spaceshipImage.getDrawable();  
        animationDrawable.start();  
		tipTextView.setText(msg);// 设置加载信息
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		loadingDialog.addContentView(view, dialoglp);
		loadingDialog.setCancelable(true);
		loadingDialog.show();
	}
	
}
