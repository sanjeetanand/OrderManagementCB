package com.om.cb.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.json.JsonObject;
import com.om.cb.bean.Customer;
import com.om.cb.util.CBManager;
import com.om.cb.util.Util;

public class CustomerDao {

	private CBManager cbm = null;
	
	public Customer getBean(String input) {
		Customer cust = new Customer();
		JSONObject jObj = new JSONObject(input);
		cust.setCustId(jObj.getString("custId"));
		cust.setCustName(jObj.getString("custName"));
		cust.setCustPhone(jObj.getString("custPhone"));
		cust.setCustEmail(jObj.getString("custEmail"));
		cust.setCustAddress(jObj.getString("custAddress"));
		return cust;
	}
	
	public List<Customer> getAllCustomer(){
		List<Customer> list = new ArrayList<Customer>();
		try {
			cbm = new CBManager();
			cbm.setBucket("customer_tb");
			List<String> custList = cbm.query(" WHERE REGEXP_CONTAINS(custId, \'.*\')", JsonObject.create().put("name", "name"));
			for(String s : custList) {
				list.add(new CustomerDao().getBean(s));
			}
		} finally {
			cbm.disconnect();
		}
		return list;
	}
	
	public Customer getCustomerById(String id){
		Customer cust = null;
		try {
			cbm = new CBManager();
			cbm.setBucket("customer_tb");
			List<String> custList = cbm.query(" WHERE custId=$custId", JsonObject.create().put("custId", id));
			if(custList != null && !custList.isEmpty()) {
				cust = new CustomerDao().getBean(custList.get(0));
			}
		} finally {
			cbm.disconnect();
		}
		return cust;
	}
	
	public boolean upsertCustomer(Customer c) {
		boolean flag = false;
		try {
			cbm = new CBManager();
			cbm.setBucket("customer_tb");
			cbm.upsert(c.getCustId(), JsonObject.fromJson(Util.buildJson(c)));
			if(cbm.getCouchbaseData(c.getCustId()) != null) {
				flag = true;
			}
		} finally {
			cbm.disconnect();
		}
		return flag;
	}
	
	public boolean removeCustomer(String id) {
		boolean flag = false;
		try {
			cbm = new CBManager();
			cbm.setBucket("customer_tb");
			cbm.remove(id);
			if(new CustomerDao().getCustomerById(id) == null) {
				flag = true;
			}
		} catch (DocumentNotFoundException e) {
			
		} finally {
			cbm.disconnect();
		}
		return flag;
	}
	
	public static void main(String[] args) {
		Customer c = new Customer("cust_005","Sanjeet","0987654321","sanjeet123@gmail.com","Patna");
		System.out.println(new CustomerDao().removeCustomer(c.getCustId()));
	}
}
