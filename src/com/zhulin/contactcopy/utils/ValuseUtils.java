package com.zhulin.contactcopy.utils;

import java.util.ArrayList;

public class ValuseUtils {
	
	public static ArrayList<String> StringToList(String resule){
		ArrayList<String> lists=new ArrayList<String>();
		String[] resules=resule.split(",");
		for (String string : resules) {
			lists.add(string);
		}
		return lists;
	}
	
	public static String ListToString(ArrayList<String> lists){
		String restule="";
		for (String string : lists) {
			restule+=string+",";
		}
		if (restule.length()>1) {
			restule=restule.substring(0, restule.length()-1);
		}
		return restule;
	}
}
