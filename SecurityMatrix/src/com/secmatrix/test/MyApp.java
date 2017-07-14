package com.secmatrix.test;

import java.util.ArrayList;
import java.util.Scanner;

import com.secmatrix.dbcon.SecurityDataDAO;
import com.secmatrix.matrix.MatrixGeneration;
import com.secmatrix.model.RequestData;

public class MyApp {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("*******************************Generation of EFORM/Security Matrix Files***********************************");
		Scanner scan=new Scanner(System.in);
		System.out.println("\t\t\tPlease Enter ProcessId : ");
		String processid=scan.next();
		System.out.println("\t\t\tIs This Single ProcessID(Y/N) : ");
		String isSingleProcess=scan.next();
		System.out.println("\t\t\tDepartment Plz(eg. CB/WB) : ");
		String department=scan.next();
		scan=new Scanner(System.in);
		System.out.println("\t\t\tIs This New ProcessID(Y/N) : ");
		String isNewProcess=scan.next();
		String steps=null;
		if(!isNewProcess.equalsIgnoreCase("Y"))
		{
			scan=new Scanner(System.in);
			System.out.println("\t\t\tPlease Enter Step Names involved here(eg. L2M01,L2M02) : ");
			steps=scan.nextLine();
		}
		scan=new Scanner(System.in);
		String countries=null;
		if(isSingleProcess.equalsIgnoreCase("Y"))
		{
			System.out.println("\t\t\tPlease Enter Country Code involved here(eg. IN,SG) : ");
			countries=scan.nextLine();
		}
		System.out.println("\t\t\tIs This Matrix for Granting Access(Y/N) ");
		String isAddition=scan.next();
		
		RequestData rd=new RequestData();
		/*rd.setIsSingleProcess(false);
		rd.setProcessId("P551");
		rd.setDepartment("CB");rd.setIsAddition(true);
		ArrayList<String> st=new ArrayList<String>();st.add("Diarize");//st.add("L2M05");st.add("L2M06");st.add("L3M01");
		rd.setStepName(st);
		ArrayList<String> ct=new ArrayList<String>();ct.add("AE");
		rd.setCountryCode(ct);
		createResultingSecMatrix(rd);
		*/
		///Single process
		rd.setIsSingleProcess(isSingleProcess.equalsIgnoreCase("Y")?true:false);
		rd.setIsNewProcess(isNewProcess.equalsIgnoreCase("Y")?true:false);
		rd.setProcessId(processid);
		rd.setDepartment(department);rd.setIsAddition(isAddition.equalsIgnoreCase("Y")?true:false);
		ArrayList<String> st=new ArrayList<String>();///stepnames arraylist
		ArrayList<String> ct=new ArrayList<String>();///countrycode arraylist
		if(!isNewProcess.equalsIgnoreCase("Y"))
		{
			///Getting selected stepnames
			String stArray[]=steps.split(",");			
			for(String dt:stArray){
				st.add(dt);//st.add("L2M12");st.add("L2M09");st.add("L2M11");			
			}
			System.out.println("steparray"+st);					
		}	
		else
		{			
			st=SecurityDataDAO.getStepNames(processid);
		}
		rd.setStepName(st);
		
		if(isSingleProcess.equalsIgnoreCase("Y"))
		{
			///Getting Country codes 
			String ctArray[]=countries.split(",");			
			for(String dt: ctArray)
				ct.add(dt);//ct.add("GM");
			System.out.println("steparray"+ct);
		}
		else
		{
			ct.add(SecurityDataDAO.getProcessNameWithCountry(processid).split(",")[1]);			
		}
		rd.setCountryCode(ct);
		
		
		System.out.println("getStepNamesLabel>>>>>>***"+rd.getIsNewProcess());
		
		MatrixGeneration.createResultingSecMatrix(rd);
		System.out.println("\n****************Creation of Security Matrix file is done. File Path : /tmp/ >>> Enter Any char to close it***********************"); 
		scan.next();
		System.exit(0);
	}
	
}
