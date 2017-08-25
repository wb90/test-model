package com.mvc.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mvc.app.pojo.User;

@Mapper
public interface UserMapper {

	@Select("select * from user where id = #{id}")
	User findById(@Param("id") int id);

}
