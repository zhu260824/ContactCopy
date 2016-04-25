package com.zhulin.contactcopy.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.library.app.ZLApplication;
import com.library.http.RequestCall;
import com.library.utils.SerializableFactory;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.activity.login.LoginActivity;
import com.zhulin.contactcopy.activity.login.RestPassWorldActivity;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.app.CApplication;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.manger.SaveTools;
import com.zhulin.contactcopy.paser.HXPhone;
import com.zhulin.contactcopy.paser.HXPhonePaser;
import com.zhulin.contactcopy.paser.User;
import com.zhulin.contactcopy.paser.UserPaser;
import com.zhulin.contactcopy.receiver.CleanContactReceiver;
import com.zhulin.contactcopy.receiver.DownAlarmReceiver;
import com.zhulin.contactcopy.utils.ContactsUtil;
import com.zhulin.contactcopy.view.MyDialog;
import com.zhulin.contactcopy.view.RadialProgress;

public class MainActivity extends BaseActivity implements OnClickListener {
	private RadialProgress progress;
	private TextView tv_sum_number,tv_down_number,tv_progress,tv_time;
	private ImageView iv_down,iv_login_out;
	private LinearLayout lin_delete,lin_change_psw;
//	lin_user_manger,lin_upmanger,lin_manger;
	private MyDialog progDialog;
	private ProgressBar pb;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progress=(RadialProgress) findViewById(R.id.progress);
		tv_down_number=(TextView) findViewById(R.id.tv_down_number);
		tv_sum_number=(TextView) findViewById(R.id.tv_sum_number);
		iv_down=(ImageView) findViewById(R.id.iv_down);
		tv_time=(TextView) findViewById(R.id.tv_time);
		iv_login_out=(ImageView) findViewById(R.id.iv_login_out);
		lin_delete=(LinearLayout) findViewById(R.id.lin_delete);
		lin_change_psw=(LinearLayout) findViewById(R.id.lin_change_psw);
//		lin_user_manger=(LinearLayout) findViewById(R.id.lin_user_manger);
//		lin_upmanger=(LinearLayout) findViewById(R.id.lin_upmanger);
//		lin_manger=(LinearLayout) findViewById(R.id.lin_manger);
		iv_down.setOnClickListener(this);
		iv_login_out.setOnClickListener(this);
		lin_delete.setOnClickListener(this);
		lin_change_psw.setOnClickListener(this);
//		lin_user_manger.setOnClickListener(this);
//		lin_upmanger.setOnClickListener(this);
		new initData(0).execute();
//		openReferDataAlarm();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_down:
			checkTime();
			break;
		case R.id.iv_login_out:
			showLoadingDialog(MainActivity.this, "注销中......");
			RequestManger.LoginOut(MainActivity.this, user.loginToken, user.id, new Listener<RequestCall>() {

				@Override
				public void onResponse(RequestCall response) {
					dismissloading();
					 if (response.getParser().getTokenerro()) 
						 Toast.makeText(MainActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
					LoginOut();
				}
			}, errorListener);
			break;
		case R.id.lin_delete:
			boolean one=getSharedPreferences("SYSTEMSET", MODE_PRIVATE).getBoolean("ONE", true);
			if (one) {
				showMsg();
			}else {
				startActivity(new Intent(MainActivity.this, PhoneMangerActivity.class));
			}
			break;
		case R.id.lin_change_psw:
			startActivityForResult(new Intent(MainActivity.this, RestPassWorldActivity.class),1);
			break;
		case R.id.lin_user_manger:
			startActivityForResult(new Intent(MainActivity.this, MangerActivity.class),1);
			break;
		case R.id.lin_upmanger:
			startActivityForResult(new Intent(MainActivity.this, UpToMangerActivity.class),1);
			break;
		default:
			break;
		}
		
	}
	
	
	private class initData extends AsyncTask<Integer, Integer, User>{
		private long  lasttime=0;

		public initData(long lasttime) {
			super();
			this.lasttime = lasttime;
		}
		
		@Override
		protected User doInBackground(Integer... params) {
			user=UserPaser.GetInstance();
			return user;
		}
		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(User result) {
			super.onPostExecute(result);
			tv_sum_number.setText(user.dayMaxNum==null?"0":user.dayMaxNum);
			tv_down_number.setText(user.dayDownMun==null?"0":user.dayDownMun);
			int sum=Integer.valueOf(tv_sum_number.getText().toString());
			int down=Integer.valueOf(tv_down_number.getText().toString());
			if (sum==0) {
				progress.setCurrentValue(0);
			}else {
				progress.setCurrentValue(down*100/sum);
			}
//			int system=Integer.valueOf(user.userRight==null?"2":user.userRight);
//			if (system==0) {
//				lin_manger.setVisibility(View.VISIBLE);
//				lin_user_manger.setVisibility(View.VISIBLE);
//				lin_upmanger.setVisibility(View.VISIBLE);
//			}else if (system==1) {
//				lin_manger.setVisibility(View.VISIBLE);
//				lin_user_manger.setVisibility(View.VISIBLE);
//				lin_upmanger.setVisibility(View.GONE);
//			}else {
//				lin_manger.setVisibility(View.GONE);
//				lin_user_manger.setVisibility(View.GONE);
//				lin_upmanger.setVisibility(View.GONE);
//			}
			long downtime=0;
			if (lasttime!=0) {
				 downtime=lasttime+Long.valueOf(user.perDownTime==null?"0":user.perDownTime);
				tv_time.setText(new SimpleDateFormat("MM月dd日 HH:mm").format(new Date(downtime)));
			}else {
				long lastDownTime=Long.valueOf(user.lastDownTime==null?"0":user.lastDownTime);
				 downtime=lastDownTime+Long.valueOf(user.perDownTime==null?"0":user.perDownTime);
				if (lastDownTime==0 || downtime==0)
					downtime=System.currentTimeMillis();
				tv_time.setText(new SimpleDateFormat("MM月dd日 HH:mm").format(new Date(downtime)));
			}
			if (downtime!=0  && downtime-System.currentTimeMillis()>=6*60*1000) {
				Intent intent = new Intent(MainActivity.this, CleanContactReceiver.class);
		        PendingIntent sender=PendingIntent.getBroadcast( MainActivity.this,0, intent, 0);
		        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
	            am.set(AlarmManager.RTC_WAKEUP,downtime-5*60*1000,sender);
			}	
			boolean isalarm=getSharedPreferences("SYSTEMSET", MODE_PRIVATE).getBoolean("Alarm", true);
			if (isalarm) {
				if (downtime!=0  && downtime-System.currentTimeMillis()>=60*1000) {
					Intent intent = new Intent(MainActivity.this, DownAlarmReceiver.class);
					PendingIntent sender=PendingIntent.getBroadcast( MainActivity.this,0, intent, 0);
		            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		            am.set(AlarmManager.RTC_WAKEUP,downtime,sender);
				}	
			}
		}
	}
	
	
	
	
	private class DetalsContacts  extends AsyncTask<Integer, Integer,Integer> {
		private int sunnum=0;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog(MainActivity.this, "正在读取联系人......");
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			ArrayList<HXPhone> phonelists=(ArrayList<HXPhone>) ContactsUtil.ReadAll(MainActivity.this);
			if (phonelists!=null && phonelists.size()>=1) {
				sunnum=phonelists.size();
				for (int i = 0; i < sunnum; i++) {
					HXPhone hPhone=phonelists.get(i);
					try {
						ContactsUtil.DeleteContacts(MainActivity.this, hPhone.name);
					} catch (Exception e) {
						e.printStackTrace();
					}
					publishProgress(i);
				}
			}else {
				dismissloading();
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (values[0]==0) {
				dismissloading();
				showProgDialog(MainActivity.this, "正在删除通讯录", 0, sunnum);
			}else {
				upProgressDialog(values[0]);
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (progDialog!=null && progDialog.isShowing()) 
				progDialog.dismiss();
		}
		
	}
	
	private class downContacts extends AsyncTask<Integer, Integer,ArrayList<HXPhone>> {
		private int sunnum=0;
		@Override
		protected ArrayList<HXPhone> doInBackground(Integer... params) {
			ArrayList<HXPhone> phonelists=HXPhonePaser.GetInstance();
			if (phonelists!=null && phonelists.size()>=1) {
				sunnum=phonelists.size();
				for (int i = 0; i < sunnum; i++) {
					HXPhone hPhone=phonelists.get(i);
					ContactsUtil.AddContacts(MainActivity.this, hPhone.name, hPhone.phone);
					publishProgress(i);
				}
			}
			return phonelists;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (values[0]==0) {
				showProgDialog(MainActivity.this, "正在写入通讯录", values[0], sunnum);
			}else {
				upProgressDialog(values[0]);
			}
		}
		
		@Override
		protected void onPostExecute(ArrayList<HXPhone> result) {
			super.onPostExecute(result);
			if (progDialog!=null && progDialog.isShowing()) 
				progDialog.dismiss();
			if (result!=null && result.size()>=1) {
				int num=result.size();
				user.dayDownMun=String.valueOf(Integer.valueOf(user.dayDownMun==null?"0":user.dayDownMun)+num);
				SaveTools.upDateUser(user);
				Toast.makeText(MainActivity.this, "成功写入"+num+"条通讯录", Toast.LENGTH_SHORT).show();
				new initData(System.currentTimeMillis()).execute();
			}
			
		}
		
	}
	
	private void LoginOut(){
		startActivity(new Intent(MainActivity.this, LoginActivity.class));
		MainActivity.this.finish();
		getSharedPreferences("NameAndPsw",MODE_PRIVATE).edit().putString("psw", "").commit();
		SerializableFactory.DetailsData(null, 2);
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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			LoginOut();
		}
	}
	
	/**
     * 两秒内按两次返回键退出程序
     */
    private long exitTime =   0;
 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
            	MainActivity.this.finish();
            	ZLApplication.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }	
	
	private void showMsg() {
		View view = View.inflate(MainActivity.this, R.layout.dialog_forget,null);
		final MyDialog dialog = new MyDialog(MainActivity.this,R.style.CustomDialog);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width * 4 / 5,LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, dialoglp);
		dialog.setCancelable(true);
		dialog.show();
		TextView tv_tip =(TextView)view.findViewById(R.id.tv_tip);
		TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
		TextView tv_cacel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_tip.setText("您的联系人将被清空");
		tv_cacel.setText("取消");
		tv_sure.setBackgroundResource(R.drawable.red_roud_dialog_btn);
		tv_sure.setTextColor(Color.WHITE);
		tv_sure.setText("立即删除");
		tv_cacel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (dialog != null && dialog.isShowing())
					dialog.dismiss();
			}
		});
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (dialog != null && dialog.isShowing())
					dialog.dismiss();
				new DetalsContacts().execute();
			}
		});
	}
	
	@SuppressLint("SimpleDateFormat")
	private void openReferDataAlarm() {
		Intent intent = new Intent(MainActivity.this, CleanContactReceiver.class);
        PendingIntent sender=PendingIntent.getBroadcast( MainActivity.this,0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)  
        long systemTime = System.currentTimeMillis();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTimeInMillis(System.currentTimeMillis());  
        // 这里时区需要设置一下，不然会有8个小时的时间差  
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.SECOND, 0);  
        calendar.set(Calendar.MILLISECOND, 0);  
        // 选择的定时时间  
        long selectTime = calendar.getTimeInMillis();  
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始  
        if(systemTime > selectTime) {  
	        calendar.add(Calendar.DAY_OF_MONTH, 1);  
	        selectTime = calendar.getTimeInMillis();  
        }  
        // 计算现在时间到设定时间的时间差  
        long time = selectTime - systemTime;  
        firstTime += time;  
        // 进行闹铃注册  
        long TIME_INTERVAL=1000L * 60 * 60 * 24;
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,TIME_INTERVAL, sender);
	}
	
	private void  checkTime(){
		 Calendar calendar = Calendar.getInstance();  
	     calendar.setTimeInMillis(System.currentTimeMillis());  
	     // 这里时区需要设置一下，不然会有8个小时的时间差  
	     calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));  
	     int hour=calendar.get(Calendar.HOUR_OF_DAY);
	     if (hour<5) {
			Toast.makeText(MainActivity.this, "每天00:00-05:00暂停下载，请稍后下载", Toast.LENGTH_SHORT).show();
		}else {
			showLoadingDialog(MainActivity.this, "正在获取联系人......");
			RequestManger.PhoneDown(MainActivity.this,user.loginToken, user.id, new Listener<RequestCall>() {

				@Override
				public void onResponse(RequestCall response) {
					dismissloading();
					if (response.getParser().getResultSuccess()) {
						new downContacts().execute();
					}else if (response.getParser().getTokenerro()) {
						LoginOut();
					}else {
						if (response.getParser().getResponseMsg()!=null) 
							Toast.makeText(MainActivity.this, response.getParser().getResponseMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			}, errorListener);
		}
	}
	
	
}
