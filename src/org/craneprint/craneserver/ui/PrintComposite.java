package org.craneprint.craneserver.ui;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.craneprint.craneserver.gcode.CraneCodePacker;
import org.craneprint.craneserver.gcode.FolderLoad;
import org.craneprint.craneserver.gcode.GCodeFile;
import org.craneprint.craneserver.gcode.GCodeUploadedEvent;
import org.craneprint.craneserver.gcode.GCodeUploadedListener;
import org.craneprint.craneserver.gcode.GCodeUploader;
import org.craneprint.craneserver.printers.HandShake;
import org.craneprint.craneserver.printers.PrintersManager;
import org.craneprint.craneserver.users.User;
import org.json.simple.parser.ParseException;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class PrintComposite extends CustomComponent implements Serializable{
	
	private static final long serialVersionUID = 1374507230896642406L;
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private HorizontalSplitPanel printingHorizSplitPanel;
	@AutoGenerated
	private TabSheet detailsTabSheet;
	@AutoGenerated
	private VerticalLayout leftVertical;
	@AutoGenerated
	private Upload uploadComponent;
	@AutoGenerated
	private Accordion printerAccordion;
	@AutoGenerated
	private Panel filePanel;
	private Panel printerPanel;
	private SlicingTab slicingTab;
	private HelpTab helpTab;
	private MyPrintsTab myPrintsTab;
	private Button logoutButton;
	private Button adminButton;
	private Button clerkButton;
	
	Craneprint_craneserverUI ui = null;
	private GCodeUploader gcodeUploader = new GCodeUploader();
	private Accordion fileAccordion;
	private HashMap<GCodeFile, String> fileHashMap = new HashMap<GCodeFile, String>();
	private PrintersManager printersManager;
	private final File folder;
	private Button closeButton;
	private final User user;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public PrintComposite(Craneprint_craneserverUI u) {
		ui = u;
		user = ui.getSessionUser();
		
		folder = new File(System.getProperty("user.home") + File.separator + "CranePrint Uploads" + File.separator + user.getUsername());
		
		FolderLoad fl = new FolderLoad(folder.getPath());
		try {
			fileHashMap = fl.loadAllFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			new Notification("Error Loading Files",
                    "There was an Error Loading all of Your Files!",
                    Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
			e.printStackTrace();
		}
		
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
		
		slicingTab = new SlicingTab(ui);
		helpTab = new HelpTab(ui);
		myPrintsTab = new MyPrintsTab(ui);
		detailsTabSheet.addTab(slicingTab, "Slicing");
		detailsTabSheet.addTab(myPrintsTab, "My Prints");
		detailsTabSheet.addTab(helpTab, "Help");
		
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
		
		clerkButton = new Button("Print Clerk View");
		clerkButton.setWidth("100%");
		clerkButton.setHeight("-1px");
		clerkButton.addClickListener(new ClickListener(){
			public void buttonClick(ClickEvent event){
				ui.showClerckUI();
			}
		});
		leftVertical.addComponent(clerkButton);
		
		adminButton = new Button("Admin View");
		adminButton.setWidth("100%");
		adminButton.setHeight("-1px");
		adminButton.addClickListener(new ClickListener(){
			public void buttonClick(ClickEvent event){
				ui.showAdminUI();
			}
		});
		leftVertical.addComponent(adminButton);
		
		closeButton = new Button("Save Notes");
		closeButton.setImmediate(false);
		closeButton.setWidth("100%");
		closeButton.setHeight("-1px");
		closeButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
		        packMetaFiles();
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
		
		// upload_1
		uploadComponent = new Upload();
		uploadComponent.setReceiver(gcodeUploader);
		uploadComponent.addFailedListener(gcodeUploader);
		uploadComponent.addSucceededListener(gcodeUploader);
		gcodeUploader.addEventListener(new GCodeUploadedListener() {
			public void handleGCodeUploadedEvent(GCodeUploadedEvent e) {
		        addFileTab(e.getGCode());
		    }
		});
		uploadComponent.setImmediate(false);
		uploadComponent.setWidth("100%");
		uploadComponent.setHeight("-1px");
		leftVertical.addComponent(uploadComponent);
		
		// accordion_4
		fileAccordion = new Accordion();
		fileAccordion.setImmediate(false);
		fileAccordion.setWidth("100%");
		fileAccordion.setHeight("-1px");
		for(Entry<GCodeFile, String> g : fileHashMap.entrySet()){
			FileTabComposite ftc = new FileTabComposite(g.getValue(), g.getKey().getNotes());
			fileAccordion.addTab(ftc, g.getValue());
		}
		
		// filePanel
		filePanel = new Panel("GCode Files");
		filePanel.setWidth("100%");
		filePanel.setHeight("-1px");
		filePanel.setContent(fileAccordion);
		leftVertical.addComponent(filePanel);
		
		return leftVertical;
	}
	
	public void addFileTab(GCodeFile g){
		for(Entry<GCodeFile, String> e : fileHashMap.entrySet()){
			if(e.getValue().equals(g.getName())/*&& e.getKey().isDeleted()*/)
				return;
		}
		fileHashMap.put(g, g.getName());
		fileAccordion.addTab(new FileTabComposite(g.getName(), g.getNotes()), g.getName());
    }
	
	public HandShake doHandShake() throws IOException, ParseException{
		return printersManager.doHandShake(printerAccordion.getTabIndex());
	}
	
	public void deleteFile(){
		// Called by the FileTabComposite in order to delete the GCodeFile
		FileTabComposite ft = (FileTabComposite)fileAccordion.getSelectedTab();
		for(Entry<GCodeFile, String> e : fileHashMap.entrySet()){
			if(e.getValue().equals(ft.getName())){
				if(e.getKey().deleteFile()){
					fileHashMap.remove(e.getKey());
					fileAccordion.removeComponent(fileAccordion.getSelectedTab());
				} else {
					new Notification("Could Not Delete File", "There was an error deleting the selected file", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
				}
			}
		}
	}
	
	public void printFile(int copies, String notes) {
		// Print the file. Send it to the correct class with the right parameters
		FileTabComposite ft = (FileTabComposite)fileAccordion.getSelectedTab();
		GCodeFile toPrint = null;
		for(Entry<GCodeFile, String> e : fileHashMap.entrySet()){
			if(e.getValue().equals(ft.getName())){
				toPrint = e.getKey();
				break;
			}
		}
		toPrint.setNotes(notes);
		for(int i = 0; i < copies; i++)
			printersManager.addFile(printerAccordion.getTabIndex(), toPrint);
		new Notification("Success", copies +" Copies of Your File Were Added to the Print Queue", Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
	}
	
	public void refreshPrintsTable(){
		myPrintsTab.refreshTable();
	}
	
	public void packMetaFiles(){
		ArrayList<CraneCodePacker> ccp = new ArrayList<CraneCodePacker>();
		for(Entry<GCodeFile, String> e : fileHashMap.entrySet()){
			GCodeFile g = e.getKey();
			FileTabComposite ftc = null;
			for(int i = 0; i < fileAccordion.getComponentCount(); i++){
				ftc = (FileTabComposite)fileAccordion.getTab(i).getComponent();
				if(ftc.getName().equals(g.getName())){
					g.setNotes(ftc.getNotes());
					ccp.add(new CraneCodePacker(g));
					break;
				}
			}
		}
		for(CraneCodePacker c : ccp){
			try {
				c.pack();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				new Notification("Error", "Could Not Close out Your Current Files", Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
				ioe.printStackTrace();
				break;
			}
		}
	}
	
	private void logout(){
		ui.getSessionUser().setLoggedOut();
	}
}