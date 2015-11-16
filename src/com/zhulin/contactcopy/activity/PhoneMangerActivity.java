package com.zhulin.contactcopy.activity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.app.CApplication;
import com.zhulin.contactcopy.paser.HXPhone;
import com.zhulin.contactcopy.utils.ContactsUtil;
import com.zhulin.contactcopy.view.MyDialog;

public class PhoneMangerActivity extends BaseActivity implements OnClickListener{
	private ListView lv;
	private TextView tv_action,tv_progress;
	private Button btn_sure;
	private MyDialog progDialog;
	private ProgressBar pb;
	private ArrayList<HXPhone> phones;
	private PhoneMangerAdapter pMangerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manger);
		setTitle("联系人管理");
		goBack();
		lv = (ListView) findViewById(R.id.lv);
		tv_action = (TextView) findViewById(R.id.tv_action);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setText("删除");
		tv_action.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ImageView iv_check=(ImageView)view.findViewById(R.id.iv_check);
				if (iv_check.getVisibility()==View.VISIBLE) {
					if (iv_check!=null && iv_check.getTag()!=null) {
						HXPhone hxPhone=(HXPhone) iv_check.getTag();
						if (hxPhone.checked) {
							iv_check.setImageResource(R.drawable.checkbox_off);
							hxPhone.checked=false;
						}else {
							iv_check.setImageResource(R.drawable.checkbox_on);
							hxPhone.checked=true;
						}
					}
				}
			}
		});
		new loadingPhones().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_action:
			if (tv_action.getText()!=null) {
				String action=tv_action.getText().toString();
				if (action.equals("全选")) {
					if (phones!=null) {
						for (HXPhone hPhone : phones) {
							hPhone.checked=true;
						}
						pMangerAdapter.notifyDataSetChanged();
					}
					tv_action.setText("全不选");
				}else if (action.equals("全不选")) {
					if (phones!=null) {
						for (HXPhone hPhone : phones) {
							hPhone.checked=false;
						}
						pMangerAdapter.notifyDataSetChanged();
					}
					tv_action.setText("全选");
				}else {
					if (phones!=null) {
						for (HXPhone hPhone : phones) {
							hPhone.checked=false;
						}
						pMangerAdapter=new PhoneMangerAdapter(true, phones);
						lv.setAdapter(pMangerAdapter);
						tv_action.setText("全选");
						btn_sure.setVisibility(View.VISIBLE);
					}
				}
			}
			break;
		case R.id.btn_sure:
			showMsg();
			break;
		default:
			break;
		}
	}
	
	private class loadingPhones extends AsyncTask<Integer, Integer, ArrayList<HXPhone>>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog(PhoneMangerActivity.this, "正在读取联系人......");
		}

		@Override
		protected ArrayList<HXPhone> doInBackground(Integer... params) {
			phones=(ArrayList<HXPhone>) ContactsUtil.ReadAll(PhoneMangerActivity.this);
			return phones;
		}
		
		@Override
		protected void onPostExecute(ArrayList<HXPhone> result) {
			super.onPostExecute(result);
			dismissloading();
			pMangerAdapter=new PhoneMangerAdapter(false, phones);
			lv.setAdapter(pMangerAdapter);
		}
		
	}
	
	private class DetalsContacts  extends AsyncTask<Integer, Integer,Integer> {
		private int sunnum=0;
		@Override
		protected Integer doInBackground(Integer... params) {
			ArrayList<HXPhone> phonelists=new ArrayList<HXPhone>();
			for (HXPhone hPhone : phones) {
				if (hPhone.checked) {
					phonelists.add(hPhone);
				}
			}
			if (phonelists!=null && phonelists.size()>=1) {
				sunnum=phonelists.size();
				for (int i = 0; i < sunnum; i++) {
					HXPhone hPhone=phonelists.get(i);
					try {
						ContactsUtil.DeleteContacts(PhoneMangerActivity.this, hPhone.name);
						phones.remove(hPhone);
					} catch (Exception e) {
						e.printStackTrace();
					}
					publishProgress(i);
				}
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (values[0]==0) {
				showProgDialog(PhoneMangerActivity.this, "正在删除通讯录", 0, sunnum);
			}else {
				upProgressDialog(values[0]);
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (progDialog!=null && progDialog.isShowing()) 
				progDialog.dismiss();
			for (HXPhone hPhone : phones) {
				hPhone.checked=false;
			}
			pMangerAdapter=new PhoneMangerAdapter(false, phones);
			lv.setAdapter(pMangerAdapter);
			tv_action.setText("操作");
			btn_sure.setVisibility(View.GONE);
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
	
	private void showMsg() {
		View view = View.inflate(PhoneMangerActivity.this, R.layout.dialog_forget,null);
		final MyDialog dialog = new MyDialog(PhoneMangerActivity.this,R.style.CustomDialog);
		LinearLayout.LayoutParams dialoglp = new LinearLayout.LayoutParams(CApplication.Width * 4 / 5,LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.addContentView(view, dialoglp);
		dialog.setCancelable(true);
		dialog.show();
		TextView tv_tip =(TextView)view.findViewById(R.id.tv_tip);
		TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
		TextView tv_cacel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_tip.setText("您选择的联系人将被删除");
		tv_cacel.setText("取消");
		tv_sure.setBackgroundResource(R.drawable.red_roud_dialog_btn);
		tv_sure.setTextColor(Color.WHITE);
		tv_sure.setText("立即删除");
		tv_cacel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (dialog != null && dialog.isShowing())
					dialog.dismiss();
				for (HXPhone hPhone : phones) {
					hPhone.checked=false;
				}
				pMangerAdapter=new PhoneMangerAdapter(false, phones);
				lv.setAdapter(pMangerAdapter);
				tv_action.setText("操作");
				btn_sure.setVisibility(View.GONE);
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
	
}
