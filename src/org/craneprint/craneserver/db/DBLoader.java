package org.craneprint.craneserver.db;

import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ConnectDBListener
 */
@WebListener
public class DBLoader implements ServletContextListener {
	//TODO: get these from the config file
	private final int port = 27017;
	private final String dbHost = "localhost";
	private final String dbName = "craneprintdb";
	private DBManager db;
	
	
    /**
     * Default constructor. 
     */
    public DBLoader() {
        // TODO Set the values for the db from the config file
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	 db.closeConnection();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	db = new DBManager(port, dbHost, dbName, arg0.getServletContext());
    	arg0.getServletContext().setAttribute("org.craneprint.craneserver.db.dbManager", db);
    }
	
}
