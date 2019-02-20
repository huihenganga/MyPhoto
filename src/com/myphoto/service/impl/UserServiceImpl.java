package com.myphoto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.UserDao;
import com.myphoto.entity.User;
import com.myphoto.service.UserService;

@Service
@Transactional
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public void saveOrUpdateUser(User t) {
		userDao.saveOrUpdate(t);
	}

	@Override
	public User getUserById(String id) {
		return userDao.findFirstByHql("from User where id=?", id);
	}

	@Override
	public void delUser(Integer id) {
		userDao.delete(id);
	}

	@Override
	public User getPubUserByToken(String token) {
		return userDao.findFirstByHql("from User where token=?", token);
	}

	@Override
	public User getUserByOpenId(String openId) {
		return userDao.findFirstByHql("from User where openId=?", openId);
	}

}
