package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exception.ResourceNotFoundException;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.ui.model.request.UpdateUserRequest;
import com.appsdeveloperblog.app.ws.ui.model.request.UserRequest;
import com.appsdeveloperblog.app.ws.ui.model.response.UserResponse;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	protected UserService userService;
	
	Map<String, UserResponse> users;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<String> getUsers(@RequestParam(value = "page", defaultValue="1") int page, 
			@RequestParam(value = "limit", defaultValue = "100") int limit,
			@RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
		
		String message = "GET /users was called! Page = " + page + " and Limit = " + limit; 
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
	
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userData) {
		UserResponse userCreated = this.userService.create(userData);
		return new ResponseEntity<UserResponse>(userCreated, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
		if (users == null || !users.containsKey(userId)) {
			throw new ResourceNotFoundException("User " + userId + " was not found.");
		}
		
		return new ResponseEntity<UserResponse>(users.get(userId), HttpStatus.OK);
	}
	
	@PutMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Void> updateUserById(@PathVariable String userId, @Valid @RequestBody UpdateUserRequest updateUserData) {
		if (users == null || !users.containsKey(userId)) {
			throw new ResourceNotFoundException("User " + userId + " was not found.");
		}
		
		UserResponse storedUser = users.get(userId);
		storedUser.setFirstName(updateUserData.getFirstName());
		storedUser.setLastName(updateUserData.getLastName());
		users.put(userId, storedUser);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> deleteUserById(@PathVariable String userId) {
		if (users == null || !users.containsKey(userId)) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		users.remove(userId);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
}
