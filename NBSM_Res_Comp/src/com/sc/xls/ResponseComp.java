package com.sc.xls;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class ResponseComp
{
	
	
	public static void createResultingResFile(Set<String> matchReskeys,Set<String> missedReskeys,Set<String> newReskeys,HashMap<String,ResponseKeyVal> oldRespList,HashMap<String,ResponseKeyVal> newRespList,String resultFile)
	{
		
		//create WorkbookSettings object
        WorkbookSettings ws = new WorkbookSettings();
  
       try{
             //create work book
             WritableWorkbook workbook = Workbook.createWorkbook(new File(resultFile), ws);
             
           //create work sheet
             WritableSheet workSheet = null;
             workSheet = workbook.createSheet("MissedResponseKeys" ,0);
             WritableSheet workSheet2 = null;
             workSheet2 = workbook.createSheet("NewResponseKeys" ,1);
             WritableSheet workSheet3 = null;
             workSheet3 = workbook.createSheet("MatchingResponseKeys" ,2);
             
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
                     //normalFormat.setBackground(Colour.RED);
                     //normalFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);  
                     
                     //int dataCol1st=3;//Heading column no
                     //int dataCol2nd=4;//Heading column no
                     int dataRowHd=0; //Heading Row no
                     //missing sheet rule heading ,write to datasheet 
                     
                     for(String str:missedReskeys)
                     {
	                     workSheet.addCell(new jxl.write.Label(0,dataRowHd,oldRespList.get(str).getDs(),normalFormat));
	                     workSheet.addCell(new jxl.write.Label(1,dataRowHd,oldRespList.get(str).getKey(),normalFormat));
	                     workSheet.addCell(new jxl.write.Label(2,dataRowHd,oldRespList.get(str).getValue(),normalFormat));
	                     
	                     dataRowHd++;
                     }
                     dataRowHd=0;
                     for(String str:newReskeys)
                     {
	                     workSheet2.addCell(new jxl.write.Label(0,dataRowHd,newRespList.get(str).getDs(),normalFormat));
	                     workSheet2.addCell(new jxl.write.Label(1,dataRowHd,newRespList.get(str).getKey(),normalFormat));
	                     workSheet2.addCell(new jxl.write.Label(2,dataRowHd,newRespList.get(str).getValue(),normalFormat));
	                     
	                     dataRowHd++;
                     }
                     dataRowHd=0;
                     for(String str:matchReskeys)
                     {
	                     workSheet3.addCell(new jxl.write.Label(0,dataRowHd,newRespList.get(str).getDs(),normalFormat));
	                     workSheet3.addCell(new jxl.write.Label(1,dataRowHd,newRespList.get(str).getKey(),normalFormat));
	                     workSheet3.addCell(new jxl.write.Label(2,dataRowHd,newRespList.get(str).getValue(),normalFormat));
	                     workSheet3.addCell(new jxl.write.Label(3,dataRowHd,oldRespList.get(str).getValue(),normalFormat));
	                     if(newRespList.get(str).getValue().equals(oldRespList.get(str).getValue()))
	                     {
	                    	 //normalFormat.setBackground(Colour.GREEN);
	                    	 workSheet3.addCell(new jxl.write.Label(4,dataRowHd,"Y",normalFormat));
	                     }
	                     else
	                     {
	                    	 //normalFormat.setBackground(Colour.RED);
	                    	 workSheet3.addCell(new jxl.write.Label(4,dataRowHd,"N",normalFormat));
	                     }
	                     dataRowHd++;
                     }
                     
                     workbook.write();
                     workbook.close();
       }
       catch(Exception ex)
       {
    	   ex.printStackTrace();
       }
       
		
		
	}
	public static void compareResponseSheet(String oldRespFile,String newRespFile,String resultFile)
	{
		HashMap<String,ResponseKeyVal> oldRespList=new HashMap<String,ResponseKeyVal>();
		HashMap<String,ResponseKeyVal> newRespList=new HashMap<String,ResponseKeyVal>();
		try{
			oldRespList= ExcelResponseReader.readExcelResponse(oldRespFile, 1);
			newRespList= ExcelResponseReader.readExcelResponse(newRespFile, 1);
			Set<String> oldResKeyss= oldRespList.keySet();
			Set<String> newResKeyss= newRespList.keySet();
			System.out.println(oldResKeyss.size()+"  ****  "+newResKeyss.size());
			String [] oldResKeys=new String[oldResKeyss.size()];
			String [] newResKeys=new String[newResKeyss.size()];
			int i=0;
			for(String str:oldResKeyss)
			{
				oldResKeys[i]=new String(str);
				i++;
			}
			
			i=0;
			for(String str:newResKeyss)
			{
				newResKeys[i]=new String(str);
				i++;
			}
											
			Arrays.sort(oldResKeys);
			Arrays.sort(newResKeys);
			
			Set<String> matchReskeys=new HashSet<String>();
			Set<String> newReskeys=new HashSet<String>();
			Set<String> missedReskeys=new HashSet<String>();
			
			for (String str:newResKeys)
			{
				if(Arrays.binarySearch(oldResKeys, str) >= 0 )
				{
					matchReskeys.add(str);
				}
				else
					newReskeys.add(str);
			}
			for (String str:oldResKeys)
			{
				if(!(Arrays.binarySearch(newResKeys, str) >= 0) )
				{
					missedReskeys.add(str);
				}				
			}
			
			////Result : matchReskeys | missedReskeys | newReskeys
			System.out.println(matchReskeys.size()+"  ****  "+missedReskeys.size()+"  ****  "+newReskeys.size());
			
			
			createResultingResFile(matchReskeys, missedReskeys, newReskeys, oldRespList, newRespList, resultFile);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	public static void main(String args[])
	{
		try {
			ResponseComp.compareResponseSheet("c:\\tmp\\IdataOutput_SCP211AE10A1030214018388_Call1[1].xls","C:\\tmp\\newresp\\IdataOutput_SCP211AE10A1030214018388_Call1[1].xls","C:\\tmp\\newresp\\Comp_Result_IdataOutput_SCP211AE10A1030214018388_Call1[1].xls");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
