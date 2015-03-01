package org.craneprint.craneserver.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.craneprint.craneserver.printers.HandShake;
import org.craneprint.craneserver.printers.PrintersManager;
import org.json.simple.parser.ParseException;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ClerkComposite extends CustomComponent {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private HorizontalSplitPanel printingHorizSplitPanel;
	@AutoGenerated
	private TabSheet detailsTabSheet;
	@AutoGenerated
	private VerticalLayout leftVertical;
	@AutoGenerated
	private Accordion printerAccordion;
	@AutoGenerated
	private Panel printerPanel;
	private AdminHelpTab adminHelpTab;
	private Button logoutButton;
	private Button printViewButton;
	
	Craneprint_craneserverUI ui = null;
	private PrintersManager printersManager;
	private Button closeButton;
	private ArrayList<PrintsForPrinterTab> printsTabs = new ArrayList<PrintsForPrinterTab>();
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ClerkComposite(Craneprint_craneserverUI u) {
		ui = u;
		buildMainLayout();
		setCompositionRoot(mainLayout);
		
		printersManager = ui.getPrintersManager();
		for(int i = 0; i < printersManager.getSize(); i++){
			PrinterTabComposite ptb = new PrinterTabComposite(ui);
			printerAccordion.addTab(ptb, printersManager.getPrinter(i).getName());
			ptb.fillComposite();
		}
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// horizontalSplitPanel_1
		printingHorizSplitPanel = buildHorizontalSplitPanel_1();
		mainLayout
				.addComponent(printingHorizSplitPanel, "top:7.0px;left:7.0px;");
		printingHorizSplitPanel.setSplitPosition(333, Unit.PIXELS);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalSplitPanel buildHorizontalSplitPanel_1() {
		// common part: create layout
		printingHorizSplitPanel = new HorizontalSplitPanel();
		printingHorizSplitPanel.setImmediate(false);
		printingHorizSplitPanel.setWidth("100%");
		printingHorizSplitPanel.setHeight("100%");
		printingHorizSplitPanel.setLocked(true);
		
		// verticalLayout_1
		leftVertical = buildVerticalLayout_1();
		printingHorizSplitPanel.addComponent(leftVertical);
		
		// tabSheet_1
		detailsTabSheet = new TabSheet();
		detailsTabSheet.setImmediate(false);
		detailsTabSheet.setWidth("100.0%");
		detailsTabSheet.setHeight("100.0%");
		printingHorizSplitPanel.addComponent(detailsTabSheet);
		
		adminHelpTab = new AdminHelpTab(ui);
		int i = 0;
		for(String s : ui.getDBManager().getAllPrinterCollectionNames()){
			printsTabs.add(new PrintsForPrinterTab(i, ui));
			i++;
		}
		i = 0;
		for(PrintsForPrinterTab pfpt : printsTabs){
			detailsTabSheet.addTab(pfpt, "Prints for " + ui.getPrintersManager().getPrinter(i).getName());
			i++;
		}
		detailsTabSheet.addTab(adminHelpTab, "Help");
		
		return printingHorizSplitPanel;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_1() {
		// common part: create layout
		leftVertical = new VerticalLayout();
		leftVertical.setImmediate(false);
		leftVertical.setWidth("98.0%");
		leftVertical.setHeight("-1px");
		leftVertical.setMargin(false);
		
		logoutButton = new Button("Logout");
		logoutButton.setWidth("100%");
		logoutButton.setHeight("-1px");
		logoutButton.addClickListener(new ClickListener(){
			public void buttonClick(ClickEvent event){
				logout();
				ui.removeUI();
				ui.showLogin();
			}
		});
		leftVertical.addComponent(logoutButton);
		
		printViewButton = new Button("Printing View");
		printViewButton.setWidth("100%");
		printViewButton.setHeight("-1px");
		printViewButton.addClickListener(new ClickListener(){
			public void buttonClick(ClickEvent event){
				ui.showPrintingUI();
			}
		});
		leftVertical.addComponent(printViewButton);
		
		closeButton = new Button("Save Configuration");
		closeButton.setImmediate(false);
		closeButton.setWidth("100%");
		closeButton.setHeight("-1px");
		closeButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
		        // TODO: Save Configuration etc.
		    }
		});
		leftVertical.addComponent(closeButton);
		
		// accordion_3
		printerAccordion = new Accordion();
		printerAccordion.setImmediate(true);
		printerAccordion.setWidth("100.0%");
		printerAccordion.setHeight("-1px");
		
		// printerPanel
		printerPanel = new Panel("Available Printers");
		printerPanel.setWidth("100%");
		printerPanel.setHeight("-1px");
		printerPanel.setContent(printerAccordion);
		leftVertical.addComponent(printerPanel);
		
		return leftVertical;
	}
	
	public HandShake doHandShake() throws IOException, ParseException{
		return printersManager.doHandShake(printerAccordion.getTabIndex());
	}
	
	private void logout(){
		ui.getSessionUser().setLoggedOut();
	}

}
