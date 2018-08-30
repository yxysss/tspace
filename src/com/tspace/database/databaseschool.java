package com.tspace.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tspace.bean.School;
import com.tspace.listener.TimerTask;

public class databaseschool {
	
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
	    	System.out.println("加载驱动失败");
	    	TimerTask.logger.error(e.toString());
	    } catch (SQLException e) {
	    	System.out.println("加载驱动失败");
	    	TimerTask.logger.error(e.toString());
	    }
	    return conn;
	}
	
	public static String getschoolname(int idschool) {
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) {
			// 系统内部异常
			return null;
		}
	    String sql = "select * from school where idschool=?";
	    PreparedStatement pstmt;
	    String result;
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	pstmt.setInt(1, idschool);
	    	ResultSet rs = pstmt.executeQuery();
	    	if(rs.next()) result = rs.getString("schoolname");
	    	else result = "";
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	// 系统内部异常
	    	result = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return result;
	}
	
	public static List<School> getAll() {
		// System.out.println("getAll");
	    Connection conn = druidconnectionpool.getConnection();
	    if (conn == null) return null;
	    List<School> schools = new ArrayList<School>();
	    String sql = "select * from school";
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
	            School school  = new School();
	            school.setidschool(rs.getInt("idschool"));
	            school.setschoolname(rs.getString("schoolname"));
	            builder.append(school.toString());
	            builder.append("\n");
	            schools.add(school);
	            // System.out.println(school.toString());
	        }
	        // System.out.println("============================");
	        // System.out.println(builder.toString());
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	schools = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return schools;
	}
	
	// 自拟sql语句，获取房间序号和房间姓名的Map
	public static HashMap<Integer, String> getMap(String sql) {
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) return null;
		HashMap<Integer, String> schools = new HashMap<Integer, String>();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
		    ResultSet rs = pstmt.executeQuery();
		    StringBuilder builder = new StringBuilder();
		    while (rs.next()) {
		        schools.put(rs.getInt("idschool"), rs.getString("schoolname"));
		    }
		} catch (SQLException e) {
		    TimerTask.logger.error(e.toString());
		    schools = null;
		} finally {
		    druidconnectionpool.closeConnection();
		}
		return schools;
	}
	
	public static int insert(School school) {
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) return -2;
		String sql = "insert into room (idschool,schoolname) values(null,?)";
		int i = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, school.getschoolname());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			i = -2;
			TimerTask.logger.error(e);
		} finally {
			druidconnectionpool.closeConnection();
		}
		return i;
	}
	
}
