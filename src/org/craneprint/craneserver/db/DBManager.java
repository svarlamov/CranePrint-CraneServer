package org.craneprint.craneserver.db;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.ServletContext;

import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.gcode.PrintStatus;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.user_composites.MyPrintsTab;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class DBManager {
	private final int port;
	private final String dbHost;
	private MongoClient mongo;
	private final String dbName;
	private ServletContext context;
	
	public DBManager(int p, String host, String name, ServletContext sc){
		this.port = p;
		this.dbHost = host;
		this.dbName = name;
		this.context = sc;
		this.connectToDB();
	}
	
	private void connectToDB(){
		try {
			mongo = new MongoClient("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		mongo.close();
	}
	
	private DB getDB(){
		return mongo.getDB(dbName);
	}
	
	private DBCollection getColl(String s){
		return this.getDB().getCollection(s);
	}
	
	public void addFileToQueue(int printerId, GCodeFile f){
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = getColl("printer" + printerId);
		
		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("name", f.getName());
		document.put("notes", f.getNotes());
		document.put("id", coll.getCount() + 1);
		document.put("user", /*TODO: Get the Actual User*/"testUser");
		document.put("path", f.getFile().getPath());
		document.put("filament_usage", GCodeFile.calculateFilamentUsage(f));
		document.put("filament_used", 0);
		document.put("print_status", PrintStatus.IN_QUE);
		document.put("added_time", new Date().getTime());
		document.put("printed_time", -1L);
		coll.insert(document);
	}
	
	public void updatePrintFailure(int printerId){
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl("printer" + printerId);
		
		/**** Update ****/
		// search document for the currently printing file
		BasicDBObject query = new BasicDBObject();
		query.put("print_status", PrintStatus.PRINTING);
		DBCursor cursor = coll.find(query);
		BasicDBObject n = null;
		while (cursor.hasNext()) {
			n = (BasicDBObject)cursor.next();
		}
	 
		BasicDBObject newDocument = new BasicDBObject();
		// Don't think a cancelled/failed print needs a time...
		// newDocument.put("printedTime", new Date().getTime());
		newDocument.put("filament_used", /*TODO: make this dynamic ie., if the printer sends a 'filamentUsed' var, then use it!*/0);
		newDocument.put("print_status", PrintStatus.CANCELLED);
	 
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);
	 
		coll.update(query, updateObj);
	}
	
	public boolean isQueueEmpty(int printerId){
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = getColl("printer" + printerId);
		
		/**** Find and display ****/
		DBObject searchQuery = buildStatusQuery();
		
		boolean empty = true;
		DBCursor cursor = coll.find(searchQuery);
		if(cursor.size() > 0){
			empty = false;
		}
		return empty;
	}
	
	private DBObject buildStatusQuery(){
		DBObject clause1 = new BasicDBObject("print_status", PrintStatus.PRINTING);  
		DBObject clause2 = new BasicDBObject("print_status", PrintStatus.IN_QUE);    
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		DBObject query = new BasicDBObject("$or", or);
		return query;
	}
	
	public void finishFile(int printerId, int status){
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl("printer" + printerId);
		
		/**** Update ****/
		// search document for the oldest file still waiting, and update it with new values
		BasicDBObject query = new BasicDBObject();
		query.put("print_status", PrintStatus.PRINTING);
		DBCursor cursor = coll.find(query);
		BasicDBObject n = null;
		while (cursor.hasNext()) {
			n = (BasicDBObject)cursor.next();
		}
		if(n != null){
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("printed_time", new Date().getTime());
			newDocument.put("filament_used", n.getLong("filament_usage"));
			newDocument.put("print_status", status);
	 
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
	 
			coll.update(query, updateObj);
		} else {
			System.out.println("Error, No Files are Currently Printing? What am I doing wrong?!");
		}
	}
	
	public boolean cancelPrint(BasicDBObject b, int pid){
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl("printer" + pid);
		
		/**** Update ****/
		// search document for the correct file and 
		BasicDBObject query = new BasicDBObject();
		query.put("id", b.get("id"));
		DBCursor cursor = coll.find(query);
		BasicDBObject n = null;
		while (cursor.hasNext()) {
			n = (BasicDBObject)cursor.next();
		}
		if(n != null){
			if(n.getInt("print_status") == PrintStatus.IN_QUE){
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("print_status", PrintStatus.CANCELLED);
				newDocument.put("printed_time", -2);
				BasicDBObject updateObj = new BasicDBObject();
				updateObj.put("$set", newDocument);
				coll.update(query, updateObj);
				return true;
			} else if(n.getInt("print_status") == PrintStatus.CANCELLED){
				return true;
			}
		}
		return false;
	}
	
	public GCodeFile getNextInQueue(int printerId){
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl("printer" + printerId);
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("print_status", PrintStatus.IN_QUE);
		
		long oldest = 0;
		BasicDBObject oldestOne = null;
		DBCursor cursor = coll.find(searchQuery);
		while (cursor.hasNext()) {
			BasicDBObject n = (BasicDBObject)cursor.next();
			if(oldest == 0 || n.getLong("added_time") < oldest){
				oldestOne = n;
				oldest = n.getLong("added_time");
			}
		}
		
		// Create GCodeFile only if there is a file to be printed
		GCodeFile toPrint = null;
		if(oldestOne != null) {
			toPrint = new GCodeFile(new File(oldestOne.getString("path")), oldestOne.getString("name"), oldestOne.getString("notes"), oldestOne.getString("user"), oldestOne.getLong("id"));
		
			/**** Update ****/
			// search document for the oldest file still waiting, and update it with new values
			BasicDBObject query = new BasicDBObject();
			query.put("id", oldestOne.getLong("id"));
			
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("print_status", PrintStatus.PRINTING);
			
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
			
			coll.update(query, updateObj);
		}
		// Finally return the file that we shall print @returns null if there are no files to print:(
		return toPrint;
	}
	
	public Table getPrintsForUser(String user, MyPrintsTab mpt){
		// TODO: Check this for all printer collections <-- VERY IMPORTANT!!!
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl(/*TODO: REPLACE THIS SOON*/"printer" + 0);
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("user", user);
		
		/**** Initialize the table ****/
		Table t = new Table();
		// The code below has static columns, defined in the code... Possibly dynamic columns
		// might make sense for easier maintenance later on, but the performance hit might
		// be pretty massive given all of the loops and stuff to be iterated to dynamically
		// create all of the rows. Plus, it doesn't seem, at least now, that this will
		// be A) User configurable or B) Updated very frequently
		
		// Create a table such that
		// Name   Status   Filament Required(g)   Filament Used(g)   Time Added   Time Printed
		// ...    ...      ...                    ...                ...          ...
		t.addContainerProperty("Name", String.class, null);
		//t.addContainerProperty("Status", String.class, null);
		t.addContainerProperty("Status", VerticalLayout.class, null);
		t.addContainerProperty("Filament Required(g)", Integer.class, null);
		t.addContainerProperty("Filament Used(g)", Integer.class, null);
		t.addContainerProperty("Printer", String.class, null);
		t.addContainerProperty("Time Added", String.class, null);
		t.addContainerProperty("Time Printed", String.class, null);
		
		DBCursor cursor = coll.find(searchQuery);
		int row = 0;
		while(cursor.hasNext()){
			BasicDBObject n = (BasicDBObject)cursor.next();
			t.addItem(this.getPrintRow(n, mpt), ++row);
		}
		
		return t;
	}
	
	private Object[] getPrintRow(BasicDBObject n, MyPrintsTab mpt){
		// TODO: Replace with list of printers and search all collections etc...
		int printerId = 0;
		Object[] o = new Object[/*Number of Columns*/7];
		o[0] = n.getString("name");
		// TODO: Add a cancel button here!
		if(n.getInt("print_status") == PrintStatus.IN_QUE){
			VerticalLayout v = new VerticalLayout();
			Button b = new Button();
			b.setCaption("Cancel");
			b.addClickListener(new ClickListener(){
			    public void buttonClick(ClickEvent event){
			        boolean b = cancelPrint(n, printerId);
			        if(b) {
			        	new Notification("Print Cancelled",
			        		    "Print Cancelled",
			        		    Notification.TYPE_TRAY_NOTIFICATION, true)
			        		    .show(Page.getCurrent());
			        	mpt.refreshTable();
			        } else {
			        	new Notification("Error",
			        		    "Print Can Not Be Cancelled",
			        		    Notification.TYPE_TRAY_NOTIFICATION, true)
			        		    .show(Page.getCurrent());
			        	mpt.refreshTable();
			        }
			    }
			});
			v.addComponent(b);
			o[1] = v;
		} else {
			Label l = new Label();
			l.setValue(PrintStatus.resolveToString(n.getInt("print_status")));
			VerticalLayout vl = new VerticalLayout();
			vl.addComponent(l);
			o[1] = vl;
		}
		o[2] = n.getInt("filament_usage");
		o[3] = n.getInt("filament_used");
		PrintersManager pm = (PrintersManager)context.getAttribute("org.craneprint.craneserver.printers.printersManager");
		o[4] = pm.getPrinter(printerId).getName();
		o[5] = new Date(n.getLong("added_time")).toGMTString();
		long p = n.getLong("printed_time");
		if(p == -1)
			o[6] = "Not Yet Complete";
		else if(p == -2)
			o[6] = "Cancelled";
		else
			o[6] = new Date(p).toGMTString();
		return o;
	}
	
}
