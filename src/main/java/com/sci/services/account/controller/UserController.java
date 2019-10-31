package com.sci.services.account.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sci.pulsar.service.UserProducer;
import com.sci.services.account.model.Status;
import com.sci.services.account.model.User;
import com.sci.services.account.model.UserBean;
import com.sci.services.account.repository.DeactivateUserService;
import com.sci.services.account.repository.GetUserService;
import com.sci.services.account.repository.UpdateUserService;
import com.sci.services.account.repository.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DeactivateUserService deactivateUserService;
	
	@Autowired
	private UpdateUserService updateUserService;

	@Autowired
	private GetUserService getUserService;
	
	/* @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	@ResponseBody */
	@GetMapping("/")
	public Flux<User> findAll() {
		LOGGER.info("findAll");
		Flux<User> userList = getUserService.getAllUsers();
		return userList; 
	}
	
	@GetMapping("/user/{id}")
	public Mono<User> getUser(@PathVariable String id) {
		LOGGER.info("find By UserId");
		Mono<String> userMono = Mono.just(id);
		return getUserService.getUser(userMono);
	}

    @DeleteMapping
    public Mono<Status> deactivateUser(@RequestBody User user){
    	Mono<User> userMono = Mono.just(user);
		return deactivateUserService.deactivateUser(userMono);
    }
    
	@PostMapping
	public Mono<Status> createUser(@RequestBody UserBean user) {
		LOGGER.info("create: {}", user);
		Mono<UserBean> userMono = Mono.just(user);
		Mono<Status> userStatus = userService.createUser(userMono);
	/*	userStatus.subscribe(status -> {
			if(ServiceStatus.SUCCESS.getStatusCode().equalsIgnoreCase(status.getStatusCode())){
				try {
					new UserProducer().produceUserMessage(user.getEmail());
					LOGGER.info("createUser - invoking pulsar producer ");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.info("createUser -- exception",e.getMessage());
					e.printStackTrace();
				}
			}
		});*/
		try {
			new UserProducer().produceUserMessage(user);
			LOGGER.info("createUser - invoking pulsar producer ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.info("createUser -- exception",e.getMessage());
			e.printStackTrace();
		}
		return userStatus;
	}
	
	@PutMapping
	public Mono<Status> updateUser(@RequestBody User user){
		Mono<User> userMono = Mono.just(user);
		return updateUserService.updateUser(userMono);	
	}
}
