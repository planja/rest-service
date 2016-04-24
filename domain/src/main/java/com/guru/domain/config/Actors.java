package com.guru.domain.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.actor.RepositoryActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class Actors {

    @Autowired
    private ActorSystem domainSystem;

    @Bean
    public ActorRef repositoryActor() {
        return domainSystem.actorOf(Props.create(RepositoryActor.class), "repositoryConfig");
    }

    @Bean
    public ActorRef repositoryActor2() {
        final String path = "akka.tcp://DomainSystem@127.0.0.1:1719/user/repositoryConfig";
        return domainSystem.actorFor(path);
    }
}
