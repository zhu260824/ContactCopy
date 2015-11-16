package com.android.volley.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.library.http.BaseParser;
import com.library.http.RequestCall;
import com.library.utils.JsonUtils;
/**
 * @author ZL
 * 自定义网络请求
 * */
public class MyPostRequest extends Request<RequestCall>{
	private RequestCall call;
	private Listener<RequestCall> sListener;
	
	public MyPostRequest(Context context, RequestCall call,
			Listener<RequestCall> sListener,ErrorListener listener) {
		super(Request.Method.POST, call.getUrl(context), listener);
		Log.d("response", "url------>"+call.getUrl(context));
		Log.d("response", JsonUtils.toJson(call.getParms()));
		this.call = call;
		this.sListener = sListener;
	}
	
	/**
	 * 重复请求次数时间设置
	 * */
	@Override
	public RetryPolicy getRetryPolicy() {
		RetryPolicy retryPolicy = new DefaultRetryPolicy(1000*3, 0, 1.0f);  
		return retryPolicy; 
	}

	/**
	 * post请求添加参数
	 * */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return call.getParms();
	}

	/**
	 * 对请求的结果进行处理
	 * */
	@Override
	protected Response<RequestCall> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString =new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d("response", jsonString);
            if (call.getParser()==null) {
            	call.setParser(new BaseParser());
			}
            call.getParser().parse(new JSONObject(jsonString));
            return Response.success(call,HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
	}
	
	/**
	 * 设置请求成功回调
	 * */
	@Override
	protected void deliverResponse(RequestCall response) {
		sListener.onResponse(response);
	}
	
	
}
