package com.myphoto.service;

import com.myphoto.entity.User;

public interface UserService {

	public void saveOrUpdateUser(User t);

	public User getUserById(String id);
	
	public void delUser(Integer id);

	public User getUserByOpenId(String openId);

	public User getPubUserByToken(String token);
}
