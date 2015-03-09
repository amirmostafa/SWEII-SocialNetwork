package com.FCI.SWE.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.ActiveUser;
import com.FCI.SWE.Models.UserEntity;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces("text/html")
public class Service {
	
	
	/*@GET
	@Path("/index")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}*/


		/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		UserEntity user = new UserEntity(uname, email, pass);
		user.saveUser();
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}
	
	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * @param uname provided user name
	 * @param pass provided user password
	 * @return user in json format
	 */
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(uname, pass);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			ActiveUser User1=new ActiveUser();
			User1.setEmail(user.getEmail());
			User1.setName(user.getName());
			//System.out.println("currentE"+User1.email);
			//System.out.println("currentN"+User1.name);
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
		}

		return object.toString();

	}

	@POST
	@Path("/Search")
	public String search(@FormParam("email") String email) {
		ActiveUser User =new ActiveUser();
		//System.out.println("Entered email:"+email);
		//System.out.println("Active User:"+User.email);
		//ActiveUser User =new ActiveUser();
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.check(email);
		if (user == null|| User.email.equals(email)  ) {
			object.put("Status", "Failed");
		} else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			user.addFriend();
		}
		return object.toString();
	}
	
	
	@POST
	@Path("/AcceptFriend")
	public String acceptFriend(@FormParam("Email") String email) {
		ActiveUser User =new ActiveUser();
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.checkStatus(email);
		if (user == null|| User.email.equals(email)  ) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "Now You and"+email+" are Friends");
			user.done();
		}

		return object.toString();

	}
	
	
	/*
	@POST
	@Path("/Requests")
	public String Requests() {
		ActiveUser User=new ActiveUser();
		JSONObject object = new JSONObject();
		UserEntity user=new UserEntity(null, null, null);
		String source[]=user.getRequests(User.email).split("/");
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			for(Integer i=1 ; i<source.length; i++){
				object.put(i.toString(), source[i]);
			}
		}

		return object.toString();

	}
*/

	
	
}