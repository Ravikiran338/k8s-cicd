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
import com.sci.services.constants.UserServiceConstants;
import com.sci.services.util.DatabaseUtil;
import com.sci.services.util.UserServiceUtil;

import io.reactivex.Flowable;
import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
@Service
public class DeactivateUserServiceImpl implements DeactivateUserService {
	
	private static UserServiceUtil util = UserServiceUtil.getInstance();
		
	public Mono<Status> deactivateUser(Mono<User> user) {
		Database db = DatabaseUtil.getInstance().getDatabase();
		Mono<Integer> userCount = deactivateUserInfo(user,db);
		Mono<Integer> loginInfoCount = deactivateUserLoginInfo(user,db);
		Flowable<Status> result = Flowable.zip(loginInfoCount, userCount, (infoCount, usrCount) -> {
			Status status = null;
			if (infoCount > 0 && usrCount > 0) {
				status = util.prepareStatus(ServiceStatus.SUCCESS.getStatusCode(),ServiceStatus.SUCCESS.getStatusDesc());
			} else {
				status = util.prepareStatus(ServiceStatus.FAILURE.getStatusCode(),ServiceStatus.FAILURE.getStatusDesc());
			}
			return status;
		});
		return Mono.from(result);
	}

	/**
	 * @param user
	 * @return
	 */
	private Mono<Integer> deactivateUserLoginInfo(Mono<User> user,Database db) {
		Mono<Integer> loginInfoCount = user.flatMap(dbUser -> {
			Flowable<Integer> updatedCount = db.update(UserQueryConstants.DEACTIVATE_USER_LOGIN_INFO)
					.parameters(UserServiceConstants.NO, new Date(), dbUser.getUserId()).counts();
			return Mono.from(updatedCount);
		});
		return loginInfoCount;
	}

	/**
	 * @param user
	 * @return
	 */
	private Mono<Integer> deactivateUserInfo(Mono<User> user,Database db) {
		Mono<Integer> userCount = user.flatMap(dbUser -> {
			Flowable<Integer> updatedCount = db.select(UserQueryConstants.SELECT_USER_ID).parameter(dbUser.getUserId()).getAs(Integer.class)
					.flatMap(userId -> db.update(UserQueryConstants.DEACTIVATE_USER).parameters(UserServiceConstants.NO, new Date(), userId).counts());
			return Mono.from(updatedCount);
		});
		return userCount;
	}
}
