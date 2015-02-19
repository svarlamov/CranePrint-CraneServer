package org.craneprint.craneserver.printers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Servlet implementation class InitPrintersManager
 */
@WebListener
public class InitPrintersManager implements ServletContextListener {
	PrintersManager pm;

    /**
     * Default constructor. 
     */
    public InitPrintersManager() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	pm = new PrintersManager();
    	pm.loadAll();
    	arg0.getServletContext().setAttribute("org.craneprint.craneserver.printers.printersManager", pm);
    }
	
}
