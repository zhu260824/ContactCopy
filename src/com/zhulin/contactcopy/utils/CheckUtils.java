package com.zhulin.contactcopy.utils;

import com.zhulin.contactcopy.app.CApplication;

import android.content.Context;
import android.widget.EditText;

public class CheckUtils {

	public static String checkLogin(EditText name, EditText psw) {
		String erroMsg = "true";
		if (name == null || name.getText() == null) {
			erroMsg = "请输入姓名";
			return erroMsg;
		}
		if (psw == null || psw.getText() == null) {
			erroMsg = "请输入密码";
			return erroMsg;
		}
		String sname = name.getText().toString();
		String spsw = psw.getText().toString();
		if (sname==null || sname.length()<1) {
			erroMsg = "请输入姓名";
			return erroMsg;
		}
		if (spsw==null || spsw.length()<1) {
			erroMsg = "请输入密码";
			return erroMsg;
		}
		return erroMsg;
	}
	
	public static String checkRestPsw(EditText old, EditText newpsw ,EditText newagain) {
		String erroMsg = "true";
		if (old == null || old.getText() == null) {
			erroMsg = "请输入原密码";
			return erroMsg;
		}
		if (newpsw == null || newpsw.getText() == null) {
			erroMsg = "请输入新密码密码";
			return erroMsg;
		}
		if (newagain == null || newagain.getText() == null) {
			erroMsg = "请输入再次输入新密码";
			return erroMsg;
		}
		String sold = old.getText().toString();
		String snpsw = newpsw.getText().toString();
		String sanpsw = newagain.getText().toString();
		String mold=CApplication.getInstance().getSharedPreferences("NameAndPsw", Context.MODE_PRIVATE).getString("psw", "");
		if (sold==null || sold.length()<1) {
			erroMsg = "请输入原密码";
			return erroMsg;
		}
		if (snpsw==null || snpsw.length()<1) {
			erroMsg = "请输入新密码密码";
			return erroMsg;
		}
		if (sanpsw==null || sanpsw.length()<1) {
			erroMsg = "请输入再次输入新密码";
			return erroMsg;
		}
		if (mold!=null && mold.length()>1) {
			if (!sold.equals(mold)) {
				erroMsg = "您输入的原密码错误";
				return erroMsg;
			}
		}
		if (!snpsw.equals(sanpsw)) {
			erroMsg = "输入的新密码不一致";
			return erroMsg;
		}
		return erroMsg;
	}
}
