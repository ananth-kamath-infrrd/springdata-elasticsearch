package com.infrrd.training.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.infrrd.training.entity.User;

@Repository
public interface UserRepository {

	List<User> findAllUserDetailsFromElastic() throws IOException;

	List<User> findUserByUserName(String userName);

	List<User> findUserByUserNameAndAddress(String userName, String address);

	void addNewUser(String uid, String userName, String address);
	
	void deleteUser(String uid);


}
