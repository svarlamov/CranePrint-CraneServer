package org.craneprint.craneserver.ui;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.users.User;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("craneprint_craneserver")
@PreserveOnRefresh
public class Craneprint_craneserverUI extends UI implements DetachListener, Serializable{
	private static final long serialVersionUID = 8923663715888618200L;
	private ServletContext servletContext;
	private PrintComposite pc;
	private LoginWindow loginWindow;
	private User user = null;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Craneprint_craneserverUI.class)
	public static class Servlet extends VaadinServlet implements Serializable{
		private static final long serialVersionUID = 7673663715888618137L; 
	}

	@Override
	protected void init(VaadinRequest request) {
		loginWindow = new LoginWindow(this);
		this.addWindow(loginWindow);
	}
	
	protected void showUI(){
		servletContext = VaadinServlet.getCurrent().getServletContext();
		pc = new PrintComposite(this);
		this.removeWindow(loginWindow);
		this.setContent(pc);
	}
	
	public User getSessionUser(){
		return user;
	}
	
	protected void setSessionUser(User u){
		user = u;
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