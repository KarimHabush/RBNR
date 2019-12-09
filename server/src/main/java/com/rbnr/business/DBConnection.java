/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbnr.business;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
/**
 *
 * @author karimhabush
 */
public class DBConnection {
    private final String keyspace = "KS_rbnr";
    private final String serverIp = "127.0.0.1";
    private Cluster cluster;
    private Session session ; 
    
    public DBConnection(){
        this.cluster = Cluster.builder()
                .addContactPoint(this.serverIp)
                .build();
        
        this.session = this.cluster.connect(this.keyspace);
    }

    public Session getSession() {
        return session;
    }
   
     public void close() {
    	this.session.close();
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
}
