package org.dubbo.test;

import java.util.HashMap;
import java.util.Map;

import org.dubbo.utils.HttpUtils;

import com.alibaba.fastjson.JSON;

public class A {
	public static void main(String[] args) {
		// Map<String, String> body = new HashMap<String, String>();
		// body.put("user_name", "AAAAA");
		// body.put("password", "AAAAA");
		// body.put("grant_type", "AAAAA");
		//
		// Map<String, String> headers = new HashMap<String, String>();
		// headers.put("v", "1.0");
		// headers.put("Content-Type", "application/json");
		//
		// String r =
		// HttpUtils.post("http://192.168.5.102:8080/wx/return_str.do",
		// JSON.toJSONString(body).toString(), headers).getContent();
		// System.err.println(r);
		// String c = HttpUtils
		// .get("http://192.168.5.102:8080/wx/return_str1.do?user_name=BBB&password=BBB",
		// null, headers).getContent();
		// System.err.println(c);
		System.err.println(getNum(1,2,3));
	}

	public static int getNum(int... num) {
		int a = 0;
		for (int x : num) {
			a += x;
		}
		return a;
	}
}
