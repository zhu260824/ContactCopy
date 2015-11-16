package com.library.http;

import java.io.Serializable;
import java.util.HashMap;

import com.library.utils.AppUtils;

import android.content.Context;
/**
 * @author ZL
 * 请求参数
 * */

public class RequestCall implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String UrlAction;
	private HashMap<String, String> parms;
	private BaseParser Parser;
	
	
	public RequestCall(String urlAction, HashMap<String, String> parms,
			BaseParser parser) {
		super();
		UrlAction = urlAction;
		this.parms = parms;
		Parser = parser;
	}

	public String getUrl(Context context){
		return AppUtils.getMetaValue(context,"APP_URL")+UrlAction;
	}
	
	public String getUrlAction() {
		return UrlAction;
	}
	public void setUrlAction(String urlAction) {
		UrlAction = urlAction;
	}
	public HashMap<String, String> getParms() {
		return parms;
	}
	public void setParms(HashMap<String, String> parms) {
		this.parms = parms;
	}

	public BaseParser getParser() {
		return Parser;
	}

	public void setParser(BaseParser parser) {
		Parser = parser;
	}
	
}
