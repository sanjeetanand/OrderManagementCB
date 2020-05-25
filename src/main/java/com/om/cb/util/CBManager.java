package com.om.cb.util;

import java.util.ArrayList;
import java.util.List;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;


public class CBManager {
	private Cluster cluster = null;
	private Bucket bucket = null;
	private String bucketName;
	private Collection collection;


	public void setBucket(String bucketName) {
		cluster = Cluster.connect("127.0.0.1", "Administrator", "password");
		this.bucketName = bucketName;
		bucket = cluster.bucket(bucketName);
		collection = bucket.defaultCollection();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public MutationResult upsert(String id, Object object) {
		//System.out.println(object.toString());
		MutationResult mr = collection.upsert(id, object);
		return mr;
	}

	public MutationResult remove(String id) {
		//System.out.println("Removing document " + id);
		return collection.remove(id);
	}

	public List<String> query(String query, JsonObject jo) {
		List<String> data = new ArrayList<String>();
		QueryResult result = cluster.query("select * from `" + bucketName + "`" + query,
				QueryOptions.queryOptions().parameters(jo));
		for (JsonObject row : result.rowsAsObject()) {
			//System.out.println("Found row: " + row.get(bucketName).toString());
			data.add(row.get(bucketName).toString());
		}
		return data;
	}

	public void disconnect() {
		if (cluster != null) {
			cluster.disconnect();
		}
	}
	
	public List<String> getCouchbaseData(String id) {
		List<String> data = null;
		CBManager couchbaseDAO = new CBManager();
		couchbaseDAO.setBucket("data");
		data = couchbaseDAO.query(" WHERE id=$id", JsonObject.create().put("id", id));
		return data;
	}
	
	public static void main(String[] args) {
		CBManager dao = new CBManager();
		dao.setBucket("customer_tb");
		List<String> list = dao.query("WHERE custId=$custId", JsonObject.create().put("custId", "cust_001"));
		dao.disconnect();
		for(String s : list) {
			System.out.println(s);
		}
		
	}

}
