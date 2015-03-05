package org.craneprint.craneserver.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class PrinterSettingField extends CustomComponent implements PrinterSettingComposite{

	@AutoGenerated
	private HorizontalLayout mainLayout;
	private TextField field;
	private Label label;
	
	private String c;
	private String k;
	private String v;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public PrinterSettingField(String caption, String key, String val) {
		c = caption;
		k = key;
		v = val;
		
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@AutoGenerated
	private void buildMainLayout() {
		// the main layout and components will be created here
		mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		
		label = new Label();
		label.setValue(c);
		label.setWidth("-1px");
		label.setHeight("40px");
		
		field = new TextField();
		field.setWidth("-1px");
		field.setHeight("-1px");
		field.setValue(v);
	}
	
	protected boolean isEmpty(){
		return (field.getValue().length() >= 1) ? false : true;
	}
	
	public String getKey(){
		return k;
	}
	
	public Object getValue(){
		return field.getValue();
	}

}