package org.craneprint.craneserver.db;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Date;

import org.craneprint.craneserver.GCodeFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.vaadin.ui.Table;

public class OLDDBManager {
	private static int port = 27017;
	private static String dbHost = "localhost";
	private static MongoClient mongo;
	private static String dbName = "craneprintdb";
	
	protected static void connectToDB(){
		try {
			mongo = new MongoClient("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	protected static void closeConnection(){
		mongo.close();
	}
	
	protected static void addFileToQueue(int printerId, GCodeFile f){
		/**** Get database ****/
		// if database doesn't exist, MongoDB will create it for you
		DB db = mongo.getDB(dbName);
	 
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
	
	protected static boolean isQueueEmpty(int printerId){
		/**** Get database ****/
		// if database doesn't exist, MongoDB will create it for you
		DB db = mongo.getDB(dbName);
	 
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = db.getCollection(Integer.toString(printerId));
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("printStatus", PrintStatus.IN_QUE);
		
		boolean empty = true;
		DBCursor cursor = coll.find(searchQuery);
		if(cursor.size() > 0){
			empty = false;
		}
		
		if(empty){
			searchQuery = new BasicDBObject();
			searchQuery.put("printStatus", PrintStatus.PRINTING);
			cursor = coll.find(searchQuery);
			if(cursor.size() > 0){
				empty = false;
			}
		}
		return empty;
	}
	
	protected static void finishFile(int printerId, int status){
		/**** Get database ****/
		// if database doesn't exist, MongoDB will create it for you
		DB db = mongo.getDB(dbName);
	 
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = db.getCollection(Integer.toString(printerId));
		
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
	
	// Arbitrary Method
	// TODO: Delete, when ready
	/*protected static void updateFile(int printerId, long printId, int status){
		/**** Get database ****
		// if database doesn't exist, MongoDB will create it for you
		DB db = mongo.getDB(dbName);
	 
		/**** Get collection / table from the printer's collection ****
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection table = db.getCollection(Integer.toString(printerId));
		
		/**** Update ****
		// search document for the oldest file still waiting, and update it with new values
		BasicDBObject query = new BasicDBObject();
		query.put("id", printId);
		DBCursor cursor = table.find(query);
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
	 
		table.update(query, updateObj);
	}*/
	
	protected static GCodeFile getNextInQueue(int printerId){
		/**** Get database ****/
		// if database doesn't exist, MongoDB will create it for you
		DB db = mongo.getDB(dbName);
	 
		/**** Get collection / table from the printer's collection ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = db.getCollection(Integer.toString(printerId));
		
		/**** Find and display ****/
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("printStatus", PrintStatus.IN_QUE);
		
		long oldest = 0;
		BasicDBObject oldestOne = null;
		boolean firstTime = true;
		DBCursor cursor = coll.find(searchQuery);
		while (cursor.hasNext()) {
			BasicDBObject n = (BasicDBObject)cursor.next();
			if(firstTime || (n.getLong("addedTime") < oldest && PrintStatus.IN_QUE == n.getInt("printStatus"))){
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
	
	public static Table getPrintsForUser(String user){
		// TODO: Check this for all printer collections <-- VERY IMPORTANT!!!
		/**** Get database ****/
		// if database doesn't exist, MongoDB will create it for you
		DB db = mongo.getDB(dbName);
	 
		/**** Get collection / table from TODO: all of the printers' collections ****/
		// if collection doesn't exist, MongoDB will create it for you
		DBCollection coll = db.getCollection(/*TODO: replace this soon!!!*/Integer.toString(0));
		
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
		// TODO: Convert status to a string!
		while(cursor.hasNext()){
			BasicDBObject n = (BasicDBObject)cursor.next();
			t.addItem(new Object[]{n.getString("name"), PrintStatus.resolveToString(n.getInt("printStatus")), n.getInt("filamentUsage"), n.getInt("filamentUsed"), new Date(n.getLong("addedTime")).toGMTString(), new Date(n.getLong("printedTime")).toGMTString()}, ++row);
		}
		
		return t;
	}
	
}
