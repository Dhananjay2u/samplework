/*
 * Created on May 10, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author in_local
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package rewards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.Properties;
public class SystemLog {

public static Properties path=new Properties();
	static{						
			initPath();					
	}

	public static void initPath()
	{
		try {								
			FileInputStream fis=new FileInputStream(new File("").getAbsolutePath()+"//path.txt");
			path.load(fis);				
			//System.out.println(p.getProperty("path"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public static void writeErrorLog(String message) {
		try {

			System.out.println("Error");
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println(message + " -- " + new Date());
			System.out.println("------------------------------------------------------------------------------------");

			OutFileWriter fileWriter = new OutFileWriter (SystemLog.path.getProperty("app_error"), true);
			fileWriter.writeLine( message + " -- " + new Date());
			fileWriter.closeFileWriter();
		} catch (Exception e) {
			System.out.println("Exception in error Log " + e);
		}
	}

	public static void writeUploadLog(String fileType, String fileName,
			String userName, String message) {
		try {
			OutFileWriter fileWriter = new OutFileWriter (SystemLog.path.getProperty("app_upload"), true);
			//fileWriter.writeLine(fileType + " - " + fileName + " file " + message + " on " + new Date());
                        fileWriter.writeLine(fileType + " - " + fileName + " "+message + " on " + new Date());
                        //fileWriter.writeLine(" ");
			fileWriter.closeFileWriter();
		} catch (Exception e) {
			System.out.println("Exception in upload Log " + e);
		}
	}


        public static void writeTextLog(File uploadFile, String uploadStaus, int stmts, boolean status) {

                    String nstmts = Integer.toString(stmts);
                    if(stmts==0){
                        nstmts = "Nil";

                    }
                    String textLogFileName = "";
                    Calendar lCal = Calendar.getInstance();
                    textLogFileName =SystemLog.path.getProperty("check_logpath")+ lCal.get( Calendar.DAY_OF_MONTH ) + "-" + lCal.get( Calendar.MONTH ) + "-" + lCal.get( Calendar.YEAR )+ ".txt";
		try {
			OutFileWriter fileWriter = new OutFileWriter (textLogFileName, true);
                        fileWriter.writeLine(uploadFile.getName() + "|" + uploadStaus+ "|"+ nstmts+ "|" + new Date());
			fileWriter.closeFileWriter();
		} catch (Exception e) {
			System.out.println("Exception in upload Log " + e);
		}
	}

 }
