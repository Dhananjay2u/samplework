package com.sc.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionGetter {

	public static Properties properties=new Properties();
	public static Connection con=null;
	

	
	static {
			
		
		if(properties.isEmpty())
		{
			try{
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("REPORTQUERY.properties"));
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("CONN.properties"));
				//properties.load(ClassLoader.getSystemResource("CONN.properties").openStream());		

				System.out.println(properties.getProperty("report1"));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		}
		System.out.println(properties.getProperty("report1"));
		System.out.println(properties.getProperty("url"));
		
	}
	
	
	public static Connection getNewConnection() throws  ClassNotFoundException, SQLException
	{
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		con=DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("pass"));
		//con[1]=DriverManager.getConnection(p.getProperty("url1"), p.getProperty("username1"), p.getProperty("pass1"));
		return con;		
	}
	
	public static void setConDetails(String url,String username,String pass,String schema) throws  ClassNotFoundException, SQLException
	{
		properties.setProperty("url", url);
		properties.setProperty("username", username);
		properties.setProperty("pass", pass);
		properties.setProperty("schema", schema);
	}
	
	
	
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException
	{
	
		if(getNewConnection()!=null)
			System.out.println("DB Connected");
		/*
		try{
		p.load(ClassLoader.getSystemResource("CONN.properties").openStream());
		System.out.println(p.getProperty("pkey"));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}*/
			
	}
	
}
