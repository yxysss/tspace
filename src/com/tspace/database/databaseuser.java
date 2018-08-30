package com.tspace.database;

import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.tspace.bean.User;
import com.tspace.listener.TimerTask;

public class databaseuser {
	
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
	
	// 添加用户
	public static int insert(User user) {
		Connection con = druidconnectionpool.getConnection();
		if (con == null) {
			// -1: 系统内部异常
			return -1;
		}
		int i = 0;
		String sql = "insert into user (iduser,username,mobile,identification,name,password,lastlogin,school,identity)"
				+ " values(null,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) con.prepareStatement(sql);
			pstmt.setString(1, user.getusername());
			pstmt.setString(2, user.getmobile());
			pstmt.setString(3, user.getidentification());
			pstmt.setString(4, user.getname());
			pstmt.setString(5, user.getpassword());
			Date date = new Date();
			pstmt.setTimestamp(6, new Timestamp(date.getTime()));
			pstmt.setInt(7, user.getschool());
			pstmt.setInt(8, user.getidentity());
			i = pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch(MySQLIntegrityConstraintViolationException micye) {
			// 唯一键冲突
			i = -10;
			TimerTask.logger.error(micye.toString());
			// System.out.println("唯一键冲突");
		} catch (SQLException e) {
			// System.out.println("添加数据库失败");
			// 系统内部异常
			i = -1;
			TimerTask.logger.error(e.toString());
		} finally {
			druidconnectionpool.closeConnection();
		}
		// System.out.println("i = " + i);
		return i;
	}
	
	// 更新用户最近登录时间
	public static void updatelogin(String username) {
	    Date date = new Date();
	    String sql = "update user set lastlogin=? where username=?";
	    Connection conn = druidconnectionpool.getConnection();
	    PreparedStatement pstmt;
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	pstmt.setString(1, TimerTask.dateToString(date));
	    	pstmt.setString(2, username);
	    	pstmt.executeUpdate();
	    	pstmt.close();
	    } catch(SQLException e) {
	    	
	    	TimerTask.logger.error(e.toString());
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	}
	
	// 根据学号获取用户
	public static User getuser(String username) {
		
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) return null;
	    String sql = "select * from user where username=?";
	    PreparedStatement pstmt;
	    StringBuilder builder = new StringBuilder();
	    User user;
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	pstmt.setString(1, username);
	    	ResultSet rs = pstmt.executeQuery();
	    	// 如果查找到相应的用户，返回
	    	if (rs.next()) {
		    	user = new User();
	            user.setiduser(rs.getInt("iduser"));
	            user.setusername(rs.getString("username"));
	            user.setmobile(rs.getString("mobile"));
	            user.setidentification(rs.getString("identification"));
	            user.setname(rs.getString("name"));
	            user.setpassword(rs.getString("password"));
	            user.setschool(rs.getInt("school"));
	            user.setidentity(rs.getInt("identity"));
	            builder.append(user.toString());
	            builder.append("\n");
	            // System.out.println(user.toString());
	    	} else {
	    		// 未查找到相应的用户，返回User对象，学号为-1
	    		user = new User();
	    		user.setiduser(-1);
	    	}
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	user = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    
	    return user;
	    
	}
	
	// 自拟sql语句获取用户
	public static List<User> get(String sql) {
		// System.out.println("getAll");
	    Connection conn = druidconnectionpool.getConnection();
	    if (conn == null) return null;
	    List<User> users = new ArrayList<User>();
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
	        	User user = new User();
	            user.setiduser(rs.getInt("iduser"));
	            user.setusername(rs.getString("username"));
	            user.setmobile(rs.getString("mobile"));
	            user.setidentification(rs.getString("identification"));
	            user.setname(rs.getString("name"));
	            user.setpassword(rs.getString("password"));
	            user.setschool(rs.getInt("school"));
	            user.setidentity(rs.getInt("identity"));
	            builder.append(user.toString());
	            builder.append("\n");
	            users.add(user);
	            // System.out.println(user.toString());
	        }
	        // System.out.println("============================");
	        //System.out.println(builder.toString());
	    } catch (SQLException e) {
	        e.printStackTrace();
	        users = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return users;
	}
	
	// 删除用户
	public static int delete(String username) {
	    Connection conn = druidconnectionpool.getConnection();
	    if (conn == null) return -1;
	    int i = 0;
	    String sql = "delete from tspace.user where username=?";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, username);
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
	
	// 根据学号获取用户密码
	public static String getpassword(String username) {
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) {
			// 连接数据库失败
			// 系统内部异常
			return null;
		}
	    String sql = "select * from user where username=?";
	    PreparedStatement pstmt;
	    String password;
	    try {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	pstmt.setString(1, username);
	    	ResultSet rs = pstmt.executeQuery();
	    	if(rs.next()) password = rs.getString("password");
	    	else password = "";
	    } catch (SQLException e) {
	    	// 语句执行失败
	    	// 系统内部异常
	    	TimerTask.logger.error(e.toString());
	    	password = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    
	    return password;
	}
	
	// 根据学号设置用户密码
	public static int setpassword(String username, String password) {
		String sql = "update user set password=? where username=?";
		Connection conn = druidconnectionpool.getConnection();
		if (conn == null) {
			// 系统内部异常
			return -1;
		}
		PreparedStatement pstmt;
		int i;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, password);
	        pstmt.setString(2, username);
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
