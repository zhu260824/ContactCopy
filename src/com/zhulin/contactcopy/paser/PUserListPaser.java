package com.zhulin.contactcopy.paser;

import java.util.ArrayList;

import org.json.JSONObject;

import com.library.http.BaseParser;
import com.library.utils.JsonUtils;
import com.library.utils.SerializableFactory;

public class PUserListPaser extends BaseParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ArrayList<User> instance = null;

	@Override
	public void parse(JSONObject obj) {
		super.parse(obj);
		try {
			if (getResultSuccess()) {
				if (getData() != null) {
					instance = (ArrayList<User>) JsonUtils.json2List(getData(), User.class);
					SerializableFactory.SaveData("PUserList", getData());
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
	public static ArrayList<User> GetInstance() {
		try {
			String jsonstring = SerializableFactory.GetData("PUserList");
			if (jsonstring != null) {
				instance = (ArrayList<User>) JsonUtils.json2List(jsonstring,User.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

}
