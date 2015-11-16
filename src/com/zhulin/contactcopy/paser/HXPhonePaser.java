package com.zhulin.contactcopy.paser;

import java.util.ArrayList;

import org.json.JSONObject;

import com.library.http.BaseParser;
import com.library.utils.JsonUtils;
import com.library.utils.SerializableFactory;

public class HXPhonePaser extends BaseParser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<HXPhone> instance=null;
	
	@Override
	public void parse(JSONObject obj) {
		super.parse(obj);
		try {
			if (getResultSuccess()) {
				if (getData() != null) {
					instance =(ArrayList<HXPhone>) JsonUtils.json2List(getData(),HXPhone.class);
					SerializableFactory.SaveData("HXPhoneList", getData());
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
	public static ArrayList<HXPhone> GetInstance() {
		try {
			String jsonstring= SerializableFactory.GetData("HXPhoneList");
			if (jsonstring!=null) {
				instance =(ArrayList<HXPhone>) JsonUtils.json2List(jsonstring,HXPhone.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

}
