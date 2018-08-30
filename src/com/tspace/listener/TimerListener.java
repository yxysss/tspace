package com.tspace.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.tspace.database.databasecount;

/**
 * Application Lifecycle Listener implementation class TimerListener
 *
 */
@WebListener
public class TimerListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public TimerListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub  
    	// System.out.println("contextDestroyed…………");
    	TimerTask.taskstop();
    	Integer[] count = new Integer[2];
    	count[0] = TimerTask.browsingvolume;
    	count[1] = TimerTask.userloginvolume;
    	databasecount.saveCount(count);
    	
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	// System.out.println("contextInitialized……");
    	TimerTask.taskbegin();
    	Integer[] count = databasecount.getCount();
    	if (count == null) return ;
    	if (count[0] != null) TimerTask.browsingvolume = count[0];
    	if (count[1] != null) TimerTask.userloginvolume = count[1];
    }
	
}
