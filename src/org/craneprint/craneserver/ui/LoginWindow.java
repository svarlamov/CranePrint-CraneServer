package org.craneprint.craneserver.ui;

import org.craneprint.craneserver.users.Authenticator;
import org.craneprint.craneserver.users.User;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

public class LoginWindow extends Window implements LoginEventListener {
	Authenticator authObj;
	Craneprint_craneserverUI ui;
	
	
	public LoginWindow(Craneprint_craneserverUI u) {
		ui = u;
		authObj = new Authenticator();
		setContent(new LoginView(this));
		this.setClosable(false);
		this.setResizable(false);
		this.setDraggable(false);
		// Look at the movie theater ticket sales example project online and check out their login system
		this.setSizeFull();
	}

	protected void showMainUI() {
		// entity manager
		setContent(new LoginView(this));
	}

	@Override
	public void handleLoginEvent(LoginEvent event) {
		boolean success = false;
		try {
			success = authObj.login(event.getLoginParameter("username").toUpperCase(), event.getLoginParameter("password"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new Notification("Login Failed", "There was an Issue Authenticating with the Server", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
			e.printStackTrace();
		}
		if(success){
			User user = new User(event.getLoginParameter("username").toUpperCase(), true);
			user.initPerms();
			ui.setSessionUser(user);
			ui.showUI();
		} else
			new Notification("Login Failed", "Check that Your Username and Password are Correct", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
	}
}
