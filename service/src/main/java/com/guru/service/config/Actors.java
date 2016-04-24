package com.guru.service.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.service.actor.RequestActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Actors {

    @Autowired
    private ActorSystem applicationSystem;

    @Bean
    public ActorRef requestActor() {
        return applicationSystem.actorOf(Props.create(RequestActor.class), "applicationConfig");
    }
}
