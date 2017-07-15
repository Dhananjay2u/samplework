package com.sc.xls;

import java.io.File;


import java.util.HashMap;

import jxl.Sheet;
import jxl.Workbook;








public class ExcelResponseReader {

	
	public static HashMap<String,ResponseKeyVal> readExcelResponse(String excelDataFile,int sheetNo) throws Exception 
    {
		File sourceFile=new File(excelDataFile);		
        Workbook w=Workbook.getWorkbook(sourceFile);
        Sheet sheet = w.getSheet(sheetNo-1);
       // HashMap<String, String> data=new HashMap<String, String>();
        
        int colss=sheet.getColumns();
        int rows=sheet.getRows();
        System.out.println("colss : "+colss+"  Rows : "+rows);
        HashMap<String,ResponseKeyVal> responseList=new HashMap<String,ResponseKeyVal>();
        	 for(int i=0;i<rows;i++)
             {
        		 
        		 if(sheet.getCell(1,i).getContents().toString().trim().length()!=0)
        		 {        			 
        			String strDs= sheet.getCell(0,i).getContents().toString().trim();
        			String strKey= sheet.getCell(1,i).getContents().toString().trim();
        			String strVal= sheet.getCell(2,i).getContents().toString().trim();
        			ResponseKeyVal kev=new ResponseKeyVal(strDs, strKey, strVal) ;
        			responseList.put(strKey,kev);	
        		 }
             }
              System.out.println(responseList);
              if(responseList.size()==0) return null;
              return responseList;
    }
	
	
	
	
}
