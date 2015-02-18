package org.craneprint.craneserver.queue;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class QueueLoader
 */
@WebServlet(description = "Load the queue once for use within the application", urlPatterns = { "/QueueLoader" })
public class QueueLoader extends HttpServlet implements ServletContextListener {
	private QueueManager qm;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueueLoader() {
        super();
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
    	qm = new QueueManager(arg0.getServletContext());
    	arg0.getServletContext().setAttribute("queueManager", qm);
    }

}
