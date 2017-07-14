package com.sc.rollback.model;

import java.util.ArrayList;

public class InsertScript  extends DMLScript{
	
	String tableName;
	ArrayList<String> colList;
	ArrayList<String> valList;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public InsertScript() {
		super();
		// TODO Auto-generated constructor stub
		colList=new ArrayList<String>();
		valList=new ArrayList<String>();
		
	}
	public ArrayList<String> getColList() {
		return colList;
	}
	public void setColList(ArrayList<String> colList) {
		this.colList = colList;
	}
	public ArrayList<String> getValList() {
		return valList;
	}
	public void setValList(ArrayList<String> valList) {
		this.valList = valList;
	}
	
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String colNames="(";
		String values="(";
		boolean col=false;
		for(String str : colList)
		{
		
			if(!col)		
				colNames+=str;
			else
				colNames+=","+str;
			
			col=true;
		}
		colNames+=")";
		col=false;
		for(String str : valList)
		{
			if(!col)		
				values+="'"+str+"'";
			else
				values+=","+"'"+str+"'";
			
			col=true;
		}
		values+=")";
		return "INSERT INTO "+tableName+" "+colNames+" VALUES "+values+" ;";
	}
	
	

}






 