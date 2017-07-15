package org.sc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileFinder {
	
	private final static HashMap<String, String> fileTypes= new HashMap<String, String>();;
	
	static{
		initializeFileTypes();
	}
	public FileFinder() {
		super();
		// TODO Auto-generated constructor stub
		
	}

	public static void initializeFileTypes()
	{		
		fileTypes.put("tiff", "image/x-tiff");
		fileTypes.put("doc", "application/msword");
		fileTypes.put("xls", "application/x-msexcel");
		fileTypes.put("pdf", "application/pdf");
		fileTypes.put("gif", "image/gif");
		fileTypes.put("jpg", "image/jpeg");
		fileTypes.put("jpeg", "image/jpeg");
		fileTypes.put("png", "image/png");
		fileTypes.put("txt", "text/plain");
		fileTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		fileTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}
	
	public  static String getMimeType(String extention)
	{
		return fileTypes.get(extention);
	}
	
	public static boolean isExist(String path)
	{
		return new File(path).exists();
	}
	public static ArrayList<File> getFiles(String path)
	{
		
		ArrayList<File> files=null;
		
		File folder=new File(path);
		if(folder.isDirectory())
		{
			files=new ArrayList<File>();
			for(File file:folder.listFiles())
			{
				if(file.isFile())
				{
					files.add(file);
				}
					
			}
		}
				
		return files;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	
		

	}

}
