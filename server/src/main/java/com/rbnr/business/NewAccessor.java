package com.rbnr.business;

import java.util.List;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.TupleValue;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import java.util.UUID;

@Accessor
public interface NewAccessor {
    
    @Query("INSERT INTO NEWS (id , username, title, content,createdAt,updatedAt) VALUES (uuid(),?,?,?,ToTimestamp(now()),ToTimestamp(now())) IF NOT EXISTS")
    ResultSet addnew(String username, String title, String content);
    
    @Query("DELETE FROM NEWS WHERE id=?  IF EXISTS ")
    ResultSet deletenew(UUID id);
    
    @Query("UPDATE NEWS SET title=? , content=? , updatedAt=ToTimestamp(now()) WHERE id=? IF EXISTS")
    ResultSet updatenew(String title, String content,UUID id);
    
    @Query("SELECT * from news")
    ResultSet getall();
    
    @Query("SELECT * from news where username=? ALLOW FILTERING")
    ResultSet getbyuser(String username);
    
    @Query("SELECT * from news where id=? ALLOW FILTERING")
    ResultSet getone(UUID id);
    
    @Query("UPDATE NEWS SET comments = comments + ? WHERE id=? IF EXISTS")
    ResultSet addcomment(List<TupleValue> comment, UUID id);
    
    @Query("UPDATE NEWS SET comments = comments - ? WHERE id=? IF EXISTS")
    ResultSet deletecomment(List<TupleValue> comment, UUID id);
    
}
