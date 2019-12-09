/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbnr.api;


import com.datastax.driver.core.Row;
import com.datastax.driver.mapping.MappingManager;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import com.rbnr.business.DBConnection;
import com.rbnr.business.UserAccessor;
import com.rbnr.business.Validator;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author karimhabush
 */

@WebService(serviceName = "WS_TEST")
public class WS_TEST {
    DBConnection conn = new DBConnection();
    /**
     * This is a sample web service operation
     */
    
    @WebMethod(operationName = "signup")
    public String signup(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "firstname") String firstname,
            @WebParam(name = "lastname") String lastname) {
        //Connection 
    	DBConnection conn = new DBConnection();

    	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();

        //Validate input data 
        Validator validator = new Validator();
        validator.validateUsername(username);
        validator.validateFirstname(firstname);
        validator.validateLastname(lastname);
        validator.validatePassword(password);
        if(!validator.isValid()){
            obj.put("status", 401);
            obj.put("errors", validator.getErrors());
            return obj.toString();
        }
        
        for(Row r :  userAccessor.signup(username, password,firstname,lastname)) {
            if(r.getBool(0)) {
                    obj.put("status", 200);
                    obj.put("success", "User added successfuly!");
            }
            else {
                    JSONObject userobj = new JSONObject();
                    userobj.put("username","Username already exists!");
                    obj.put("status", 401);
                    obj.put("errors", userobj);
            }
        }
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "login")
    public String login(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();

        Validator validator = new Validator();
        validator.validateUsername(username);
        validator.validatePassword(password);
        if(!validator.isValid()){
            obj.put("errors", validator.getErrors());
            return obj.toString();
        }


        Row r = userAccessor.getOne(username).one();
        if(r !=null) {
    		if(r.getString("password").equals(password)) {
    			obj.put("status", 200);
        		obj.put("username", r.getString("username"));
        		obj.put("firstname", r.getString("firstname"));
        		obj.put("lastname", r.getString("lastname"));	
    		}
    		else {
    		    JSONObject userobj = new JSONObject();
                userobj.put("password","Password is incorrect!");
                obj.put("status", 401);
                obj.put("errors", userobj);
        	}

        }
        else {
            JSONObject userobj = new JSONObject();
            userobj.put("username","Username does not exist!");
            obj.put("status", 401);
            obj.put("errors", userobj);
            
        }
        //Close Connection 
        conn.close();
        return obj.toString();
    }



@WebMethod(operationName = "deleteuser")
    public String deleteuser(
            @WebParam(name = "username") String username) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Delete 
        JSONObject obj = new JSONObject();
        Validator validator = new Validator();
        validator.validateUsername(username);
        if(!validator.isValid()){
            obj.put("errors", validator.getErrors());
            return obj.toString();
        }
        
        for(Row r :  userAccessor.deleteuser(username)) {
        	
        	if(r.getBool(0)) {
        		obj.put("status", 200);
        		obj.put("success", "User deleted successfuly");
        	}
        	else {
        		obj.put("status", 500);
        		obj.put("error", "Username not found");
        	}
        }
        
        //Close Connection 
        conn.close();
        
        return obj.toString();
    }
    
    @WebMethod(operationName = "updateuser")
    public String updateuser(
            @WebParam(name = "username") String username,
            @WebParam(name = "firstname") String firstname,
            @WebParam(name = "lastname") String lastname) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Update the user in DB 
        JSONObject obj = new JSONObject();
        
        Validator validator = new Validator();
        validator.validateUsername(username);
        validator.validateFirstname(firstname);
        validator.validateLastname(lastname);
        if(!validator.isValid()){
            obj.put("errors", validator.getErrors());
            return obj.toString();
        }
        
        for(Row r :  userAccessor.updateuser(firstname,lastname,username)) {
        	
        	if(r.getBool(0)) {
        		obj.put("status", 200);
        		obj.put("success", "User updated successfuly");
        	}
        	else {
        		obj.put("status", 500);
        		obj.put("error", "Username not found");
        	}
        }
        
        //Close Connection 
        conn.close();
        
        return obj.toString();
    }
    
    @WebMethod(operationName = "addfriend")
    public String addfriend(
            @WebParam(name = "username") String username,
            @WebParam(name = "friend") String friend) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();
        Validator validator = new Validator();
        validator.validateUsername(username);
        validator.validateUsername(friend);
        if(!validator.isValid()){
            obj.put("errors", validator.getErrors());
            return obj.toString();
        }
        List<String> friends = new ArrayList<String>();
        friends.add(friend);
        
        Row f = userAccessor.getOne(friend).one();
        if(f !=null && !username.equals(friend)) {
            for(Row row :  userAccessor.addfriend(friends,username)) {
        	
            	if(row.getBool(0)) {
            		obj.put("status", 200);
            		obj.put("success", "Friend added successfuly!");
            	}
            	else {
            		obj.put("status", 500);
            		obj.put("error", "Username not found");
            	}
            }   
    	
        }
        else {
    		obj.put("status", 500);
    		obj.put("error", "Friend's username not accepted!");
        }
        
        
        //Close Connection 
        conn.close();
        
        return obj.toString();
    }
    
    @WebMethod(operationName = "deletefriend")
    public String deletefriend(
            @WebParam(name = "username") String username,
            @WebParam(name = "friend") String friend) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();
        Validator validator = new Validator();
        validator.validateUsername(username);
        validator.validateUsername(friend);
        if(!validator.isValid()){
            obj.put("errors", validator.getErrors());
            return obj.toString();
        }



        List<String> friends = new ArrayList<String>();
        friends.add(friend);
        
        for(Row r :  userAccessor.deletefriend(friends,username)) {
        	
        	if(r.getBool(0)) {
        		obj.put("status", 200);
        		obj.put("success", "Friend deleted successfuly!");
        	}
        	else {
        		obj.put("status", 500);
        		obj.put("error", "Username not found!");
        	}
        }
        
        //Close Connection 
        conn.close();
        
        return obj.toString();
    }


}

