package org.craneprint.craneserver.db;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Date;

import org.craneprint.craneserver.GCodeFile;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.vaadin.ui.Table;

public class DBManager {
	private final int port;
	private final String dbHost;
	private MongoClient mongo;
	private final String dbName;
	
	public DBManager(int p, String host, String name){
		this.port = p;
		this.dbHost = host;
		this.dbName = name;
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
	
	public void addFileToQueue(int printerId, GCodeFile f){
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = db.getCollection(Integer.toString(printerId));
	 
		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("name", f.getName());
		document.put("notes", f.getNotes());
		document.put("id", coll.getCount() + 1);
		document.put("user", /*TODO: Get the Actual User*/"testUser");
		document.put("path", f.getFile().getPath());
		document.put("filamentUsage", GCodeFile.calculateFilamentUsage(f));
		document.put("filamentUsed", 0);
		document.put("printStatus", PrintStatus.IN_QUE);
		document.put("addedTime", new Date().getTime());
		document.put("printedTime", -1L);
		coll.insert(document);
	}
	
	public void updatePrintFailure(int printerId){
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl(printerId);
		
		/**** Update ****/
		// search document for the currently printing file
		BasicDBObject query = new BasicDBObject();
		query.put("printStatus", PrintStatus.PRINTING);
		DBCursor cursor = coll.find(query);
		BasicDBObject n = null;
		while (cursor.hasNext()) {
			n = (BasicDBObject)cursor.next();
		}
	 
		BasicDBObject newDocument = new BasicDBObject();
		// Don't think a cancelled/failed print needs a time...
		// newDocument.put("printedTime", new Date().getTime());
		newDocument.put("filamentUsed", /*TODO: make this dynamic ie., if the printer sends a 'filamentUsed' var, then use it!*/0);
		newDocument.put("printStatus", PrintStatus.CANCELLED);
	 
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);
	 
		coll.update(query, updateObj);
	}
	
	public boolean isQueueEmpty(int printerId){
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = db.getCollection(Integer.toString(printerId));
		
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
		DBObject clause1 = new BasicDBObject("printStatus", PrintStatus.PRINTING);  
		DBObject clause2 = new BasicDBObject("printStatus", PrintStatus.IN_QUE);    
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		DBObject query = new BasicDBObject("$or", or);
		return query;
	}
	
	private DBCollection getColl(int id){
		return this.getDB().getCollection(Integer.toString(id));
	}
	
	public void finishFile(int printerId, int status){
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl(printerId);
		
		/**** Update ****/
		// search document for the oldest file still waiting, and update it with new values
		BasicDBObject query = new BasicDBObject();
		query.put("printStatus", PrintStatus.PRINTING);
		DBCursor cursor = coll.find(query);
		BasicDBObject n = null;
		while (cursor.hasNext()) {
			n = (BasicDBObject)cursor.next();
		}
	 
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("printedTime", new Date().getTime());
		newDocument.put("filamentUsed", n.getLong("filamentUsage"));
		newDocument.put("printStatus", status);
	 
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);
	 
		coll.update(query, updateObj);
	}
	
	public GCodeFile getNextInQueue(int printerId){
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl(printerId);
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("printStatus", PrintStatus.IN_QUE);
		
		long oldest = 0;
		BasicDBObject oldestOne = null;
		DBCursor cursor = coll.find(searchQuery);
		while (cursor.hasNext()) {
			BasicDBObject n = (BasicDBObject)cursor.next();
			if(oldest == 0 || n.getLong("addedTime") < oldest){
				oldestOne = n;
				oldest = n.getLong("addedTime");
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
			newDocument.put("printStatus", PrintStatus.PRINTING);
			
			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
			
			coll.update(query, updateObj);
		}
		// Finally return the file that we shall print @returns null if there are no files to print:(
		return toPrint;
	}
	
	public Table getPrintsForUser(String user){
		// TODO: Check this for all printer collections <-- VERY IMPORTANT!!!
		/**** Get database ****/
		DB db = this.getDB();
	 
		/**** Get collection / table from the printer's collection ****/
		DBCollection coll = getColl(/*TODO: REPLACE THIS SOON*/0);
		
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
		t.addContainerProperty("Status", String.class, null);
		t.addContainerProperty("Filament Required(g)", Integer.class, null);
		t.addContainerProperty("Filament Used(g)", Integer.class, null);
		t.addContainerProperty("Time Added", String.class, null);
		t.addContainerProperty("Time Printed", String.class, null);
		
		DBCursor cursor = coll.find(searchQuery);
		int row = 0;
		while(cursor.hasNext()){
			BasicDBObject n = (BasicDBObject)cursor.next();
			t.addItem(this.getPrintRow(n), ++row);
		}
		
		return t;
	}
	
	private Object[] getPrintRow(BasicDBObject n){
		Object[] o = new Object[/*Number of Columns*/6];
		o[0] = n.getString("name");
		o[1] = PrintStatus.resolveToString(n.getInt("printStatus"));
		o[2] = n.getInt("filamentUsage");
		o[3] = n.getInt("filamentUsed");
		o[4] = new Date(n.getLong("addedTime")).toGMTString();
		long p = n.getLong("printedTime");
		if(p == -1)
			o[5] = "Not Yet Complete";
		else
			o[5] = new Date(p).toGMTString();
		return o;
	}
	
}
