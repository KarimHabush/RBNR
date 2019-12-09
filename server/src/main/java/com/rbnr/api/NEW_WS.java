/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbnr.api;


import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.TupleType;
import com.datastax.driver.core.TupleValue;
import com.datastax.driver.mapping.MappingManager;
import com.rbnr.business.DBConnection;
import com.rbnr.business.NewAccessor;
import com.rbnr.business.ReactionAccessor;
import com.rbnr.business.UserAccessor;
import com.rbnr.mapreduce.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author karimhabush
 */
@WebService(serviceName = "NEW_WS")
public class NEW_WS {
    DBConnection conn = new DBConnection();
    /**
     * This is a sample web service operation
     */
    
    @WebMethod(operationName = "addnew")
    public String addnew(
            @WebParam(name = "username") String username,
            @WebParam(name = "title") String title,
            @WebParam(name = "content") String content) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();
        ResultSet u = userAccessor.getOne(username);
        if(u.one() !=null) {
            for(Row r :  newAccessor.addnew(username, title, content )) {
                if(r.getBool(0)) {
                        obj.put("status", 200);
                        obj.put("success", "New added successfuly!");
                }
                else {
                        obj.put("status", 500);
                        obj.put("error", "Unexpected error!");
                }
            }
        }
        else {
    		obj.put("status", 500);
    		obj.put("error", "Username not found!");
        }
        
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "deletenew")
    public String deletenew(
            @WebParam(name = "id") String id) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();
        
        for(Row r :  newAccessor.deletenew(UUID.fromString(id))) {
            if(r.getBool(0)) {
                    obj.put("status", 200);
                    obj.put("success", "New deleted successfuly!");
            }
            else {
                    obj.put("status", 500);
                    obj.put("error", "ID not found!");
            }
        }
        
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "updatenew")
    public String updatenew(
            @WebParam(name = "id") String id,
            @WebParam(name = "title") String title,
            @WebParam(name = "content") String content) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        
        //Sign up 
        JSONObject obj = new JSONObject();
        
        for(Row r :  newAccessor.updatenew(title,content,UUID.fromString(id))) {
            if(r.getBool(0)) {
                    obj.put("status", 200);
                    obj.put("success", "New updated successfuly!");
            }
            else {
                    obj.put("status", 500);
                    obj.put("error", "ID not found!");
            }
        }
        
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "getall")
    public String getall() {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        ReactionAccessor reactionAccessor = manager.createAccessor(ReactionAccessor.class);

        
        //Sign up 
        JSONObject nw;
        JSONArray arr = new JSONArray();
        Mapper mapred = new Mapper();
        Map<String, List<Integer>> reactions = new HashMap<String, List<Integer>>();
        reactions = mapred.map(reactionAccessor.getall());
        Map<String,Integer> scores = new HashMap<String,Integer>();
        scores = mapred.reduce(reactions);
        for(Row r :  newAccessor.getall()) {
            nw = new JSONObject();
            nw.put("id", r.getUUID("id").toString());
            nw.put("username", r.getString("username"));
            nw.put("title", r.getString("title"));
            nw.put("content", r.getString("content"));
            nw.put("createdAt", r.getTimestamp("createdat").toString());
            nw.put("updatedAt", r.getTimestamp("updatedat").toString());
            nw.put("total_score", scores.get(r.getUUID("id").toString()));
            
            arr.add(nw);
        }
        JSONObject obj = new JSONObject();
        obj.put("status", 200);
        obj.put("results",arr);
        
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "getbyuser")
    public String getbyuser(
    @WebParam(name = "username") String username) {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        ReactionAccessor reactionAccessor = manager.createAccessor(ReactionAccessor.class);

        
        //Sign up 
        JSONObject nw;
        JSONArray arr = new JSONArray();
        Mapper mapred = new Mapper();
        Map<String, List<Integer>> reactions = new HashMap<String, List<Integer>>();
        reactions = mapred.map(reactionAccessor.getall());
        Map<String,Integer> scores = new HashMap<String,Integer>();
        scores = mapred.reduce(reactions);

        for(Row r :  newAccessor.getbyuser(username)) {
            nw = new JSONObject();
            nw.put("id", r.getUUID("id").toString());
            nw.put("username", r.getString("username"));
            nw.put("title", r.getString("title"));
            nw.put("content", r.getString("content"));
            nw.put("createdAt", r.getTimestamp("createdat").toString());
            nw.put("updatedAt", r.getTimestamp("updatedat").toString());
            nw.put("total_score", scores.get(r.getUUID("id").toString()));
            
            arr.add(nw);
        }
        JSONObject obj = new JSONObject();
        obj.put("status", 200);
        obj.put("results",arr);
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    
    @WebMethod(operationName = "getone")
    public String getone(
    @WebParam(name = "id") String id)  {
        //Connection 
	DBConnection conn = new DBConnection();

	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        ReactionAccessor reactionAccessor = manager.createAccessor(ReactionAccessor.class); 
        
        JSONArray arr = new JSONArray();
        JSONObject robj;
        for(Row react : reactionAccessor.getall(UUID.fromString(id))){
            if(react!=null){
                robj = new JSONObject();
                robj.put("username", react.getString("username"));
                robj.put("score", react.getInt("score"));
                arr.add(robj);
            }
        }


        //Sign up
        JSONObject nw = new JSONObject();
        JSONObject obj = new JSONObject();
        for(Row r :  newAccessor.getone(UUID.fromString(id))) {
            if(r!=null){
                nw.put("id", r.getUUID("id").toString());
                nw.put("username", r.getString("username"));
                nw.put("title", r.getString("title"));
                nw.put("content", r.getString("content"));
                nw.put("createdAt", r.getTimestamp("createdat").toString());
                nw.put("updatedAt", r.getTimestamp("updatedat").toString());
                nw.put("comments", r.getList("comments", TupleValue.class).toString());
                nw.put("reactions",arr);
                obj.put("status", 200);
                obj.put("results",nw);
            }
            else {
                obj.put("status", 500);
                obj.put("error","ID not found!");
            }
        }
        
        
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "addreaction")
    public String addreaction(
            @WebParam(name = "id") String newsid,
            @WebParam(name = "username") String username,
            @WebParam(name = "score") int score)
    {
        
        //Connection 
	   DBConnection conn = new DBConnection();

	   //Mapping the database
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        ReactionAccessor reactionAccessor = manager.createAccessor(ReactionAccessor.class);
        
        
        JSONObject obj = new JSONObject();
        Row user = userAccessor.getOne(username).one();
        Row news = newAccessor.getone(UUID.fromString(newsid)).one();
        
        if(user==null || news ==null){
            obj.put("status", 500);
            obj.put("error","Username or news not found!");
            return obj.toJSONString();
        }
        
        if(Math.abs(score)!=1){
            obj.put("status", 500);
            obj.put("error","Score should equal to 1 or -1!");
            return obj.toJSONString();
        }
        
        
        reactionAccessor.addreaction(UUID.fromString(newsid),username,score);
        
        obj.put("status", 200);
        obj.put("success", "Reaction added successfuly!");

        //Close Connection 
        conn.close();
        return obj.toJSONString();
    }
    
    @WebMethod(operationName = "deletereaction")
    public String deletereaction(
            @WebParam(name = "id") String news_id,
            @WebParam(name = "username") String username) 
    {
        //Connection 
	   DBConnection conn = new DBConnection();

	   //Mapping the database
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        ReactionAccessor reactionAccessor = manager.createAccessor(ReactionAccessor.class);
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);

        JSONObject obj = new JSONObject();
        
        Row user = userAccessor.getOne(username).one();
        Row news = newAccessor.getone(UUID.fromString(news_id)).one();
        
        
        
        if(user==null || news ==null){
            obj.put("status", 500);
            obj.put("error","Username or news not found!");
            return obj.toJSONString();
        }
        
        for(Row r :  reactionAccessor.deletereaction(UUID.fromString(news_id),username)) {

            if(r.getBool(0)) {
                obj.put("status", 200);
                obj.put("success", "Reaction deleted successfuly!");
            }
            else {
                obj.put("status", 500);
                obj.put("error", "Unexpected error, try again later!");
            }
        }
        //Close Connection 
        conn.close();
        return obj.toString();
    }

    @WebMethod(operationName = "getreaction")
    public String getreaction(
            @WebParam(name = "news_id") String news_id,
            @WebParam(name = "username") String username) 
    {
        //Connection 
       DBConnection conn = new DBConnection();

       //Mapping the database
        MappingManager manager = new MappingManager(conn.getSession());
        
        
        //Accessor 
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        ReactionAccessor reactionAccessor = manager.createAccessor(ReactionAccessor.class);
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);

        JSONObject obj = new JSONObject();
        
        Row user = userAccessor.getOne(username).one();
        Row news = newAccessor.getone(UUID.fromString(news_id)).one();
        
        
        
        if(user==null || news ==null){
            obj.put("status", 500);
            obj.put("error","Username or news not found!");
            return obj.toJSONString();
        }
        
        Row r =  reactionAccessor.getone(UUID.fromString(news_id),username).one();

            if(r!=null) {
                obj.put("status", 200);
                obj.put("score", r.getInt("score"));
            }
            else {
                obj.put("status", 500);
                obj.put("error", "Unexpected error, try again later!");
            }
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "addcomment")
    public String addcomment(
            @WebParam(name = "id") String id,
            @WebParam(name = "username") String username,
            @WebParam(name = "comment") String comment) 
    {
        //Connection 
    	DBConnection conn = new DBConnection();

    	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        
        JSONObject obj = new JSONObject();
        
        
        
        
        Row u = userAccessor.getOne(username).one();
        Row n = newAccessor.getone(UUID.fromString(id)).one();
        
        List<DataType> types = new ArrayList<>();
        types.add(DataType.text());
        types.add(DataType.text());

        
        TupleType tuple = conn.getCluster().getMetadata().newTupleType(types);
        TupleValue value = tuple.newValue(username,comment);
        
        List<TupleValue> comments = new ArrayList<>();
        comments.add(value);
        if(u!=null && n!=null){
            for(Row r :  newAccessor.addcomment(comments,UUID.fromString(id))) {

                if(r.getBool(0)) {
                    obj.put("status", 200);
                    obj.put("success", "Comment added successfuly!");
                }
                else {
                    obj.put("status", 500);
                    obj.put("error", "Unexpected error, try again later!");
                }
            }

        }
        else {
            obj.put("status", 500);
            obj.put("error","Username or id not found!");
        }
        //Close Connection 
        conn.close();
        return obj.toString();
    }
    
    @WebMethod(operationName = "deletecomment")
    public String deletecomment(
            @WebParam(name = "id") String id,
            @WebParam(name = "username") String username,
            @WebParam(name = "comment") String comment) 
    {
        //Connection 
    	DBConnection conn = new DBConnection();

    	//Mapping the table
        MappingManager manager = new MappingManager(conn.getSession());
        
		
        //Accessor 
        NewAccessor newAccessor = manager.createAccessor(NewAccessor.class);
        UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
        
        //Sign up 
        
        JSONObject obj = new JSONObject();
        
        Row u = userAccessor.getOne(username).one();
        Row n = newAccessor.getone(UUID.fromString(id)).one();
        
        List<DataType> types = new ArrayList<>();
        types.add(DataType.text());
        types.add(DataType.text());

        
        TupleType tuple = conn.getCluster().getMetadata().newTupleType(types);
        TupleValue value = tuple.newValue(username,comment);
        
        List<TupleValue> comments = new ArrayList<>();
        comments.add(value);
        if(u!=null && n!=null){
            for(Row r :  newAccessor.deletecomment(comments,UUID.fromString(id))) {

                if(r.getBool(0)) {
                    obj.put("status", 200);
                    obj.put("success", "Comment deleted successfuly!");
                }
                else {
                    obj.put("status", 500);
                    obj.put("error", "Unexpected error, try again later!");
                }
            }

        }
        else {
            obj.put("status", 500);
            obj.put("error","Username or id not found!");
        }
        //Close Connection 
        conn.close();
        return obj.toString();
    }
}
