/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodcu.util;

import com.kodcu.service.ClientProvider;
import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 *
 * @author hakdogan
 */
public class ElasticSearchSystemEventListener implements SystemEventListener {

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if(event instanceof PostConstructApplicationEvent){

            /* Preparing the ElasticSearch Client */
            System.out.println("*********************************************");
            System.out.println("Preparing the ElasticSearch Client");
            ClientProvider.instance().prepareClient();
            System.out.println("The ElasticSearch Client was prepared");
            System.out.println("*********************************************");
        }
        
        if(event instanceof PreDestroyApplicationEvent){

            /* ElasticSearch node is closing */
            System.out.println("*********************************************");
            System.out.println("ElasticSearch Node is closing");
            ClientProvider.instance().closeNode();
            System.out.println("ElasticSearch Node was closed");
            System.out.println("*********************************************");
            
        }
        
    }

    @Override
    public boolean isListenerForSource(Object o) {
         return (o instanceof Application);
    }
    
}
