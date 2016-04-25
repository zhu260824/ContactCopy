package com.zhulin.contactcopy.activity.receiver;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.library.http.RequestCall;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.app.CApplication;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.paser.HXPhone;
import com.zhulin.contactcopy.utils.ContactsUtil;
import com.zhulin.contactcopy.view.MyDialog;

public class CleanContactActivity extends BaseActivity {
	private MyDialog progDialog,tipDialog;
	private ProgressBar pb;
	private TextView tv_progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(CleanContactActivity.this,R.layout.dialog_tip_message, null);
		tipDialog = new MyDialog(CleanContactActivity.this,R.style.loading_dialog);
		TextView tipTextView = (TextView) view.findViewById(R.id.tv_tip);// 提示文字
		tipTextView.setText("下载时间就快到了！是否请空联系人？");// 设置加载信息
		Button sure=(Button) view.findViewById(R.id.btn_sure);
		sure.setVisibility(View.GONE);
//		sure.setText("清空");
//		sure.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				new DetalsContacts().execute();
//				RefershUserData();
//			}
//		});
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width / 4 * 3,LinearLayout.LayoutParams.WRAP_CONTENT);
		tipDialog.addContentView(view, dialoglp);
		tipDialog.setCancelable(true);
		tipDialog.show();
		new DetalsContacts(false).execute();
//		RefershUserData();
	}

	
	private class DetalsContacts  extends AsyncTask<Integer, Integer,Integer> {
		private int sunnum=0;
		private boolean isStop=true;
		
		public DetalsContacts(boolean isStop) {
			super();
			this.isStop = isStop;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (tipDialog!=null && tipDialog.isShowing()) 
				tipDialog.dismiss();
			showLoadingDialog(CleanContactActivity.this, "正在读取联系人......");
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			ArrayList<HXPhone> phonelists=(ArrayList<HXPhone>) ContactsUtil.ReadAll(CleanContactActivity.this);
			if (phonelists!=null && phonelists.size()>=1) {
				sunnum=phonelists.size();
				for (int i = 0; i < sunnum; i++) {
					HXPhone hPhone=phonelists.get(i);
					try {
						ContactsUtil.DeleteContacts(CleanContactActivity.this, hPhone.name);
					} catch (Exception e) {
						e.printStackTrace();
					}
					publishProgress(i);
				}
			}else {
				dismissloading();
				CleanContactActivity.this.finish();
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (values[0]==0) {
				dismissloading();
				showProgDialog(CleanContactActivity.this, "正在删除通讯录", 0, sunnum);
			}else {
				upProgressDialog(values[0]);
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (progDialog!=null && progDialog.isShowing()) 
				progDialog.dismiss();
			if (!isStop) {
				new DetalsContacts(true).execute();
			}else {
				CleanContactActivity.this.finish();
			}
		}
	}
	
	
	private void showProgDialog(Context context, String tip,int num,int sunnum) {
		View view = View.inflate(context, R.layout.progress_dialog,null);
		progDialog = new MyDialog(context,R.style.loading_dialog);
		TextView tv_tip=(TextView) view.findViewById(R.id.tv_tip);
		tv_progress=(TextView) view.findViewById(R.id.tv_num);
		TextView tv_sunnum=(TextView) view.findViewById(R.id.tv_sunnum);
		pb=(ProgressBar) view.findViewById(R.id.pb);
		pb.setMax(sunnum);
		pb.setProgress(num);
		tv_progress.setText(num+"");
		tv_sunnum.setText("/"+sunnum);
		tv_tip.setText(tip);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width*4/5,LinearLayout.LayoutParams.WRAP_CONTENT);
		progDialog.addContentView(view, dialoglp);
		progDialog.setCancelable(false);
		progDialog.show();
	}
	
	private void upProgressDialog(int num){
		if (progDialog!=null && progDialog.isShowing() && pb!=null && tv_progress!=null) {
			pb.setProgress(num);
			tv_progress.setText(num+"");
		}
	}
	
	private void RefershUserData(){
		String name = getSharedPreferences("NameAndPsw", MODE_PRIVATE).getString("username", "");
		if (name != null && name.length() >= 1) {
			String psw=getSharedPreferences("NameAndPsw", MODE_PRIVATE).getString("psw", "");
		
			RequestManger.Login(CleanContactActivity.this,name, psw,new Listener<RequestCall>() {
	
				@Override
				public void onResponse(RequestCall response) {
				}
			}, errorListener);
		}
	}
}
