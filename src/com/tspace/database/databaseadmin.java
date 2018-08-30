package com.tspace.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tspace.bean.Admin;
import com.tspace.listener.*;

public class databaseadmin {
	
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
	        // System.out.println("加载驱动成功");
	    } catch (ClassNotFoundException e) {
	    	// System.out.println("加载驱动失败");
	    	TimerTask.logger.fatal("databaseadmin - 加载驱动失败");
	        e.printStackTrace();
	    } catch (SQLException e) {
	    	// System.out.println("加载驱动失败");
	    	TimerTask.logger.fatal("databaseadmin - 加载驱动失败");
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	public static int insert(Admin admin) {
		Connection con = (Connection) druidconnectionpool.getConnection();
		if (con == null) return -1;
		int i = 0;
		String sql = "insert into admin (idadmin,adminname,password,lastlogin)"
				+ " values(null,?,?,null)";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) con.prepareStatement(sql);
			pstmt.setString(1, admin.getadminname());
			pstmt.setString(2, admin.getpassword());
			i = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// System.out.println("添加数据库失败");
			i = -1;
			TimerTask.logger.error(e.toString());
		} finally {
			druidconnectionpool.closeConnection();
		}
		return i;
	}
	
	public static int updatelogin(String idadmin) {
		
	    int i = 0;
	    Date date = new Date(System.currentTimeMillis());
	    String sql = "update admin set lastlogin='" + TimerTask.dateToString(date) + "' where idadmin=" + idadmin;
	    Connection conn = (Connection) druidconnectionpool.getConnection();
	    if (conn == null) return -1;
	    PreparedStatement pstmt;
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	i = pstmt.executeUpdate();
	    	pstmt.close();
	    } catch(SQLException e) {
	    	i = -1;
	    	TimerTask.logger.error(e.toString());
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return i;
	}
	
	public static Admin query(String adminname) {
		
		Connection conn = (Connection) druidconnectionpool.getConnection();
	    String sql = "select * from admin where adminname=" + adminname;
	    PreparedStatement pstmt;
	    StringBuilder builder = new StringBuilder();
	    Admin admin = new Admin();
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	ResultSet rs = pstmt.executeQuery();
	    	rs.next();
            admin.setidadmin(rs.getInt("idadmin"));
            admin.setadminname(rs.getString("adminname"));
            admin.setpassword(rs.getString("password"));
            builder.append(admin.toString());
            builder.append("\n");
            // System.out.println(admin.toString());
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	admin = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return admin;
	}
	
	public static List<Admin> get(String sql) {
		// System.out.println("getAll");
	    Connection conn = (Connection) druidconnectionpool.getConnection();
	    List<Admin> admins = new ArrayList<Admin>();
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
	        	Admin admin = new Admin();
	            admin.setidadmin(rs.getInt("iadmin"));
	            admin.setadminname(rs.getString("adminname"));
	            admin.setpassword(rs.getString("password"));
	            builder.append(admin.toString());
	            builder.append("\n");
	            admins.add(admin);
	            // System.out.println(admin.toString());
	        }
	        // System.out.println("============================");
	        // System.out.println(builder.toString());
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	admins = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return admins;
	}
	
	public static int delete(String adminname) {
	    Connection conn = (Connection) druidconnectionpool.getConnection();
	    if (conn == null) return -1;
	    int i = 0;
	    String sql = "delete from admin where adminname=" + adminname;
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
	
	public static String getpassword(String adminname) {
		Connection conn = (Connection) druidconnectionpool.getConnection();
		if (conn == null) {
			// 系统异常
			return null;
		}
	    String sql = "select * from admin where adminname='" + adminname +"'";
	    PreparedStatement pstmt;
	    String password;
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	ResultSet rs = pstmt.executeQuery();
	    	if (rs.next()) {
	    		// 返回获得的密码
	    		password = rs.getString("password");
	    	} else {
	    		// 用户名不存在
	    		password = "";
	    	}
	    } catch (SQLException e) {
	    	// 系统异常
	    	TimerTask.logger.error(e.toString());
	    	password = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return password;
	}
	
	public static Boolean setpassword(String studentid, String password) {
		String sql = "update user set password='" + password + "' where studentid='" + studentid + "'";
		Connection conn = (Connection) druidconnectionpool.getConnection();
		PreparedStatement pstmt;
		int i;
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
	    if(i < 0) return false;
	    return true;
	}
}
