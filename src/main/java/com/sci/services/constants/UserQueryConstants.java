/**
 * 
 */
package com.sci.services.constants;

/**
 * @author mn259
 *
 */
public class UserQueryConstants {
	
	public static final String DEACTIVATE_USER_LOGIN_INFO = "UPDATE user_login_info_tbl set active_flag=?,last_modified_datetime=? where user_id =?";
	
	public static final String DEACTIVATE_USER = "UPDATE user_tbl set active_flag=?,last_modified_datetime=? where user_id =?";
	public static final String SELECT_USER_ID = "SELECT user_id FROM user_tbl e " + "WHERE user_id = ?";
	
	
	public static final String FIND_ALL_USERS =	"select b.USER_ID,b.FIRST_NAME,b.MIDDLE_NAME,b.LAST_NAME,b.active_flag,b.last_modified_datetime,a.EMAIL,a.PHONE,a.FAX,c.USERNAME,c.ROLE_ID,d.BUILDING_NAME,d.STREET_NAME,d.CITY,d.STATE from user_details_tbl a join user_tbl b on a.user_id=b.user_id join user_login_info_tbl c on a.user_id=c.user_id  join address_details_tbl d on a.ADDRESS_DETAILS_ID=d.ADDRESS_DETAILS_ID ";	
	public static final String FIND_BY_USER_ID = "select b.USER_ID,b.FIRST_NAME,b.MIDDLE_NAME,b.LAST_NAME,b.active_flag,b.last_modified_datetime,a.EMAIL,a.PHONE,a.FAX,c.USERNAME,c.ROLE_ID,d.BUILDING_NAME,d.STREET_NAME,d.CITY,d.STATE from user_details_tbl a join user_tbl b on a.user_id=b.user_id join user_login_info_tbl c on a.user_id=c.user_id  join address_details_tbl d on a.ADDRESS_DETAILS_ID=d.ADDRESS_DETAILS_ID where b.USER_ID=?";
	
	
	public static final String UPDATE_USER = "UPDATE user_tbl set first_name=?, last_name=?,middle_name=?, active_flag=?,last_modified_datetime=? where user_id =?";
	
	public static final String SELECT_USER_DETAILS_ID = "SELECT user_details_id FROM user_details_tbl ae " + "WHERE user_id = ?";
	public static final String UPDATE_USER_DETAILS = "UPDATE user_details_tbl set user_id=?,email=?,phone=?,active_flag=?,last_modified_datetime=? where user_details_id =?";
	
	public static final String SELECT_USER_ADDR_DETAILS_ID = "SELECT address_details_id FROM user_details_tbl ae " + "WHERE ae.user_id = ?";
	public static final String UPDATE_USER_ADDR_DETAILS = "UPDATE address_details_tbl set building_name=?, street_name=?,city=?, state=?,last_modified_datetime=? where address_details_id =?";
	
	public static final String SELECT_USER_LOGIN_ID = "SELECT user_login_id FROM user_login_info_tbl e " + "WHERE user_id = ?";
	public static final String UPDATE_USER_LOGIN = "UPDATE user_login_info_tbl set user_id=?, role_id=?,username=?,active_flag=?,last_modified_datetime=? where user_login_id =?";	


	public static final String CREATE_USER = "INSERT INTO user_tbl(first_name, last_name,middle_name,active_flag,last_modified_datetime) VALUES (?, ?, ?,?,?)";
	public static final String SELECT_USER = "SELECT user_id, first_name, last_name, middle_name,active_flag,last_modified_datetime FROM user_tbl e " + "WHERE user_id = ?";
	
	public static final String CREATE_USER_DETAILS =  "INSERT INTO user_details_tbl(user_id,email,phone,address_details_id,active_flag,last_modified_datetime) VALUES (?,?,?,?,?,?)";

	public static final String CREATE_USER_LOGIN_INFO = "INSERT INTO user_login_info_tbl(user_id, role_id,username, password,active_flag,last_modified_datetime) VALUES (?, ?, ?,?,?,?)";
	public static final String SELECT_USER_LOGIN_INFO = "SELECT user_id FROM user_login_info_tbl e " + "WHERE user_login_id = ?";
	
	public static final String CREATE_USER_ADDR_DETAILS = "INSERT INTO address_details_tbl(building_name, street_name,city, state,active_flag,last_modified_datetime) VALUES (?, ?, ?,?,?,?)";
	public static final String SELECT_USER_ADDRESS_DETAILS_ID = "SELECT address_details_id FROM address_details_tbl ae " + "WHERE address_details_id = ?";
	
	public static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts_tbl";
}
