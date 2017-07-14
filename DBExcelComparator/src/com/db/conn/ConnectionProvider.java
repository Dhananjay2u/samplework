package com.db.conn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

public static Properties p=new Properties();
	
	public static Connection[] getConnection() throws  ClassNotFoundException, SQLException
	{
		Connection con[]=new Connection[2];		
			
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con[0]=DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("pass"));
		con[1]=DriverManager.getConnection(p.getProperty("url1"), p.getProperty("username1"), p.getProperty("pass1"));
		return con;		
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
