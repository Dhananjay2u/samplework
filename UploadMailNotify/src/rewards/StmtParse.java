/**
 * Parser for Control-D Reports.
 *
 * File: StmtParse.java Version : 1.0
 */

package rewards;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.*;

public class StmtParse {

    private String uploadPath = SystemLog.path.getProperty("check_upload");
    private String outputPath = SystemLog.path.getProperty("check_output");
    private OutFileWriter rewardOutWriter = null;
    private BufferedReader bufferedFileReader;
    private String stmtOutFile;
    private boolean writeStatus = false;
    private File fileToParse = null;
    private File uploadDelFile = null;
    //public  static int nStmt = 0;
    boolean success = false;
    //ExcelWriter writeExcel = new ExcelWriter();

    StmtParse() {
        bufferedFileReader = null;
    }
    static int size= 0;
    static int m=0;

    public int fileLines(String file){
        try{

            String rdLine;
            int nLines =0;

            if (file == null) {
            System.out.println("file path is null");
            }
            bufferedFileReader = new  BufferedReader(new FileReader(file));

            while((rdLine  = bufferedFileReader.readLine())!=null){
               size= nLines++;
            }

            System.out.println("NO. of lines:"+size);
            }catch (java.io.IOException e) {
                SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
            } catch (Exception e) { e.printStackTrace();
                e.printStackTrace();
                SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);
            }finally{
                try {
                    if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    }

                }catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
                }

            }

            return size;
  }


    private void readFile(File fileParse) {

        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();
        String[] stmtLine = new String[9000];
        String strPrev1 = null;
        String strPrev2 = null;
        String strSpace = " ";
        int i=0;
        int j=0;
        int k=0;
        int z = 0;
        int totalLines = 0;
        int Lines1 = 0;
        int Lines2 = 0;
        int arrayCount = 0;
        int nStmt = 0;
        if (file == null) {
            System.out.println("file path is null");
            return;
        }

        try {
            bufferedFileReader = new  BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }


        try{
            stmtLine[i] = bufferedFileReader.readLine();
        do{

            if(stmtLine[i].substring(0,1).equals("1")){
                stmtLine[i] = stmtLine[i].substring(1);
            }
            if (stmtLine[i].substring(0,1).equalsIgnoreCase("0")){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = strSpace;
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].substring(0,1).equalsIgnoreCase("-")){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = strSpace;
                       i++;
                       stmtLine[i] = strSpace;
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].length()>0 && stmtLine[i].indexOf(' ')==0){
                 stmtLine[i] = stmtLine[i].substring(1);
            }
            if (stmtLine[i].length()>0 && stmtLine[i].indexOf('+')==0){
                        if (stmtLine[i-1].indexOf(" ")>-1){
                              stmtLine[i-1] = stmtLine[i-1].replaceAll(" ","");
                        }
                              stmtLine[i-1] = stmtLine[i-1].substring(0,9)+stmtLine[i].substring(10,42)+stmtLine[i-1].substring(41);
                              stmtLine[i] = bufferedFileReader.readLine();

                        if (stmtLine[i].indexOf(" ")>-1){
                              stmtLine[i] = stmtLine[i].replaceAll(" ","");
                        }

                        if(stmtLine[i].indexOf("0        NEW BALANCE")>-1){
                              strPrev1 = stmtLine[i];
                              stmtLine[i] = strSpace;
                              i++;
                              stmtLine[i] = strPrev1.substring(1);
                        }

            }
            if(stmtLine[i].indexOf("")>-1){
                        stmtLine[i] = stmtLine[i].replaceAll("","");
            }

            if(stmtLine[i].indexOf('1')==1){
                    if (stmtLine[i].indexOf("1")>-1){
                        stmtLine[i] = stmtLine[i].replaceAll("1","");
                    }
                    if (stmtLine[i].indexOf("1")>-1){
                         stmtLine[i] = stmtLine[i].replaceAll("1","");
                    }
            }
            if(stmtLine[i].indexOf("")>-1 && stmtLine[i].length()>2){

                    if((stmtLine[i-1].indexOf("")>-1) && (stmtLine[i-2].indexOf("")>-1)){
                       stmtLine[i-2] = stmtLine[i];
                       stmtLine[i-1] = bufferedFileReader.readLine();
                            if (stmtLine[i-1].indexOf(" ")>-1){
                                stmtLine[i-1] = stmtLine[i-1].replaceAll(" ","");
                            }
                       stmtLine[i] = bufferedFileReader.readLine();

                    }
            }

            if(stmtLine[i].length()>0){
            if(stmtLine[i].indexOf("")>-1 && stmtLine[i-1].indexOf("")>-1){
                    stmtLine[i-1] = "";
                    stmtLine[i] = "";
            }
            }

            if(stmtLine[i].indexOf("llllll")>-1){
                     for(int n=0;n<=i;n++){
                         if(stmtLine[n].indexOf("END OF STATEMENT")>-1){
                         nStmt = nStmt + 1;
                         }

                         if(k==0){

                             if( (stmtLine[n].indexOf("llllll")>-1 ) || (stmtLine[n].indexOf("")>-1) || (stmtLine[n].indexOf("")>-1)){
                                rewardOutWriter.writeLine(Rpad(stmtLine[n],100));
                             }else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1)&&  !(stmtLine[n].indexOf("")>-1)&&  !(stmtLine[n].indexOf("")>-1) && !(stmtLine[n].indexOf("")>-1) ){
                                rewardOutWriter.writeLine(Rpad("",0));
                            }else{
                                rewardOutWriter.writeLine(Rpad(stmtLine[n],99));
                            }
                             Lines1++;

                         }else{
                            if( (stmtLine[n].indexOf("llllll")>-1 ) || (stmtLine[n].indexOf("")>-1) || (stmtLine[n].indexOf("")>-1)){
                                rewardOutWriter.writeLine(Rpad(stmtLine[n],100));
                            }else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1)&&  !(stmtLine[n].indexOf("")>-1)&&  !(stmtLine[n].indexOf("")>-1) && !(stmtLine[n].indexOf("")>-1) && !(stmtLine[n].indexOf("")>-1)){
                                rewardOutWriter.writeLine(Rpad("",0));
                            }else if(stmtLine[n].trim().length()==2 &&  (stmtLine[n].indexOf("")>-1)){
                                rewardOutWriter.writeLine(Rpad("",2));
                            }
                            else{
                                 if(stmtLine[n].indexOf("")==0 && !(stmtLine[n].indexOf("")==1)){
                                    rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                                 }
                                 else{
                                rewardOutWriter.writeLine(Rpad(stmtLine[n],99));
                                 }
                            }
                            Lines2++;

                         }

                     }

                    for(int n=0;n<=i;n++){
                        stmtLine[n] ="";
                    }
                       i=-1;
                       k++;

             }
            i++;
            j++;
     }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

           System.out.println("No. of STATEMENTS : "+ (nStmt));
           //writeExcel.writeExcelSheet("Started",fileParse,nStmt,false);
            do{
               arrayCount++;
               z++;
            }while(stmtLine[z]!=null);

            for(int n=0;n<arrayCount;n++){
                        if(stmtLine[n].indexOf("")>-1)
                            rewardOutWriter.writeLine(Rpad("",1));
                        else
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],99));
            }

        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
                if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",(nStmt),true);
                }

    }

    private void readHOLD(File fileParse){

        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();

        if (file == null) {
            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readHOLD : " + e);
            return;
        }

        String line;
        line = null;
        int holdLine = 0;
        boolean b;
        b=false;
        String stmtLine[] = new String[1000];
        String strPrev1 = null;
        String strPrev2 = null;
        String strPrev3 = null;
        String strPrev4 = null;
        int i=0;
        int j=0;

        try{
            stmtLine[i] = bufferedFileReader.readLine();

        do{
            if(stmtLine[i].indexOf("1")==0){
                    stmtLine[i] = "";
            }
            if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
            }
            if(stmtLine[i].indexOf("+")>-1){
                    stmtLine[i] = bufferedFileReader.readLine();
            }
            if(stmtLine[i].indexOf('-')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].indexOf('0')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }
            if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
            }

            if(i==11){
                if(stmtLine[i].length()>10){
                       System.out.println("LINE "+stmtLine[i]);
                        stmtLine[i] = " "+stmtLine[i];

                }
            }

            if(stmtLine[i].indexOf("")>-1){

                for(int n=0;n<=i;n++){
                if(j==0){
                    if(stmtLine[n].indexOf("")>-1){
                        rewardOutWriter.writeLine(Rpad("",133));
                    }else if(n==2 || n==3 || n==5 || n==8 || n==9 ){
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],0));
                    }else{
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],0));
                    }
                }
                else{
                    if(stmtLine[n].indexOf("")>-1){
                        rewardOutWriter.writeLine(Rpad("",133));
                    }else if(n==0 || n==1 || n==3 || n==4 || n==6 || n==7 || n==9 || n==10){
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],0));
                    }else{
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],132));
                    }
                }
                }

               for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
               }
               i=-1;
               j++;
               holdLine++;
            }
            i++;



            }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

         }catch (java.io.IOException e) {
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
        System.out.println("No. of STATEMENTS : "+ (holdLine-1));
        if(writeStatus){
                success =  uploadDelFile.delete();
               // System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",holdLine-1,true);
                }


    }

    private void readLO(File fileParse){

        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();
        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }

        String line;
        line = null;
        int loLine = 0;
        boolean b;
        b=false;
        String stmtLine[] = new String[1000];
        String strPrev1 = null;
        String strPrev2 = null;
        String strPrev3 = null;
        String strPrev4 = null;
        int i=0;
        int j=0;

        try{
            stmtLine[i] = bufferedFileReader.readLine();
        do{

            if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
            }

            if(stmtLine[i].indexOf("1>>>>>>")>-1){
                    stmtLine[i] = "";
                    stmtLine[i] = Rpad(stmtLine[i],1);
            }
            if(stmtLine[i].indexOf("+")>-1){
                    stmtLine[i] = bufferedFileReader.readLine();
            }
            if(stmtLine[i].indexOf('-')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].indexOf('0')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if(stmtLine[i].indexOf(" ")>-1 ||(stmtLine[i].indexOf("")==0 && !(stmtLine[i].indexOf("")==1))){

                for(int n=0;n<=i;n++){
                   /*if(j==0) {

                        if(stmtLine[n].trim().length()==0)
                        rewardOutWriter.writeLine(Rpad("",0));
                        else if(stmtLine[n].indexOf(" ")>-1 || (stmtLine[n].trim().length()>1 && stmtLine[n].indexOf("")>-1))
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],137));
                        else if(stmtLine[n].indexOf("")>-1 && !(stmtLine[i].indexOf("")==1))
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                        else
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],136)); */
                    if(j==0){
                       rewardOutWriter.writeLine(Rpad(" ",136));
                   }else{
                    if(stmtLine[n].indexOf(" ")>-1 || (stmtLine[n].trim().length()>1 && stmtLine[n].indexOf("")>-1))
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],137));
                    else if(stmtLine[n].indexOf("")>-1 && !(stmtLine[i].indexOf("")==1))
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                   else
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],136));
                }
                     j++;
                }


               for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
               }
               i=-1;

                loLine++;
            }
            i++;



           }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);
        }catch (java.io.IOException e) {
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ loLine);

               if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",loLine,true);
                }



    }

     private void readLF(String file){
        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }

        String line;
        line = null;
        int loLine = 0;
        boolean b;
        b=false;
        try {

            while ((line = bufferedFileReader.readLine()) != null) {
                 loLine++;
                 if(line.indexOf("1")>-1){
                    line = line.replaceAll("1","");

                 }


                  if(line.indexOf('-')==0){

                    line = line.replaceAll("-","");
                    rewardOutWriter.writeLine("");
                    rewardOutWriter.writeLine("");

                   }
             if(line.indexOf('+')==0){

                    continue;
                 }
                 if (line.length()>0 && line.indexOf(' ')==0){
                 line = line.substring(0);
                 }
                 if(loLine==1){

                 /*  line = line.replaceAll("1>>>>>>CTD SYS USE ONLY : INP9001  - LO-080102","");
                    line = line.replaceAll(">>>>>>CTD SYS USE ONLY : INP9001  - LF-080104","");
                    line = line.replaceAll("1>>>>>>CTD SYS USE ONLY : INP9001  - LM-080105","");
                    line = line.replaceAll("1>>>>>>CTD SYS USE ONLY"," ");*/
                     rewardOutWriter.writeLine("");
                     //line = line.substring(55);
                     continue;
                 }
                 if(line.indexOf("1")>-1){
                        line = line.replaceAll("1","");
                 }
                if(line.indexOf(' ')==0){

                    line = line.substring(1);

                   }

                    rewardOutWriter.writeLine(line);

            }
         }catch (java.io.IOException e) {
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
    }

      private void readAPPS(File fileParse){

          uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();
        if (file == null) {
            System.out.println("file path is null");
            return;
        }

        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }

        String[] stmtLine = new String[1000];
        String strPrev1 = null;
        String strPrev2 = null;
        String strPrev3 = null;
        String strPrev4 = null;
        int i=0;
        int j=0;
        int appsLine =0;

        try{
            stmtLine[i] = bufferedFileReader.readLine();

        do{
            if(i==0 && stmtLine[i].indexOf("1")>-1){
                stmtLine[i] = "";
            }

            if(stmtLine[i].indexOf('-')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }

            if (stmtLine[i].indexOf('0')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }

             if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");

            }

            if(stmtLine[i].indexOf("")>-1){

                for(int n=0;n<=i;n++){
                if(j==0){
                    if(stmtLine[n].indexOf("")>-1){
                        rewardOutWriter.writeLine(Rpad("",137));
                    }else if(n==2 || n==3 || n==5 || n==8 || n==9 ){
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],0));
                    }else{
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],136));
                    }
                }
                else{
                    if(stmtLine[n].indexOf("")>-1){
                        rewardOutWriter.writeLine(Rpad("",137));
                    }else if(n==1 || n==2 || n==4 || n==7 || n==8 ){
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],0));
                    }else{
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],136));
                    }
                }
                }

               for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
               }
               i=-1;
               j++;
               appsLine++;
            }
            i++;

        }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

     }catch(java.io.IOException e){
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
     }catch(Exception e){
            e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);
     }finally{
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }
            }catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

     System.out.println("No. of STATEMENTS : "+ appsLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",appsLine,true);
                }



    }

     private void readSUM(File fileParse){
         uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();

        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String line;
        line = null;
        int sumLine = 0;
        boolean b;
        b=false;

             String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int j=0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();

        do{
           if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
            }

            if(stmtLine[i].indexOf("1200")>-1){
                        stmtLine[i] = stmtLine[i].replaceAll("1200","200");
            }


           if (stmtLine[i].indexOf('0')==0 ){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
                    }
            if (stmtLine[i].indexOf('-')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = " "+strPrev1.substring(1);
            }
             if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if(stmtLine[i].indexOf("200")>-1 || stmtLine[i].indexOf("")>-1){

               for(int n=0;n<=i;n++){
                        if(stmtLine[n].indexOf("200")>-1 )
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],133));
                        else  if(stmtLine[n].indexOf("")>-1 && !(stmtLine[n].indexOf("2")==1))
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                        else if(stmtLine[n].trim().length()==0 )
                            rewardOutWriter.writeLine(Rpad("",0));
                        else
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],132));

               }
                for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
               }
               i=-1;
                j++;
                sumLine++;
            }

         i++;
           }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);
           /*
            while ((line = bufferedFileReader.readLine()) != null) {
                 sumLine++;
                 if(line.indexOf("1")>-1){
                    line = line.replaceAll("1","");

                 }

                if (line.length()>0 && line.indexOf(' ')==0){
                 line = line.substring(1);
                 }

                if(sumLine==1){
                      /*   if(line.indexOf("1200")>-1){
                    line = line.replaceAll("1200","200");

                 }
                     line =line.substring(1);

                 }
                 if(line.indexOf("-                   TOTAL NUMBER OF STATEMENTS SUPPRESSED =")>-1){

                 //System.out.println("INside:"+sumLine);
                 rewardOutWriter.writeLine("");
                 rewardOutWriter.writeLine("");
                 line = line.replaceAll("-                   TOTAL NUMBER OF STATEMENTS SUPPRESSED =","                   TOTAL NUMBER OF STATEMENTS SUPPRESSED =");
                 }

           /*    if(sumLine==2){
                     System.out.println("INside:"+sumLine);
                        if(line.indexOf("-")==0){

                 rewardOutWriter.writeLine("");
                 rewardOutWriter.writeLine("");
                 line = ' '+line.substring(2);
                        }

                 }
                  if(line.indexOf("0TOTAL")>-1){

                 rewardOutWriter.writeLine("");
                 line = line.replaceAll("0TOTAL","TOTAL");
                 }


                    rewardOutWriter.writeLine(line);

            }*/
         }catch (java.io.IOException e) {
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ sumLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",sumLine,true);
                }



    }




      private void readDR(File fileParse){

        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();
        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }

        String line;
        line = null;
        int drLine = 0;
        boolean b;
        b=false;

        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int j=0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();

        do{
           if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
                    if(stmtLine[i].indexOf("DJDE")>-1){
                        stmtLine[i] = "";
                    }
            }

            if(stmtLine[i].indexOf("1>>>>>>")>-1){
                    stmtLine[i] = "";
                    stmtLine[i] = Rpad(stmtLine[i],1);
            }

            if (stmtLine[i].indexOf('0')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = " "+strPrev1.substring(1);
                    }
            if (stmtLine[i].indexOf('-')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = " "+strPrev1.substring(1);
            }
             if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if(stmtLine[i].indexOf(" ")>-1 ||(stmtLine[i].indexOf("")==0 && !(stmtLine[i].indexOf("")==2))){

               if(stmtLine[i].indexOf(" ")>-1){
                    drLine++;
               }


               for(int n=0;n<=i;n++){

                        if(stmtLine[n].indexOf(" ")>-1 )
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],137));
                        else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1) &&  !(stmtLine[n].indexOf("")>-1)  )
                                rewardOutWriter.writeLine(Rpad("",0));
                        else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1))
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                        else
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],136));

               }
                for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
               }
               i=-1;
                j++;

            }

         i++;
           }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);


         }catch (java.io.IOException e) {
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ drLine);
         if(writeStatus){
                success =  uploadDelFile.delete();
        //        System.out.println(" Upload File Deleted : " +success);
          //      System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",drLine,true);
                }


    }
   private void readIMXAP(File fileParse){
       uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();

        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int n;
        int lineCount = 0;
        int imxapLine =0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();
        do{

            if(stmtLine[i].indexOf('1')==0&& stmtLine[i].length()==1){
                stmtLine[i] = stmtLine[i].substring(1);
            }
            if(i==0){
                if(stmtLine[i].indexOf("1>>>>>>CTD SYS USE ONLY : INP9004  - IMXAP081")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1>>>>>>CTD SYS USE ONLY : INP9004  - IMXAP081","");
                }
                else if(stmtLine[i].indexOf("1")==0){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
                }
            }
            if(stmtLine[i].indexOf('|')==41){
                stmtLine[i] = stmtLine[i].replace('|','3');
            }
             if(stmtLine[i].indexOf('¦')>-1){
                stmtLine[i] = stmtLine[i].replace('¦','|');
            }
            if(stmtLine[i].indexOf("1")>-1){
                stmtLine[i] = stmtLine[i].replaceAll("1","");
            }
            if(stmtLine[i].indexOf('+')==0 && stmtLine[i].indexOf(' ')==1){
                continue;
            }
            if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }
            if(stmtLine[i].indexOf('+')==0 && stmtLine[i].indexOf('-')!=1){
                stmtLine[i-1] = stmtLine[i].substring(1);
                continue;
            }

            lineCount++;
            if((stmtLine[i].indexOf("")>-1)){
                imxapLine++;
            }
            i++;
       }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

        /*for(int n=0;n<i;n++){
            rewardOutWriter.writeLine(stmtLine[n]);
        }*///stmtLine[i-1] = stmtLine[i-1].trim();
             for( n=0;n<i;n++){

                    if((stmtLine[n].indexOf("")>-1)){

                            if(n==i-1){
                                rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                            }
                            else{
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],133));
                            }
                    }
                    else if((n==lineCount)&&(stmtLine[n].indexOf("")>-1)){
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                            System.out.println("I am line"+" "+n+" "+stmtLine[n]);
                    }
                    else{
                        rewardOutWriter.writeLine(Rpad(stmtLine[n],132));
                    }
            }

        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ (imxapLine-6));
         if(writeStatus){
                success =  uploadDelFile.delete();
            //    System.out.println(" Upload File Deleted : " +success);
              //  System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",imxapLine-6,true);
                }

   }

    private void readIMXHP(File fileParse){

        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();

        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int imxhpLine =0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();
        do{

            if(i==0){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
            }
            if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if (stmtLine[i].indexOf('0')==0&& stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].indexOf('0')==0&& (stmtLine[i].indexOf('C')==1 || stmtLine[i].indexOf('E')==1 ||stmtLine[i].indexOf('B')==1 ||stmtLine[i].indexOf('L')==1 ||stmtLine[i].indexOf('I')==1)){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].indexOf('-')==0 && (stmtLine[i].indexOf(' ')==1 || stmtLine[i].indexOf('N')==1)){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if (stmtLine[i].indexOf("---------------------------------------------------------------------------------")>-1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if(stmtLine[i].indexOf("1")>-1){
                stmtLine[i] = stmtLine[i].replaceAll("1","");
           }

            if(stmtLine[i].indexOf("")>-1){
                 for(int n=0;n<=i;n++){
		 if(stmtLine[n].indexOf("END OF STATEMENT")>-1){             
			imxhpLine++;
			}

                    if(stmtLine[n].indexOf("")>-1 )
                            rewardOutWriter.writeLine(Rpad("",133));
                    else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1))
                            rewardOutWriter.writeLine(Rpad("",0));
                    else
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],132));

                 }
                 for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
                 }
                i=-1;
            
            }

         i++;
           }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);



        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
        System.out.println("No. of STATEMENTS : "+ imxhpLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
   //             System.out.println(" Upload File Deleted : " +success);
     //           System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",imxhpLine,true);
                }

   }
      private void readXPO(File fileParse){
        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();


        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        String strPrev3 = null;
        int i=0;
        int intLineCnt = 0;
        int xpoLine = 0;

        try{
             stmtLine[i] = bufferedFileReader.readLine();

        do{

            if(i==0 && stmtLine[i].indexOf("1")==0){
                    stmtLine[i] = stmtLine[i].replaceFirst("1","SS");
            }

            if(stmtLine[i].indexOf('+')==0){

                stmtLine[i] = bufferedFileReader.readLine();

            }
            if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if (stmtLine[i].indexOf('0')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }

            if(stmtLine[i].indexOf("1")>-1){
                stmtLine[i] = stmtLine[i].replaceAll("1","");
            }

           if (stmtLine[i].indexOf('-')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if(stmtLine[i].indexOf("-") > -1 ){

                stmtLine[i] = stmtLine[i].replaceFirst("-","");
                   strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(0);

            }

            if(stmtLine[i].indexOf(" ") > -1 || stmtLine[i].indexOf("") > -1){
                if(stmtLine[i].indexOf(" ") > -1){
                    xpoLine++;
                }

                //System.out.println("array length : "+i);
                    for(int n=0;n<=i;n++){


                       if((n==73 || n==146 ||n==219||n==292) && (stmtLine[n].trim().length()<=1)){
                                 strPrev3 = stmtLine[n];
                                 stmtLine[n]=" ";
                                 if(((stmtLine[n].indexOf(" ")>-1) &&(strPrev3.indexOf(" ")>-1))||((stmtLine[n].indexOf(" ")>-1) &&(strPrev3.indexOf("")>-1))){
                                 rewardOutWriter.writeLine(strPrev3);
                                 }
                                 else{
                                 rewardOutWriter.writeLine(stmtLine[n]);
                                 rewardOutWriter.writeLine(strPrev3);
                                 }
                                 //System.out.println("I am line"+" "+n+" "+stmtLine[n]);
                        }
                        else{
                        //System.out.println("Line "+" "+n+" "+stmtLine[n]);
                        rewardOutWriter.writeLine(stmtLine[n]);
                        }
                    }

                    for(int n=0;n<=i;n++){
                        stmtLine[n] ="";
                    }

                    i=-1;

            }
         i++;
         intLineCnt++;

         }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
        System.out.println("No. of STATEMENTS : "+ xpoLine);
         if(writeStatus){
                success =  uploadDelFile.delete();
       //         System.out.println(" Upload File Deleted : " +success);
         //       System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",xpoLine,true);
                }

   }


      private void readIWB(File fileParse){

          uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
            String file = fileParse.getAbsolutePath();


        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int iwbLine =0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();

        do{


            if(i==0 && stmtLine[i].indexOf("1")==0){
                    stmtLine[i] = stmtLine[i].replaceFirst("1","SS");
            }
            if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if (stmtLine[i].indexOf('0')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
                    }
            if (stmtLine[i].indexOf('-')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.substring(1);
            }
            if(stmtLine[i].indexOf("1")>-1){
                stmtLine[i] = stmtLine[i].replaceAll("1","");
           }
            if(stmtLine[i].indexOf("+")>-1 && stmtLine[i].indexOf("+")==0){
                stmtLine[i] = stmtLine[i].substring(1);
           }

         i++;
           }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

        for(int n=0;n<i;n++){
            if(stmtLine[n].indexOf("")>-1){
                iwbLine++;
            }
            rewardOutWriter.writeLine(stmtLine[n]);

        }

        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
        System.out.println("No. of STATEMENTS : "+ (iwbLine-3));
         if(writeStatus){
                success =  uploadDelFile.delete();
           //     System.out.println(" Upload File Deleted : " +success);
             //   System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",iwbLine-3,true);
                }


   }
     private void readXD(File fileParse){

         uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();


        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int j=0;
        int xdLine = 0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();

        do{
           if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
                    if(stmtLine[i].indexOf("DJDE")>-1){
                        stmtLine[i] = "";
                    }
            }

            if(stmtLine[i].indexOf("1>>>>>>")>-1){
                    stmtLine[i] = "";
                    stmtLine[i] = Rpad(stmtLine[i],1);
            }

            if (stmtLine[i].indexOf('0')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = " "+strPrev1.substring(1);
                    }
            if (stmtLine[i].indexOf('-')==0 && stmtLine[i].indexOf(' ')==1){
                        strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = " "+strPrev1.substring(1);
            }
             if(stmtLine[i].length()>1 && stmtLine[i].indexOf(' ')==0){
                stmtLine[i] = stmtLine[i].substring(1);
            }

            if(stmtLine[i].indexOf(" ")>-1 ||(stmtLine[i].indexOf("")==0 && !(stmtLine[i].indexOf("")==2))){
               if(stmtLine[i].indexOf(" ")>-1){
                 xdLine++;
               }
               for(int n=0;n<=i;n++){

                        if(stmtLine[n].indexOf(" ")>-1 )
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],137));
                        else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1) &&  !(stmtLine[n].indexOf("")>-1)  )
                                rewardOutWriter.writeLine(Rpad("",0));
                        else if(stmtLine[n].trim().length()==0 &&  !(stmtLine[n].indexOf("")>-1))
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],1));
                        else
                            rewardOutWriter.writeLine(Rpad(stmtLine[n],136));

               }
                for(int n=0;n<=i;n++){
                    stmtLine[n] ="";
               }
               i=-1;
                j++;

            }

         i++;
           }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }
        System.out.println("No. of STATEMENTS : "+ xdLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",xdLine,true);
                }



   }

    private void readDDA(File fileParse){

        uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();


        if (file == null) {
            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[5000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int lines =1;
        int ddaLine = 0;
        try{

           stmtLine[i] = bufferedFileReader.readLine();
            do{
                if(stmtLine[i].indexOf(' ')==0){
                    stmtLine[i] = stmtLine[i].substring(1);
                }
                if(stmtLine[i].indexOf('+')==0 ||stmtLine[i].indexOf('4')==0){
                    stmtLine[i] = stmtLine[i].substring(1);
                }
                if(stmtLine[i].indexOf("|")>-1){
                    stmtLine[i] = stmtLine[i].replace('|',' ');
                }
                if(stmtLine[i].indexOf("************************************************************************")>-1){
                    i++;
                    stmtLine[i] = bufferedFileReader.readLine();
                    stmtLine[i] = stmtLine[i].substring(1);
                    i++;
                    stmtLine[i] = bufferedFileReader.readLine();
                    if(stmtLine[i].indexOf("I")!=1&&stmtLine[i].indexOf("P")!=1){
                        stmtLine[i-1] = stmtLine[i-1].replaceAll("","");
                        stmtLine[i] = stmtLine[i].replaceAll("  "," ");
                        i++;
                        stmtLine[i] = "";
                        i++;
                        stmtLine[i] = " ";
                }
                if(stmtLine[i].indexOf("I")==1){
                    stmtLine[i] = stmtLine[i].substring(1);
                }
                }


                if(stmtLine[i].indexOf('^')>-1){
                    stmtLine[i] = stmtLine[i].replace('^','ª');
                }


                if(stmtLine[i].indexOf(".....×")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll(".....×",".....-");
                }
                if (stmtLine[i].indexOf('0')==0){
                   strPrev1 = stmtLine[i];
                   stmtLine[i] = "";
                   i++;
                   stmtLine[i] = strPrev1.substring(1);
                }
                if (stmtLine[i].indexOf('-')==0 && (stmtLine[i].indexOf('D')==1 ||stmtLine[i].indexOf('A')==1)){
                   strPrev1 = stmtLine[i];
                   stmtLine[i] = "";
                   i++;
                   stmtLine[i] = "";
                   i++;
                   stmtLine[i] = strPrev1.substring(1);
                }
                if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
                }
                if(i==0 && lines==1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","SS");
                }
                if(stmtLine[i].indexOf(" Pigeon")>-1){
                    stmtLine[i] = stmtLine[i].replaceFirst(" Pigeon","Pigeon");
                }
                if (stmtLine[i].indexOf(".PPPPPP FORMS=MNH") > -1 || stmtLine[i].indexOf("DJDE   JDE=DFLT,JDL=DFAULT,END;") > -1 || stmtLine[i].indexOf(".") > -1 ||stmtLine[i].indexOf("") > -1){
                    

                    for(int n=0;n<=i;n++){
			if(stmtLine[n].indexOf("TOTAL")>-1){
			
                    ddaLine++;
			//System.out.println("dda:"+ddaLine);
                    }
                        rewardOutWriter.writeLine(stmtLine[n]);

                    }
                    for(int n=0;n<=i;n++){
                        stmtLine[n] ="";
                    }
                    i=-1;

                }
                i++;
                lines++;
            }while((stmtLine[i] = bufferedFileReader.readLine())!=null);


        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ ddaLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
               // System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",ddaLine,true);
                }

   }
     private void readMM(File fileParse){

         uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();


        if (file == null) {
            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[5000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int lines =1;
        int mmLine =0;
        try{

            stmtLine[i] = bufferedFileReader.readLine();
            do{
                if(stmtLine[i].indexOf(' ')==0){
                    stmtLine[i] = stmtLine[i].substring(1);
                }
                if(stmtLine[i].indexOf('+')==0 ||stmtLine[i].indexOf('4')==0){
                    stmtLine[i] = stmtLine[i].substring(1);
                }
                if(stmtLine[i].indexOf("|")>-1){
                    stmtLine[i] = stmtLine[i].replace('|','³');
                }
                if(stmtLine[i].indexOf("************************************************************************")>-1){
                    i++;
                    stmtLine[i] = bufferedFileReader.readLine();
                    stmtLine[i] = stmtLine[i].substring(1);
                    i++;
                    stmtLine[i] = bufferedFileReader.readLine();
                    if(stmtLine[i].indexOf("I")!=1&&stmtLine[i].indexOf("P")!=1){
                        stmtLine[i-1] = stmtLine[i-1].replaceAll("","");
                        stmtLine[i] = stmtLine[i].replaceAll("  "," ");
                        i++;
                        stmtLine[i] = "";
                        i++;
                        stmtLine[i] = " ";
                }
                if(stmtLine[i].indexOf("I")==1){
                    stmtLine[i] = stmtLine[i].substring(1);
                }
                }
                if(stmtLine[i].indexOf(".....×....³.^..")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll(".....×....³.^..",".....-....³.ª..");
                }
                if (stmtLine[i].indexOf('0')==0){
                   strPrev1 = stmtLine[i];
                   stmtLine[i] = "";
                   i++;
                   stmtLine[i] = strPrev1.substring(1);
                }
                if (stmtLine[i].indexOf('-')==0 && (stmtLine[i].indexOf('D')==1 ||stmtLine[i].indexOf('A')==1||stmtLine[i].indexOf('B')==1 ||stmtLine[i].indexOf('L')==1 )||stmtLine[i].indexOf('N')==1 ){
                   strPrev1 = stmtLine[i];
                   stmtLine[i] = "";
                   i++;
                   stmtLine[i] = "";
                   i++;
                   stmtLine[i] = strPrev1.substring(1);
                }
                if(stmtLine[i].indexOf("1")>-1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","");
                }
                if(i==0 && lines==1){
                    stmtLine[i] = stmtLine[i].replaceAll("1","SS");
                }
                if(stmtLine[i].indexOf(" Pigeon")>-1){
                    stmtLine[i] = stmtLine[i].replaceFirst(" Pigeon","Pigeon");
                }
                if (stmtLine[i].indexOf(".PPPPPP FORMS=MNH") > -1 || stmtLine[i].indexOf("DJDE   JDE=DFLT,JDL=DFAULT,END;") > -1 || stmtLine[i].indexOf(".") > -1 ||stmtLine[i].indexOf("") > -1){
                    

                    for(int n=0;n<=i;n++){
			if(stmtLine[n].indexOf("YOUR ACCOUNT BALANCES") > -1){
                    mmLine++;
                    }
                        rewardOutWriter.writeLine(stmtLine[n]);
                    }
                    for(int n=0;n<=i;n++){
                        stmtLine[n] ="";
                    }
                    i=-1;

                }
                i++;
                lines++;
            }while((stmtLine[i] = bufferedFileReader.readLine())!=null);

        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);

        } finally {
            try {

                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }

            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ mmLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",mmLine,true);
                }

   }

      private void readXS(File fileParse){
          uploadDelFile = new File(uploadPath + "/" +  fileParse.getName());
        String file = fileParse.getAbsolutePath();


        if (file == null) {

            System.out.println("file path is null");
            return;
        }
        try {
            bufferedFileReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.readFile 1 : " + e);
            return;
        }
        String[] stmtLine = new String[900000];
        String strPrev1 = null;
        String strPrev2 = null;
        int i=0;
        int xsLine =0;
        try{
            stmtLine[i] = bufferedFileReader.readLine();
        do{

            if(i==0){
                stmtLine[i] = "";
                i++;
                stmtLine[i] = bufferedFileReader.readLine();
            }
            if(i==1){
                stmtLine[i] = "";
                i++;
                stmtLine[i] = bufferedFileReader.readLine();
            }
            if(i==2){
                stmtLine[i] = "";
                i++;
                stmtLine[i] = bufferedFileReader.readLine();
            }
            if(i==3){
                stmtLine[i] = " ";
                i++;
                stmtLine[i] = bufferedFileReader.readLine();
            }
            if(i>3){
                if(stmtLine[i].indexOf('')!=0){
                if(stmtLine[i].indexOf('0')==0){
                       strPrev1 = stmtLine[i];
                       stmtLine[i] = "";
                       i++;
                       stmtLine[i] = strPrev1.replace('0',' ');
                }
                stmtLine[i] = stmtLine[i].substring(1);
                }
            }
            i++;
        }while( (stmtLine[i] = bufferedFileReader.readLine())!=null);

        for(int n=0;n<i;n++){
            //rewardOutWriter.writeLine(stmtLine[n]);
           if(n==0||n==1||n==2||n==27||n==29||n==31||n==37||n==39||n==33||n==46){
            rewardOutWriter.writeLine(stmtLine[n]);
            }
            else{
            rewardOutWriter.writeLine(Rpad(stmtLine[n],136));
            }
           xsLine++;
        }
        }catch (IOException e) {
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 2 : " + e);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            System.out.println("strLine:"+stmtLine);
            SystemLog.writeErrorLog("StmtParse.readFile 3 : " + e);
        } finally {
            try {
                if (bufferedFileReader!= null) {
                    bufferedFileReader.close();
                    bufferedFileReader = null;
                    writeStatus = true;
                }
            } catch(Exception e){
                SystemLog.writeErrorLog("StmtParse.readFile 4 : " + e);
            }
        }

        System.out.println("No. of STATEMENTS : "+ xsLine);
        if(writeStatus){
                success =  uploadDelFile.delete();
                //System.out.println(" Upload File Deleted : " +success);
                //System.out.println(" File to be Deleted :" + uploadPath + "/" +  fileParse.getName());
                SystemLog.writeTextLog(fileToParse,"Completed",xsLine,true);
                }


   }



    public void parse(String MnuOption) {
        try {
            File fleList = new File(uploadPath);
            File stmtFiles[]= fleList.listFiles();
            String fileName="";
            boolean  del;

             if(stmtFiles.length==0){
                System.out.println("NO FILE FOUND");
                SystemLog.writeUploadLog("No file found ", fileToParse.getAbsolutePath(), "", "upload not started");
             }

            for(int intFileCnt = 0; intFileCnt < stmtFiles.length; intFileCnt ++) {

                fileToParse = stmtFiles[intFileCnt];
                SystemLog.writeUploadLog("Started ", fileToParse.getAbsolutePath(), "", "upload started");



                rewardOutWriter = new OutFileWriter(outputPath + "/" +  fileToParse.getName(),false);
                fileName = fileToParse.getName();

               if(MnuOption.equalsIgnoreCase(Main.MENU_STMT)){

                         if(fileName.indexOf("CS7")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readFile(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                              System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD CARD or MANHATTAN STMT FILES and SELECT MENU OPTION '1'");

                            SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
							 System.out.println("\n");
                             writeStatus = true;

                         }
               }

               if(MnuOption.equalsIgnoreCase(Main.MENU_HOLD)){
                        if(fileName.indexOf("MANH")>-1 || fileName.indexOf("OLC")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readHOLD(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD HOLD or MANH FILES and SELECT MENU OPTION '2'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                              System.out.println("\n");
                             writeStatus = true;

                         }
               }

               if(MnuOption.equalsIgnoreCase(Main.MENU_LO)){
                         if(fileName.indexOf("LF")>-1 ||fileName.indexOf("LO")>-1 ||fileName.indexOf("LM")>-1 ||fileName.indexOf("RO")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readLO(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                              System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD LO or LM or RO FILES and SELECT MENU OPTION '3'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                              System.out.println("\n");
                             writeStatus = true;
                         }
               }

                if(MnuOption.equalsIgnoreCase(Main.MENU_APPS)){
                        if(fileName.indexOf("AP0")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readAPPS(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD APPS FILES and SELECT MENU OPTION '4'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;

                         }
                }

                if(MnuOption.equalsIgnoreCase(Main.MENU_SUM)){
                         if(fileName.indexOf("SUM")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readSUM(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD SUMMARY FILES and SELECT MENU OPTION '5'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }

                if(MnuOption.equalsIgnoreCase(Main.MENU_LF)){
                         if(fileName.indexOf("LF")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readLF(fileToParse.getAbsolutePath());
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD LF FILES and SELECT MENU OPTION '16'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }

                }
                if(MnuOption.equalsIgnoreCase(Main.MENU_DDA)){
                         if(fileName.indexOf("DDA")>-1){
                            System.out.println("Parsing : " + fileToParse.getAbsolutePath());
                            readDDA(fileToParse);
                            System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                            System.out.println("\n");
                            writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD DDA FILES and SELECT MENU OPTION '6'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }

                   if(MnuOption.equalsIgnoreCase(Main.MENU_MM)){

                         if(fileName.indexOf("MM")>-1 ||fileName.indexOf("INT")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readMM(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD MM or INT or DDASTMTPART1 FILE and SELECT MENU OPTION '7'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }

                if(MnuOption.equalsIgnoreCase(Main.MENU_DR)){
                         if(fileName.indexOf("DR0")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readDR(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD DR FILES and SELECT MENU OPTION '8'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }

                if(MnuOption.equalsIgnoreCase(Main.MENU_IMXAP)){
                         if(fileName.indexOf("IMXAP")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readIMXAP(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD IMXAP FILES and SELECT MENU OPTION '9'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }

                 if(MnuOption.equalsIgnoreCase(Main.MENU_XD)){
                         if(fileName.indexOf("XD0")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readXD(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED ");
                             System.out.println("PLEASE UPLOAD XD0 FILE and SELECT MENU OPTION '10'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
               }

                if(MnuOption.equalsIgnoreCase(Main.MENU_IMXHP)){
                         if(fileName.indexOf("IMXHP")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readIMXHP(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD IMXHP or IMX542 FILES and SELECT MENU OPTION '11'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;

                         }
                }

                  if(MnuOption.equalsIgnoreCase(Main.MENU_XPO)){
                         if(fileName.indexOf("XP0")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readXPO(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED ");
                             System.out.println("PLEASE UPLOAD XP0 FILE and SELECT MENU OPTION '12'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }


                if(MnuOption.equalsIgnoreCase(Main.MENU_IWB)){
                         if(fileName.indexOf("IWB")>-1 ||fileName.indexOf("SWB")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readIWB(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED ");
                             System.out.println("PLEASE UPLOAD IWB or SWB FILE and SELECT MENU OPTION '13'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                }

                 if(MnuOption.equalsIgnoreCase(Main.MENU_XS)){
                          if(fileName.indexOf("XSO")>-1){
                             System.out.println("PARSING : " + fileToParse.getAbsolutePath());
                             readXS(fileToParse);
                             System.out.println(fileToParse.getName()+" PROCESS COMPLETED SUCCESSFULLY");
                             System.out.println("\n");
                             writeStatus = false;
                         }
                         else{
                             System.out.println(fileToParse.getAbsoluteFile()+" PROCESS NOT COMPLETED");
                             System.out.println("PLEASE UPLOAD XS FILE and SELECT MENU OPTION '14'");
                             SystemLog.writeTextLog(fileToParse,"Error.Selected option is not correct for this file",0,true);
                             System.out.println("\n");
                             writeStatus = true;
                         }
                 }
                rewardOutWriter.closeFileWriter();

                /*Preventing writing of output file in case of wrong option selection*/

                File outputDelFile = new File(outputPath + "/" +  fileToParse.getName());
                boolean success;

                if(writeStatus){
                success =  outputDelFile.delete();
                //System.out.println(" Output File Deleted : " +success);
                //System.out.println(" Output File to be Deleted :" + outputPath + "/" +  fileToParse.getName());
                SystemLog.writeUploadLog("Error ",fileToParse.getAbsolutePath(),""," Selected option is not correct for this file");
                }
                else{
                SystemLog.writeUploadLog("Completed ", fileToParse.getAbsolutePath(), "", "upload completed");

                }
            }

        } catch (Exception e) { e.printStackTrace();
            SystemLog.writeErrorLog("StmtParse.parse : " + e);
        } finally {
            try {
               rewardOutWriter.closeFileWriter();

            } catch (Exception e) { e.printStackTrace();

            }
        }
    }


    public static boolean isNumeric(String s){
		s = s.toLowerCase();
        if((s == null) || (s.length() < 1) )
              return false;
//            throw new IllegalArgumentException("String should have length > 0");

        String strAlphabets[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        for (int ab = 0; ab <= strAlphabets.length-1; ab++)
        {
            if(s.indexOf(strAlphabets[ab]) > -1)
            {
                return false;
            }
        }
        return true;
    }

	public static String Lpad(String StrLPad,int intStrLen) {
        int xx    = 0;
        int ii    = 0;
        String yy    = "";
        xx = StrLPad.length();
        if (xx < intStrLen){
           for (ii = 1; ii <= (intStrLen - xx); ii++){
               yy = yy + " ";
           }
           return yy + StrLPad;
        }
        else
        {
           return StrLPad.substring(1, intStrLen+1);
        }
    }
    public static String Rpad(String StrRPad,int intStrLen) {
        int xx    = 0;
        int ii    = 0;
        String yy    = "";
        xx = StrRPad.length();
        if (xx < intStrLen){
           for (ii = 1; ii <= (intStrLen - xx) ; ii++){
               yy = yy + " ";
           }
           return StrRPad + yy ;

        }
        else
        {
           if (xx == intStrLen)
              return StrRPad;
           else
              return StrRPad.substring(1, intStrLen);
        }
    }


}