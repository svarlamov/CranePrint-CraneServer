package org.craneprint.craneserver.ui;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.users.User;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@Theme("craneprint_craneserver")
public class Craneprint_craneserverUI extends UI implements DetachListener, Serializable{
	private static final long serialVersionUID = 8923663715888618200L;
	private ServletContext servletContext;
	private PrintComposite pc;
	private AdminComposite ac;
	private ClerkComposite cc;
	private LoginWindow loginWindow;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Craneprint_craneserverUI.class)
	public static class Servlet extends VaadinServlet implements Serializable{
		private static final long serialVersionUID = 7673663715888618137L; 
	}

	@Override
	protected void init(VaadinRequest request) {
		if(getSessionUser() != null && getSessionUser().isLoggedIn())
			showPrintingUI();
		else {
			showLogin();
		}
	}
	
	protected void showPrintingUI(){
		servletContext = VaadinServlet.getCurrent().getServletContext();
		pc = new PrintComposite(this);
		this.removeWindow(loginWindow);
		this.setContent(pc);
	}
	
	protected void showAdminUI(){
		servletContext = VaadinServlet.getCurrent().getServletContext();
		ac = new AdminComposite(this);
		for(Window w : this.getWindows()){
			w.close();
		}
		this.removeWindow(loginWindow);
		this.setContent(ac);
	}
	
	protected void showClerckUI(){
		servletContext = VaadinServlet.getCurrent().getServletContext();
		cc = new ClerkComposite(this);
		for(Window w : this.getWindows()){
			w.close();
		}
		this.removeWindow(loginWindow);
		this.setContent(cc);
	}
	
	protected void showLogin(){
		loginWindow = new LoginWindow(this);
		this.addWindow(loginWindow);
	}
	
	protected void removeUI(){
		this.setContent(null);
	}
	
	public User getSessionUser(){
		return VaadinSession.getCurrent().getAttribute(User.class);
	}
	
	protected void setSessionUser(User u){
		VaadinSession.getCurrent().setAttribute(User.class, u);
	}
	
	@Override
    public void detach(DetachEvent event) {
		// TODO: Make sure that I still don't have to do anything else on detachment
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