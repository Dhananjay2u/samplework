package com.sc.xml.xls;

public class FieldMapping {
	
	private String isisField;
	private String eopsField;
	private String defaultValue;
	
	
	
	public FieldMapping() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FieldMapping(String isisField, String eopsField, String defaultValue) {
		super();
		this.isisField = isisField;
		this.eopsField = eopsField;
		this.defaultValue = defaultValue;
	}
	public String getIsisField() {
		return isisField;
	}
	public void setIsisField(String isisField) {
		this.isisField = isisField;
	}
	public String getEopsField() {
		return eopsField;
	}
	public void setEopsField(String eopsField) {
		this.eopsField = eopsField;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	

}
