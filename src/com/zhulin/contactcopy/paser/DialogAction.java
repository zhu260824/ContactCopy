package com.zhulin.contactcopy.paser;

import java.io.Serializable;

public class DialogAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int actionid;
	private String actionname;

	public DialogAction(int actionid, String actionname) {
		super();
		this.actionid = actionid;
		this.actionname = actionname;
	}

	public int getActionid() {
		return actionid;
	}

	public void setActionid(int actionid) {
		this.actionid = actionid;
	}

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

}
