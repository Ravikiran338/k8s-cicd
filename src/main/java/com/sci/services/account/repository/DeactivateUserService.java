/**
 * 
 */
package com.sci.services.account.repository;

import com.sci.services.account.model.Status;
import com.sci.services.account.model.User;

import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
public interface DeactivateUserService {
	public Mono<Status> deactivateUser(Mono<User> user);

}
