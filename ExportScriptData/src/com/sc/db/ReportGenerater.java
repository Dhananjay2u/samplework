package com.sc.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

import java.util.Properties;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;



public class ReportGenerater {
	
	
	
	public static ResultSet getTableRecord(String sql,Connection con) throws ClassNotFoundException, SQLException
	{
		//initialise();
		if (con==null)
			con=ConnectionGetter.getNewConnection();
		Statement st=con.createStatement();
		ResultSet res=  st.executeQuery(sql);
		return res;
	}
	
	public static String getQueryForReport(String reportName)
	{
		String sql=null;
		sql=ConnectionGetter.properties.getProperty(reportName);		
		return sql;
	}
	
	public static File createReport(String reportName,Connection con)
	{
	
		//create WorkbookSettings object
        WorkbookSettings ws = new WorkbookSettings();
      
        try{
        	Date curDate=new Date();
        	String filename=curDate.getTime()+"_temp_report.xls";
        	
             //create work book
             WritableWorkbook workbook = Workbook.createWorkbook(new File(filename), ws);
   
             //create work sheet
             WritableSheet workSheet = null;
             workSheet = workbook.createSheet("FetchedReport" ,0);
            
             //WritableSheet workSheet2 = null;
             //workSheet2 = workbook.createSheet("Sheet2",1);
             
             //SheetSettings sh = workSheet.getSettings();
             
             //Creating Writable font to be used in the report  
             WritableFont normalFont = new WritableFont(WritableFont.createFont("Calibri"),
             9,
             WritableFont.BOLD,  false,
             UnderlineStyle.NO_UNDERLINE,Colour.DARK_GREEN);
    
             //creating plain format to write data in excel sheet
             WritableCellFormat normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);       
             
             normalFormat.setBackground(Colour.LIGHT_TURQUOISE);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK);        
             int dataCol1st=0;//Heading column no
             int dataRowHd=0; //Heading Row no
             
             
             ResultSet result=getTableRecord(getQueryForReport(reportName),con);
             ResultSetMetaData metaData= result.getMetaData();
             int noOfCols=metaData.getColumnCount();
             for (int i=1;i<=noOfCols;i++)
             {
            	 //workSheet.setColumnView(dataCol1st, 50);             
	             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,metaData.getColumnName(i),normalFormat));
	           
             }
             
             ///for data format is changed
             
             //Creating Writable font to be used in the report  
             normalFont = new WritableFont(WritableFont.createFont("Calibri"),
             9,
             WritableFont.NO_BOLD,  false,
             UnderlineStyle.NO_UNDERLINE,Colour.DARK_GREEN);
    
             //creating plain format to write data in excel sheet
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);       
             
             normalFormat.setBackground(Colour.WHITE);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK); 
             
             while(result.next())
             {
            	 dataRowHd++;
            	 dataCol1st=0;
            	 for (int i=1;i<=noOfCols;i++)
                 {
                	 //workSheet.setColumnView(dataCol1st, 50);             
    	             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,result.getObject(i)!=null?result.getObject(i).toString():null,normalFormat));
    	           
                 }
             }
             result.close();
             

             //write to the excel sheet
             workbook.write();
   
             //close the workbook
             workbook.close();
             
             return new File(filename);
        }catch(Exception e){
	        e.printStackTrace();
	        return null;
	  } 	
	}    
	
	
        public static void main(String [] args)
        {
        	createReport("report1",null);
        }
        
	
	

}
