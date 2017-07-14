package test;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

public class UserInfoFromLDAP extends ExcelWriter{
	private static String eDirProviderURL = null;
	private static String ldapEDirSecDmn = null;
	private static DirContext ldapContext;
	private static String configFilePath = null;
	//private static String eDirConnPool = null;
	static FileInputStream fis = null;
	int shtno = 1;
	int row = 1;
	public static String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
	static{
		try{
			configFilePath = "C:/DK/DK/Work/3-may-2016/eDirectoryconfigGroup.properties";
	       
	        fis = new FileInputStream(configFilePath);
	        Properties prop = new Properties();
	        prop.load(fis);
			
			//eDirConnPool = "com.sun.jndi.ldap.connect.pool";
			eDirProviderURL = prop.getProperty("ldapurl");
			ldapEDirSecDmn = "ou=users,o=standardchartered";
			
			Hashtable<String,String> env = new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,INITCTX);
			//env.put(eDirConnPool, Boolean.TRUE.toString());		
			env.put(Context.SECURITY_AUTHENTICATION,"simple");
			env.put(Context.PROVIDER_URL, eDirProviderURL);
			
		    ldapContext = new InitialLdapContext(env, null);
		   }
			catch (Exception e) 
			{
				e.printStackTrace();
			}finally{
				try {
					if(fis != null ){
						fis.close();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	}
	
	public  UserInfoFromLDAP() {
		this.fileName = "C:/DK/DK/Work/3-may-2016/UserId.xls";
	}
	
	private static List<Attribute> getEDUser( String userId ) {
		List<Attribute> arrayList = new ArrayList<Attribute>();

		String strFilter = "(&(objectClass=person)";
		strFilter += "(cn="+ userId +"))";
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		try {
			NamingEnumeration<?> results = ldapContext.search(ldapEDirSecDmn,	strFilter, searchCtls);
			if (results != null) {
					while (results.hasMoreElements()) {
					SearchResult searchResult = (SearchResult) results.next();
					arrayList.add(searchResult.getAttributes().get("groupMembership"));
					arrayList.add(searchResult.getAttributes().get("fullName"));
					arrayList.add(searchResult.getAttributes().get("co"));
					arrayList.add(searchResult.getAttributes().get("givenName"));
					arrayList.add(searchResult.getAttributes().get("mail"));
					arrayList.add(searchResult.getAttributes().get("employeeStatus"));
					arrayList.add(searchResult.getAttributes().get("cn"));
				}// end of if

			}// end of if
		}// end of outer try - catch block
		catch (NamingException e) {
			e.printStackTrace();

		} 
		return arrayList;
	}
	
	void write(){
		 RandomAccessFile rd = null;		 
		 Map<String, Object> userIdsList = null;
		 Map<String,String> userCountryMap = null;
		try {
			rd = new RandomAccessFile ("C:/DK/DK/Work/3-may-2016/userList.txt","r");
			
			String file = "USER_ID";
			init();
			userCountryMap = getCountryCode();
			createSheet(file,0);
			generateHeaders();
			int rows = 0;
			while(true){
				String line = rd.readLine();
				if(line == null){
					break;
				}else{
					rows++;
				List<Attribute> result =getEDUser(line); 
				
				//System.out.println("Result ### " +result.size());
				
				if(result != null && result.size() > 0){
				   userIdsList = getGroup( result );
				   if(userIdsList != null && userIdsList.size() > 0){
				   generateRegularReport(userIdsList,file,line,rows,userCountryMap);
				   }else{
					  generateRegularReport(userIdsList,file,line,rows,userCountryMap);  
				   }
				}else{
					  userIdsList = null;
					  generateRegularReport(userIdsList,file,line,rows,userCountryMap);  
				 }
			  }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
			if(rd != null) rd.close();
			if(ldapContext != null) ldapContext.close();
			closeexcel();
			}catch(Exception e1){
				
			}
		}
	}
	
	private Map<String,String> getCountryCode(){
		Map<String,String> userCountry = new HashMap<String,String> ();
		RandomAccessFile rdCntry = null;
		try {
			rdCntry = new RandomAccessFile ("C:/DK/DK/Work/3-may-2016/ProcessID_CountryCode.txt","r");
			while(true){
				String line = rdCntry.readLine();
				if(line == null){
					break;
				}else{
					String processId = line.substring(0, 4);
					String countryCode = line.substring(5);
					userCountry.put(processId, countryCode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				  if( rdCntry != null ){
					  rdCntry.close();
				  }
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return userCountry;
	}
	
	public void closeexcel() throws Exception
	{
	  close();
	  Class cs=String.class;
	  
	}
	
	public static Map<String, Object> getGroup(List<Attribute> result){
	    String allGroups = "";
	    String group = "";
	    String groups = "";
		String eopscn = "";		
		String userFullName = "";
		String countryCode = "";
		String givenName = "";
		String eMail = "";
		String empStatus = "";
		String cn = "";
		int equalInx;
		Map<String, Object> groupMap = new HashMap<String, Object>(); 

		StringTokenizer stringTokenizer = null;
	  try{

				Object groupmember=result.get(0);
				Object fullName=result.get(1);
				Object co=result.get(2);
				Object givenNameL=result.get(3);
				Object mail=result.get(4);
				Object employeeStatus=result.get(5);
				Object cnn=result.get(6);
				
				if(fullName != null){
					userFullName = fullName.toString();
					userFullName = userFullName.replaceFirst("fullName:", "").trim();
				}
				if(co != null){
					countryCode = co.toString();
					countryCode = countryCode.replaceFirst("co:", "").trim();
				}
				if(givenNameL != null){
					givenName = givenNameL.toString();
					givenName = givenName.replaceFirst("givenName:", "").trim();
				}
				if(mail != null){
					eMail = mail.toString();
					eMail = eMail.replaceFirst("mail:", "").trim();
				}
				
				if(employeeStatus != null){
					empStatus = employeeStatus.toString();
					empStatus = empStatus.replaceFirst("employeeStatus:", "").trim();
				}
				
				if(cnn != null){
					cn = cnn.toString();
					cn = cn.replaceFirst("cn:", "").trim();
				}
				
				  groupMap.put("userFullName", userFullName);
			      groupMap.put("countryCode", countryCode);
			      groupMap.put("givenName", givenName);
			      groupMap.put("mail", eMail);
			      groupMap.put("employeeStatus", empStatus);
			      groupMap.put("psid", cn);
				/*
				if( groupmember != null ){
					allGroups =  groupmember.toString();
					allGroups = allGroups.replaceFirst("groupMembership:", "");
					allGroups = allGroups.trim();
					  stringTokenizer = new StringTokenizer(allGroups, ",");
					     while(stringTokenizer.hasMoreElements()){
					    	 eopscn = stringTokenizer.nextToken().toString();
					    	 if(eopscn.indexOf("cn")>-1){
									equalInx = eopscn.indexOf("=");
									group = eopscn.substring(equalInx+1);	
								}else if(eopscn != null && eopscn.equalsIgnoreCase("ou=ACLeOPS")){
									groups = groups + group +",";
								}
					   }
					     
					  if( groups != "" ){
						  groups = groups.substring(0,groups.length() - 1);
					      groupMap.put("groupId", groups);
					      groupMap.put("userFullName", userFullName);
					      groupMap.put("countryCode", countryCode);
					      groupMap.put("givenName", givenName);
					      groupMap.put("mail", eMail);
					  }
      			    }else{
				       System.out.println("No Group");
			      }*/
    }catch(Exception e){
	  e.printStackTrace();
   }
    return groupMap;
  }

	
   /* private static List<String> getUserIds( List<Attribute> result ){
    	List<String>  userIdList = null;
    	StringTokenizer stringTokenizer = null;
    	String eopscn = null;
    	int equalInx = 0;
    	String userId = null;
      try{	
    	  String allGroups = null;
    	 userIdList = new ArrayList<String> ();
    	 Object member=result.get(0);
    	 if( member != null ){
				allGroups =  member.toString();
				System.out.println("Member ## " +allGroups);
				
				allGroups = allGroups.replaceFirst("member:", "");
				allGroups = allGroups.trim();
				  stringTokenizer = new StringTokenizer(allGroups, ",");
				     while(stringTokenizer.hasMoreElements()){
				    	 eopscn = stringTokenizer.nextToken().toString();
				    	 if(eopscn.indexOf("cn")>-1){
								equalInx = eopscn.indexOf("=");
								userId = eopscn.substring(equalInx+1);	
								userIdList.add(userId);
				    	 }
				     }
    	 }
    	
      }catch(Exception e){
    	  e.printStackTrace();
      }
      return userIdList;
    }*/
	
    
    private void generateHeaders(){
        try{
             writeHeader(0,1, "SI NO");
             writeHeader(0,2, "User Id");
             writeHeader(0,3," Name ");
             writeHeader(0,4,"User Status");
             writeHeader(0,5,"Country Name");
             writeHeader(0,6,"GivenName");
             writeHeader(0,7,"Email");
             writeHeader(0,8,"Emp Status");
  		   }catch(Exception ex){
              ex.printStackTrace();
         }
    }

    public void generateRegularReport(Map<String, Object> gprprocessMap,String file,String userId,int row,Map<String,String> userCountryMap) throws Exception {
        try{
        	 String userGroup = null;
        	 String userFullName = null;
        	 String countryName = null;
        	// String countrCode = null;
        	 String givenNameX = null;
        	 String mailX = null;
        	 String empStatus = null;
        	 String psid = null;
        	 if( gprprocessMap != null && gprprocessMap.size() > 0){
        	     userGroup = (String)gprprocessMap.get("groupId") ;
        	     userFullName = (String)gprprocessMap.get("userFullName");
        	     countryName = (String)gprprocessMap.get("countryCode");
        	     givenNameX = (String)gprprocessMap.get("givenName");
        	     mailX = (String)gprprocessMap.get("mail");
        	     empStatus = (String)gprprocessMap.get("employeeStatus");
        	     psid = (String)gprprocessMap.get("psid");
        	     //countrCode = "";
        	     //countrCode = generateCountryCode(userGroup,userCountryMap);
        	  }else{
        		  userGroup = "No User";
         	      userFullName = " ";
         	      countryName = "";
         	     // countrCode = "";
         	      givenNameX = "";
         	      mailX = "";
        	  }
        	  
        	writeCellValue1(row, 1, String.valueOf(row));
        	writeCellValue1(row, 2, psid);
        	writeCellValue1(row, 3, userFullName);
        	writeCellValue1(row, 4, userGroup);
        	writeCellValue1(row, 5, countryName);
        	//writeCellValue1(row, 6, countrCode);
        	writeCellValue1(row, 6, givenNameX);
        	writeCellValue1(row, 7, mailX);
        	writeCellValue1(row, 8, empStatus);
        	
            if(row>60000) {
   			  row=2;
   			createSheet(file+(++shtno),1);
   			sheet.getSettings().setDefaultColumnWidth(16);
   			generateHeaders();
           }
           
         }catch(Exception ex) {
         ex.printStackTrace();
         throw ex;
        }finally{
           }

        }  
    
    private String generateCountryCode( String userGroup, Map<String,String> userCountryMap){
    	String countryCodes = "";
    	List<String> countryList = new ArrayList<String> (); 
    	try {
			 String[] process = userGroup.split(",");
			 for(String processId : process){
				 String countrys = userCountryMap.get(processId.substring(0, 4));
				 
				 if(!countryList.contains(countrys)){
				      countryList.add(countrys);
				      countryCodes = countryCodes + countrys + ",";
				 }
			 }
			 
			 if( countryCodes != ""){
				 countryCodes = countryCodes.substring(0,countryCodes.length() - 1);
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
    	//StringTokenizer st = new StringTokenizer();
    	
    	return countryCodes;
    }
    
}


