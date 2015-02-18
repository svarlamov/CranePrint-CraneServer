package org.craneprint.craneserver.ui;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.queue.QueueManager;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("craneprint_craneserver")
public class Craneprint_craneserverUI extends UI implements DetachListener{
	private ServletContext servletContext;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Craneprint_craneserverUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		servletContext = VaadinServlet.getCurrent().getServletContext();
		Enumeration<String> en = servletContext.getAttributeNames();
		while(en.hasMoreElements()){
			System.out.println(en.nextElement());
		}
		PrintComposite pc = new PrintComposite(this);
		setContent(pc);
	}
	
	@Override
    public void detach(DetachEvent event) {
		// TODO: Make sure that I still don't have to do anything on close of the HTTP session
	}
	
	public PrintersManager getPrintersManager(){
		return (PrintersManager)servletContext.getAttribute("printersManager");
	}
	
	public DBManager getDBManager(){
		return (DBManager)servletContext.getAttribute("dbManager");
	}
	
	public QueueManager getQueueManager(){
		return (QueueManager)servletContext.getAttribute("queueManager");
	}
}