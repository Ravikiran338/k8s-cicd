/**
 * 
 */
package com.sci.services.constants;

/**
 * @author mn259
 *
 */
public enum ServiceStatus {

	SUCCESS("00", "Success"), FAILURE("99", "Failed");

	private ServiceStatus(String statusCode, String statusDesc) {
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
	}

	private String statusCode;
	private String statusDesc;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

}
