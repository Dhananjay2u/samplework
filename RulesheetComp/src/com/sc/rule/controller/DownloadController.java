package com.sc.rule.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadController
 */
public class DownloadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("application/vnd.ms-excel");
		String str=request.getParameter("filepath");
		int ind=str.lastIndexOf("//");
		response.setHeader("Content-Disposition","attachment;filename="+str.substring(ind+2));
		File fi=new File(request.getParameter("filepath"));
		FileInputStream fis=new FileInputStream(fi);
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
