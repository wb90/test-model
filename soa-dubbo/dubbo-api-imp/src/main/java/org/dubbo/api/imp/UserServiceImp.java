package org.dubbo.api.imp;

import org.dubbo.api.dao.UserMapper;
import org.dubbo.api.service.UserService;
import org.dubbo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImp implements UserService {

	@Autowired
	UserMapper userMapper;

	public User findById(int id) {
		User us = userMapper.findById(1);
		return us;
	}

}
