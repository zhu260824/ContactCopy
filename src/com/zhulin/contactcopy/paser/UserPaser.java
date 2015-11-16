package com.zhulin.contactcopy.paser;

import org.json.JSONObject;

import com.library.http.BaseParser;
import com.library.utils.JsonUtils;
import com.library.utils.SerializableFactory;

public class UserPaser extends BaseParser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static User instance=null;
	
	@Override
	public void parse(JSONObject obj) {
		super.parse(obj);
		try {
			if (getResultSuccess()) {
				if (getData() != null) {
					instance =(User) JsonUtils.fromJson(getData(),User.class);
					SerializableFactory.SaveData("UserData", getData());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			parseError("");
		}
	}
	
	/*
	 * 代码中如果需要获取到启动对象返回值时，通过此方法获取
	 */
	public static User GetInstance() {
		try {
			String jsonstring= SerializableFactory.GetData("UserData");
			if (jsonstring!=null) {
				instance =(User) JsonUtils.fromJson(jsonstring,User.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

}
