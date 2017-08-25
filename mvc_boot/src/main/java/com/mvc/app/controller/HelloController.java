package com.mvc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.app.mapper.UserMapper;
import com.mvc.app.pojo.User;

@Controller
@RequestMapping(value = "/cs")
public class HelloController {

	@Autowired
	private UserMapper userMapper;

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String index() {
		User user = userMapper.findById(1);
		System.err.println("AAAAAAAAAAAAAAAAAAAAA");
		return user.toString();
	}

}