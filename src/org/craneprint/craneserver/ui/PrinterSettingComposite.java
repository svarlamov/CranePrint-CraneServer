package org.craneprint.craneserver.ui;


public interface PrinterSettingComposite {
	public boolean isNull();
	public String getKey();
	public boolean isInt();
	public int getIntValue();
	public Object getValue();
}