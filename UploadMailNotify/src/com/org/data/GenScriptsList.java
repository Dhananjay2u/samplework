package com.org.data;

import java.util.ArrayList;

public class GenScriptsList {

	private ArrayList<String> insertScripts=null;
	private ArrayList<String> updateScripts=null;
	private ArrayList<String> deleteScripts=null;
	private String tableName=null;
	public GenScriptsList(ArrayList<String> deleteScripts,
			ArrayList<String> insertScripts, ArrayList<String> updateScripts) {
		super();
		this.deleteScripts = deleteScripts;
		this.insertScripts = insertScripts;
		this.updateScripts = updateScripts;
	}
	public GenScriptsList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public ArrayList<String> getInsertScripts() {
		return insertScripts;
	}
	public void setInsertScripts(ArrayList<String> insertScripts) {
		this.insertScripts = insertScripts;
	}
	public ArrayList<String> getUpdateScripts() {
		return updateScripts;
	}
	public void setUpdateScripts(ArrayList<String> updateScripts) {
		this.updateScripts = updateScripts;
	}
	public ArrayList<String> getDeleteScripts() {
		return deleteScripts;
	}
	public void setDeleteScripts(ArrayList<String> deleteScripts) {
		this.deleteScripts = deleteScripts;
	}
	
	
	
}
