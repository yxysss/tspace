package com.tspace.bean;

import java.util.Date;

import com.tspace.listener.TimerTask;

import net.sf.json.JSONObject;

public class Application implements Comparable<Application> {

	private Integer idapplication;
	private int idroom;
	private Date starttime;
	private Date endtime;
	private String applicant;
	private String reason;
	private String participants;
	
	// accept: 接受
	// decline: 拒绝
	// unsettle: 未审核
	private String state;
	
	private Date applytime;
	
	private Date audittime; 
	
	private String password;
	
	public Integer getidapplication() {
		return idapplication;
	}
	
	public int getidroom() {
		return idroom;
	}
	
	public Date getstarttime() {
		return starttime;
	}
	
	public Date getendtime() {
		return endtime;
	}
	
	public String getapplicant() {
		return applicant;
	}
	
	public String getreason() {
		return reason;
	}
	
	public String getparticipants() {
		return participants;
	}
	
	public String getstate() {
		return state;
	}
	
	public Date getapplytime() {
		return applytime;
	}
	
	public Date getaudittime() {
		return audittime;
	}
	
	public String getpassword() {
		return password;
	}
	
	public void setidapplication(Integer idapplication) {
		this.idapplication = idapplication;
	}
	
	public void setidroom(int idroom) {
		this.idroom = idroom;
	}
	
	public void setstarttime(Date starttime) {
		this.starttime = starttime;
	}
	
	public void setendtime(Date endtime) {
		this.endtime = endtime;
	}
	
	public void setapplicant(String applicant) {
		this.applicant = applicant;	
	}
	
	public void setreason(String reason) {
		this.reason = reason;
	}
	
	public void setparticipants(String participants) {
		this.participants = participants;
	}
	
	public void setstate(String state) {
		this.state = state;
	}
	
	public void setapplytime(Date applytime) {
		this.applytime = applytime;
	}
	
	public void setaudittime(Date audittime) {
		this.audittime = audittime;
	}
	
	public void setpassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("idapplication", idapplication);
		jsonobject.put("idroom", idroom);
		jsonobject.put("starttime", TimerTask.dateToString(starttime));
		jsonobject.put("endtime", TimerTask.dateToString(endtime));
		jsonobject.put("applicant", applicant);
		jsonobject.put("reason", reason);
		jsonobject.put("participants", participants);
		jsonobject.put("state", state);
		jsonobject.put("applytime", TimerTask.dateToString(applytime));
		if (audittime != null) jsonobject.put("audittime", TimerTask.dateToString(audittime));
		else jsonobject.put("audittime", null);
		jsonobject.put("password", password);
		return jsonobject.toString();
	}
	
	@Override
	public int compareTo(Application application) {
		if (idroom < application.getidroom()) return 1;
		if (idroom > application.getidroom()) return -1;
		if (starttime.getTime() < application.getstarttime().getTime()) return 1;
		if (starttime.getTime() > application.getstarttime().getTime()) return -1;
		if (endtime.getTime() < application.getendtime().getTime()) return 1;
		if (endtime.getTime() > application.getendtime().getTime()) return -1;
		if (applytime.getTime() < application.getapplytime().getTime()) return 1;
		return -1;
	}
	
}
