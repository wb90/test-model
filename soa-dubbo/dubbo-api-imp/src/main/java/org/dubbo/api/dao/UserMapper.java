package org.dubbo.api.dao;

import org.dubbo.pojo.User;

public interface UserMapper {
	User findById(int id);
}
