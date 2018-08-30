package com.tspace.database;

import java.util.Date;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import org.apache.naming.java.javaURLContextFactory;

import com.tspace.bean.Room;
import com.tspace.listener.TimerTask;

public class databaseroom {
	
	// 对room表的写操作都要使用锁！
	
	@SuppressWarnings("unused")
	private static Connection getCon() {
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://127.0.0.1:3306/tspace";
	    String username = "root";
	    String password = "Yxy781920613";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	        System.out.println("加载驱动成功");
	    } catch (ClassNotFoundException e) {
	    	System.out.println("加载驱动失败0");
	        TimerTask.logger.error(e.toString());
	    } catch (SQLException e) {
	    	System.out.println("加载驱动失败1");
	    	TimerTask.logger.error(e.toString());
	    }
	    return conn;
	}
	
	// 添加房间
	public static int insert(Room room) {
		synchronized (TimerTask.roomlock) {
			Connection con = druidconnectionpool.getConnection();
			if (con == null) 
				// 数据库连接失败
				return -1;
			int i = 0;
			String sql = "insert into room (idroom,nameroom,addressroom,schoolroom,capacityroom,typeroom,descriptionroom,pictureroom,availableroom,todayroom,todaydate) values(null,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt;
			try {
				pstmt = (PreparedStatement) con.prepareStatement(sql);
				pstmt.setString(1, room.getnameroom());
				pstmt.setString(2, room.getaddressroom());
				pstmt.setInt(3, room.getschoolroom());
				pstmt.setInt(4, room.getcapacityroom());
				pstmt.setString(5, room.gettyperoom());
				pstmt.setString(6, room.getdescriptionroom());
				pstmt.setString(7, room.getpictureroom());
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < 30; j ++) {
					builder.append('0');
				}
				pstmt.setString(9, builder.toString());
				for (int k = 0; k < 180; k ++) {
					builder.append('0');
				}
				pstmt.setString(8, builder.toString());
				Date todaydate = new Date();
				pstmt.setDate(10, new java.sql.Date(todaydate.getTime()));
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				// System.out.println("添加数据库失败");
				i = -2;
				TimerTask.logger.error(e.toString());
			} finally {
				druidconnectionpool.closeConnection();
			}
			return i;
		}
	}
	
	// 更新房间
	public static int update(Room room) {
		synchronized (TimerTask.roomlock) {
			Connection con = druidconnectionpool.getConnection();
			if (con == null) 
				// 数据库连接失败
				return -1;
			int i = 0;
			String sql = "update tspace.room set nameroom=?,addressroom=?,schoolroom=?,capacityroom=?,typeroom=?,descriptionroom=?,pictureroom=? where idroom=?";
			PreparedStatement pstmt;
			try {
				pstmt = (PreparedStatement) con.prepareStatement(sql);
				pstmt.setString(1, room.getnameroom());
				pstmt.setString(2, room.getaddressroom());
				pstmt.setInt(3, room.getschoolroom());
				pstmt.setInt(4, room.getcapacityroom());
				pstmt.setString(5, room.gettyperoom());
				pstmt.setString(6, room.getdescriptionroom());
				pstmt.setString(7, room.getpictureroom());
				pstmt.setInt(8, room.getidroom());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				// System.out.println("更新数据库失败");
				i = -2;
				TimerTask.logger.error(e.toString());
			} finally {
				druidconnectionpool.closeConnection();
			}
			return i;
		}
	}
	
	public static Room getroom(int idroom) {
		
	    String sql = "select * from room where idroom=" + idroom;
	    List<Room> rooms = get(sql);
	    if (rooms == null) {
	    	return null;
	    }
	    if (rooms.size() == 0) {
	    	Room room = new Room();
	    	room.setidroom(-1);
	    	return room;
	    }
	    
	    return rooms.get(0);
	    
	}
	
	// 自拟sql语句，获取房间列表
	public static List<Room> get(String sql) {
		// System.out.println("getAll");
	    Connection conn = druidconnectionpool.getConnection();
	    if (conn == null) return null;
	    List<Room> rooms = new ArrayList<Room>();
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        StringBuilder builder = new StringBuilder();
	        // System.out.println("============================");
	        while (rs.next()) {
	        	/*
	            for (int i = 1; i <= col; i++) {
	                System.out.print(rs.getString(i) + "\t");
	                if ((i == 2) && (rs.getString(i).length() < 8)) {
	                    System.out.print("\t");
	                }
	             }
	             */
	            Room room = new Room();
	            room.setidroom(rs.getInt("idroom"));
	            room.setnameroom(rs.getString("nameroom"));
	            room.setaddressroom(rs.getString("addressroom"));
	            room.setcapacityroom(rs.getInt("capacityroom"));
	            room.settyperoom(rs.getString("typeroom"));
	            room.setdescriptionroom(rs.getString("descriptionroom"));  
	            room.setpictureroom(rs.getString("pictureroom"));
	            room.setavailableroom(rs.getString("availableroom"));
	            room.settodayroom(rs.getString("todayroom"));
	            room.settodaydate(rs.getDate("todaydate"));
	            builder.append(room.toString());
	            builder.append("\n");
	            rooms.add(room);
	            // System.out.println(room.toString());
	        }
	        // System.out.println("============================");
	        // System.out.println(builder.toString());
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	rooms = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return rooms;
	}
	
	// 自拟sql语句，获取房间序号和房间姓名的Map
	public static HashMap<Integer, String> getMap(String sql) {
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) return null;
		HashMap<Integer, String> rooms = new HashMap<Integer, String>();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
		    ResultSet rs = pstmt.executeQuery();
		    StringBuilder builder = new StringBuilder();
		    while (rs.next()) {
		        rooms.put(rs.getInt("idroom"), rs.getString("nameroom"));
		    }
		} catch (SQLException e) {
		    TimerTask.logger.error(e.toString());
		    rooms = null;
		} finally {
		    druidconnectionpool.closeConnection();
		}
		return rooms;
	}
	
	// 删除房间
	public static int delete(int idroom) {
		synchronized (TimerTask.roomlock) {
		    Connection conn = druidconnectionpool.getConnection();
		    if (conn == null) return -1;
		    int i = 0;
		    String sql = "delete from room where idroom=" + idroom;
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        i = pstmt.executeUpdate();
		        // System.out.println("result: " + i);
		        pstmt.close();
		    } catch (SQLException e) {
		    	i = -1;
		    	TimerTask.logger.error(e.toString());
		    } finally {
		    	druidconnectionpool.closeConnection();
		    }
		    return i;
		}
	}
	
	// 每天晚上12：00-1：00，更新房间信息
	public static boolean updateavailableroom() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    int year = cal.get(Calendar.YEAR);//获取年份
	    int month=cal.get(Calendar.MONTH)+1;//获取月份
	    int day=cal.get(Calendar.DATE);//获取日
	    StringBuilder dateform = new StringBuilder();
	    dateform.append(year+"-");
	    if (month<10) dateform.append("0");
	    dateform.append(month+"-");
	    if(day<10) dateform.append("0");
	    dateform.append(day+"");
	    // System.out.println("updateavailableroom,"+dateform.toString());
	    boolean flag = true;
		synchronized (TimerTask.roomlock) {
		    String sql = "select * from room where todaydate<>'" + dateform.toString() + "'";
		    // System.out.println("updateavailableroom,"+sql);
		    List<Room> rooms = get(sql);
		    if (rooms == null) return false;
		    // System.out.println("rooms size = " + rooms.size());
		    StringBuilder availableroom = null;
		    StringBuilder todayroom = null;
		    if (rooms.size() == 0) return true;
		    for (int i = 0; i < rooms.size(); i ++) {
		    	Room room = rooms.get(i);
		    	availableroom = new StringBuilder(room.getavailableroom());
		    	todayroom = new StringBuilder(availableroom.substring(0, 30));
		    	availableroom = new StringBuilder(availableroom.substring(30));
				availableroom.append("000000000000000000000000000000");
				room.setavailableroom(availableroom.toString());
				room.settodayroom(todayroom.toString());
		    }
		    Connection conn = druidconnectionpool.getConnection();
		    if (conn == null) return false;
		    PreparedStatement pstmt;
			String sql0 = "update room set availableroom=?,todayroom=?,todaydate=? where idroom=?";
			try {
				pstmt = (PreparedStatement) conn.prepareStatement(sql0);
				for (int i = 0; i < rooms.size(); i ++) {
					Room room = rooms.get(i);
					pstmt.setString(1, room.getavailableroom());
					pstmt.setString(2, room.gettodayroom());
					java.sql.Date sdate = new java.sql.Date(new Date().getTime());
					pstmt.setDate(3, sdate);
					pstmt.setInt(4, room.getidroom());
					int result = pstmt.executeUpdate();
					// System.out.println("updateavailableroom,result=" + result);
				}
				pstmt.close();
			} catch (SQLException e) {
				TimerTask.logger.error(e.toString());
				flag = false;
			} finally {
				druidconnectionpool.closeConnection();
			}
		}
		return flag;
	}
	
	public static Integer getCount(String sql) {
		Connection con = druidconnectionpool.getConnection();
		if (con == null) {
			// 系统异常
			return null;
		}
		PreparedStatement pstmt;
		ResultSet rs;
		Integer count;
		try {
			pstmt = (PreparedStatement) con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			} else {
				// 系统异常
				count = null;
			}
			
		} catch (SQLException e) {
			// 系统异常
			TimerTask.logger.error(e.toString());
			count = null;
		} finally {
			druidconnectionpool.closeConnection();
		}
		return count;
	}
}
