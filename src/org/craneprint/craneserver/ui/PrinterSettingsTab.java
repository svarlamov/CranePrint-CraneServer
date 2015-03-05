package org.craneprint.craneserver.ui;

import org.craneprint.craneserver.db.DBManager;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PrinterSettingsTab extends CustomComponent {

	@AutoGenerated
	private VerticalLayout mainLayout;
	
	private Craneprint_craneserverUI ui;
	private int printerId;
	private HorizontalLayout generalLayout;
	private Accordion extruderAccordion;
	private Accordion shapeAccordion;
	private Accordion webcamAccordion;
	private Accordion gcodeAccordion;
	private Accordion mainAccordion;
	private Label nameLabel;
	private TextField nameField;
	private Label ipLabel;
	private TextField ipField;
	private Label portLabel;
	private TextField portField;
	private Label passwordLabel;
	private PasswordField passwordField;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public PrinterSettingsTab(int pid, Craneprint_craneserverUI u) {
		ui = u;
		printerId = pid;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
	}

	@AutoGenerated
	private void buildMainLayout() {
		// the main layout and components will be created here
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		mainAccordion = new Accordion();
		mainAccordion.setImmediate(false);
		mainAccordion.setWidth("100%");
		mainAccordion.setHeight("-1px");
		mainLayout.addComponent(mainAccordion);
		
		generalLayout = makeGeneralSettingsView();
		mainAccordion.addTab(generalLayout, "General Settings");
	}
	
	private HorizontalLayout makeGeneralSettingsView() {
		DBManager db = ui.getDBManager();
		HorizontalLayout hl = new HorizontalLayout();
		VerticalLayout lvl = new VerticalLayout();
		VerticalLayout rvl = new VerticalLayout();
		
		nameLabel = new Label();
		nameLabel.setImmediate(false);
		nameLabel.setValue("Printer Name: ");
		nameLabel.setWidth("-1px");
		nameLabel.setHeight("40px");
		lvl.addComponent(nameLabel);
		
		ipLabel = new Label();
		ipLabel.setImmediate(false);
		ipLabel.setValue("Printer IP: ");
		ipLabel.setWidth("-1px");
		ipLabel.setHeight("40px");
		lvl.addComponent(ipLabel);
		
		portLabel = new Label();
		portLabel.setImmediate(false);
		portLabel.setValue("Server Port: ");
		portLabel.setWidth("-1px");
		portLabel.setHeight("40px");
		lvl.addComponent(portLabel);
		
		passwordLabel = new Label();
		passwordLabel.setImmediate(false);
		passwordLabel.setValue("Printer Password: ");
		passwordLabel.setWidth("-1px");
		passwordLabel.setHeight("40px");
		lvl.addComponent(passwordLabel);
		
		hl.addComponent(lvl);
		
		nameField = new TextField();
		nameField.setImmediate(false);
		nameField.setValue((String)db.getPrinterProperty(printerId, "name"));
		nameField.setWidth("-1px");
		nameField.setHeight("-1px");
		rvl.addComponent(nameField);
		
		ipField = new TextField();
		ipField.setImmediate(false);
		ipField.setValue((String)db.getPrinterProperty(printerId, "ip"));
		ipField.setWidth("-1px");
		ipField.setHeight("-1px");
		rvl.addComponent(ipField);
		
		portField = new TextField();
		portField.setImmediate(false);
		portField.setValue(((Integer)db.getPrinterProperty(printerId, "port")).toString());
		portField.setWidth("-1px");
		portField.setHeight("-1px");
		rvl.addComponent(portField);
		
		passwordField = new PasswordField();
		passwordField.setImmediate(false);
		passwordField.setWidth("-1px");
		passwordField.setHeight("-1px");
		rvl.addComponent(passwordField);
		
		hl.addComponent(rvl);
		
		return hl;
	}
	
	protected void saveConfig(){
		if(nameField.getValue().length() > 0 && ipField.getValue().length() > 0 && portField.getValue().length() > 0){
			ui.getDBManager().setPrinterProperty(printerId, "name", nameField.getValue());
			ui.getDBManager().setPrinterProperty(printerId, "ip", ipField.getValue());
			ui.getDBManager().setPrinterProperty(printerId, "port", new Integer(portField.getValue()));
		} else {
			new Notification("Empty Field", "All Required Fields Must Be Completed in Order to Save", Notification.Type.ERROR_MESSAGE);
		}
	}

}
