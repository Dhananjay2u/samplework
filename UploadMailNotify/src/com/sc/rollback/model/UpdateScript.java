package com.sc.rollback.model;

import java.util.ArrayList;

public class UpdateScript  extends DMLScript{

	String tableName;
	ArrayList<String> colList;
	ArrayList<String> valList;
	String condition;
	
	public UpdateScript() {
		super();
		// TODO Auto-generated constructor stub
		colList=new ArrayList<String>();
		valList=new ArrayList<String>();
		
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String setValues="";
		if(colList.size()==valList.size())
		{
			for(int i=0;i<colList.size();i++)
			{
				String val="null";
				if(valList.get(i)!=null)
					val="'"+valList.get(i)+"'";
				if(i!=0)
					setValues+= ", "+colList.get(i)+"="+val+" ";
				else
					setValues+= " "+colList.get(i)+"="+val+" ";
			}
		}
		
		return "UPDATE "+tableName+" SET "+setValues+" WHERE "+condition+" ;";
	}
	
	
}
