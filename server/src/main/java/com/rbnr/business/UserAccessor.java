package com.rbnr.business;

import java.util.List;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface UserAccessor {
    
    @Query("INSERT INTO USERS (username , password, firstname, lastname,createdAt,updatedAt) VALUES (?,?,?,?,ToTimestamp(now()),ToTimestamp(now())) IF NOT EXISTS")
    ResultSet signup(String username,String password,String firstname,String lastname);

    @Query("SELECT * FROM USERS WHERE username=?")
    ResultSet getOne(String username);

    @Query("DELETE FROM USERS WHERE username=? IF EXISTS")
    ResultSet deleteuser(String username);

    @Query("UPDATE USERS SET firstname=? , lastname=? , updatedAt=ToTimestamp(now()) WHERE username=? IF EXISTS")
    ResultSet updateuser(String firstname,String lastname, String username);

    @Query("UPDATE USERS SET password=? , updatedAt=ToTimestamp(now()) WHERE username=? IF EXISTS")
    ResultSet updatepassword(String password, String username);

    @Query("UPDATE USERS SET friends = friends + ? WHERE username = ? IF EXISTS")
    ResultSet addfriend(List<String> friend,String username);

    @Query("UPDATE USERS SET friends = friends - ? WHERE username = ? IF EXISTS")
    ResultSet deletefriend(List<String> friend,String username);	
}
