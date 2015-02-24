package org.craneprint.craneserver.ui;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.queue.QueueManager;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("craneprint_craneserver")
public class Craneprint_craneserverUI extends UI implements DetachListener{
	private ServletContext servletContext;
	private PrintComposite pc;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Craneprint_craneserverUI.class)
	public static class Servlet extends VaadinServlet implements SessionDestroyListener {
	    @Override
	    public void sessionDestroy(SessionDestroyEvent event) {
	    	System.out.println("Session Destroyed!");
	        // Do session end stuff here
	    	Collection<UI> uis = event.getSession().getUIs();
	    	for(UI u : uis){
	    		if(u instanceof Craneprint_craneserverUI){
	    			Craneprint_craneserverUI cu = (Craneprint_craneserverUI)u;
	    			cu.pc.packMetaFiles();
	    		}
	    	}
	    }
	}

	@Override
	protected void init(VaadinRequest request) {
		servletContext = VaadinServlet.getCurrent().getServletContext();
		Enumeration<String> en = servletContext.getAttributeNames();
		while(en.hasMoreElements()){
			System.out.println(en.nextElement());
		}
		pc = new PrintComposite(this);
		setContent(pc);
	}
	
	@Override
    public void detach(DetachEvent event) {
		// TODO: Make sure that I still don't have to do anything on detachment
	}
	
	public PrintersManager getPrintersManager(){
		return (PrintersManager)servletContext.getAttribute("org.craneprint.craneserver.printers.printersManager");
	}
	
	public DBManager getDBManager(){
		return (DBManager)servletContext.getAttribute("org.craneprint.craneserver.db.dbManager");
	}
	
	public QueueManager getQueueManager(){
		return (QueueManager)servletContext.getAttribute("org.craneprint.craneserver.queue.queueManager");
	}
}