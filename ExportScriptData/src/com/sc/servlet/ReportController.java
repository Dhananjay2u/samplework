package com.sc.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sc.db.ReportGenerater;

/**
 * Servlet implementation class ReportController
 */
public class ReportController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//File f=new File("");
		//properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("filename.properties"));
		//System.out.println(f.get+">>>>>>");
		response.setContentType("application/vnd.ms-excel");
		String str=request.getParameter("report");
		//int ind=str.lastIndexOf("//");
		response.setHeader("Content-Disposition","attachment;filename="+str+".xls");
		
		FileInputStream fis= new FileInputStream(ReportGenerater.createReport(str, null));
		
		//if(fis==null) System.out.println("NULLLLLLLL");
		//File fi=new File(request.getParameter("filepath"));
		//FileInputStream fis=new FileInputStream(fi);
		OutputStream fos=response.getOutputStream();
		int rd=fis.read();
		
		while(rd!=-1)
		{
			fos.write(rd);
			rd=fis.read();
		}
		fos.flush();
		fis.close();
		fos.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
