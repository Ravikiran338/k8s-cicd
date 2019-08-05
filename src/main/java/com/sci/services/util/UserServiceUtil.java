/**
 * 
 */
package com.sci.services.util;

import com.sci.services.account.model.Status;

/**
 * @author mn259
 *
 */
public class UserServiceUtil {
	
	private static UserServiceUtil userServiceUtil = null;

	private UserServiceUtil() {

	}
	public static UserServiceUtil getInstance() {
		if (userServiceUtil == null){
			userServiceUtil = new UserServiceUtil();
		}
		return userServiceUtil;
	}
	
	/**
	 * @param statusCode
	 * @param statusDesc
	 */
	public Status prepareStatus(String statusCode,String statusDesc) {
		Status status = new Status();
		status.setStatusCode(statusCode);
		status.setStatusDesc(statusDesc);
		return status;
	} 
}
