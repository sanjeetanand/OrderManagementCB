package com.om.cb;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.om.cb.bean.Customer;
import com.om.cb.dao.CustomerDao;

@SpringBootApplication
@RestController
public class OmcbApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmcbApplication.class, args);
	}
	
	@RequestMapping("/")
	public String index() {
		return "Welcome to Order Management Coucbase";
	}
	
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public List<Customer> getAllCustomer() {
		return new CustomerDao().getAllCustomer();
	}
	
	@RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
	public Customer getCustomerById(@PathVariable("id") String id) {
		return new CustomerDao().getCustomerById(id);
	}
	
	@RequestMapping(value = "/customer", method = RequestMethod.POST)
	public boolean addCustomer(@RequestBody Customer customer) {
		return new CustomerDao().upsertCustomer(customer);
	}
	
	@RequestMapping(value = "/customer", method = RequestMethod.PUT)
	public boolean updateCustomer(@RequestBody Customer customer) {
		return new CustomerDao().upsertCustomer(customer);
	}
	
	@RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
	public boolean removeCustomer(@PathVariable("id") String id) {
		return new CustomerDao().removeCustomer(id);
	}

}
