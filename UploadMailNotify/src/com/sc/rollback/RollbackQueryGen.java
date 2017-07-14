package com.sc.rollback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.org.upload.ConnectionProvider;
import com.sc.rollback.model.DMLScript;
import com.sc.rollback.model.DeleteScript;
import com.sc.rollback.model.InsertScript;
import com.sc.rollback.model.UpdateScript;

public class RollbackQueryGen {
	
	ArrayList<String> scriptList=new ArrayList<String>();
	
	public ArrayList<DMLScript> genRollbackScript(ArrayList<DMLScript> dmlList)
	{
		Connection con=null;
		try {
			con=ConnectionProvider.getConnection()[0];
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<DMLScript> rollbackDMLScripts=new ArrayList<DMLScript>();
		for(DMLScript dml:dmlList)
		{
			if(dml instanceof InsertScript)
			{
				DeleteScript des=createRollbackForInsert((InsertScript)dml,con);
				//System.out.println("Rollback for Insert****");
				//System.out.println(((InsertScript)dml));
				//System.out.println(des);
				rollbackDMLScripts.add(des);
			}
			else if(dml instanceof UpdateScript)
			{
				ArrayList<UpdateScript> upsList= createRollbackForUpdate((UpdateScript)dml,con);
				for(UpdateScript ups: upsList)
				{
					rollbackDMLScripts.add(ups);
				}
			}
			else if(dml instanceof DeleteScript)
			{
				ArrayList<InsertScript> insList= createRollbackForDelete((DeleteScript)dml,con);
				for(InsertScript ins: insList)
				{
					rollbackDMLScripts.add(ins);
				}
			}
		}
		
		return rollbackDMLScripts;		
	}
	
	public DeleteScript createRollbackForInsert(InsertScript ins,Connection con)
	{
		
		try {
			
			DatabaseMetaData dmd= con.getMetaData();
			String str=ins.getTableName();
			str=str.substring(0,str.indexOf("."));
			//System.out.println("Schema "+ str+"  "+ins.getTableName().substring(ins.getTableName().indexOf(".")+1));
			ResultSet rs=dmd.getPrimaryKeys(null, str.trim().toUpperCase(),ins.getTableName().substring(ins.getTableName().indexOf(".")+1).trim().toUpperCase() );
			DeleteScript des=new DeleteScript();
			des.setTableName(ins.getTableName());
			String condition="";
			
			while(rs.next())
			{
				String col=rs.getString(4);
				for (String cols:ins.getColList())
				{
					if(cols.equalsIgnoreCase(col))
					{						
							condition +=" "+col+"='"+ins.getValList().get(ins.getColList().indexOf(cols))+"' AND ";
					}
				}
			}
			if(condition.length()==0)
			{
				for (String cols:ins.getColList())
				{											
							condition +=" "+cols+"='"+ins.getValList().get(ins.getColList().indexOf(cols))+"' AND ";					
				}
			}
			//System.out.println("cond "+ condition);
			des.setCondition(condition.substring(0,condition.lastIndexOf("AND")));
			rs.close();
		
			return des;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally{
			
		}
	}
	
	public ArrayList<InsertScript> createRollbackForDelete(DeleteScript des,Connection con)
	{
		try {
		
		
		String selectQry=" SELECT * FROM "+des.getTableName();
		selectQry+=" WHERE "+des.getCondition();
		
		Statement st=con.createStatement();
		ResultSet rs= st.executeQuery(selectQry);
		
		ArrayList<InsertScript> insList=new ArrayList<InsertScript>();
		ResultSetMetaData rsmd=rs.getMetaData();
		int colCount=rsmd.getColumnCount();	
		while(rs.next())
		{
			InsertScript ins=new InsertScript();
			ins.setTableName(des.getTableName());
			for (int col=1;col<=colCount;col++)
			{	
				ins.getColList().add(rsmd.getColumnName(col));
				String val=rs.getObject(col)!=null?rs.getObject(col).toString():null;
				ins.getValList().add(val);
			}
			insList.add(ins);
		}
		rs.close();
		return insList;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<UpdateScript> createRollbackForUpdate(UpdateScript ups,Connection con)
	{
		
		try {
			
			
			String selectQry=" SELECT * FROM "+ups.getTableName();
			selectQry+=" WHERE "+ups.getCondition();
			
			Statement st=con.createStatement();
			ResultSet rs= st.executeQuery(selectQry);
			DatabaseMetaData dmd= con.getMetaData();
			
			String str=ups.getTableName();
			str=str.substring(0,str.indexOf("."));
			//System.out.println("***"+str.trim().toUpperCase()+"******"+ups.getTableName().substring(ups.getTableName().indexOf(".")+1).trim().toUpperCase()+"*****");
			ResultSet rss=dmd.getPrimaryKeys(null, str.trim().toUpperCase(),ups.getTableName().substring(ups.getTableName().indexOf(".")+1).trim().toUpperCase());
					
			ArrayList<String> conditionColList=new ArrayList<String>();
			while(rss.next())
			{
				conditionColList.add(rss.getString(4));				
			}
			
			ArrayList<UpdateScript> updateList=new ArrayList<UpdateScript>();
			
			
			while(rs.next())
			{
				UpdateScript upss=new UpdateScript();
				upss.setTableName(ups.getTableName());		
				
				
				for(String col:ups.getColList())
				{
					String val=rs.getObject(col)!=null?rs.getObject(col).toString():null;
					upss.getColList().add(col);
					upss.getValList().add(val);
				}
				String condition=" ";
				for(String col:conditionColList)
				{
					boolean exist=false;
					String val=null;
					for(String coll:ups.getColList())
					{
						if(col.equalsIgnoreCase(coll))
						{
							val=ups.getValList().get(ups.getColList().indexOf(coll));
							exist=true;
							break;
						}
					}
					
					if(!exist)
						val=rs.getObject(col)!=null?rs.getObject(col).toString():null;
								
					if(val!=null)
						val="'"+val+"'";
					
					if(!(ups.getColList().indexOf(col)==(ups.getColList().size()-1)))
						condition+=col+"="+val+" AND ";
					else
						condition+=col+"="+val+" ";
				}
				
				if(condition.trim().length()==0)
				{
					ResultSetMetaData rsmd=rs.getMetaData();
					int colCount=rsmd.getColumnCount();	
					for (int col=1;col<=colCount;col++)
					{									
						boolean exist=false;
						String val=null;
						for(String coll:ups.getColList())
						{
							if(rsmd.getColumnName(col).equalsIgnoreCase(coll))
							{
								val=ups.getValList().get(ups.getColList().indexOf(coll));
								exist=true;
								break;
							}
						}
						if(!exist)
							val=rs.getObject(col)!=null?rs.getObject(col).toString():null;
						if(val!=null)
						{
							val="'"+val+"'";
						}
								condition +=" "+rsmd.getColumnName(col)+"="+val+" AND ";					
					}
					
				}
				//System.out.println(condition);
				
				upss.setCondition(condition.substring(0,condition.lastIndexOf("AND")));
				updateList.add(upss);				
			}
			rs.close();
			rss.close();
			
			return updateList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<DMLScript> readQuery()
	{
		ArrayList<DMLScript> dmlList=new ArrayList<DMLScript>();
		
		for(String query:scriptList)
		{
		//String query ="select  * from  table ";
			query =query.trim();
			String str[]=query.split(" ");
			//for(String st: str)
				//System.out.println(st);
			if(ScriptConstant.INSERT.equals(str[0]))
			{
				InsertScript ins=createInsertScript(query);	
				dmlList.add(ins);
			}
			else if(ScriptConstant.UPDATE.equals(str[0]))
			{
				UpdateScript ups=createUpdateScript(query);
				dmlList.add(ups);
			}
			else if(ScriptConstant.DELETE.equals(str[0]))
			{
				DeleteScript des=createDeleteScript(query);
				dmlList.add(des);
			}
		}
		return dmlList;
	}
	
	public ArrayList<String>  getWordUsingSpace(String data)
	{
		String arr[]=data.split("\\s");
		ArrayList<String> queryWord=new ArrayList<String>();
		for(String ar:arr)
		{	
			if(ar!=null && ar.length()!=0)
			{
				System.out.println(ar+"---");
				queryWord.add(ar);
			}
		}
		return queryWord;
	}
	
	public void readScriptFile(String scriptFile)
	{
		scriptList.clear();
		File scFile=new File(scriptFile);
		BufferedReader br=null;
		try {
				br=new BufferedReader(new FileReader(scFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query=null;
		try {
			query = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (query!=null)
		{
			if(query.length()>0)
				scriptList.add(query);
			try {
				query = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public  InsertScript createInsertScript(String query)
	{
		//query="Insert into HUBSIT3.STATUS (NAME,VALUE) values ('ABCD','49');";
		ArrayList<String> queryWord=getWordUsingSpace(query);
		
		InsertScript ins=new InsertScript();
		
		//System.out.println(queryWord.get(2).indexOf("("));
		String tableName=null;
		if(queryWord.get(2).indexOf("(")==-1)
		{			
			tableName=queryWord.get(2).trim();
			//System.out.println(tableName);
		}
		else
		{
			tableName=queryWord.get(2).trim().substring(0, (queryWord.get(2).indexOf("(")));
			//System.out.println(tableName);
		}
		ins.setTableName(tableName);
		
		int firstopenbrace=query.indexOf("(");
		int firstclosebrace=query.indexOf(")");
		
		String colStr=query.substring((firstopenbrace+1),firstclosebrace);
		String colStrarr[]=colStr.split(",");
		for(String s:colStrarr)
		{
			ins.getColList().add(s);
			//System.out.print(s+"^");
		}
		
		String rawdataaftercolDef=query.substring(firstclosebrace);
		int valuesopenbrace=rawdataaftercolDef.indexOf("(");
		int valuesclosebrace=rawdataaftercolDef.lastIndexOf(")");
		
		String valStr=rawdataaftercolDef.substring((valuesopenbrace+1),valuesclosebrace);
		
		String valStrarr[]=valStr.split(",");
		for(String s:valStrarr)
		{
			s=s.trim();
			if(s.charAt(0)=='\'')
			{
				ins.getValList().add(s.substring(1,(s.length()-1)));
				//System.out.print(s.substring(1,(s.length()-1))+"$");
			}
			else
			{
				ins.getValList().add(s);
				//System.out.print(s);
			}
		}
		
		return ins;
	}
	
	
	public UpdateScript createUpdateScript(String query)
	{
		//query="UPDATE HUBSIT3.STATUS SET value='49' WHERE NAME='Terminated' ; ";
		ArrayList<String> queryWord=getWordUsingSpace(query);
		
		UpdateScript ups=new UpdateScript();
		ups.setTableName(queryWord.get(1));
		//System.out.println(queryWord.get(1));
		int setValInd=query.indexOf(" "+queryWord.get(2)+" ");
		//System.out.println(queryWord.get(2));
		setValInd+=3;
		String whereCond=null;
		for(String q:queryWord)
		{
			
			if(q.equalsIgnoreCase("where"))
			{
				whereCond=q;
				break;
			}							
		}
		int whereInd=query.indexOf(" "+whereCond+" ");
		//System.out.println(query+"***"+whereInd);
		String condition=null;
		if(whereInd>0)
		{
			condition=query.substring((whereInd+7));
		}
		ups.setCondition(condition.substring(0,(condition.lastIndexOf(";"))));
			//System.out.println(ups.getCondition());
			String setValues=query.substring(setValInd+2,whereInd);
			
			//System.out.println(setValues);
		String setValSet[]=setValues.split(",");
		for(String setVal:setValSet)
		{
			String colVal[]=setVal.split("=");
			ups.getColList().add(colVal[0].trim());
			String tmp=colVal[1];
			tmp.trim();
			if(tmp.charAt(0)=='\'')
				ups.getValList().add(tmp.substring(tmp.indexOf('\'')+1,tmp.lastIndexOf('\'')));
			else
				ups.getValList().add(tmp);
		}
		
		System.out.println(ups.getColList());
		System.out.println(ups.getValList());
		
		return ups;
	}
	
	public DeleteScript createDeleteScript(String query)
	{
		//query="delete From HUBSIT3.Eventsubscription where eventid in ('SAU0001','SAU0002','SAU0002','SAU0009','SAU0008','SAU0007','SAU0006','SAU0005','SAU0004','SAU0003','SAU0012','SAU0011','SAU0010','SAU0013','SAU0018','SAU0017','SAU0016','SAU0015','SAU0014','SAU0013','SAU0022','SAU0021','SAU0020','SAU0019','SAU0025','SAU0024','SAU0023','SAU0026','SAU0040','SAU0041') and userid in ('1172004','1225013'); ";
		ArrayList<String> queryWord=getWordUsingSpace(query);
		
		DeleteScript dsc=new DeleteScript();
		dsc.setTableName(queryWord.get(2));
		
		String whereCond=null;
		for(String q:queryWord)
		{
			
			if(q.equalsIgnoreCase("where"))
			{
				whereCond=q;
				break;
			}							
		}
		int whereInd=query.indexOf(" "+whereCond+" ");
		//System.out.println(query+"***"+whereInd);
		String condition=null;
		if(whereInd>0)
		{
			condition=query.substring((whereInd+7));
		}
		dsc.setCondition(condition.substring(0,(condition.lastIndexOf(";"))));
		
		System.out.println(dsc.getTableName());
		System.out.println(dsc.getCondition());
		
		return dsc;
	}
	
	public static void main(String args[]) throws IOException
	{
		RollbackQueryGen rg=new RollbackQueryGen();
		
		rg.readScriptFile("C:\\Users\\1463361\\Desktop\\abc.txt");
		ArrayList<DMLScript> dmlList=rg.readQuery();
		ArrayList<DMLScript> rollDMLList= rg.genRollbackScript(dmlList);
		
		File f=new File("C:\\Users\\1463361\\Desktop\\out.txt");
		PrintWriter out=new PrintWriter(new FileWriter(f));
		for (DMLScript dml:rollDMLList)
		{
			out.println(dml);
			System.out.println(dml);
		}
		out.flush();
		out.close();
		
	}
	
}
