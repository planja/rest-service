package com.guru.domain.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.actor.RepositoryActor;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

@Configuration
@ComponentScan("com.guru.domain.actor")
public class Actors {

    @Autowired
    private ActorSystem domainSystem;


    @Bean
    public ActorRef repositoryActor() {
        return domainSystem.actorOf(Props.create(RepositoryActor.class), "repositoryConfig");
    }

/*    @Bean(name = "serviceActor")
    public ActorRef serviceActor() {
        ActorRef actorRef = domainSystem.actorOf(SpringExtProvider.get(domainSystem).props("ServiceActor"), "RepositoryService");
        return actorRef;
    }*/
}
