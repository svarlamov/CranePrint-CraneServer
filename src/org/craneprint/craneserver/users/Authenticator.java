package org.craneprint.craneserver.users;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.craneprint.craneserver.db.DBManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.vaadin.server.VaadinServlet;

public class Authenticator {
	private String username;
	private boolean loggedIn = false;
	private final String USER_AGENT = "Mozilla/5.0";
	
	public Authenticator(){
		super();
	}
	
	public boolean login(String u, String p) throws Exception{
		username = u.toUpperCase();
		/*
		username = u.toUpperCase();
		String resp = sendPost(makeLoginJSON(username, p));
		JSONObject json = (JSONObject)new JSONParser().parse(resp);
		if(json.containsKey("Error"))
			return false;
		else if(json.containsKey("AuthenticationResult") && (long)json.get("AuthenticationResult") == 0){
			loggedIn = true;
			registerInDB(username, username.toLowerCase() + "@cranbrook.edu");
			return true;
		}
		else if(json.containsKey("AuthenticationResult") && (long)json.get("AuthenticationResult") == 1)
			return false;
		return false;*/
		return true;
	}
	
	public boolean logout(){
		loggedIn = false;
		return true;
	}
	
	public String getUsername(){
		return username;
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	@SuppressWarnings("deprecation")
	private String sendPost(String jsonParameters) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		String fResp = "";
		try {
		    HttpPost request = new HttpPost("https://cranbrook.myschoolapp.com/api/SignIn");
		    StringEntity params = new StringEntity(jsonParameters);
		    request.addHeader("Content-Type", "application/json");
		    request.addHeader("Accept","application/json");
		    request.setEntity(params);
		    
		    response = httpClient.execute(request);
		}catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    httpClient.getConnectionManager().shutdown();
		    fResp = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		}
		return fResp;
	}
	
	private String makeLoginJSON(String u, String p){
		JSONObject j = new JSONObject();
		j.put("From", "");
		j.put("InterfaceSource", "WebApp");
		j.put("Password", JSONObject.escape(p));
		j.put("Username", JSONObject.escape(u));
		j.put("remember", false);
		
		return j.toJSONString();
	}
	
	private void registerInDB(String un, String em){
		DBManager db = (DBManager)VaadinServlet.getCurrent().getServletContext().getAttribute("org.craneprint.craneserver.db.dbManager");
		db.registerUser(un, em);
	}
}
