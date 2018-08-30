package com.tspace.bean;

import net.sf.json.JSONObject;

public class User {
	
	private Integer iduser;
	
	private String username;
	
	private String mobile;
	
	private String identification;
	
	private String name;
	
	private String password;
	
	private int school;
	
	// 0表示学生，1表示老师
	private int identity;
	
	public Integer getiduser() {
		return iduser;
	}
	
	public String getusername() {
		return username; 
	}
	
	public String getmobile() {
		return mobile;
	}
	
	public String getidentification() {
		return identification;
	}
	
	public String getname() {
		return name;
	}
	
	public String getpassword() {
		return password;
	}
	
	public int getschool() {
		return school;
	}
	
	public int getidentity() {
		return identity;
	}
	
	public void setiduser(Integer iduser) {
		this.iduser = iduser;
	}
	
	public void setusername(String username) {
		this.username = username;
	}
	
	public void setmobile(String mobile) {
		this.mobile = mobile;
	}
	
	public void setidentification(String identification) {
		this.identification = identification;
	}
	
	public void setname(String name) {
		this.name = name;
	}
	
	public void setpassword(String password) {
		this.password = password;
	}
	
	public void setschool(int school) {
		this.school = school;
	}
	
	public void setidentity(int identity) {
		this.identity = identity;
	}
	
	public String toString() {
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("iduser", iduser);
		jsonobject.put("username", username);
		jsonobject.put("mobile", mobile);
		jsonobject.put("identification", identification);
		jsonobject.put("name", name);
		jsonobject.put("password", password);
		jsonobject.put("identity", identity);
		return jsonobject.toString();
	}

}
