/**
 * 
 */
package com.sci.services.account.model;

import java.io.Serializable;

/**
 * @author mn259
 *
 */
public class AddressBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String buildingName;
	private String street;
	private String city;
	private String state;
	private String activeFlag;
	
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	
}
