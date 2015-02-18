package org.craneprint.craneserver.ui;

import java.util.HashMap;
import java.util.Set;

import org.craneprint.craneserver.printers.PrinterStatus;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class PrinterTabComposite extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Label stateLabel;
	private Label stateVLabel;
	private HorizontalLayout stateLayout;
	
	private Label queueLabel;
	private Label queueVLabel;
	private Label notesLabel;
	private HorizontalLayout queueLayout;
	private Button connectButton;
	private TextArea notesArea;
	private HorizontalLayout toolsLayout;
	private VerticalLayout leftTools;
	private VerticalLayout rightTools;
	//private HashMap<Label, Label> tools = new HashMap<Label, Label>();

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	public PrinterTabComposite(/*some data maybe*/) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		// TODO add user code here
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// stateLabel
		stateLabel = new Label();
		stateLabel.setImmediate(false);
		stateLabel.setWidth("-1px");
		stateLabel.setHeight("-1px");
		stateLabel.setValue("Machine State: ");
		// stateVLabel
		stateVLabel = new Label();
		stateVLabel.setImmediate(false);
		stateVLabel.setWidth("-1px");
		stateVLabel.setHeight("-1px");
		stateVLabel.setValue("");
		// stateLayout
		stateLayout = new HorizontalLayout();
		stateLayout.setImmediate(false);
		stateLayout.setWidth("100%");
		stateLayout.setHeight("-1px");
		stateLayout.addComponent(stateLabel);
		stateLayout.addComponent(stateVLabel);
		mainLayout.addComponent(stateLayout);
		
		// queueLabel
		queueLabel = new Label();
		queueLabel.setImmediate(false);
		queueLabel.setWidth("-1px");
		queueLabel.setHeight("-1px");
		queueLabel.setValue("Queue Size: ");
		// queueVLabel
		queueVLabel = new Label();
		queueVLabel.setImmediate(false);
		queueVLabel.setWidth("-1px");
		queueVLabel.setHeight("-1px");
		queueVLabel.setValue("");
		// queueLayout
		queueLayout = new HorizontalLayout();
		queueLayout.setImmediate(false);
		queueLayout.setWidth("100%");
		queueLayout.setHeight("-1px");
		queueLayout.addComponent(queueLabel);
		queueLayout.addComponent(queueVLabel);
		mainLayout.addComponent(queueLayout);
		
		// left and right tool layouts
		leftTools = new VerticalLayout();
		leftTools.setImmediate(false);
		leftTools.setWidth("-1px");
		leftTools.setHeight("-1px");
		rightTools = new VerticalLayout();
		rightTools.setImmediate(false);
		rightTools.setWidth("-1px");
		rightTools.setHeight("-1px");
		// toolsLayout
		toolsLayout = new HorizontalLayout();
		toolsLayout.setImmediate(false);
		toolsLayout.setWidth("100%");
		toolsLayout.setHeight("-1px");
		toolsLayout.addComponent(leftTools);
		toolsLayout.addComponent(rightTools);
		mainLayout.addComponent(toolsLayout);
		// Intialize a preliminary message about the tools
		Label l = new Label();
		l.setValue("");
		leftTools.addComponent(new Label("Tool0: "));
		rightTools.addComponent(l);
		
		// notesLabel
		notesLabel = new Label();
		notesLabel.setImmediate(false);
		notesLabel.setWidth("-1px");
		notesLabel.setHeight("-1px");
		notesLabel.setValue("Notes: ");
		mainLayout.addComponent(notesLabel);
		// notesArea
		notesArea = new TextArea();
		notesArea.setImmediate(false);
		notesArea.setWidth("100.0%");
		notesArea.setHeight("100px");
		notesArea.setEnabled(false);
		mainLayout.addComponent(notesArea);
		
		// connectButton
		connectButton = new Button();
		connectButton.setImmediate(true);
		connectButton.setWidth("100%");
		connectButton.setHeight("-1px");
		connectButton.setCaption("Connect");
		connectButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event){
				Component c = getParent();
		    	Component d = c.getParent();
		    	Component e = d.getParent();
		    	Component f = e.getParent();
		    	Component g = f.getParent();
		    	PrintComposite h = (PrintComposite)g.getParent();
		    	HandShake hs = h.doHandShake();
		    	// Set the data in the UI
		    	stateVLabel.setValue(PrinterStatus.getStringForInt(hs.getStatus()));
		    	queueVLabel.setValue(hs.getQueueSize());
		    	notesArea.setValue(hs.getNotes());
		    	// Reset the tools stuff
		    	leftTools.removeAllComponents();
		    	rightTools.removeAllComponents();
		    	// Set them up now
		    	HashMap<String, String> toPut = hs.getTools();
		    	Set<String> k = (Set<String>)toPut.keySet();
				String[] keys = k.toArray(new String[k.size()]);
		    	for(int i = 0; i < toPut.size(); i++){
		    		Label lbl = new Label();
		    		lbl.setValue(toPut.get(keys[i]));
		    		Label lbl1 = new Label(keys[i] + ": ");
		    		leftTools.addComponent(lbl1);
		    		rightTools.addComponent(lbl);
		    	}
		    	connectButton.setCaption("Refresh");
			}
		});
		mainLayout.addComponent(connectButton);
		
		return mainLayout;
	}

}
