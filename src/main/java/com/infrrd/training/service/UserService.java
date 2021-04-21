package com.infrrd.training.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infrrd.training.entity.User;
import com.infrrd.training.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUserInfo() throws IOException {
		
		return userRepository.findAllUserDetailsFromElastic();
	}

	public List<User> getUserDataByName(String userName) {
		return userRepository.findUserByUserName(userName);
	}

	public List<User> getUserDataByNameAndAddress(String userName, String address) {
		return userRepository.findUserByUserNameAndAddress(userName, address);
	}

	public void addNewUser(String uid, String userName, String address) {
		userRepository.addNewUser(uid, userName, address);
	}
	
	public void removeUser(String uid) {
		userRepository.deleteUser(uid);
	}
}
