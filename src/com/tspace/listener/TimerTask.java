package com.tspace.listener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tspace.database.databaseroom;

public class TimerTask {
	
	public static String dateToString(Date time){
	    SimpleDateFormat formatter;
	    formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	    String ctime = formatter.format(time);
	    return ctime;
	}
	
	private static Thread thread = null;
	
	public static int year=0, month=0, day=0;
	
	public static Object roomlock = new Object();
	
	public static volatile int tmpcount = 0;
	
	public static final Logger logger = LogManager.getLogger("TSpace");
	
	public static Integer browsingvolume = 0;
	
	public static Integer userloginvolume = 0;
	
	static class TimerThread extends Thread {
		public void run() {
			
			/*boolean flag = databaseroom.updateavailableroom();
			System.out.println("flag=" + flag);*/
			/*
			if ( y!=year || m!=month || d!=day) {
				
			}
			*/
			
			while (1!=0) {
				Calendar calendar = Calendar.getInstance();
				int y = calendar.get(Calendar.YEAR);
				int m = calendar.get(Calendar.MONTH) + 1;
				int d = calendar.get(Calendar.DAY_OF_MONTH);
				int h = calendar.get(Calendar.HOUR_OF_DAY);
				
				if (y != year || m != month || d != day) {
					
					while (1!=0) {
						boolean flag = databaseroom.updateavailableroom();
						// System.out.println(flag);
						if (flag == true) 
						break;
					}
					
					year = y; month = m; day = d;  
					
				}
				
				try {
					// 每1分钟查询一次
					Thread.sleep(1000*60);
				} catch (Exception e) {
					TimerTask.logger.error(e);
				}
			}
			
		}
	}
	
	public static void taskbegin() {
		// logger.error(roomlock.toString());
		thread = new TimerThread();
		thread.start();
	}
	
	public static void taskstop() {
		if (thread == null) return ;
		if (thread.isAlive()) {
			thread.interrupt();
		}
		thread = null;
	}

}
