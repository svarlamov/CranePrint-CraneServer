package org.craneprint.craneserver;

import javax.servlet.annotation.WebServlet;

import org.craneprint.craneserver.db.DBManager;
import org.craneprint.craneserver.db.QueueManager;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("craneprint_craneserver")
public class Craneprint_craneserverUI extends UI implements DetachListener{
	//TODO: get these from the config file
	private final int port = 27017;
	private final String dbHost = "localhost";
	private final String dbName = "craneprintdb";
	private DBManager db;
	private QueueManager qm;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Craneprint_craneserverUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		this.qm = new QueueManager();
		setupDB();
		PrintComposite pc = new PrintComposite();
		setContent(pc);
	}
	
	@Override
    public void detach(DetachEvent event) {
        db.closeConnection();
    }
	
	private void setupDB(){
		db = new DBManager(port, dbHost, dbName);
	}
	
	public DBManager getDBManager(){
		return db;
	}
	
	public QueueManager getQueueManager(){
		return qm;
	}
}