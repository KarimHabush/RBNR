/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbnr.business;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import java.util.UUID;

/**
 *
 * @author karimhabush
 */
@Accessor
public interface ReactionAccessor {
    
    @Query("INSERT INTO reactions(id_news, username, score) values(?,?,?) ")
    ResultSet addreaction(UUID id_news, String username, int score);
    
    @Query("DELETE FROM reactions where id_news=? and username=?")
    ResultSet deletereaction(UUID id_news , String username);
    
    @Query("SELECT username, score FROM reactions WHERE id_news = ? ALLOW FILTERING")
    ResultSet getall(UUID id);
    
    @Query("SELECT * FROM reactions ")
    ResultSet getall();

    @Query("SELECT * FROM reactions WHERE id_news = ? and username = ?")
    ResultSet getone(UUID id, String username);
    
}
