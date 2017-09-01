package org.dubbo.action;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dubbo.api.service.UserService;
import org.dubbo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;

@Controller
@Scope("prototype")
@RequestMapping(value = "/wx")
public class UserAction {

	@Autowired
	public UserService userService;

	@RequestMapping(value = "/listUser", method = RequestMethod.GET)
	public void listUser(HttpServletRequest request) {
		User u = userService.findById(1);
		System.err.println(u.getName());
	}

	@RequestMapping(value = "/listUsers", method = RequestMethod.GET)
	public ModelAndView listUsers(HttpServletRequest request) {
		ModelAndView andView = new ModelAndView();
		User u = userService.findById(1);
		andView.addObject("A",u);
		andView.setViewName("index");
		return andView;
	}

	@RequestMapping(value = "/return_str", method = RequestMethod.POST)
	public void return_str(@RequestBody String body, Writer writer,
			@RequestHeader Map<String, String> mp) {
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			JSONObject jsonObject = JSONObject.parseObject(body);
			String code = "";
			if (StrKit.notBlank(mp.get("v"))) {
				if ("1.0".equals(mp.get("v"))) {
					code = "1";
				} else {
					code = "401";
				}
			} else {
				code = "400";
			}
			if ("AAAAA".equals(jsonObject.getString("user_name"))) {
				User u = userService.findById(1);
				m.put("status", true);
				m.put("result", JSONObject.toJSONString(u));
				m.put("err_code", code);
			} else {
				m.put("status", false);
				m.put("result", "");
				m.put("err_code", code);
			}
			writer.write(JSONObject.toJSONString(m));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@RequestMapping(value = "/return_str1", method = RequestMethod.GET)
	public void return_str1(@RequestParam String user_name, String password,
			Writer writer, @RequestHeader Map<String, String> mp) {
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			System.err.println(user_name);
			System.err.println(password);
			String code = "";
			if (StrKit.notBlank(mp.get("v"))) {
				if ("1.0".equals(mp.get("v"))) {
					code = "1";
				} else {
					code = "401";
				}
			} else {
				code = "400";
			}
			if ("AAAAA".equals(user_name)) {
				User u = userService.findById(1);
				m.put("status", true);
				m.put("result", JSONObject.toJSONString(u));
				m.put("err_code", code);
			} else {
				m.put("status", false);
				m.put("result", "");
				m.put("err_code", code);
			}
			writer.write(JSONObject.toJSONString(m));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
