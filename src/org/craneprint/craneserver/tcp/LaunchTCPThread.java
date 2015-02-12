package org.craneprint.craneserver.tcp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class LaunchTCPListener
 *
 */
@WebListener
public class LaunchTCPThread implements ServletContextListener {
	TCPThread listener;
	Thread t;

    /**
     * Default constructor. 
     */
    public LaunchTCPThread() {
        // TODO Auto-generated constructor stub
    	listener = new TCPThread();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	listener.closeConnection();
    	t.stop();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	t = new Thread(listener);
    	t.start();
    }
	
}
