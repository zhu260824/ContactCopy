package com.zhulin.contactcopy.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.library.UMengCountUtils;
import com.library.app.ZLApplication;
import com.library.http.RequestCall;
import com.umeng.update.UmengUpdateAgent;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.activity.MainActivity;
import com.zhulin.contactcopy.activity.SettingActivity;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.utils.CheckUtils;

/**
 * Created by ZL on 2015/10/19.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button btn_login;
	private LinearLayout lin_rember;
	private ImageView iv_check;
	private EditText et_phone, et_psw;
	private TextView tv_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		UMengCountUtils.upDataOnLine(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		btn_login = (Button) findViewById(R.id.btn_login);
		lin_rember = (LinearLayout) findViewById(R.id.lin_rember);
		iv_check = (ImageView) findViewById(R.id.iv_check);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_psw = (EditText) findViewById(R.id.et_psw);
		tv_setting=(TextView) findViewById(R.id.tv_setting);
		tv_setting.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		lin_rember.setOnClickListener(this);
		iv_check.setTag(true);
		String name = getSharedPreferences("NameAndPsw", MODE_PRIVATE).getString("username", "");
		if (name != null && name.length() >= 1) {
			et_phone.setText(name);
			et_psw.setText(getSharedPreferences("NameAndPsw", MODE_PRIVATE).getString("psw", ""));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String checks = CheckUtils.checkLogin(et_phone, et_psw);
			if (checks.equals("true")) {
				showLoadingDialog(LoginActivity.this, "正在登陆中......");
				RequestManger.Login(LoginActivity.this, et_phone.getText().toString(), et_psw.getText().toString(),new Listener<RequestCall>() {

							@Override
							public void onResponse(RequestCall response) {
								dismissloading();
								if (response.getParser().getResponseMsg() != null)
									Toast.makeText(LoginActivity.this,response.getParser().getResponseMsg(),Toast.LENGTH_SHORT).show();
								if (response.getParser().getResultSuccess()) {
									if (iv_check != null
											&& iv_check.getTag() != null) {
										boolean checked = (Boolean) iv_check
												.getTag();
										if (checked) {
											getSharedPreferences("NameAndPsw",MODE_PRIVATE).edit().putString("username",et_phone.getText().toString()).putString("psw",et_psw.getText().toString()).commit();
										} else {
											getSharedPreferences("NameAndPsw",MODE_PRIVATE).edit().putString("psw", "").commit();
										}
									}
									startActivity(new Intent(LoginActivity.this,MainActivity.class));
									LoginActivity.this.finish();
								}
							}
						}, errorListener);
			} else {
				Toast.makeText(LoginActivity.this, checks, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.lin_rember:
			if (iv_check != null && iv_check.getTag() != null) {
				boolean checked = (Boolean) iv_check.getTag();
				if (checked) {
					iv_check.setImageResource(R.drawable.checkbox_off);
					iv_check.setTag(false);
				} else {
					iv_check.setImageResource(R.drawable.checkbox_on);
					iv_check.setTag(true);
				}
			}
			break;
		case R.id.tv_setting:
			startActivity(new Intent(LoginActivity.this,SettingActivity.class));
			break;
		default:
			break;
		}

	}

	/**
	 * 两秒内按两次返回键退出程序
	 */
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				LoginActivity.this.finish();
				ZLApplication.exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
