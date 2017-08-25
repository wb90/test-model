package org.dubbo.api.main;

import java.io.IOException;

import org.dubbo.api.service.UserService;
import org.dubbo.pojo.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboMain {

	static ClassPathXmlApplicationContext context;

	public static void main(String[] args) throws IOException {
		context = new ClassPathXmlApplicationContext(
				new String[] { "dubbo.xml" });
		context.start();
//		btest_uu(context);
		System.in.read(); // 按任意键退出
		// Main.main(args);

	}

/*	public static void btest_uu(ClassPathXmlApplicationContext context) {
		System.err.println("333");
		UserService imp = (UserService) context.getBean("userService");
		User us = imp.findById(1);
		System.err.println(us.getName());
	}*/

}
