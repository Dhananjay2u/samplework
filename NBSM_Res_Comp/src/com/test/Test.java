package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.StringTokenizer;

public class Test {

	
	public static int checkResponseCode(String a060, String a020, String countryCode)
	{
		if(a060!=null && a020!=null && countryCode!=null)
		{
			if(a060.equalsIgnoreCase("YES") && a020.equalsIgnoreCase("NO") && countryCode.equalsIgnoreCase("KE"))
				return 12;
			else if(a060.equalsIgnoreCase("YES") && a020.equalsIgnoreCase("YES") && countryCode.equalsIgnoreCase("KE"))
				return 8;
			else if(a060.equalsIgnoreCase("YES") && a020.equalsIgnoreCase("YES") && !countryCode.equalsIgnoreCase("KE") )
				return 11;
			else
				return 15;
		}
		else			
			return 15;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			System.out.println(checkResponseCode("YES", "NO", "NG")+"--->");
		

	}
	
	public static void displayAllCombSum()	
	{
		Integer intArray[]=new Integer[10];
		Random rd =new Random();
		for(int i=0;i<10;i++)
			intArray[i]=new Integer(rd.nextInt(10));
		
		for(Integer ir:intArray)
			System.out.print(ir);
		
		Arrays.sort(intArray);
		System.out.println(">>>>");

		for(Integer ir:intArray)
			System.out.print(ir);
		
		int val=6;
		for(int i =0;i<10;i++)
		{
			if(intArray[i]<=val)
			{
				int lim=val-intArray[i];
				
				for (int j=9;j>=0;j--)			
				{
					
					if(intArray[j]>=lim)
					{
						if(i!=j)
						{
							if((intArray[i]+intArray[j])==val)
							{
								System.out.println(" Index ["+i+","+j+"] >>> Values ["+intArray[i]+","+intArray[j]+"]");
							}
						}
					}
				}
			}
			else
			{
				break;
			}
		}

	}
	
	

	static String checkDate(String strDate)
	  {
	    int dd = 0;
	    int mm = 0;
	    int yy = 0;
	    if(strDate != null && strDate.trim().length()!=0){
	     StringTokenizer strToken = new StringTokenizer(strDate, "/");
	     if(strToken.countTokens() == 3 && strDate.length()<=10)
	     {
	    	 try {
	    		 SimpleDateFormat formatter=  new SimpleDateFormat("dd/MM/yyyy");
	 			formatter.parse(strDate);
	 		} catch (ParseException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 			return "Invalid Date/Format";
	 		}
	      try {
	         dd = Integer.parseInt(strToken.nextToken());
	         mm = Integer.parseInt(strToken.nextToken());
	         yy = Integer.parseInt(strToken.nextToken());
	         if(dd==0 || mm==0 || yy==0 || mm>12)return "Invalid Date/Format";
	         if((yy % 4 == 0 && yy % 100 != 0) || yy % 400 == 0)
	         {
	          if(mm==2 && dd>29)          
	           return "Invalid Day of Feb(Max-29)";
	         }
	         else
	         {
	          if(mm==2 && dd>28)          
	            return "Invalid Day of Feb(Max-28)";
	         }
	         switch(mm)
	         {
	         	case 1:
	         	case 3:
	         	case 5:
	         	case 7:
	         	case 8:
	         	case 10:
	         	case 12:
	         		if(dd>31)
	         			return "Invalid Day(Max-31)";
	         		break;
	         	case 4:
	         	case 6:
	         	case 9:
	         	case 11:
	         		if(dd>30)
	         			return "Invalid Day(Max-30)";
	         }
	         
	       }
	      catch (Exception e) {
	        System.out.println("Invalid Date/Format !");
	        return "Invalid Date/Format";
	       }
	     }
	     else{
	      System.out.println("Invalid Date/Format !");
	        return "Invalid Date/Format";
	     }
	    }
	    return null;
	  }
	
	static  String minority(String date)
    {
     String syout="";
     Calendar currentDate = Calendar.getInstance(); 
        SimpleDateFormat formatter=  new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(currentDate.getTime());
        try {
			formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String str[]=today.split("/");
        String str1[]=date.split("/");
        int len=str.length;
        int diff=Integer.parseInt(str[len-1])-Integer.parseInt(str1[len-1]);
       if(Integer.parseInt(str[len-1])<Integer.parseInt(str1[len-1]))
           syout="Future Date Entered";
       else if(diff<15)
           syout="AGE <15 for Customer";
              return syout;
    	}

	
	

}
