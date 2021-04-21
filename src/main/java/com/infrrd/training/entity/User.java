package com.infrrd.training.entity;

public class User {

	private String userId;
	private String userName;
	private String address;

	public User() {
		super();
	}

	public User(String userId, String userName, String address) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.address= address;
		
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", address=" + address + "]";
	}
}
