package com.org.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import common.Logger;
import eops.common.db.JPersistenceProvider;
import eops.common.util.LogWriter;



public class ConnectionProvider {
	
	public static Properties p=new Properties();
	
	private static JPersistenceProvider sProvider=null;
	private static JPersistenceProvider tProvider=null;
	
	//static LogWriter log = LogWriter.getInstance("ConnectionProvider","", "DBCompConnectionProvider");
	public static JPersistenceProvider getsProvider() {
		return sProvider;
	}

	public static void setsProvider(JPersistenceProvider sProvider) {
		ConnectionProvider.sProvider = sProvider;
	}

	public static JPersistenceProvider gettProvider() {
		return tProvider;
	}

	public static void settProvider(JPersistenceProvider tProvider) {
		ConnectionProvider.tProvider = tProvider;
	}


	public static Connection[] getConnection() throws  ClassNotFoundException, SQLException
	{
		
		
		Connection con[]=new Connection[2];		
		if(sProvider!=null && tProvider!=null )
		{
			con[0]=sProvider.getSQLConnection();
			con[1]=tProvider.getSQLConnection();		
			if(con[0]==null || con[1]==null){
				System.err.println("There has been error while trying to get Connection using JPersistenceProvider");
				return null;
			}
			else{
				System.out.println("Connection Established using JPersistenceProvider...");
				return con;
			}
				
		}
		else if(!p.isEmpty())
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con[0]=DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("pass"));
			con[1]=DriverManager.getConnection(p.getProperty("url1"), p.getProperty("username1"), p.getProperty("pass1"));
			if(con[0]==null || con[1]==null){
				System.err.println("There has been error while trying to get Connection using Normal JDBC Connection details");
				return null;
			}
			else{
				System.out.println("Connection Established using Normal JDBC Connection...");
				return con;
			}		
		}
		else
		{
			System.err.println("There is no available way to establish Connection to DB");
			return null;
		}
	}
	static{
		File ff=new File("");
		System.out.println(ff.getAbsolutePath());
		try {
			p.load(new FileReader(new File(ff.getAbsolutePath()+"\\CONN.properties")));
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(p.isEmpty())
		{
			try{
				p.load(ClassLoader.getSystemResource("CONN.properties").openStream());
				System.out.println(p.getProperty("pkey"));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		}
	}
	
	
	

	

	
public static void main(String args[]) throws IOException
{
	try{
	p.load(ClassLoader.getSystemResource("CONN.properties").openStream());
	System.out.println(p.getProperty("pkey"));
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
}
	

}
