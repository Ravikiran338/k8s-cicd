package com.sci.services.account.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.davidmoten.rx.jdbc.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sci.services.account.model.Account;
import com.sci.services.account.model.Status;
import com.sci.services.account.model.UserBean;
import com.sci.services.account.model.UserPojo;
import com.sci.services.constants.UserQueryConstants;
import com.sci.services.constants.ServiceStatus;
import com.sci.services.constants.UserServiceConstants;
import com.sci.services.util.DatabaseUtil;
import com.sci.services.util.UserServiceUtil;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
	
    private static UserServiceUtil util = UserServiceUtil.getInstance();
    	
	public Flux<Account> getAllAccounts() {
		Flowable<Account> employeeFlowable = DatabaseUtil.getInstance().getDatabase().select(UserQueryConstants.SELECT_ALL_ACCOUNTS).get(rs -> {
			Account account = new Account();
			account.setId(rs.getString(UserServiceConstants.ACCOUNT_ID));
			account.setNumber(rs.getString(UserServiceConstants.ACCOUNT_NUMBER));
			account.setCustomerId(rs.getString(UserServiceConstants.CUSTOMER_ID));
			account.setAmount(Double.valueOf(rs.getString(UserServiceConstants.BALANCE)));
			return account;
		});
		return Flux.from(employeeFlowable);
	}

	@Override
	public Mono<Status> createUser(Mono<UserBean> monoUser) {
		Database db = DatabaseUtil.getInstance().getDatabase();
		Mono<UserPojo> newMonoUser = createUserInfo(monoUser,db);
		Mono<UserPojo> newAddressUser = executeAddressMono(newMonoUser,db);
		Mono<UserPojo> newMonoLoginUser = executeUserLoginMono(newAddressUser,db);
		return executeUserDetailsMono(newMonoLoginUser,db);
	}

	/**
	 * @param monoUser
	 * @return
	 */
	private Mono<UserPojo> createUserInfo(Mono<UserBean> monoUser,Database db) {
		Mono<UserPojo> newMonoUser = monoUser.flatMap(newUser -> {
			Flowable<Integer> userId = db.update(UserQueryConstants.CREATE_USER)
					.parameters(newUser.getFirstName(), newUser.getLastName(), newUser.getMiddleName(),UserServiceConstants.YES, new Date())
					.returnGeneratedKeys().getAs(Integer.class);
			Flowable<UserPojo> userFlowable = db.select(UserQueryConstants.SELECT_USER).parameterStream(userId).get(rs -> {
				return prepareUserPojo(newUser, rs);
			});
			return Mono.from(userFlowable);
		});
		return newMonoUser;
	}

	/**
	 * @param createUserDetailsSql
	 * @param userDtlsSQL
	 * @param newMonoLoginUser
	 * @return
	 */
	private Mono<Status> executeUserDetailsMono(Mono<UserPojo> newMonoLoginUser,Database db) {
		Mono<Status> newMonoUserDtls = newMonoLoginUser.flatMap(user -> {
			Flowable<Integer> userLoginId = db.update(UserQueryConstants.CREATE_USER_DETAILS)
					.parameters(user.getUserId(), user.getEmail(), user.getPhone(),user.getAddressId(),UserServiceConstants.YES,new Date()).counts();
			Flowable<Status> result = userLoginId.map(new Function<Integer,Status>() {
				@Override
				public Status apply(Integer count) throws Exception {
					Status status = null;
					if (count > 0) {
						status = util.prepareStatus(ServiceStatus.SUCCESS.getStatusCode(),ServiceStatus.SUCCESS.getStatusDesc()) ;
					} else{
						status = util.prepareStatus(ServiceStatus.FAILURE.getStatusCode(),ServiceStatus.FAILURE.getStatusDesc()) ;
					}
					return status;
				}
            });
			return Mono.from(result);
		});
		return newMonoUserDtls;
	}

	private Mono<UserPojo> executeUserLoginMono(Mono<UserPojo> newAddressUser,Database db) {
		Mono<UserPojo> newMonoLoginUser = newAddressUser.flatMap(user -> {
			Flowable<Integer> userLoginId = db.update(UserQueryConstants.CREATE_USER_LOGIN_INFO).parameters(user.getUserId(), user.getRoleId(),
					user.getUsername(), passwordEncoder().encode(user.getPassword()), UserServiceConstants.YES, new Date())
					.returnGeneratedKeys().getAs(Integer.class);
			Flowable<UserPojo> userLoginFlowable = db.select(UserQueryConstants.SELECT_USER_LOGIN_INFO).parameterStream(userLoginId).get(rs -> {
				return user;
			});
			return Mono.from(userLoginFlowable);
		});
		return newMonoLoginUser;
	}

	private Mono<UserPojo> executeAddressMono(Mono<UserPojo> newMonoUser,Database db) {
		Mono<UserPojo> newAddressUser = newMonoUser.flatMap(user -> {
			Flowable<Integer> userAddrId = db.update(UserQueryConstants.CREATE_USER_ADDR_DETAILS)
				        	.parameters(user.getAddress().getBuildingName(),
					     	    user.getAddress().getStreet(), user.getAddress().getCity(),
						        user.getAddress().getState(), UserServiceConstants.YES,new Date())
				             .returnGeneratedKeys().getAs(Integer.class);
			Flowable<UserPojo> userLoginFlowable = db.select(UserQueryConstants.SELECT_USER_ADDRESS_DETAILS_ID).parameterStream(userAddrId).get(rs -> {
				user.setAddressId(rs.getString(UserServiceConstants.ADDR_DTLS_ID));
				return user;
			});
			return Mono.from(userLoginFlowable);
		});
		return newAddressUser;
	}
	
	/**
	 * @param newUser
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private UserPojo prepareUserPojo(UserBean newUser, ResultSet rs) throws SQLException {
		UserPojo user = new UserPojo();
		user.setUserId(rs.getString(UserServiceConstants.USER_ID));
		user.setFirstName(rs.getString(UserServiceConstants.FIRST_NAME));
		user.setLastName(rs.getString(UserServiceConstants.LAST_NAME));
		user.setMiddleName(rs.getString(UserServiceConstants.MIDDLE_NAME));
		user.setActiveFlag(rs.getString(UserServiceConstants.ACTIVE_FLAG));
		user.setLastModified(rs.getString(UserServiceConstants.LAST_MODIFIED_DATETIME));
		user.setUsername(newUser.getUsername());
		user.setPassword(newUser.getPassword());
		user.setPhone(newUser.getPhone());
		user.setEmail(newUser.getEmail());
		user.setRoleId(newUser.getRoleId());
		user.setAddress(newUser.getAddress());
		return user;
	}
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
}
