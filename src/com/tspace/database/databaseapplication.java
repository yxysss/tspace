package com.tspace.database;

import java.util.Date;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.tspace.bean.Application;
import com.tspace.bean.Room;
import com.tspace.listener.TimerTask;

public class databaseapplication {
	
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
	        e.printStackTrace();
	    } catch (SQLException e) {
	    	System.out.println("加载驱动失败");
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	// 检查申请人在当天是否有申请
	// -1:error 0:false 1:true
	private static int checkvalidate(Application application, Connection con) {
		
		String applicant = application.getapplicant();
		java.util.Date applydate = application.getstarttime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(applydate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		applydate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		java.util.Date next = calendar.getTime();
		
		PreparedStatement pstmt;
		String sql = "select * from application where applicant=? and state<>'decline' and state<>'cancel' and starttime>? and starttime<?";
		// System.out.println(sql);
		// System.out.println(applydate.toLocaleString() + "\n" + next.toLocaleString());
		try {
	    	pstmt = (PreparedStatement) con.prepareStatement(sql);
	    	pstmt.setString(1, applicant);
	    	pstmt.setString(2, TimerTask.dateToString(applydate));
	    	pstmt.setString(3, TimerTask.dateToString(next));
	    	ResultSet rs = pstmt.executeQuery();
	    	if (rs.next()) {
	    		pstmt.close();
	    		druidconnectionpool.closeConnection();
	    		return 0;
	    	}
	    	pstmt.close();
	    	druidconnectionpool.closeConnection();
	    	
	    } catch (SQLException e) {
	    	TimerTask.logger.error(e.toString());
	    	return -1;
	    }
		return 1;
	}
	
	// 查看申请的时间段是否已被占用（申请时的页面可能存在信息滞后）
	// 查询room表
	// -1:error 0:false 1:true
	public static int checktime(Application application, int starttime0, int endtime0) {
		
		Room room = databaseroom.getroom(application.getidroom());
		if (room == null) return -1;
		String availableroom = room.getavailableroom();
		int d = starttime0 / 100;
		int s = starttime0 % 100;
		int e = endtime0 % 100;
		for (int i = 31*(d-1)+s-1; i < 31*(d-1)+e; i ++) {
			if (availableroom.indexOf(i) == '1') return 0;
		}
		return 1;
	}
	
	// 添加申请
	public static int insert(Application application) {
		
		Connection con = (Connection) druidconnectionpool.getConnection();
		// System.out.println(con);
		// 连接数据库出错
		// 系统内部异常
		if (con == null) return -2; 
		if (checkvalidate(application, con) == 0) {
			// 申请人在申请的那一天已有其他申请
			druidconnectionpool.closeConnection();
			return -3;
		}
		
		synchronized(TimerTask.roomlock) {
			int idroom = application.getidroom();
			Room room = databaseroom.getroom(idroom);
			Date starttime = application.getstarttime();
			Date endtime = application.getendtime();
			Date date = room.gettodaydate();
			Calendar today = Calendar.getInstance();
			today.setTime(date);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			Calendar thatday = Calendar.getInstance();
			thatday.setTime(starttime);
			thatday.set(Calendar.HOUR_OF_DAY, 0);
			thatday.set(Calendar.MINUTE, 0);
			thatday.set(Calendar.SECOND, 0);
			thatday.set(Calendar.MILLISECOND, 0);
			int days = ((int)(thatday.getTime().getTime()/1000)-(int)(today.getTime().getTime()/1000))/3600/24;
			// System.out.println("days="+days);
			int hourstart = starttime.getHours(), hourend = endtime.getHours();
			int minutestart = starttime.getMinutes(), minuteend = endtime.getMinutes();
			int startid = (hourstart-7); startid *= 2; if (minutestart == 30) startid += 1;
			int endid = (hourend-7); endid *= 2; if (minuteend == 0) endid -= 1;
			// System.out.println("startid="+startid+",endid="+endid);
			int flag = checktime(application, days*100+startid, days*100+endid);
			if (flag == 0) {
				druidconnectionpool.closeConnection();
				// 申请的时间段已被使用
				return -4;
			}
			if (flag == -1) {
				druidconnectionpool.closeConnection();
				// 申请的房间不存在
				return -6;
			}
			int i = 0;
			String sql = "insert into application (idapplication,idroom,starttime,endtime,participants,reason,state,applytime,applicant,audittime,password)"
					+ " values(null,?,?,?,?,?,?,?,?,null,null)";
			PreparedStatement pstmt;
			try {
				con = (Connection) druidconnectionpool.getConnection();
				// System.out.println("111111");
				// System.out.println(con);
				pstmt = (PreparedStatement) con.prepareStatement(sql);
				// System.out.println("333333");
				pstmt.setInt(1, application.getidroom());
				pstmt.setTimestamp(2, new Timestamp(application.getstarttime().getTime()));
				pstmt.setTimestamp(3, new Timestamp(application.getendtime().getTime()));
				pstmt.setString(4, application.getparticipants());
				pstmt.setString(5, application.getreason());
				pstmt.setString(6, application.getstate());
				java.util.Date present = new java.util.Date();
				pstmt.setTimestamp(7, new Timestamp(present.getTime()));
				pstmt.setString(8, application.getapplicant());
				i = pstmt.executeUpdate();
				// System.out.println("222222");
				pstmt.close();
			} catch (SQLException e) {
				// System.out.println("添加数据库失败");
				// 语句执行出错
				// 系统内部异常
				i = -2;
				TimerTask.logger.error(e.toString());
			} finally {
				druidconnectionpool.closeConnection();
			}
			return i;
		}
	}
	
	
	// 查询用户正在运行中的申请（待审核未过期申请以及审核通过的未过期申请）
	public static List<Application> getRunning(String applicant) {
		
		Date rightnow = new Date();
	    String sql = "select * from application where applicant='" + applicant + "' and endtime>'" + TimerTask.dateToString(rightnow) +"' and state<>'decline' and state<>'cancel'";
	    return get(sql);
	    
	    
	}

	// 查询用户的历史申请（被拒绝的申请以及审核通过的过期申请以及被取消的申请）
	public static List<Application> getPast(String applicant) {
			
		Date rightnow = new Date();
		String sql = "select * from application where (applicant='" + applicant + "' and endtime<'" + TimerTask.dateToString(rightnow) +"') or state='decline' or state='cancel'";
		return get(sql);
		    
	}

	
	// 根据申请号获取申请
	public static Application getapplication(int idapplication) {
		String sql = "select * from application where idapplication=" + idapplication;
		List<Application> applications = get(sql);
		if (applications != null) {
			Application application = applications.get(0);
			if (application == null) {
				application = new Application();
				application.setidapplication(-1);
			
			}
			return application;
		} else {
			return null;
		}
	}
	
	
	// 自拟sql语句获取申请
	public static List<Application> get(String sql) {
	    Connection conn = (Connection) druidconnectionpool.getConnection();
	    if (conn == null) return null;
	    List<Application> applications = new ArrayList<Application>();
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
	            Application application = new Application();
	            application.setidapplication(rs.getInt(1));
	            application.setidroom(rs.getInt(2));
	            application.setstarttime(rs.getTimestamp(3));
	            application.setendtime(rs.getTimestamp(4));
	            application.setapplicant(rs.getString(5));
	            application.setreason(rs.getString(6));
	            application.setparticipants(rs.getString(7));
	            application.setstate(rs.getString(8));
	            application.setapplytime(rs.getTimestamp(9));
	            application.setaudittime(rs.getTimestamp(10));
	            application.setpassword(rs.getString(11));
	            builder.append(application.toString());
	            builder.append("\n");
	            applications.add(application);
	            // System.out.println(application.toString());
	        }
	        // System.out.println("============================");
	        // System.out.println(builder.toString());
	    } catch (SQLException e) {
	        TimerTask.logger.error(e.toString());
	        applications = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return applications;
	}
	
	// 管理员审核获取申请api，按房间号和提交申请时间有序排列
	public static List<Application> getsorted(String sql) {
		// System.out.println("getAll");
	    Connection conn = druidconnectionpool.getConnection();
	    if (conn == null) return null;
	    TreeSet<Application> applications = new TreeSet<Application>();
	    List<Application> applylist = new ArrayList<Application>();
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
	            Application application = new Application();
	            application.setidapplication(rs.getInt("idapplication"));
	            application.setidroom(rs.getInt("idroom"));
	            application.setstarttime(rs.getTimestamp("starttime"));
	            application.setendtime(rs.getTimestamp("endtime"));
	            application.setapplicant(rs.getString("applicant"));
	            application.setreason(rs.getString("reason"));
	            application.setparticipants(rs.getString("participants"));
	            application.setstate(rs.getString("state"));
	            application.setapplytime(rs.getTimestamp("applytime"));
	            application.setaudittime(rs.getTimestamp("audittime"));
	            application.setpassword(rs.getString("password"));
	            builder.append(application.toString());
	            builder.append("\n");
	            applications.add(application);
	            // System.out.println(application.toString());
	        }
	        // System.out.println("============================");
	        // System.out.println(builder.toString());
	        for (Iterator<Application> iter = applications.descendingIterator(); iter.hasNext();) {
	        	applylist.add(iter.next());
	        }
	    } catch (SQLException e) {
	        TimerTask.logger.error(e.toString());
	        applylist = null;
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return applylist;
	}
	
	// 用户取消申请
	// -1: error 0: useless 1: success
	public static int cancelapplication(String studentid, int idapplication) {
	    Connection conn = (Connection) druidconnectionpool.getConnection();
	    if (conn == null) {
	    	// 系统内部异常
	    	return -1;
	    }
	    int i = 0;
	    String sql = "update application set state='cancel' where idapplication=? and applicant=? and state='unsettle'";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, idapplication);
	        pstmt.setString(2, studentid);
	        i = pstmt.executeUpdate();
	        // System.out.println("result: " + i);
	        pstmt.close();
	    } catch (SQLException e) {
	    	i = -1;
	        e.printStackTrace();
	    } finally {
	    	druidconnectionpool.closeConnection();
	    }
	    return i;
	}
	
	// 管理员接受申请
	/*
	public static int acceptapplication(int idapplication, int password) {
		
		
		// sql事务处理
		// 1.修改room表中的房间预约信息
		// 2.修改被接受的申请的状态
		// 3.修改与被接受申请相冲突的申请的状态
		synchronized (TimerTask.roomlock) {
			Application application = getapplication(idapplication);
			if (application == null) {
				// 系统内部异常
				return -2;
			}
			if (application.getidapplication() == -1) {
				// 被接受申请不存在
				return -3;
			}
			if (!application.getstate().equals("unsettle")) {
				// 申请已被审核
				return -6;
			}
			int idroom = application.getidroom();
			Date starttime = application.getstarttime();
			Date endtime = application.getendtime();
			Date date = databaseroom.getroom(idroom).gettodaydate();
			Calendar today = Calendar.getInstance();
			today.setTime(date);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			Calendar thatday = Calendar.getInstance();
			thatday.setTime(starttime);
			thatday.set(Calendar.HOUR_OF_DAY, 0);
			thatday.set(Calendar.MINUTE, 0);
			thatday.set(Calendar.SECOND, 0);
			thatday.set(Calendar.MILLISECOND, 0);
			int days = ((int)(thatday.getTime().getTime()/1000)-(int)(today.getTime().getTime()/1000))/3600/24;
			System.out.println("days="+days);
			int hourstart = starttime.getHours(), hourend = endtime.getHours();
			int minutestart = starttime.getMinutes(), minuteend = endtime.getMinutes();
			int startid = (hourstart-7); startid *= 2; if (minutestart == 30) startid += 1;
			int endid = (hourend-7); endid *= 2; if (minuteend == 0) endid -= 1;
			System.out.println("startid="+startid+",endid="+endid);
			String sql = "select * from room where idroom=" + idroom;
			Connection conn = (Connection) druidconnectionpool.getConnection();
			if (conn == null) {
				// 系统内部异常
				return -2;
			}
		    PreparedStatement pstmt;
		    Room room = null;
		    StringBuilder availableroom = null;
			try {
				pstmt = (PreparedStatement)conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					room = new Room();
					availableroom = new StringBuilder(rs.getString("availableroom"));
					for (int i = startid-1; i < endid; i ++) {
						availableroom.setCharAt(i+(days-1)*30, '1');
					}
				}
				pstmt.close();
			} catch (SQLException e) {
				druidconnectionpool.closeConnection();
				TimerTask.logger.error(e.toString());
				// 系统内部异常
				return -2;
			}
			if (availableroom == null) {
				druidconnectionpool.closeConnection();
				// 申请中的房间不存在
				return -4;
			}
			date = new Date();
			String sql0 = "update room set availableroom='" + availableroom.toString() + "' where idroom=" + idroom;
			String sql1 = "update application set state='accept', audittime='" + date.toLocaleString() + "'" + ", password=" + password + " where idapplication="+idapplication;
			System.out.println("sql1="+sql1);
			String sql2 = "update application set state='decline', audittime='" + date.toLocaleString() + "' where idroom=" + idroom + " and state='unsettle' and starttime<'" + application.getendtime().toLocaleString()+"' and endtime>'" + application.getendtime().toLocaleString() + "'";
			System.out.println("sql2="+sql2);
			String sql3 = "update application set state='decline', audittime='" + date.toLocaleString() + "' where idroom=" + idroom + " and state='unsettle' and endtime>'" + application.getstarttime().toLocaleString() + "' and endtime<'" + application.getendtime().toLocaleString() + "'";
			System.out.println("sql3="+sql3);
			try {
				druidconnectionpool.startTransaction();
				Statement stmt = (Statement) conn.createStatement(); 
				stmt.executeUpdate(sql0);
				stmt.executeUpdate(sql1);
				stmt.executeUpdate(sql2);
				stmt.executeUpdate(sql3);
				druidconnectionpool.commit();
				druidconnectionpool.closeConnection();
				stmt.close();
			} catch (SQLException e) {
				druidconnectionpool.rollback();
				druidconnectionpool.closeConnection();
				TimerTask.logger.error(e.toString());
				// 系统内部异常
				return -2;
			}
		}
		// 操作成功
		return 0;
		
	}
	*/
	
	// -2：系统异常，执行失败
	// 0：执行成功
	// -3：申请不存在
	// -6：申请已被审核
	// -4：房间不存在
	public static int acceptapplication(int idapplication, int password) {
		synchronized(TimerTask.roomlock) {
			String sql = "{CALL acceptapplication(?,?,?)}";
			Connection conn = (Connection) druidconnectionpool.getConnection();
			if (conn == null) {
				// 系统异常
				return -2;
			}
			int result = 0;
			try {
				CallableStatement cstm = conn.prepareCall(sql);
				cstm.setInt(1, idapplication);
				cstm.setString(2, new Integer(password).toString());
				cstm.registerOutParameter(3, Types.INTEGER);
				boolean flag = cstm.execute();
				// System.out.println(flag);
				result = cstm.getInt(3);
				// System.out.println("result="+result);
				cstm.close();
			} catch (SQLException e) {
				TimerTask.logger.error(e.toString());
				// 执行失败，回滚
				result = -2;
				// e.printStackTrace();
			} finally {
				druidconnectionpool.closeConnection();
			}
			return result;
		}
	}
	
	// 管理员拒绝申请
	public static int declineapplication(int idapplication) {
		
		String sql = "update application set state='decline' where idapplication=? and state='unsettle'";
		Connection conn = (Connection) druidconnectionpool.getConnection();
		if (conn == null) {
			// 系统内部异常
			return -2;
		}
		PreparedStatement pstmt;
		int i;
		try {
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
			pstmt.setInt(1, idapplication);
			i = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			TimerTask.logger.error(e.toString());
			// 系统内部异常
			i = -2;
		} finally {
			druidconnectionpool.closeConnection();
		}
		if (i == -2) return i;
		
		if (i > 0) {
			// 操作成功
			return 0;
		} else {
			// 操作失败
			return -3;
		}
		
	}
	
	public static Integer getCount(String sql) {
		Connection con = (Connection) druidconnectionpool.getConnection();
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
			count = null;
			// 系统异常
			TimerTask.logger.error(e.toString());
		} finally {
			druidconnectionpool.closeConnection();
		}
		return count;
	}
	
	public static Integer setPassword(int idapplication, int password) {
		Connection con = (Connection) druidconnectionpool.getConnection();
		if (con == null) {
			// 系统异常
			return -2;
		}
		String sql = "update application set password=" + password + " where idapplication=" + idapplication;
		PreparedStatement pstmt;
		Integer rows;
		try {
			pstmt = (PreparedStatement) con.prepareStatement(sql);
			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			TimerTask.logger.error(e.toString());
			// 系统异常
			rows = -2;
		} finally {
			druidconnectionpool.closeConnection();
		}
		return rows;
	}
	
}
