package com.org.data;

import java.util.ArrayList;

public class DDLScript {
	
	private String tableName;
	private ColumnInfo col;
	private char typeOfDDL; //C,A,D
	
	
	public DDLScript() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public DDLScript(String tableName, ColumnInfo col, char typeOfDDL) {
		super();
		this.tableName = tableName;
		this.col = col;
		this.typeOfDDL = typeOfDDL;
	}


	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public ColumnInfo getCol() {
		return col;
	}
	public void setCol(ColumnInfo col) {
		this.col = col;
	}
	public char getTypeOfDDL() {
		return typeOfDDL;
	}
	public void setTypeOfDDL(char typeOfDDL) {
		this.typeOfDDL = typeOfDDL;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch(typeOfDDL)
		{
			case 'C':
				return "ALTER TABLE "+getTableName()+" ADD ("+col.colName+" "+col.colType+" "+((col.getSize()==0)?" ":"("+col.getSize()+")")+" "+ ((col.isNullable())?" NULL ":" NOT NULL ") +");";
			case 'A':
				return "ALTER TABLE "+getTableName()+" MODIFY ("+col.colName+" "+col.colType+" "+((col.getSize()==0)?" ":"("+col.getSize()+")")+" "+ ((col.isNullable())?" NULL ":" NOT NULL ") +");";
			case 'D':
				return "ALTER TABLE "+getTableName()+" DROP COULMN ("+col.colName+");";
		}
		return super.toString();
	}
	
	
	

}
