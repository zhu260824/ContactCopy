package com.zhulin.contactcopy.paser;

import java.io.Serializable;

public class HXPhone implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;//
	public String name;//
	public String phone;//
	public boolean checked=false;
	public HXPhone() {
		super();
	}
	public HXPhone(String id, String name, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
	}
}
