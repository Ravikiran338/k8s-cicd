/**
 * 
 */
package com.sci.services.account.repository;

import com.sci.services.account.model.Account;
import com.sci.services.account.model.Status;
import com.sci.services.account.model.UserBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
public interface UserService {
	public Flux<Account> getAllAccounts();
	public Mono<Status> createUser(Mono<UserBean> user);
}
