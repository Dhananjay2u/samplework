/*
 * ExcelWriter.java
 *
 * Created on June 5, 2007, 4:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rewards;

/**
 *
 *
 * @author 1241182
 */
import jxl.Workbook;
import jxl.Cell;
import jxl.Sheet;
import jxl.write.biff.CellValue;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.write.WriteException;
import java.io.File;
import jxl.read.biff.WorkbookParser;
import java.io.IOException;
//import jxl.write.biff.CellValue;
import rewards.ExcelReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.Calendar;

public class ExcelWriter {


    public ExcelWriter() {
    }
    String excelFileName = "";
    int lineCount = 0;
    int rowCount = 0;
    int columnCount =0;
    int sheetCount;
    String stmts=null;
    String[] fileNames = new String[500];
    String[] uploadStatuss = new String[500];
    String[] uploadTime = new String[500];
    String[] noStmts = new String[500];

   public void writeExcelSheet() {

       Calendar lCal = Calendar.getInstance();
       String textLogFileName = SystemLog.path.getProperty("check_logpath")+lCal.get( Calendar.DAY_OF_MONTH ) + "-" + lCal.get( Calendar.MONTH ) + "-" + lCal.get( Calendar.YEAR )+ ".txt";
       BufferedReader textReader = null;
       try{
           textReader = new BufferedReader(new FileReader(textLogFileName));

       }catch(Exception e){
        e.printStackTrace();
       }
       String[] textLine = new String[1000];
       int i=0;
        int m = 0;
       try{
           textLine[i]  = textReader.readLine();
           do{
                StringTokenizer stok = new StringTokenizer(textLine[i], "|");
				int n = 1;

				while(stok.hasMoreTokens()){

					if ( n == 1){
						fileNames[m] = stok.nextToken();
                                        }
                                        if(n==2){
                                                uploadStatuss[m] = stok.nextToken();
                                        }
                                        if(n==3){
                                                uploadTime[m] = stok.nextToken();
                                        }
                                        if(n==4){
                                                noStmts[m] = stok.nextToken();
                                        }
                                        n++;
                                }
                                m++;
               i++;
           }while((textLine[i]=textReader.readLine())!=null);


       }catch(Exception e){
       e.printStackTrace();

       }


   try{

          int z=0;
          String excelFile2 = SystemLog.path.getProperty("check_reportpath")+lCal.get( Calendar.DAY_OF_MONTH ) + "-" + lCal.get( Calendar.MONTH ) + "-" + lCal.get( Calendar.YEAR )+".xls";

          WritableWorkbook workBookWriter = Workbook.createWorkbook(new File(excelFile2));
          WritableSheet sheet1 = workBookWriter.createSheet("Uploads",0);

          Date uploadDate = new Date();
          String upDate = uploadDate.toString();

          sheet1.addCell(new Label(0,z,"File Name"));
          sheet1.addCell(new Label(1,z,"Upload Staus"));
          sheet1.addCell(new Label(2,z,"No. of Statements"));
          sheet1.addCell(new Label(3,z,"Uploaded Date"));
          for(z=1;z<m;z++){
          sheet1.addCell(new Label(0,z,fileNames[z]));
          sheet1.addCell(new Label(1,z,uploadStatuss[z]));
          sheet1.addCell(new Label(2,z,uploadTime[z]));
          sheet1.addCell(new Label(3,z,noStmts[z]));
          System.out.println("ROW : "+z+" "+fileNames[z]+uploadStatuss[z]+uploadTime[z]+noStmts[z]);
          }


          workBookWriter.write();
          workBookWriter.close();
          System.out.println("\n");
          System.out.println("\n");
          System.out.println("Report Generation finished");
          System.out.println("REPORT NAME: "+ excelFile2);
    }catch(Exception e){
        e.printStackTrace();
    }


   }
}






     /*
    try{

        excelFileName = "C:/Documents and Settings/in_local/My Documents/controld/check/log/uploadLog.xls";
        ExcelReader excelReader = new ExcelReader(excelFileName);
        sheetCount = excelReader.getSheets();
        stmts= Integer.toString(nStmts);
        excelReader.readSheet(0);

           while(excelReader.next()){
                fileNames[rowCount] = excelReader.getStringValue(0).trim();
                uploadStatuss[rowCount] = excelReader.getStringValue(1).trim();
                uploadTime[rowCount] = excelReader.getStringValue(2).trim();
                noStmts[rowCount] = excelReader.getStringValue(3).trim();
                System.out.println("ROW : "+rowCount+" "+fileNames[rowCount]+uploadStatuss[rowCount]+uploadTime[rowCount]+noStmts[rowCount]);
               rowCount++;

           }
         excelReader.close();

     }catch(Exception ie){
        ie.printStackTrace();
    }

   File excelFile = new File(excelFileName);
                boolean del = excelFile.delete();

                if(del)
                    System.out.println("FILE DELETED SUCCESS:"+excelFileName);
                else
                    System.out.println("FILE NOT DELETED SUCCESS:"+excelFileName);






    WritableWorkbook excelWriter = Workbook.createWorkbook(new File(excelFileName));
    WritableSheet excelSheet = excelWriter.createSheet("ExcelSheet",0);
    WritableSheet excelSheet1 = excelWriter.createSheet("ExcelSheet",1);


    String stmts = Integer.toString(nStmts);

    System.out.println("excelFileName :"+ excelFileName);

    for(rowCount=0;rowCount<=excelSheet.getRows();rowCount++){
        excelCells = excelSheet.getCell(rowCount,columnCount);
        System.out.println("Cell "+rowCount+"Cell Length");
        if(excelCells.getContents()!=null){
            appStatus = true;
            System.out.println("excelCells.getContents() :"+excelCells.getContents());
        }

    }
        try{
                             if((appStatus)){
                             excelSheet.addCell(new Label(0,rowCount,uploadStatus));
                             excelSheet.addCell(new Label(1,rowCount,uploadFile.getName()));
                             excelSheet.addCell(new Label(2,rowCount,stmts));
                             lineCount++;
                             }
                             excelWriter.write();

                     }catch(WriteException we){
                        we.printStackTrace();
                     }finally{
                        excelWriter.close();

                     }
        */

