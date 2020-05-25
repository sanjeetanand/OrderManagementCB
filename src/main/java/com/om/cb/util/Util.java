package com.om.cb.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.om.cb.bean.Customer;

public class Util {


	public static String buildJson(Customer c) {
		JSONObject jo = new JSONObject(c);
		Map<String, Object> map = jo.toMap();
		String result = "{";
		result += map.entrySet().stream().map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
				.collect(Collectors.joining(","));
		result += "}";
		return result;
	}

	public static String buildJsonString(Customer c) {
		HashMap<String, Object> row = new HashMap<String, Object>();
		GsonBuilder builder = new GsonBuilder();
		Gson g = builder.create();

		row.put("custId", c.getCustId());
		row.put("custName", c.getCustName());
		row.put("custPhone", c.getCustPhone());
		row.put("custEmail", c.getCustEmail());
		row.put("custAddress", c.getCustAddress());

		return g.toJson(row);
	}

	public static void main(String[] args) {
		Customer c = new Customer("cust_001","Sanjeet","1234567890","sanjeet@gmail.com","Patna");
		System.out.println(Util.buildJson(c));
		System.out.println(Util.buildJsonString(c));
	}
}
