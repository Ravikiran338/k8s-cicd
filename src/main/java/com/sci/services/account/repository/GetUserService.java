/**
 * 
 */
package com.sci.services.account.repository;

import com.sci.services.account.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
public interface GetUserService {
	public Flux<User> getAllUsers();
	public Mono<User> getUser(Mono<String> usrId);
}
