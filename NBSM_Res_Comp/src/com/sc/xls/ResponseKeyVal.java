package com.sc.xls;


public class ResponseKeyVal
{
	private String ds;
	private String key;
	private String value;
	
	
	public ResponseKeyVal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResponseKeyVal(String ds, String key, String value) {
		super();
		this.ds = ds;
		this.key = key;
		this.value = value;
	}
	public String getDs() {
		return ds;
	}
	public void setDs(String ds) {
		this.ds = ds;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DS : "+ds+" | Key : "+key+" | Value : "+value;
	}
	
	
}