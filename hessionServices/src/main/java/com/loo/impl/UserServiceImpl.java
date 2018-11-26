package com.loo.impl;

import com.loo.facade.UserService;

public class UserServiceImpl implements UserService {

	public void addUser(String userInfo) {
		System.out.println("add userInfo:" + userInfo);
	}

}
