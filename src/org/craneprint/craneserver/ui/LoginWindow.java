package org.craneprint.craneserver.ui;

import org.craneprint.craneserver.users.Authenticator;

import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.Window;

public class LoginWindow extends Window implements LoginEventListener {
	Authenticator authObj;
	Craneprint_craneserverUI ui;
	
	
	public LoginWindow(Craneprint_craneserverUI u) {
		ui = u;
		authObj = new Authenticator();
		setContent(new LoginView(this));
	}

	protected void showMainUI() {
		// entity manager
		setContent(new LoginView(this));
	}

	@Override
	public void handleLoginEvent(LoginEvent event) {
		/*
		 * TODO: Login!
		String username = event.getLoginParameter("username");
		String password = event.getLoginParameter("password");
		if (Authenticator.login(username, password)) {
			showMainUI();
		}
		*/
		if(authObj.login(event.getLoginParameter("username"), event.getLoginParameter("password"))){
			ui.showUI();
		}
	}
}
