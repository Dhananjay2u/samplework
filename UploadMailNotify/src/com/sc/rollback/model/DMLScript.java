package com.sc.rollback.model;

public abstract class DMLScript {

	private String tableName;

	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
