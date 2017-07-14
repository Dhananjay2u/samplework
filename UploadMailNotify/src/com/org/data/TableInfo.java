package com.org.data;

import java.util.ArrayList;

public class TableInfo {

	public ArrayList<ColumnInfo> columnList=new  ArrayList<ColumnInfo>();
	public String tbName=null;
	public String pkey=null;
	public String getPkey() {
		return pkey;
	}
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}
	public ArrayList<ColumnInfo> getColumnList() {
		return columnList;
	}
	public void setColumnList(ArrayList<ColumnInfo> columnList) {
		this.columnList = columnList;
	}
	public String getTbName() {
		return tbName;
	}
	public void setTbName(String tbName) {
		this.tbName = tbName;
	}
	
	public ArrayList<String> getAllColNames()
	{
		ArrayList<String> colList=new ArrayList<String>();
		for(ColumnInfo ci:columnList)
		{
			colList.add(ci.getColName());
		}
		return colList;
	}
}
