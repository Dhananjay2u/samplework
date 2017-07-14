package com.secmatrix.model;

import java.util.ArrayList;

public class UserLocation {

	private String countryCode;
	private ArrayList<String> location;
	
	
	public UserLocation(String countryCode, ArrayList<String> location) {
		super();
		this.countryCode = countryCode;
		this.location = location;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public ArrayList<String> getLocation() {
		return location;
	}
	public void setLocation(ArrayList<String> location) {
		this.location = location;
	}
	
}
