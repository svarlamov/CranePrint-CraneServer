package org.craneprint.craneserver.db;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.craneprint.craneserver.Craneprint_craneserverUI;

import com.vaadin.ui.UI;

/**
 * Application Lifecycle Listener implementation class ConnectDBListener
 * ATTN: WITH THE NEW IMPLEMENTATION OF THE DBManager CLASS THIS SERVLET
 * 		 IS NO LONGER NECESCARY. THIS FILE SHOULD BE REMOVED FROM THE PROJECT.
 * TODO: DELETE WHEN READY
 *
 */
@WebListener
public class ConnectDBListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ConnectDBListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	 //Craneprint_craneserverUI ui = (Craneprint_craneserverUI)UI.getCurrent();
    	 //ui.getDBManager().closeConnection();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
