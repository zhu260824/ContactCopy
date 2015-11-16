package com.zhulin.contactcopy.paser;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;//
	public String account;// 账号
	public String name;// 姓名
	public String userRight;// 用户权限 0超级管理员 1管理员，2普通用户
	public String status;// 状态 0禁用，1启用
	public String dayMaxNum;// 每日的最大下载量
	public String dayDownMun;// 每日的已下载量
	public String perDownNum;// 每次下载数
	public String perDownTime;// 下载时间间隔
	public String lastDownTime;// 最后下载时间
	public String loginToken;// token
	public boolean selced=false;

}
