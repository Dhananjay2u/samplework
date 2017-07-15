package com.sc.xml.xls;

import java.io.File;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class GenExcelData {

	public static void createExcelFieldsMapping(ArrayList<FieldMapping> fieldList,String outFileFieldMapping)
	{
		
		//create WorkbookSettings object
        WorkbookSettings ws = new WorkbookSettings();
  
       try{
             //create work book
             WritableWorkbook workbook = Workbook.createWorkbook(new File(outFileFieldMapping), ws);
             
           //create work sheet
             WritableSheet workSheet = null;
             workSheet = workbook.createSheet("FieldsMapping" ,0);
            
             
             WritableFont normalFont = new WritableFont(WritableFont.createFont("Cambria"),
                     WritableFont.DEFAULT_POINT_SIZE,
                     WritableFont.NO_BOLD,  false,
                     UnderlineStyle.NO_UNDERLINE);
         
                     //creating plain format to write data in excel sheet
                     WritableCellFormat normalFormat = new WritableCellFormat(normalFont);
                     normalFormat.setWrap(true);
                     normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
                     normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                     normalFormat.setWrap(true);    
                     
                     normalFormat.setBackground(Colour.AQUA);
                     normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);  
                     
                     
                     //int dataCol1st=3;//Heading column no
                     //int dataCol2nd=4;//Heading column no
                     int dataRowHd=0; //Heading Row no
                     //missing sheet rule heading ,write to datasheet 
                     
                     workSheet.addCell(new jxl.write.Label(0,dataRowHd,"ISIS Field",normalFormat));
                     workSheet.addCell(new jxl.write.Label(1,dataRowHd,"EOPSField",normalFormat));
                     workSheet.addCell(new jxl.write.Label(2,dataRowHd,"DefaultValue",normalFormat));
                     
                     normalFormat = new WritableCellFormat(normalFont);
                     normalFormat.setWrap(true);
                     normalFormat.setAlignment(jxl.format.Alignment.CENTRE);
                     normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                     normalFormat.setWrap(true);             
                     //normalFormat.setBackground(Colour.RED);
                     normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
                     for(FieldMapping fmap:fieldList)
                     {
                    	 dataRowHd++;
	                     workSheet.addCell(new jxl.write.Label(0,dataRowHd,fmap.getIsisField(),normalFormat));
	                     workSheet.addCell(new jxl.write.Label(1,dataRowHd,fmap.getEopsField(),normalFormat));
	                     workSheet.addCell(new jxl.write.Label(2,dataRowHd,fmap.getDefaultValue(),normalFormat));                     
	                     
                     }
                  
                     
                     workbook.write();
                     workbook.close();
       }
       catch(Exception ex)
       {
    	   ex.printStackTrace();
       }
	}
	
}
