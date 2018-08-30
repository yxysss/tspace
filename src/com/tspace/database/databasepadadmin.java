package com.tspace.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tspace.bean.Padadmin;
import com.tspace.listener.TimerTask;

public class databasepadadmin {
	
	
	public static Padadmin query(String padadminname) {
		Connection conn = (Connection) druidconnectionpool.getConnection();
	    String sql = "select * from padadmin where padadminname=" + padadminname;
	    PreparedStatement pstmt;
	    StringBuilder builder = new StringBuilder();
	    Padadmin padadmin = new Padadmin();
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	ResultSet rs = pstmt.executeQuery();
	    	rs.next();
            padadmin.setidpadadmin(rs.getInt("idpadadmin"));
            padadmin.setpadadminname(rs.getString("padadminname"));
            padadmin.setpassword(rs.getString("password"));
            builder.append(padadmin.toString());
            builder.append("\n");
            // System.out.println(padadmin.toString());
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	padadmin = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return padadmin;
	}
	
	public static String getpassword(String padadminname) {
		Connection conn = (Connection) druidconnectionpool.getConnection();
		if (conn == null) {
			// 系统异常
			return null;
		}
	    String sql = "select * from padadmin where padadminname='" + padadminname +"'";
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

}
