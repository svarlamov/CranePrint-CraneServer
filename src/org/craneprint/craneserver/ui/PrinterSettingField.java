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
	private boolean isInt = false;

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
	
	public PrinterSettingField(String caption, String key, int val) {
		isInt = true;
		c = caption;
		k = key;
		v = Integer.toString(val);
		
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@AutoGenerated
	private void buildMainLayout() {
		// the main layout and components will be created here
		mainLayout = new HorizontalLayout();
		mainLayout.setWidth("-1px");
		mainLayout.setHeight("-1px");
		
		label = new Label();
		label.setValue(c);
		label.setWidth("-1px");
		label.setHeight("40px");
		mainLayout.addComponent(label);
		
		field = new TextField();
		field.setWidth("-1px");
		field.setHeight("-1px");
		field.setValue(v);
		mainLayout.addComponent(field);
	}
	
	public boolean isNull(){
		return (field.getValue().length() >= 1) ? false : true;
	}
	
	public String getKey(){
		return k;
	}
	
	public boolean isInt(){
		return isInt;
	}
	
	public Object getValue(){
		return field.getValue();
	}
	
	public int getIntValue(){
		return new Integer(field.getValue());
	}

}
