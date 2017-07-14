package test;

public class UserGroupSecurityMatrix {
  
	
	
	public static void main(String arg[]){
	  UserInfoFromLDAP userMaster = new UserInfoFromLDAP();
	  //UserGroupSecurityMasterMail userMaster = new UserGroupSecurityMasterMail ();
	 //UserGroupSecurityMasterUserId userMaster = new UserGroupSecurityMasterUserId();
	  // UserGroupSecurityMasterPartha userMaster = new UserGroupSecurityMasterPartha();
	  userMaster.write();
	  System.out.println("Completed");
	  
	  String str="aa|bb|cc";
  }
  
	
	
}
