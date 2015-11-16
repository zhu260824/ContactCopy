package com.zhulin.contactcopy.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.library.http.RequestCall;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.paser.PUserListPaser;
import com.zhulin.contactcopy.paser.User;
import com.zhulin.contactcopy.paser.UserPaser;
import com.zhulin.contactcopy.utils.ValuseUtils;

public class UpToMangerActivity extends BaseActivity implements OnClickListener{
	private ListView lv;
	private MangerAdapter mAadapter;
	private TextView tv_action;
	private Button btn_sure;
	private ArrayList<User> pUserLists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manger);
		setTitle("提拔管理员");
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
		new initData().execute();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_action:
			mAadapter = new MangerAdapter(true,pUserLists);
			lv.setAdapter(mAadapter);
			btn_sure.setVisibility(View.VISIBLE);
			tv_action.setVisibility(View.GONE);
			break;
		case R.id.btn_sure:
			new upToManger().execute();
			break;
		default:
			break;
		}

	}
	
	private class upToManger  extends AsyncTask<Integer, Integer, User>{
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog(UpToMangerActivity.this, "正在上传操作......");
		}

		@Override
		protected User doInBackground(Integer... params) {
			return UserPaser.GetInstance();
		}
		
		@Override
		protected void onPostExecute(User user) {
			super.onPostExecute(user);
			ArrayList<String> idlist=new ArrayList<String>();
			for (int i = 0; i < pUserLists.size(); i++) {
				if (pUserLists.get(i).selced) {
					idlist.add(pUserLists.get(i).id);
				}
			}
			RequestManger.UpUserData(UpToMangerActivity.this, user.loginToken, user.id, ValuseUtils.ListToString(idlist), 5, "1",new Listener<RequestCall>() {

				@Override
				public void onResponse(RequestCall response) {
					dismissloading();
					if (response.getParser().getResponseMsg()!=null)
						Toast.makeText(UpToMangerActivity.this, response.getParser().getResponseMsg(), Toast.LENGTH_SHORT).show();
					if (response.getParser().getResultSuccess()) {
						mAadapter = new MangerAdapter(false,pUserLists);
						lv.setAdapter(mAadapter);
						btn_sure.setVisibility(View.GONE);
						tv_action.setVisibility(View.VISIBLE);
					}else if (response.getParser().getTokenerro()) {
						 Toast.makeText(UpToMangerActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
						Intent backdata=new Intent();
						setResult(RESULT_OK,backdata);
						UpToMangerActivity.this.finish();
					}
				}
			}, errorListener);
		}
	}
	
	private class initData extends AsyncTask<Integer, Integer, User>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog(UpToMangerActivity.this, "正在读取用户列表......");
		}

		@Override
		protected User doInBackground(Integer... params) {
			return UserPaser.GetInstance();
		}
		
		@Override
		protected void onPostExecute(User user) {
			super.onPostExecute(user);
			RequestManger.PUserList(UpToMangerActivity.this, user.loginToken, user.id, new Listener<RequestCall>() {

				@Override
				public void onResponse(RequestCall response) {
					dismissloading();
					if (response.getParser().getResultSuccess()) {
						pUserLists=PUserListPaser.GetInstance();
						mAadapter = new MangerAdapter(false,pUserLists);
						lv.setAdapter(mAadapter);
					}else if (response.getParser().getTokenerro()) {
						 Toast.makeText(UpToMangerActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
						Intent backdata=new Intent();
						setResult(RESULT_OK,backdata);
						UpToMangerActivity.this.finish();
					}
				}
			}, errorListener);
		}
	}
}
