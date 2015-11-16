package com.zhulin.contactcopy.activity.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.library.http.RequestCall;
import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.app.BaseActivity;
import com.zhulin.contactcopy.manger.RequestManger;
import com.zhulin.contactcopy.paser.User;
import com.zhulin.contactcopy.paser.UserPaser;
import com.zhulin.contactcopy.utils.CheckUtils;

/**
 * Created by ZL on 2015/10/19.
 */
public class RestPassWorldActivity extends BaseActivity {
	private EditText et_old,et_new,et_newagain;
	private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_passworld);
        goBack();
        et_old=(EditText) findViewById(R.id.et_old);
        et_new=(EditText) findViewById(R.id.et_new);
        et_newagain=(EditText) findViewById(R.id.et_newagain);
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String check=CheckUtils.checkRestPsw(et_old, et_new, et_newagain);
				if (check.equals("true")) {
					User user= UserPaser.GetInstance();
					RequestManger.RestPassWorld(RestPassWorldActivity.this, user.loginToken, user.id, et_new.getText().toString(), new Listener<RequestCall>() {

						@Override
						public void onResponse(RequestCall response) {
							if (response.getParser().getResponseMsg()!=null) 
								Toast.makeText(RestPassWorldActivity.this, response.getParser().getResponseMsg(), Toast.LENGTH_SHORT).show();
							if (response.getParser().getTokenerro()) 
								 Toast.makeText(RestPassWorldActivity.this, "您的账号在其他设备登录,您被迫下线", Toast.LENGTH_SHORT).show();
							RestPassWorldActivity.this.finish();
						}
					}, errorListener);
				}else {
					Toast.makeText(RestPassWorldActivity.this, check, Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
}
