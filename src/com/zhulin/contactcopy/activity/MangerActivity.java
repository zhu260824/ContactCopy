package com.zhulin.contactcopy.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.library.http.RequestCall;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.app.CApplication;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.paser.DialogAction;
import com.zhulin.contactcopy.paser.MUserListPaser;
import com.zhulin.contactcopy.paser.PUserListPaser;
import com.zhulin.contactcopy.paser.User;
import com.zhulin.contactcopy.paser.UserPaser;
import com.zhulin.contactcopy.utils.ValuseUtils;
import com.zhulin.contactcopy.view.MyDialog;

public class MangerActivity extends BaseActivity implements OnClickListener {
	private ListView lv;
	private MangerAdapter mAadapter;
	private TextView tv_action;
	private Button btn_sure;
	private int actionType=1;
	private String changeNumber="1";
	private ArrayList<User> userLists;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manger);
		setTitle("用户管理");
		goBack();
		lv = (ListView) findViewById(R.id.lv);
		tv_action = (TextView) findViewById(R.id.tv_action);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		tv_action.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (position==0) {
					return;
				}
				ImageView iv_check=(ImageView) view.findViewById(R.id.iv_check);
				if (iv_check!=null && iv_check.getVisibility()==View.VISIBLE) {
					TextView tv_name=(TextView) view.findViewById(R.id.tv_name);
					if (tv_name!=null && tv_name.getTag()!=null) {
						User user=(User) tv_name.getTag();
						if (user!=null) {
							if (user.selced) {
								iv_check.setImageResource(R.drawable.checkbox_off);
								user.selced=false;
							}else {
								iv_check.setImageResource(R.drawable.checkbox_on);
								user.selced=true;
							}
							mAadapter.notifyDataSetChanged();
						}
					}
				}
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				if (position!=0) {
					TextView tv_name=(TextView) view.findViewById(R.id.tv_name);
					if (tv_name!=null && tv_name.getTag()!=null) {
						((User) tv_name.getTag()).selced=true;
						showAction(true);
						return true;
					}
				}
				return false;
			}
		});
		new initData().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_action:
			showAction(false); 
			break;
		case R.id.btn_sure:
			new upUserMsg().execute();
			break;
		default:
			break;
		}

	}

	private void showAction(final boolean isOne){
		View view = View.inflate(MangerActivity.this, R.layout.dialog_action,null);
		final MyDialog dialog = new MyDialog(MangerActivity.this,R.style.loading_dialog);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width * 4 / 5,LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, dialoglp);
		ListView lv_action=(ListView) view.findViewById(R.id.lv_action);
		DialogAdapter dAdapter=new DialogAdapter(getActions());
		lv_action.setAdapter(dAdapter);
		dialog.setCancelable(true);
		dialog.show();
		lv_action.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				TextView tv_action_name=(TextView) view.findViewById(R.id.tv_action_name);
				if (tv_action_name!=null && tv_action_name.getTag()!=null) {
					String actionId=tv_action_name.getTag().toString();
					if (actionId!=null) {
						actionType=Integer.valueOf(actionId);
						switch (actionType) {
						case 1:
						case 2:
						case 3:
							if (dialog!=null || dialog.isShowing()) 
								dialog.dismiss();
							showSetNumber(isOne);
							break;
						case 4:
							if (dialog!=null || dialog.isShowing()) 
								dialog.dismiss();
							showUserStatus(isOne);
							break;
						case 5:
							if (dialog!=null || dialog.isShowing()) 
								dialog.dismiss();
							if (isOne) {
								new upUserMsg().execute();
							}else {
								listToChecked();
							}
							break;

						default:
							break;
						}
					}
				}
				
			}
		});
	}
	
	
	private void showSetNumber(final boolean isOne){
		View view = View.inflate(MangerActivity.this, R.layout.dialog_inputnumber,null);
		final MyDialog dialog = new MyDialog(MangerActivity.this,R.style.loading_dialog);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width * 4 / 5,LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, dialoglp);
		final EditText et_number =(EditText) view.findViewById(R.id.et_number);
		TextView tv_sure=(TextView) view.findViewById(R.id.tv_sure);
		TextView tv_tip=(TextView) view.findViewById(R.id.tv_tip);
		switch (actionType) {
		case 1:
			tv_tip.setText("请输入您需要设置的总下载量");
			break;
		case 2:
			tv_tip.setText("请输入您需要设置的单次下载量");	
			break;
		case 3:
			tv_tip.setText("请输入您需要设置的时间间隔（分钟）");
			break;
		default:
			break;
		}
		dialog.setCancelable(true);
		dialog.show();
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (et_number.getText()!=null) {
					changeNumber=et_number.getText().toString();
					if (changeNumber!=null) {
						if (dialog!=null || dialog.isShowing()) 
							dialog.dismiss();
						if (isOne) {
							new upUserMsg().execute();
						}else {
							listToChecked();
						}
					}else {
						Toast.makeText(MangerActivity.this,"请输入需要修改数量", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(MangerActivity.this,"请输入需要修改数量", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void showUserStatus(final boolean isOne){
		View view = View.inflate(MangerActivity.this, R.layout.dialog_action,null);
		final MyDialog dialog = new MyDialog(MangerActivity.this,R.style.loading_dialog);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width * 4 / 5,LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, dialoglp);
		ListView lv_action=(ListView) view.findViewById(R.id.lv_action);
		ArrayList<DialogAction> actions=new ArrayList<DialogAction>();
		DialogAction daAction1=new DialogAction(7, "禁用");
		DialogAction daAction2=new DialogAction(8, "正常");
		DialogAction daAction3=new DialogAction(9, "删除");
		actions.add(daAction1);actions.add(daAction2);actions.add(daAction3);
		DialogAdapter dAdapter=new DialogAdapter(actions);
		lv_action.setAdapter(dAdapter);
		dialog.setCancelable(true);
		dialog.show();
		lv_action.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				TextView tv_action_name=(TextView) view.findViewById(R.id.tv_action_name);
				if (tv_action_name!=null && tv_action_name.getTag()!=null) {
					String actionId=tv_action_name.getTag().toString();
					if (actionId!=null) {
						actionType=Integer.valueOf(actionId);
						if (dialog!=null || dialog.isShowing()) 
							dialog.dismiss();
						if (isOne) {
							new upUserMsg().execute();
						}else {
							listToChecked();
						}
					}
				}
			}
		});
	}
	
	
	private ArrayList<DialogAction> getActions(){
		ArrayList<DialogAction> actions=new ArrayList<DialogAction>();
		DialogAction daAction1=new DialogAction(1, "设置用户每日最大下载量");
		DialogAction daAction2=new DialogAction(2, "设置用户每次下载量");
		DialogAction daAction3=new DialogAction(3, "设置用户下载时间间隔");
		DialogAction daAction4=new DialogAction(4, "设置用户账号状态");
		actions.add(daAction1);actions.add(daAction2);actions.add(daAction3);
		actions.add(daAction4);
		String system=user.userRight==null?"1":user.userRight;
		if (system.equals("0")) {
			DialogAction daAction5=new DialogAction(5, "设置用户为普通用户");
			actions.add(daAction5);
		}
		return actions;
	}
	
	private void listToChecked(){
		if (userLists!=null && userLists.size()>=1) {
			for (User puser : userLists) {
				puser.selced=false;
			}
		}
		mAadapter = new MangerAdapter(true,userLists);
		lv.setAdapter(mAadapter);
		btn_sure.setVisibility(View.VISIBLE);;
		tv_action.setVisibility(View.GONE);
		Toast.makeText(MangerActivity.this, "请勾选需要设置的用户，然后点击确认", Toast.LENGTH_SHORT).show();
	}
	
	private class upUserMsg extends AsyncTask<Integer, Integer, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog(MangerActivity.this, "正在读取用户列表......");
		}

		@Override
		protected String doInBackground(Integer... params) {
			ArrayList<String> ids=new ArrayList<String>();
			for (int i = 0; i < userLists.size(); i++) {
				if (userLists.get(i).selced) {
					ids.add(userLists.get(i).id);
				}
			}
			return ValuseUtils.ListToString(ids);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			int action=1;String data="1";
			switch (actionType) {
			case 1:
				action=1;
				data=changeNumber;
				RequestManger.UpUserData(MangerActivity.this, user.loginToken, user.id, result, action, data, listener, errorListener);
				break;
			case 2:
				action=3;
				data=changeNumber;		
				RequestManger.UpUserData(MangerActivity.this, user.loginToken, user.id, result, action, data, listener, errorListener);
				break;
			case 3:
				action=2;
				data=String.valueOf((Integer.valueOf(changeNumber))*60*1000);
				RequestManger.UpUserData(MangerActivity.this, user.loginToken, user.id, result, action, data, listener, errorListener);
				break;
			case 5:
				action=5;
				data="2";
				RequestManger.UpUserData(MangerActivity.this, user.loginToken, user.id, result, action, data, listener, errorListener);
				break;
			case 7:
				action=4;
				data="0";
				RequestManger.UpUserData(MangerActivity.this, user.loginToken, user.id, result, action, data, listener, errorListener);
				break;
			case 8:
				action=4;
				data="1";
				RequestManger.UpUserData(MangerActivity.this, user.loginToken, user.id, result, action, data, listener, errorListener);
				break;
			case 9:
			RequestManger.DeleteUser(MangerActivity.this, user.loginToken, user.id, result,listener,errorListener);
				break;
			default:
				break;
			}
		}
	}

	
	private Listener<RequestCall> listener=new Listener<RequestCall>() {

		@Override
		public void onResponse(RequestCall response) {
			dismissloading();
			if (response.getParser().getResultSuccess()) {
				new initData().execute();
			}else if (response.getParser().getTokenerro()) {
				 Toast.makeText(MangerActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
				Intent backdata=new Intent();
				setResult(RESULT_OK,backdata);
				MangerActivity.this.finish();
			}
		}
	};
	
	
	private class initData extends AsyncTask<Integer, Integer, User>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog(MangerActivity.this, "正在读取用户列表......");
		}
		@Override
		protected User doInBackground(Integer... params) {
			user=UserPaser.GetInstance();
			return user;
		}
		
		@Override
		protected void onPostExecute(User reult) {
			super.onPostExecute(reult);
			String system=user.userRight==null?"1":user.userRight;
			if (system.equals("0")) {
				RequestManger.MUserList(MangerActivity.this,user.loginToken, user.id, new Listener<RequestCall>() {

					@Override
					public void onResponse(RequestCall response) {
						dismissloading();
						if (response.getParser().getResultSuccess()) {
							userLists=MUserListPaser.GetInstance();
							mAadapter = new MangerAdapter(false,userLists);
							lv.setAdapter(mAadapter);
						}else if (response.getParser().getTokenerro()) {
							 Toast.makeText(MangerActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
							Intent backdata=new Intent();
							setResult(RESULT_OK,backdata);
							MangerActivity.this.finish();
						}
					}
				}, errorListener);
			}else {
				RequestManger.PUserList(MangerActivity.this, user.loginToken, user.id, new Listener<RequestCall>() {

					@Override
					public void onResponse(RequestCall response) {
						dismissloading();
						if (response.getParser().getResultSuccess()) {
							userLists=PUserListPaser.GetInstance();
							mAadapter = new MangerAdapter(false,userLists);
							lv.setAdapter(mAadapter);
						}else if (response.getParser().getTokenerro()) {
							 Toast.makeText(MangerActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
							Intent backdata=new Intent();
							setResult(RESULT_OK,backdata);
							MangerActivity.this.finish();
						}
					}
				}, errorListener);
			}
		}
	}
}
