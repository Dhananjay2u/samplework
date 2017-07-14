package com.secmatrix.model;

import java.util.ArrayList;

public class RequestData {
	
	private Boolean isSingleProcess;
	private Boolean isNewProcess;
	private String processId;
	private ArrayList<String> countryCode;
	private ArrayList<String> stepName;
	private String dap;
	private String branch;
	private String info;
	private String department;
	private Boolean isAddition;
	private ArrayList<UserLocation> userLocation;	
	
	public RequestData() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public RequestData(Boolean isSingleProcess, Boolean isNewProcess,
			String processId, ArrayList<String> countryCode,
			ArrayList<String> stepName, String department, Boolean isAddition,
			ArrayList<UserLocation> userLocation) {
		super();
		this.isSingleProcess = isSingleProcess;
		this.isNewProcess = isNewProcess;
		this.processId = processId;
		this.countryCode = countryCode;
		this.stepName = stepName;
		this.department = department;
		this.isAddition = isAddition;
		this.userLocation = userLocation;
	}

	public Boolean getIsSingleProcess() {
		return isSingleProcess;
	}
	public void setIsSingleProcess(Boolean isSingleProcess) {
		this.isSingleProcess = isSingleProcess;
	}
	public Boolean getIsNewProcess() {
		return isNewProcess;
	}
	public void setIsNewProcess(Boolean isNewProcess) {
		this.isNewProcess = isNewProcess;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public ArrayList<String> getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(ArrayList<String> countryCode) {
		this.countryCode = countryCode;
	}
	public ArrayList<String> getStepName() {
		return stepName;
	}
	public void setStepName(ArrayList<String> stepName) {
		this.stepName = stepName;
	}
	public String getDap() {
		return dap;
	}
	public void setDap(String dap) {
		this.dap = dap;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Boolean getIsAddition() {
		return isAddition;
	}
	public void setIsAddition(Boolean isAddition) {
		this.isAddition = isAddition;
	}


	public ArrayList<UserLocation> getUserLocation() {
		return userLocation;
	}


	public void setUserLocation(ArrayList<UserLocation> userLocation) {
		this.userLocation = userLocation;
	}

	
	
	
}
