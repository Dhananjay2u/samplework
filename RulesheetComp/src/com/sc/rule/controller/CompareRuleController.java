package com.sc.rule.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.org.xls.ExcelComparator;
import com.sc.rule.bs.VeryBigFileException;

/**
 * Servlet implementation class CompareRuleController
 */
public class CompareRuleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	   private boolean isMultipart;
	   private String existfilePath;
	   private String newfilePath;
	   private String outfilePath;
	   private String rootPath;
	   private int maxFileSize = 2 * 1024 * 1024;
	   private int maxMemSize = 4 * 1024;
	   private File file ;


	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompareRuleController() {
        super();
        // TODO Auto-generated constructor stub
        
    }
    @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	existfilePath=getServletContext().getInitParameter("existFilePath");
	    newfilePath=getServletContext().getInitParameter("newFilePath");
	    outfilePath=getServletContext().getInitParameter("outFilePath");
	    rootPath=getServletContext().getInitParameter("root");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub 
		 System.out.println("Begin."); 
		 String uploadedFileName=null;
		 try{
			 uploadedFileName=uploadRulesheet(request);
		 	}
		 catch(Exception ex) {
			 ex.printStackTrace();			 
			 System.out.println(ex);
			 response.sendRedirect("index.jsp?err="+ex.getMessage());
			 return;
		 }
	     System.out.println("Upload Done.#"+uploadedFileName);
	     try
	     {
	    	 ExcelComparator.getFilesToCompare(rootPath);
	    	 System.out.println("Comparison is done.#");
	     }
	     catch(Exception ex)
	     {
	    	 System.out.println("Error in comparison of rulesheets.#");
	    	 ex.printStackTrace();	    	 
	    	 response.sendRedirect("index.jsp?err=Error in comparison of rulesheets.#"+ex.getMessage());
	    	 return;
	     }
	   if(!new File(outfilePath+"Comp_Res_"+uploadedFileName).exists())
	   {
		   System.out.println("error in name or type");
		   response.sendRedirect("index.jsp?err=Any of Files, Not having correct file name/type.##");		   
	   }
	   else{
		   System.out.println("All given files deletion status :. Exist#" +deleteFile(existfilePath+uploadedFileName)+" NEW#"+deleteFile(newfilePath+uploadedFileName));
		   response.sendRedirect("index.jsp?compFilepath="+outfilePath+"Comp_Res_"+uploadedFileName);
	   }
	}
	
    private boolean deleteFile(String fileName)
    {
    		return new File(fileName).delete();
    }
    private String uploadRulesheet(HttpServletRequest request) throws VeryBigFileException,Exception
	{
		String uploadedFilename=null;
		final String filename=null;
		// Check that we have a file upload request
	      isMultipart = ServletFileUpload.isMultipartContent(request);
	      
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	     
	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // maximum file size to be uploaded.
	      upload.setSizeMax( maxFileSize );

	      
	      // Parse the request to get file items.
	      List fileItems = upload.parseRequest(request);
		
	      // Process the uploaded file items
	      Iterator i = fileItems.iterator();

	      long dt=new Date().getTime();
	      while ( i.hasNext () ) 
	      {
	         FileItem fi = (FileItem)i.next();
	         if ( !fi.isFormField () )	
	         {
	        	 
	            // Get the uploaded file parameters
	            String fieldName = fi.getFieldName();
	            String fileName = fi.getName();
	            String contentType = fi.getContentType();
	            boolean isInMemory = fi.isInMemory();
	            long sizeInBytes = fi.getSize();
	            if(sizeInBytes>(2*1024*1024))
	            	throw new VeryBigFileException("File is Bigger than 2 MB size");
	            String filePath=null;
	            if(fieldName.equals("existingfile"))
	            	filePath=existfilePath;
	            else if(fieldName.equals("newfile"))
	            	filePath=newfilePath;
	            	// Write the file
	            if( fileName.lastIndexOf("\\") >= 0 ){
	               file = new File( filePath + dt+
	               fileName.substring( fileName.lastIndexOf("\\")+1)) ;
	               System.out.println(fileName.substring( fileName.lastIndexOf("\\"))+">>>>>>>>>>>>>>");
	               uploadedFilename= dt + fileName.substring( fileName.lastIndexOf("\\")+1);
	            }else{
	               file = new File( filePath + dt+
	               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	               uploadedFilename= dt + fileName.substring( fileName.lastIndexOf("\\")+1);
	            }
	            fi.write( file ) ;
	            System.out.println("Upload Done...#"+file.getName()+file.getAbsolutePath());
	         }
	      }
	    
	   
	  return uploadedFilename;    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
