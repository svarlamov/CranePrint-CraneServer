package org.craneprint.craneserver.tcp;

import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class LaunchTCPListener
 *
 */
@WebListener
public class TCPContext implements ServletContextListener {
	// TODO: Get these values from config
	private final int cranePort = 6770;
	private Thread thread;
	private TCPListenerThread tcp;

    /**
     * Default constructor. 
     */
    public TCPContext() {
        // TODO Setup all of the variables from the config file
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	thread.stop();
    	tcp.closeConnection();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Implement for multiple printers
    	tcp = new TCPListenerThread(arg0.getServletContext(), cranePort);
    	thread = new Thread(tcp);
    	thread.start();
    	arg0.getServletContext().setAttribute("org.craneprint.craneserver.tcp.tcpThread", thread);
    }
	
}
