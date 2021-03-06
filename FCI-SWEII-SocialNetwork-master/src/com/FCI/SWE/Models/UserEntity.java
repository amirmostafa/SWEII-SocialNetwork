package com.FCI.SWE.Models;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity {
	private String name;
	private String email;
	private String password;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;

	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}

	/**
	 * 
	 * This static method will form UserEntity class using json format contains
	 * user data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	public static UserEntity getUser(String json) {

		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			return new UserEntity(object.get("name").toString(), object.get(
					"email").toString(), object.get("password").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			//System.out.println(entity.getProperty("name").toString());
			
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				return returnedUser;
			}
		}

		return null;
	}
	
	public static UserEntity check(String email) {
		ActiveUser User=new ActiveUser();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQueryF = new Query("FriendRequest");
		Query gaeQuery = new Query("users");
		PreparedQuery pqF = datastore.prepare(gaeQueryF);
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Boolean check=true;
		
		for(Entity entityF : pqF.asIterable()){
			//System.out.println("destination"+entityF.getProperty("destination").toString());
			if(entityF.getProperty("destination").toString().equals(email)&&entityF.getProperty("source").toString().equals(User.email) || entityF.getProperty("source").toString().equals(email)&&entityF.getProperty("destination").toString().equals(User.email) ){
				check=false;
				}
		}
	if(check){
		for (Entity entity : pq.asIterable()) {
			//System.out.println("email"+entity.getProperty("email").toString());
			if (entity.getProperty("email").toString().equals(email)) {
				
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				//System.out.println("Success");
				return returnedUser;
			  }
			}
		}
		return null;
		
	}
	
	public static UserEntity checkStatus(String email) {
		ActiveUser User=new ActiveUser();
		UserEntity returnedUser=new UserEntity("a", "b", "c");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQueryF = new Query("FriendRequest");
		PreparedQuery pqF = datastore.prepare(gaeQueryF);
		Boolean check=true;
		int i=0;
		for(Entity entityF : pqF.asIterable()){
			i++;
			//System.out.println("destination"+entityF.getProperty("destination").toString()+"/"+"Source"+entityF.getProperty("source").toString()+"/"+"status"+entityF.getProperty("status").toString());
			if(entityF.getProperty("source").toString().equals(email)&&entityF.getProperty("destination").toString().equals(User.email)&&entityF.getProperty("status").toString().equals("Pending") ){
			
				returnedUser = new UserEntity(""+i, email, "c");
				check=true;
				break;
			}else{
				check=false;
			}
		}
		if (check==false){
			return null;	
		}else{
			return returnedUser;
		}
		
		
	}
	
	
	public static String getRequests(String email) {
		String req="";
		Boolean check=true;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("FriendRequest");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			//System.out.println(entity.getProperty("name").toString());
			if (entity.getProperty("destination").toString().equals(email)) {
				
				String source=entity.getProperty("source").toString();
				//System.out.println("source"+entity.getProperty("source").toString());
				req=req+"/"+source;
				//System.out.println("req:"+req);
				check=false;
			}
		}
		
		if(check==false){
			return req;
		}
		return null;
	}
	

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity employee = new Entity("users", list.size() + 1);

		employee.setProperty("name", this.name);
		employee.setProperty("email", this.email);
		employee.setProperty("password", this.password);
		datastore.put(employee);

		return true;

	}
	
	public Boolean addFriend() {
		ActiveUser User=new ActiveUser();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("FriendRequest");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity employee = new Entity("FriendRequest", list.size() + 1);

		employee.setProperty("source", User.email);
		employee.setProperty("destination", this.email);
		employee.setProperty("status", "Pending");
		datastore.put(employee);
		
		return true;

	}
	
	public Boolean done() {
		ActiveUser User=new ActiveUser();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("FriendRequest");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		int id=Integer.parseInt(this.name);
		//System.out.println("ID"+id);
		Entity employee = new Entity("FriendRequest",id);

		employee.setProperty("source", User.email);
		employee.setProperty("destination", this.email);
		employee.setProperty("status", "Accepted");
		datastore.put(employee);
		
		return true;

	}
}



