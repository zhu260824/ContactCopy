package com.zhulin.contactcopy.manger;

import java.util.HashMap;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.MyPostRequest;
import com.android.volley.toolbox.Volley;
import com.library.http.BaseParser;
import com.library.http.RequestCall;
import com.zhulin.contactcopy.paser.HXPhonePaser;
import com.zhulin.contactcopy.paser.MUserListPaser;
import com.zhulin.contactcopy.paser.PUserListPaser;
import com.zhulin.contactcopy.paser.UserPaser;

public class RequestManger {

	public static void Login(Context context,String username,String password,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("username", username);
		parms.put("password", password);
		RequestCall call = new RequestCall(UrlManger.LOGIN, parms,new UserPaser());
		PostRequest(context, call, listener, errorListener);
	}
	
	public static void LoginOut(Context context,String token,String userid,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		RequestCall call = new RequestCall(UrlManger.LOGINOUT, parms,new BaseParser());
		PostRequest(context, call, listener, errorListener);
	}
	
	public static void PhoneDown(Context context,String token,String userid,String reqid,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		parms.put("reqid", reqid);
		RequestCall call = new RequestCall(UrlManger.PHONEDOWN, parms,new HXPhonePaser());
		PostRequest(context, call, listener, errorListener);
	}
	
	public static void PhoneDownCall(Context context,String token,String userid,String reqid,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		parms.put("reqid", reqid);
		RequestCall call = new RequestCall(UrlManger.DOWNCALL, parms,new BaseParser());
		PostRequest(context, call, listener, errorListener);
	}
	
	public static void PUserList(Context context,String token,String userid,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		RequestCall call = new RequestCall(UrlManger.PUSERLIST, parms,new PUserListPaser());
		PostRequest(context, call, listener, errorListener);
	}
	public static void MUserList(Context context,String token,String userid,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		RequestCall call = new RequestCall(UrlManger.MUSERLIST, parms,new MUserListPaser());
		PostRequest(context, call, listener, errorListener);
	}
	public static void RestPassWorld(Context context,String token,String userid,String  password,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		parms.put("password", password);
		RequestCall call = new RequestCall(UrlManger.RESTPSW, parms,new MUserListPaser());
		PostRequest(context, call, listener, errorListener);
	}
	public static void DeleteUser(Context context,String token,String userid,String  upuserid,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		parms.put("upuserid", upuserid);
		RequestCall call = new RequestCall(UrlManger.DELETEUSER, parms,new MUserListPaser());
		PostRequest(context, call, listener, errorListener);
	}
	/**
	 * upuserid  修改用户的id （多条以,分割）
	 * dayMaxNum，修改用户的每日的最大下载量    action=1
	 * perDownTime修改用户的下载时间间隔  action=2
	 * perDownNum修改用户的每次下载数  action=3
	 * status修改用户的状态，0禁用，1启用  action=4
	 * userRight修改用户权限，1管理员，2普通用户  action=5
	 * 
	 * */
	public static void UpUserData(Context context,String token,String userid,String upuserid,int action,String data,Listener<RequestCall> listener, ErrorListener errorListener){
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		parms.put("userid", userid);
		parms.put("upuserid", upuserid);
		switch (action) {
		case 1:
			parms.put("dayMaxNum", data);
			break;
		case 2:
			parms.put("perDownTime", data);
			break;
		case 3:
			parms.put("perDownNum", data);
			break;
		case 4:
			parms.put("status", data);
			break;
		case 5:
			parms.put("userRight", data);
			break;
		default:
			break;
		}
		RequestCall call = new RequestCall(UrlManger.UPUSERS, parms,new BaseParser());
		PostRequest(context, call, listener, errorListener);
	}
	
	/**
	 * post发起请求
	 * */
	private static void PostRequest(Context context, RequestCall call,Listener<RequestCall> listener, ErrorListener errorListener) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		MyPostRequest mPostRequest = new MyPostRequest(context, call, listener,errorListener);
		requestQueue.add(mPostRequest);
	}
}
