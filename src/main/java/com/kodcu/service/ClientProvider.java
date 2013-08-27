/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodcu.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 *
 * @author hakdogan
 */
public class ClientProvider {
    
    private static ClientProvider instance = null;
    private static Object lock      = new Object();
    
    private Client client;
    private Node node;

    public static ClientProvider instance(){
        
        if(instance == null) { 
            synchronized (lock) {
                if(null == instance){
                    instance = new ClientProvider();
                }
            }
        }
        return instance;
    }

    public void prepareClient(){
        node   = nodeBuilder().node();
        client = node.client();
    }

    public void closeNode(){
        
        if(!node.isClosed())
            node.close();

    }
    
    public Client getClient(){
        return client;
    }
    
    
    public void printThis() {
        System.out.println(this);
    }
    
}
