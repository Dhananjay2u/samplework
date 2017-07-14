package com.secmatrix.dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.secmatrix.model.RequestData;

public class SecurityDataDAO {
	
	
	
	public static ArrayList<String> getGroupActivityNames(String processId)
	{
		ArrayList<String>  grpActvtyName= new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			String sql="select processid,groupid,programid,programname,description, countrycode from "+ConnectionProvider.p.getProperty("secschema")+".groupactivity where processid=? and programname !='Inbox' order by programid ";
			System.out.println("Printing Sql for GroupActivity :>>"+sql);
			PreparedStatement st=con.prepareStatement(sql);
			st.setString(1, processId);
			ResultSet rs=st.executeQuery();
			while(rs.next())
				grpActvtyName.add(rs.getString(1)+"_"+rs.getString(4)+"_"+rs.getString(2));			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grpActvtyName; 
	}
	
	public static ArrayList<String> getGroupActivityNamesSingleProcess(String processId, String country)
	{
		ArrayList<String>  grpActvtyName= new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			String sql="select ur.countrycode||'-'||pdpg.processid||'-'||pdpg.programname||'-'|| ur.roleid as Rolemapping, pdpg.processid,pdpg.programname,pdpg.groupid,ur.roleid,ur.countrycode from "+ConnectionProvider.p.getProperty("secschema")+".userroleacl ur join "+ConnectionProvider.p.getProperty("secschema")+".groupactivity pdpg on ur.processid=? and pdpg.programname !='Inbox' and ur.usergroup=pdpg.groupid  and ur.processid=pdpg.processid and ur.countrycode=?  order by pdpg.programname ";
			System.out.println("Printing Sql for GroupActivity :>>"+sql);
			PreparedStatement st=con.prepareStatement(sql);
			st.setString(1, processId);
			st.setString(2, country);
			ResultSet rs=st.executeQuery();
			while(rs.next())
				grpActvtyName.add(rs.getString(1));			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grpActvtyName; 
	}
	
	public static String getProcessName(String processId)
	{
		String procName="";
		try {
			Connection con=ConnectionProvider.getNewConnection();
			PreparedStatement st=con.prepareStatement("select Description from "+ConnectionProvider.p.getProperty("secschema")+".processdefinition where processid=?");
			st.setString(1, processId);
			ResultSet rs=st.executeQuery();
			if(rs.next())
				procName=rs.getString(1);			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return procName; 
	}
	
	public static String getProcessNameWithCountry(String processId)
	{
		String procNameCountry="";
		try {
			Connection con=ConnectionProvider.getNewConnection();
			PreparedStatement st=con.prepareStatement("select Description,countrycode from "+ConnectionProvider.p.getProperty("secschema")+".processdefinition where processid=?");
			st.setString(1, processId);
			ResultSet rs=st.executeQuery();
			if(rs.next())
				procNameCountry=rs.getString(1)+","+rs.getString(2);			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return procNameCountry; 
	}
	
	
	public static ArrayList<String> getLocationByCountry(String countryCode)
	{
		ArrayList<String> userLocation=new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			PreparedStatement st=con.prepareStatement("select userlocation from "+ConnectionProvider.p.getProperty("hubschema")+".userlocation where countrycode=?");
			st.setString(1, countryCode);
			ResultSet rs=st.executeQuery();
			while(rs.next())
				userLocation.add(rs.getString(1));
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userLocation; 
	}
	
	public static ArrayList<String> getSingleProcessProgramRoleMapping(RequestData requestData,String country)
	{
		ArrayList<String> grpmapping=new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			Statement st=con.createStatement();
			ArrayList<String> steps=requestData.getStepName();
			String stepnames="";
			for(String name: steps)
				stepnames+="'"+name+"',";
			stepnames=stepnames.substring(0, stepnames.length()-1);
			String sql="select ur.countrycode||'-'||pdpg.processid||'-'||pdpg.actiondescription||'-'|| ur.roleid as Rolemapping, pdpg.processid,pdpg.actiondescription,pdpg.groupid,ur.roleid,ur.countrycode from "+ConnectionProvider.p.getProperty("secschema")+".userroleacl ur join (select pd.processid,actiondescription,groupid,pd.programid from "+ConnectionProvider.p.getProperty("secschema")+".programdefinition pd join "+ConnectionProvider.p.getProperty("secschema")+".programgroup pg on pd.processid='"+requestData.getProcessId()+"' and pd.programid in("+stepnames+") and pd.processid=pg.processid and pd.programid=pg.programid  order by pd.programid ) pdpg on ur.processid='"+requestData.getProcessId()+"' and ur.usergroup=pdpg.groupid and ur.countrycode='"+country+"'  order by pdpg.programid ";
			System.out.println("getSingleProcessProgramRoleMapping>>>"+sql);
			ResultSet rs=st.executeQuery(sql);
			
			while(rs.next())
				grpmapping.add(rs.getString(1));
			
			//Retrieving Groupactivity description details -- Start
			if(requestData.getIsNewProcess())
			{
				ArrayList<String> grpActvtyName= SecurityDataDAO.getGroupActivityNamesSingleProcess(requestData.getProcessId(),country);
				for(String actvty: grpActvtyName)
				{					
					grpmapping.add(actvty);
				}
			}
			//Retrieving Groupactivity description details -- End
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(grpmapping+">>>");
		return grpmapping;
	}
	
	public static  HashMap<String,String> getCountrySnglProcessProgramRoleMapping(RequestData requestData,String country)
	{
		HashMap<String,String> cntryProgramRoleMappingMap=new HashMap<String,String>();
		
		ArrayList<String> mapping =getSingleProcessProgramRoleMapping(requestData, country);
		for( String value : mapping)
		{
			String dt[]=value.split("-");
			cntryProgramRoleMappingMap.put(dt[3], dt[0]+"-"+dt[1]+"-"+dt[2]);
		}
		
		return cntryProgramRoleMappingMap;
	}
	
	public static ArrayList<String> getProcessProgramGroupMapping(RequestData requestData)
	{
		ArrayList<String> grpmapping=new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			Statement st=con.createStatement();
			ArrayList<String> steps=requestData.getStepName();
			String stepnames="";
			for(String name: steps)
				stepnames+="'"+name+"',";
			stepnames=stepnames.substring(0, stepnames.length()-1);
			String sql="select pd.processid||'_'||actiondescription||'_'||groupid from "+ConnectionProvider.p.getProperty("secschema")+".programdefinition pd join "+ConnectionProvider.p.getProperty("secschema")+".programgroup pg on pd.processid='"+requestData.getProcessId()+"' and pd.programid in("+stepnames+") and pd.processid=pg.processid and pd.programid=pg.programid order by pd.programid ";
			System.out.println("getProcessProgramGroupMapping>>>"+sql);
			ResultSet rs=st.executeQuery(sql);
			//if(rs.next())System.out.println(stepnames+">>>"+sql);
			while(rs.next())
				grpmapping.add(rs.getString(1));
			
			//Retrieving Groupactivity description details -- Start
			if(requestData.getIsNewProcess())
			{
				ArrayList<String> grpActvtyName= SecurityDataDAO.getGroupActivityNames(requestData.getProcessId());
				for(String actvty: grpActvtyName)
				{					
					grpmapping.add(actvty);
				}
			}
			//Retrieving Groupactivity description details -- End
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(grpmapping+">>>");
		return grpmapping;
	}
	public static ArrayList<String> getStepNamesLabel(RequestData requestData, String country)
	{
		ArrayList<String> steplabels=new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			Statement st=con.createStatement();
			ArrayList<String> steps=requestData.getStepName();
			String stepnames="";
			for(String name: steps)
				stepnames+="'"+name+"',";
			stepnames=stepnames.substring(0, stepnames.length()-1);
			String sql="select actiondescription from "+ConnectionProvider.p.getProperty("secschema")+".programdefinition where processid='"+requestData.getProcessId()+"' and programid in("+stepnames+") order by programid ";
			System.out.println("getStepNamesLabel>>>"+sql);
			ResultSet rs=st.executeQuery(sql);
			//if(rs.next())System.out.println(stepnames+">>>"+sql);
			while(rs.next())
				steplabels.add(rs.getString(1));
			
			//System.out.println("getStepNamesLabel>>>>>>"+requestData.getIsNewProcess());
			//Retrieving Groupactivity description details -- Start
			if(requestData.getIsNewProcess())
			{
				if(requestData.getIsSingleProcess())
				{										
					ArrayList<String> grpActvtyName= SecurityDataDAO.getGroupActivityNamesSingleProcess(requestData.getProcessId(), country);
					for(String actvty: grpActvtyName)
					{
						steplabels.add(actvty.split("-")[2]);
					}
				}
				else
				{
					ArrayList<String> grpActvtyName= SecurityDataDAO.getGroupActivityNames(requestData.getProcessId());
					for(String actvty: grpActvtyName)
					{
						steplabels.add(actvty.split("_")[1]);
					}
				}
			}
			//Retrieving Groupactivity description details -- End
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return steplabels;
	}

	public static ArrayList<String> getStepLabelAndNames(RequestData requestData)
	{
		ArrayList<String> steplabelAndNames=new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			Statement st=con.createStatement();
			ArrayList<String> steps=requestData.getStepName();
			String stepnames="";
			for(String name: steps)
				stepnames+="'"+name+"',";
			stepnames=stepnames.substring(0, stepnames.length()-1);
			String sql="select actiondescription,programid from "+ConnectionProvider.p.getProperty("secschema")+".programdefinition where processid='"+requestData.getProcessId()+"' and programid in("+stepnames+") ";
			System.out.println("getStepNamesLabel>>>"+sql);
			ResultSet rs=st.executeQuery(sql);
			//if(rs.next())System.out.println(stepnames+">>>"+sql);
			while(rs.next())
				steplabelAndNames.add(rs.getString(1)+","+rs.getString(2));
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return steplabelAndNames;
	}
	
	public static ArrayList<String> getStepNames(String processId)
	{
		ArrayList<String> stepnames=new ArrayList<String>();
		try {
			Connection con=ConnectionProvider.getNewConnection();
			Statement st=con.createStatement();
						
			String sql="select programid from "+ConnectionProvider.p.getProperty("secschema")+".programdefinition where processid='"+processId+"' ";
			System.out.println("getStepNames>>>"+sql);
			ResultSet rs=st.executeQuery(sql);
			//if(rs.next())System.out.println(stepnames+">>>"+sql);
			while(rs.next())
				stepnames.add(rs.getString(1));
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stepnames;
	}
}
