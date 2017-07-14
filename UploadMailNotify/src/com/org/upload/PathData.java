package com.org.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PathData {
	public static Properties p=new Properties();
	static{						
			try {
				p.load(new FileReader(new File(new File("").getAbsolutePath()+"//path.txt")));				
				System.out.println(p.getProperty("check_logpath"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
