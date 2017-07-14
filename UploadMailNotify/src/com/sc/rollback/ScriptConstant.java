package com.sc.rollback;

public enum ScriptConstant {
	
	INSERT("insert"),UPDATE("update"),DELETE("delete"),FROM("from"),WHERE("where");
	
	public  String str;
	
	private ScriptConstant(String str) {
		this.str=str;
	}

	public String getStr() {
		return str;
	}
	
	public boolean equals(String str) {		
		return this.str.equalsIgnoreCase(str); 
	}
	
}
