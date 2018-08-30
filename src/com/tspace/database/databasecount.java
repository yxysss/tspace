package com.tspace.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tspace.listener.TimerTask;

public class databasecount {

	
	public static Integer[] getCount() {
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) return null;
		String sql = "select * from tspace.count";
		Integer[] count = new Integer[2];
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	if ("browsingvolume".equals(rs.getString("countname"))) count[0] = rs.getInt("totalcount");
	        	if ("userloginvolume".equals(rs.getString("countname"))) count[1] = rs.getInt("totalcount");
	        }
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	count = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return count;
	}
	
	public static boolean saveCount(Integer[] count) {
		Connection con = druidconnectionpool.getConnection();
		if (con == null) return false;
		String sql0 = "update tspace.count set totalcount=" + count[0] + " where countname='browsingvolume'";
		String sql1 = "update tspace.count set totalcount=" + count[1] + " where countname='userloginvolume'";
		PreparedStatement pstmt;
		boolean flag = true;
		int result;
		try {
			pstmt = (PreparedStatement) con.prepareStatement(sql0);
			pstmt.executeUpdate();
			pstmt = (PreparedStatement) con.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			TimerTask.logger.error(e.toString());
			flag = false;
		}
		return flag;
	}
}
