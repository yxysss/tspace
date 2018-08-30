package com.tspace.bean;

import net.sf.json.JSONObject;

public class Admin {
	
	private Integer idadmin; 
	
	private String adminname;
	
	private String password;
	
	public void setadminname(String adminname) {
		this.adminname = adminname;
	}
	
	public void setpassword(String password) {
		this.password = password;
	}
	
	public String getadminname() {
		return adminname;
	}
	
	public String getpassword() {
		return password;
	}

	/**
	 * @return the idadmin
	 */
	public Integer getidadmin() {
		return idadmin;
	}

	/**
	 * @param idadmin the idadmin to set
	 */
	public void setidadmin(Integer idadmin) {
		this.idadmin = idadmin;
	}
	
	public String toString() {
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("idadmin", idadmin);
		jsonobject.put("adminname", adminname);
		jsonobject.put("password", password);
		return jsonobject.toString();
	}

}

