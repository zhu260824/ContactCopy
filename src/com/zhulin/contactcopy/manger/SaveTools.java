package com.zhulin.contactcopy.manger;

import android.util.Log;

import com.library.utils.JsonUtils;
import com.library.utils.SerializableFactory;
import com.zhulin.contactcopy.paser.User;

public class SaveTools {

	public static void upDateUser (User user){
		Log.i("userdata", JsonUtils.toJson(user));
		SerializableFactory.SaveData("UserData", JsonUtils.toJson(user));
	}
	
}
