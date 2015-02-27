package org.craneprint.craneserver.ui;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.printers.PrintersManager;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@Theme("craneprint_craneserver")
public class Craneprint_craneserverUI extends UI implements DetachListener{
	private ServletContext servletContext;
	private PrintComposite pc;
	private LoginWindow loginWindow;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Craneprint_craneserverUI.class)
	public static class Servlet extends VaadinServlet { 
	}

	@Override
	protected void init(VaadinRequest request) {
		loginWindow = new LoginWindow(this);
		this.addWindow(loginWindow);
	}
	
	protected void showUI(){
		servletContext = VaadinServlet.getCurrent().getServletContext();
		Enumeration<String> en = servletContext.getAttributeNames();
		while(en.hasMoreElements()){
			System.out.println(en.nextElement());
		}
		pc = new PrintComposite(this);
		this.removeWindow(loginWindow);
		this.setContent(pc);
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
	
	/*
	 * Not Used
	public QueueManager getQueueManager(){
		return (QueueManager)servletContext.getAttribute("org.craneprint.craneserver.queue.queueManager");
	}*/
}