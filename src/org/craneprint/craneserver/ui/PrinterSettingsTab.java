package org.craneprint.craneserver.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;

public class PrinterSettingsTab extends CustomComponent {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	
	private Craneprint_craneserverUI ui;
	private int printerId;
	private Accordion generalAccordion;
	private Accordion extruderAccordion;
	private Accordion shapeAccordion;
	private Accordion webcamAccordion;
	private Accordion gcodeAccordion;
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
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		generalAccordion = new Accordion();
		generalAccordion.setCaption("General Setttings");
		generalAccordion.setImmediate(false);
		generalAccordion.setWidth("100%");
		generalAccordion.setHeight("-1px");
		mainLayout.addComponent(generalAccordion);
		
		extruderAccordion = new Accordion();
		extruderAccordion.setCaption("Extruder Setttings");
		extruderAccordion.setImmediate(false);
		extruderAccordion.setWidth("100%");
		extruderAccordion.setHeight("-1px");
		mainLayout.addComponent(extruderAccordion);
		
		shapeAccordion = new Accordion();
		shapeAccordion.setCaption("Printer Shape Setttings");
		shapeAccordion.setImmediate(false);
		shapeAccordion.setWidth("100%");
		shapeAccordion.setHeight("-1px");
		mainLayout.addComponent(shapeAccordion);
		
		webcamAccordion = new Accordion();
		webcamAccordion.setCaption("Webcam Setttings");
		webcamAccordion.setImmediate(false);
		webcamAccordion.setWidth("100%");
		webcamAccordion.setHeight("-1px");
		mainLayout.addComponent(webcamAccordion);
		
		gcodeAccordion = new Accordion();
		gcodeAccordion.setCaption("GCode Setttings");
		gcodeAccordion.setImmediate(false);
		gcodeAccordion.setWidth("100%");
		gcodeAccordion.setHeight("-1px");
		mainLayout.addComponent(gcodeAccordion);
	}

}
