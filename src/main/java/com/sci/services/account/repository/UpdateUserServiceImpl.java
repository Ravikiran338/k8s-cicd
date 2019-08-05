/**
 * 
 */
package com.sci.services.account.repository;

import java.util.Date;

import org.davidmoten.rx.jdbc.Database;
import org.springframework.stereotype.Service;

import com.sci.services.account.model.Status;
import com.sci.services.account.model.User;
import com.sci.services.constants.UserQueryConstants;
import com.sci.services.constants.ServiceStatus;
import com.sci.services.util.DatabaseUtil;
import com.sci.services.util.UserServiceUtil;

import io.reactivex.Flowable;
import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
@Service
public class UpdateUserServiceImpl implements UpdateUserService {

	private static UserServiceUtil util = UserServiceUtil.getInstance();
	
	public Mono<Status> updateUser(Mono<User> dbUser) {
		Database db = DatabaseUtil.getInstance().getDatabase();
		Mono<Integer> userCount = updateUserInfo(dbUser,db);
		Mono<Integer> loginInfoCount = updateUserLoginInfo(dbUser,db);
		Mono<Integer> addressCount = updateUserAddress(dbUser,db);
		Mono<Integer> usrDtlsCount = updateUserDetails(dbUser,db);
		Flowable<Status> result = Flowable.zip(loginInfoCount, userCount, addressCount, usrDtlsCount,
				(infoCount, usrCount, addrCount, userDtlsCount) -> {
					Status status = new Status();
					if (infoCount > 0 && usrCount > 0 && addrCount > 0 && userDtlsCount > 0) {
						status = util.prepareStatus(ServiceStatus.SUCCESS.getStatusCode(),ServiceStatus.SUCCESS.getStatusDesc());
					} else {
						status = util.prepareStatus(ServiceStatus.FAILURE.getStatusCode(),ServiceStatus.FAILURE.getStatusDesc());
					}
					return status;
				});
		return Mono.from(result);
	}

	/**
	 * @param dbUser
	 * @return
	 */
	private Mono<Integer> updateUserInfo(Mono<User> dbUser,Database db) {
		Mono<Integer> userCount = dbUser.flatMap(user -> {
			Flowable<Integer> updatedCount = db.select(UserQueryConstants.SELECT_USER_ID)
					.parameter(user.getUserId()).getAs(
							Integer.class)
					.flatMap(userId -> db.update(UserQueryConstants.UPDATE_USER).parameters(user.getFirstName(), user.getLastName(),
							user.getMiddleName(), user.getActiveFlag(), new Date(), userId).counts());
			return Mono.from(updatedCount);
		});
		return userCount;
	}

	/**
	 * @param dbUser
	 * @return
	 */
	private Mono<Integer> updateUserDetails(Mono<User> dbUser,Database db) {
		Mono<Integer> usrDtlsCount = dbUser.flatMap(loginUser -> {
			Flowable<Integer> updatedCount = db.select(UserQueryConstants.SELECT_USER_DETAILS_ID).parameter(loginUser.getUserId())
					.getAs(Integer.class).flatMap(
							userLoginId -> db
									.update(UserQueryConstants.UPDATE_USER_DETAILS).parameters(loginUser.getUserId(), loginUser.getEmail(),
											loginUser.getPhone(), loginUser.getActiveFlag(), new Date(), userLoginId)
									.counts());
			return Mono.from(updatedCount);
		});
		return usrDtlsCount;
	}

	/**
	 * @param dbUser
	 * @return
	 */
	private Mono<Integer> updateUserAddress(Mono<User> dbUser,Database db) {
		Mono<Integer> addressCount = dbUser.flatMap(addressUser -> {
			Flowable<Integer> updatedCount = db.select(UserQueryConstants.SELECT_USER_ADDR_DETAILS_ID).parameter(addressUser.getUserId())
					.getAs(Integer.class)
					.flatMap(addressId -> db.update(UserQueryConstants.UPDATE_USER_ADDR_DETAILS)
							.parameters(addressUser.getAddress().getBuildingName(),
									addressUser.getAddress().getStreet(), addressUser.getAddress().getCity(),
									addressUser.getAddress().getState(), new Date(), addressId)
							.counts());
			return Mono.from(updatedCount);
		});
		return addressCount;
	}

	/**
	 * @param dbUser
	 * @return
	 */
	private Mono<Integer> updateUserLoginInfo(Mono<User> dbUser,Database db) {
		Mono<Integer> loginInfoCount = dbUser.flatMap(loginUser -> {
			Flowable<Integer> updatedCount = db.select(UserQueryConstants.SELECT_USER_LOGIN_ID).parameter(loginUser.getUserId())
					.getAs(Integer.class).flatMap(
							userLoginId -> db
									.update(UserQueryConstants.UPDATE_USER_LOGIN).parameters(loginUser.getUserId(), 3,
											loginUser.getUsername(), loginUser.getActiveFlag(), new Date(), userLoginId)
									.counts());
			return Mono.from(updatedCount);
		});
		return loginInfoCount;
	}

}