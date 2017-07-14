package com.secmatrix.matrix;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.secmatrix.dbcon.SecurityDataDAO;
import com.secmatrix.model.RequestData;
import com.secmatrix.util.GenUtil;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class MatrixGeneration {
	
	public static void createUVTUserIdSnglProcessRMSExcel(String resultingFileName, RequestData requestData, String country)
	{
	
		//create WorkbookSettings object
        WorkbookSettings ws = new WorkbookSettings();
      
        try{
             //create work book
             WritableWorkbook workbook = Workbook.createWorkbook(new File(resultingFileName), ws);
   
             //create work sheet
             WritableSheet workSheet = null;
             workSheet = workbook.createSheet(country+"_"+requestData.getProcessId() ,0);
            
             WritableSheet workSheet2 = null;
             workSheet2 = workbook.createSheet("Security Matrix",1);
             
             //SheetSettings sh = workSheet.getSettings();
             
             //Creating Writable font to be used in the report  
             WritableFont normalFont = new WritableFont(WritableFont.createFont("Calibri"),
             9,
             WritableFont.BOLD,  false,
             UnderlineStyle.NO_UNDERLINE,Colour.BLUE);
    
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
             //write to datasheet 
             workSheet.mergeCells(0, 0, 5, 0);
             //workSheet.setColumnView(dataCol1st, 50);             
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Access to Groups",normalFormat));
             dataRowHd++;dataCol1st--;
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"S.No.",normalFormat));
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Location",normalFormat));
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Bank ID",normalFormat));
             workSheet.setColumnView(dataCol1st, 20); 
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Name of the User",normalFormat));
             workSheet.setColumnView(dataCol1st, 15); 
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"User Location",normalFormat));
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"User Type",normalFormat));
             ///change for single process
             Collection<String> list =SecurityDataDAO.getCountrySnglProcessProgramRoleMapping(requestData,country).values();             
             for(String stGroup:list)
             {
            	 workSheet.setColumnView(dataCol1st,stGroup.length()+2); 
            	 workSheet.addCell(new jxl.write.Label(dataCol1st,dataRowHd,stGroup,normalFormat));
            	 workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd-1,"",normalFormat));
             }
             ////data write to Uvt user end and security matrix for groupmapping starts
             
             normalFont = new WritableFont(WritableFont.createFont("Calibri"),
                     11,
                     WritableFont.BOLD,  false,
                     UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);       
             
             normalFormat.setBackground(Colour.AQUA);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK); 
             dataCol1st=0;//Heading column no
             dataRowHd=0; //Heading Row no
             workSheet2.setColumnView(dataCol1st,20);            
             workSheet2.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Group Name",normalFormat));
             workSheet2.setColumnView(dataCol1st,20); 
             workSheet2.addCell(new jxl.write.Label(dataCol1st,dataRowHd,"Step Name",normalFormat));
             
             normalFont = new WritableFont(WritableFont.createFont("Calibri"),
                     11,
                     WritableFont.NO_BOLD,  false,
                     UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);                    
             //normalFormat.setBackground(Colour.AQUA);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK); 
             ///change for single process
             HashMap<String, String> listGroupStep= SecurityDataDAO.getCountrySnglProcessProgramRoleMapping(requestData,country);
             Set<String> keySet= listGroupStep.keySet();
             for(String str:keySet)
             {
            	 dataCol1st=0;
            	 dataRowHd++;
            	 String value=listGroupStep.get(str);
            	 workSheet2.setColumnView(dataCol1st,str.length()+2);            
                 workSheet2.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,str,normalFormat));
                 workSheet2.setColumnView(dataCol1st,value.length()+2); 
                 workSheet2.addCell(new jxl.write.Label(dataCol1st,dataRowHd,value,normalFormat));
             }
             
             //write to the excel sheet
             workbook.write();
   
             //close the workbook
             workbook.close();
        }catch(Exception e){
	        e.printStackTrace();
	  } 	
		
	}
	

	public static void createCBEFormRMSExcel(String resultingFileName, RequestData requestData, String country)
	{
		
		//create WorkbookSettings object
        WorkbookSettings ws = new WorkbookSettings();
      
        try{
             //create work book
             WritableWorkbook workbook = Workbook.createWorkbook(new File(resultingFileName), ws);
   
             //create work sheet
             WritableSheet workSheet = null;
             workSheet = workbook.createSheet(country+"_"+requestData.getProcessId() ,0);
            
             //SheetSettings sh = workSheet.getSettings();
             
             //Creating Writable font to be used in the report  
             WritableFont normalFont = new WritableFont(WritableFont.createFont("Arial"),
             WritableFont.DEFAULT_POINT_SIZE,
             WritableFont.BOLD,  false,
             UnderlineStyle.NO_UNDERLINE);
    
             //creating plain format to write data in excel sheet
             WritableCellFormat normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);       
             
             //normalFormat.setBackground(Colour.RED);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK);        
             int dataCol1st=0;//Heading column no
             int dataRowHd=0; //Heading Row no
             //write to datasheet 
            
             workSheet.setColumnView(dataCol1st, 17);             
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Request Group*",normalFormat));
             workSheet.setColumnView(dataCol1st, 40);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"User Location *",normalFormat));
             workSheet.setColumnView(dataCol1st, 8);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"DAP",normalFormat));
             workSheet.setColumnView(dataCol1st, 8);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Branch",normalFormat));
             workSheet.setColumnView(dataCol1st, 8);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Info",normalFormat));
             workSheet.setColumnView(dataCol1st, 9);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Country*",normalFormat));
             workSheet.setColumnView(dataCol1st, 12);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Department*",normalFormat));
             workSheet.setColumnView(dataCol1st, 25);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Process*",normalFormat));
             workSheet.setColumnView(dataCol1st, 25);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Virtual Profile*",normalFormat));
             workSheet.setColumnView(dataCol1st, 40);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Group Mapping*",normalFormat));
             workSheet.setColumnView(dataCol1st, 10);
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,"Activity*",normalFormat));
             
             ////Header data over, now rest data info in tabular data
             dataCol1st=0;//Heading column no
             dataRowHd=1; //Heading Row no
             
             normalFont = new WritableFont(WritableFont.createFont("Arial"),
    	             WritableFont.DEFAULT_POINT_SIZE,
    	             WritableFont.NO_BOLD,  false,
    	             UnderlineStyle.NO_UNDERLINE);
             
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);       
             
             //normalFormat.setBackground(Colour.RED);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK);        
             
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,country,normalFormat));
             
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.LEFT);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);      
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,GenUtil.getStringDataFromArrayList(SecurityDataDAO.getLocationByCountry(country)),normalFormat));
             
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);      
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,requestData.getDap(),normalFormat));
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,requestData.getBranch(),normalFormat));
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,requestData.getInfo(),normalFormat));
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,country,normalFormat));
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,requestData.getDepartment(),normalFormat));
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,requestData.getProcessId()+"-"+SecurityDataDAO.getProcessName(requestData.getProcessId()),normalFormat));
             
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,GenUtil.getStringDataFromArrayList(SecurityDataDAO.getStepNamesLabel(requestData,country)),normalFormat));
             
             if(requestData.getIsSingleProcess())
             {
            	///little change in method for single process
                 workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,GenUtil.getStringDataFromArrayList(SecurityDataDAO.getSingleProcessProgramRoleMapping(requestData,country)),normalFormat));                 
             }
             else
             {
            	 workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,GenUtil.getStringDataFromArrayList(SecurityDataDAO.getProcessProgramGroupMapping(requestData)),normalFormat));
             }
             workSheet.addCell(new jxl.write.Label((dataCol1st++),dataRowHd,requestData.getIsAddition()?"Addition":"Removal",normalFormat));
          
             //write to the excel sheet
             workbook.write();
   
             //close the workbook
             workbook.close();
		   }catch(Exception e){
		        e.printStackTrace();
		  } 		
	
	}
	
	public static void createUVTUserIdRMSExcel(String resultingFileName, RequestData requestData, String country)
	{
	
		//create WorkbookSettings object
        WorkbookSettings ws = new WorkbookSettings();
      
        try{
             //create work book
             WritableWorkbook workbook = Workbook.createWorkbook(new File(resultingFileName), ws);
   
             //create work sheet
             WritableSheet workSheet = null;
             workSheet = workbook.createSheet(country+"_"+requestData.getProcessId() ,0);
            
             WritableSheet workSheet2 = null;
             workSheet2 = workbook.createSheet("Security Matrix",1);
             
             //SheetSettings sh = workSheet.getSettings();
             
             //Creating Writable font to be used in the report  
             WritableFont normalFont = new WritableFont(WritableFont.createFont("Calibri"),
             9,
             WritableFont.BOLD,  false,
             UnderlineStyle.NO_UNDERLINE,Colour.BLUE);
    
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
             //write to datasheet 
             workSheet.mergeCells(0, 0, 5, 0);
             //workSheet.setColumnView(dataCol1st, 50);             
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Access to Groups",normalFormat));
             dataRowHd++;dataCol1st--;
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"S.No.",normalFormat));
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Location",normalFormat));
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Bank ID",normalFormat));
             workSheet.setColumnView(dataCol1st, 20); 
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Name of the User",normalFormat));
             workSheet.setColumnView(dataCol1st, 15); 
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"User Location",normalFormat));
             workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"User Type",normalFormat));
             
             ArrayList<String> list =SecurityDataDAO.getProcessProgramGroupMapping(requestData);             
             for(String stGroup:list)
             {
            	 workSheet.setColumnView(dataCol1st,stGroup.length()+2); 
            	 workSheet.addCell(new jxl.write.Label(dataCol1st,dataRowHd,stGroup,normalFormat));
            	 workSheet.addCell(new jxl.write.Label(dataCol1st++,dataRowHd-1,"",normalFormat));
             }
             ////data write to Uvt user end and security matrix for groupmapping starts
             
             normalFont = new WritableFont(WritableFont.createFont("Calibri"),
                     11,
                     WritableFont.BOLD,  false,
                     UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);       
             
             normalFormat.setBackground(Colour.AQUA);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK); 
             dataCol1st=0;//Heading column no
             dataRowHd=0; //Heading Row no
             workSheet2.setColumnView(dataCol1st,20);            
             workSheet2.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,"Group Name",normalFormat));
             workSheet2.setColumnView(dataCol1st,20); 
             workSheet2.addCell(new jxl.write.Label(dataCol1st,dataRowHd,"Step Name",normalFormat));
             
             normalFont = new WritableFont(WritableFont.createFont("Calibri"),
                     11,
                     WritableFont.NO_BOLD,  false,
                     UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
             normalFormat = new WritableCellFormat(normalFont);
             normalFormat.setWrap(true);
             normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
             normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
             normalFormat.setShrinkToFit(true);                    
             //normalFormat.setBackground(Colour.AQUA);
             normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,
             jxl.format.Colour.BLACK); 
             
             ArrayList<String> listGroupStep= SecurityDataDAO.getProcessProgramGroupMapping(requestData);
             for(String str:listGroupStep)
             {
            	 dataCol1st=0;
            	 dataRowHd++;
            	 String dt[]=str.split("_");
            	 workSheet2.setColumnView(dataCol1st,20);            
                 workSheet2.addCell(new jxl.write.Label(dataCol1st++,dataRowHd,dt[0]+"_"+dt[2],normalFormat));
                 workSheet2.setColumnView(dataCol1st,20); 
                 workSheet2.addCell(new jxl.write.Label(dataCol1st,dataRowHd,dt[1],normalFormat));
             }
             
             //write to the excel sheet
             workbook.write();
   
             //close the workbook
             workbook.close();
        }catch(Exception e){
	        e.printStackTrace();
	  } 	
		
	}
	
	public static void createResultingSecMatrix(RequestData requestData) 
	{
				
		if(requestData.getIsSingleProcess())//&& requestData.getCountryCode().size()>1
		{
			ArrayList<String> countryCode=requestData.getCountryCode();
			for(String country: countryCode)
			{
				//retrieving countrycode for which , Sec matrix is getting created
				String resultingFileName="c://tmp//CBRMSData-"+country+" v1 0 M2(Process ID-"+requestData.getProcessId()+").xls";
				String resultingFileName1="c://tmp//Template for UVT UserIDs_"+country+"_"+requestData.getProcessId()+".xls";
				createCBEFormRMSExcel(resultingFileName, requestData, country);
				createUVTUserIdSnglProcessRMSExcel(resultingFileName1, requestData, country);//createUVTUserIdRMSExcel(resultingFileName1, requestData, country);
			}
		}
		else
		{
			//retrieving countrycode for which , Sec matrix is getting created
	        String country=requestData.getCountryCode().get(0);
			String resultingFileName="c://tmp//CBRMSData-"+country+" v1 0 M2(Process ID-"+requestData.getProcessId()+").xls";
			String resultingFileName1="c://tmp//Template for UVT UserIDs_"+country+"_"+requestData.getProcessId()+".xls";
			createCBEFormRMSExcel(resultingFileName, requestData, country);
			createUVTUserIdRMSExcel(resultingFileName1, requestData, country);
		}	
				
	}
	
	

}
