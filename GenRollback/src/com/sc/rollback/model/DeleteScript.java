package com.sc.rollback.model;

public class DeleteScript extends DMLScript{
	
	private String tableName;
	private String condition;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DELETE FROM "+tableName+" WHERE "+condition+" ;";
	}

}
