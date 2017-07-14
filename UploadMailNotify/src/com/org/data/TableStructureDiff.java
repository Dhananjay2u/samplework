package com.org.data;

import java.util.ArrayList;

public class TableStructureDiff {

	ArrayList<String> columnDiffList=null;
	ArrayList<String> newColumnList=null;
	ArrayList<String> removedColumnList=null;
	TableInfo ti1=null;
	TableInfo ti2=null;
	ArrayList<String> macthingCols=null;
	ArrayList<DDLScript> colLevelDDLScript=null;
	
	
	
	public ArrayList<DDLScript> getColLevelDDLScript() {
		return colLevelDDLScript;
	}
	public void setColLevelDDLScript(ArrayList<DDLScript> colLevelDDLScript) {
		this.colLevelDDLScript = colLevelDDLScript;
	}
	public ArrayList<String> getMacthingCols() {
		return macthingCols;
	}
	public void setMacthingCols(ArrayList<String> macthingCols) {
		this.macthingCols = macthingCols;
	}
	public TableStructureDiff() {
		super();
	}
	public TableStructureDiff(ArrayList<String> columnDiffList,
			ArrayList<String> newColumnList, ArrayList<String> removedColumnList,ArrayList<String>  matchingCols) {
		super();
		this.columnDiffList = columnDiffList;
		this.newColumnList = newColumnList;
		this.removedColumnList = removedColumnList;
		this.macthingCols=matchingCols;
	}
	
	public TableStructureDiff(ArrayList<String> columnDiffList,
			ArrayList<String> newColumnList,
			ArrayList<String> removedColumnList, TableInfo ti1, TableInfo ti2) {
		super();
		this.columnDiffList = columnDiffList;
		this.newColumnList = newColumnList;
		this.removedColumnList = removedColumnList;
		this.ti1 = ti1;
		this.ti2 = ti2;
	}
	
	public TableStructureDiff(ArrayList<String> columnDiffList,
			ArrayList<String> newColumnList,
			ArrayList<String> removedColumnList, TableInfo ti1, TableInfo ti2,
			ArrayList<String> macthingCols,
			ArrayList<DDLScript> colLevelDDLScript) {
		super();
		this.columnDiffList = columnDiffList;
		this.newColumnList = newColumnList;
		this.removedColumnList = removedColumnList;
		this.ti1 = ti1;
		this.ti2 = ti2;
		this.macthingCols = macthingCols;
		this.colLevelDDLScript = colLevelDDLScript;
	}
	
	public TableInfo getTi1() {
		return ti1;
	}
	public void setTi1(TableInfo ti1) {
		this.ti1 = ti1;
	}
	public TableInfo getTi2() {
		return ti2;
	}
	public void setTi2(TableInfo ti2) {
		this.ti2 = ti2;
	}
	public ArrayList<String> getColumnDiffList() {
		return columnDiffList;
	}
	public void setColumnDiffList(ArrayList<String> columnDiffList) {
		this.columnDiffList = columnDiffList;
	}
	public ArrayList<String> getNewColumnList() {
		return newColumnList;
	}
	public void setNewColumnList(ArrayList<String> newColumnList) {
		this.newColumnList = newColumnList;
	}
	public ArrayList<String> getRemovedColumnList() {
		return removedColumnList;
	}
	public void setRemovedColumnList(ArrayList<String> removedColumnList) {
		this.removedColumnList = removedColumnList;
	}

	
	
}
