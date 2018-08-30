package com.tspace.bean;

import java.util.Date;

import com.tspace.listener.TimerTask;

import net.sf.json.JSONObject;

public class Room {
	
	// 房间的id（唯一）
	private int idroom;
	
	// 房间名称（唯一）
	private String nameroom;
	
	// 房间地址（唯一）
	private String addressroom;
	
	// 房间所属学校
	private int schoolroom;
	
	// 房间最大容量
	private int capacityroom;
	
	// 房间类型
	private String typeroom;
	
	// 房间描述
	private String descriptionroom;
	
	// 房间图片地址
	private String pictureroom;
	
	// 房间未来一周的预约情况
	// 未来一周，从早上7点30到晚上10点30，共30*7=210个时间段
	private String availableroom;
	
	// 房间今日的使用情况
	// 从早上7点30到晚上10点30，共30个时间段
	private String todayroom;
	
	// 房间今日的日期
	private Date todaydate;
	
	public int getidroom() {
		return idroom;
	}
	
	public String getnameroom() {
		return nameroom;
	}
	
	public String getaddressroom() {
		return addressroom;
	}
	
	public int getschoolroom() {
		return schoolroom;
	}
	
	public int getcapacityroom() {
		return capacityroom;
	}
	
	public String gettyperoom() {
		return typeroom;
	}
	
	public String getdescriptionroom() {
		return descriptionroom;
	}
	
	public String getpictureroom() {
		return pictureroom;
	}
	
	public String getavailableroom() {
		return availableroom;
	}
	
	public String gettodayroom() {
		return todayroom;
	}
	
	public Date gettodaydate() {
		return todaydate;
	}
	
	public void setidroom(int idroom) {
		this.idroom = idroom;
	}
	
	public void setnameroom(String nameroom) {
		this.nameroom = nameroom;
	}

	public void setschoolroom(int schoolroom) {
		this.schoolroom = schoolroom;
	}
	
	public void setcapacityroom(int capacityroom) {
		this.capacityroom = capacityroom;
	}
	
	public void settyperoom(String typeroom) {
		this.typeroom = typeroom;
	}
	
	public void setaddressroom(String addressroom) {
		this.addressroom = addressroom;
	}
	
	public void setdescriptionroom(String descriptionroom) {
		this.descriptionroom = descriptionroom;
	}
	
	public void setpictureroom(String pictureroom) {
		this.pictureroom = pictureroom;
	}
	
	public void setavailableroom(String availableroom) {
		this.availableroom = availableroom;
	}
	
	public void settodayroom(String todayroom) {
		this.todayroom = todayroom;
	}
	
	public void settodaydate(Date todaydate) {
		this.todaydate = todaydate;
	}
	
	public String toString() {
		
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("idroom", idroom);
		jsonobject.put("nameroom", nameroom);
		jsonobject.put("capacityroom", capacityroom);
		jsonobject.put("schoolroom", schoolroom);
		jsonobject.put("typeroom", typeroom);
		jsonobject.put("addressroom", addressroom);
		jsonobject.put("descriptionroom", descriptionroom);
		jsonobject.put("pictureroom", pictureroom);
		jsonobject.put("availableroom", availableroom);
		jsonobject.put("todayroom", todayroom);
		if (todaydate != null) jsonobject.put("todaydate", TimerTask.dateToString(todaydate));
		else jsonobject.put("todaydate", null);
		return jsonobject.toString();
	}

}
