package com.infrrd.training.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infrrd.training.entity.User;
import com.infrrd.training.service.UserService;

@RestController
@RequestMapping(value = "/userinfo")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// End point to retrieve documents of all users in index- userimage
	@GetMapping(value="/alluser", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAllUser() throws IOException{
		return userService.getAllUserInfo();
	}
	
	// End point to retrieve documents of all users with username = userName
	@GetMapping(value="/alluser/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getUserByName(@PathVariable String userName) {
		return userService.getUserDataByName(userName);
	}
	
	// End point to retrieve documents of all users with username = userName and address = address
	@GetMapping(value="/alluser/{userName}/{address}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getUserByNameAndAddress(@PathVariable String userName, @PathVariable String address) {
		return userService.getUserDataByNameAndAddress(userName, address);
	}
	
	// End point to add a new user 
	@PostMapping(value="/adduser/{uid}/{userName}/{address}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String addNewUser(@PathVariable String uid, @PathVariable String userName, @PathVariable String address) {
		try {
			userService.addNewUser(uid,userName, address);
			return  "Success";
		} catch(Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
	
	//End point to delete a user with userId = uid
	@PostMapping(value="/deleteuser/{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteUser(@PathVariable String uid) {
		try {
			userService.removeUser(uid);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
}