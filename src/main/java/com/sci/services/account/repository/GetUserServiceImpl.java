/**
 * 
 */
package com.sci.services.account.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.sci.services.account.model.AddressBean;
import com.sci.services.account.model.User;
import com.sci.services.constants.UserQueryConstants;
import com.sci.services.constants.UserServiceConstants;
import com.sci.services.util.DatabaseUtil;

import io.reactivex.Flowable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
@Service
public class GetUserServiceImpl implements GetUserService {
	
	public Flux<User> getAllUsers() {
		Flowable<User> employeeFlowable = DatabaseUtil.getInstance().getDatabase().select(UserQueryConstants.FIND_ALL_USERS).get(rs -> {
			return prepareUser(rs);
		});
		return Flux.from(employeeFlowable);
	}

	@Override
	public Mono<User> getUser(Mono<String> usrId) {
		return usrId.flatMap(userId -> {
			Flowable<User> userLoginFlowable = DatabaseUtil.getInstance().getDatabase().select(UserQueryConstants.FIND_BY_USER_ID).parameter(userId).get(rs -> {
				return prepareUser(rs);
			});
			return Mono.from(userLoginFlowable);	
		});
	}
	
	/**
	 * @param newUser
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private User prepareUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setUserId(rs.getString(UserServiceConstants.USER_ID));
		user.setFirstName(rs.getString(UserServiceConstants.FIRST_NAME));
		user.setLastName(rs.getString(UserServiceConstants.LAST_NAME));
		user.setMiddleName(rs.getString(UserServiceConstants.MIDDLE_NAME));
		user.setActiveFlag(rs.getString(UserServiceConstants.ACTIVE_FLAG));
		user.setLastModified(rs.getString(UserServiceConstants.LAST_MODIFIED_DATETIME));
		user.setUsername(rs.getString(UserServiceConstants.USER_NAME));
		user.setPhone(rs.getString(UserServiceConstants.PHONE));
		user.setEmail(rs.getString(UserServiceConstants.EMAIL));
		user.setFax(rs.getString(UserServiceConstants.FAX));
		user.setRoleId(rs.getString(UserServiceConstants.ROLE_ID));
		user.setActiveFlag(rs.getString(UserServiceConstants.ACTIVE_FLAG));
		AddressBean address = new AddressBean();
		address.setBuildingName(rs.getString(UserServiceConstants.BUILDING_NAME));
		address.setStreet(rs.getString(UserServiceConstants.STREET_NAME));
		address.setCity(rs.getString(UserServiceConstants.CITY));
		address.setState(rs.getString(UserServiceConstants.STATE));
		user.setAddress(address);
		return user;
	}
}
