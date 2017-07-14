package com.secmatrix.util;

import java.util.ArrayList;

public class GenUtil {
	
	public static String getStringDataFromArrayList(ArrayList<String> list)
	{
		String data="";
		for(String dt:list)
			data=data+"\n"+dt;
		return data;
	}

}
