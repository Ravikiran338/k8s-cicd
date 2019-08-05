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
public interface UpdateUserService {
	public Mono<Status> updateUser(Mono<User> user);
}
