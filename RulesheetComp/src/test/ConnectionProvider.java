package test;

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
	public static Connection con=null;
	public static Connection getNewConnection() throws  ClassNotFoundException, SQLException
	{
							
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con=DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("pass"));
		//con[1]=DriverManager.getConnection(p.getProperty("url1"), p.getProperty("username1"), p.getProperty("pass1"));
		return con;		
	}
	
	public static void setConDetails(String url,String username,String pass,String schema) throws  ClassNotFoundException, SQLException
	{
		p.setProperty("url", url);
		p.setProperty("username", username);
		p.setProperty("pass", pass);
		p.setProperty("schema", schema);
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
		System.out.println(p.get("schema"));
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
