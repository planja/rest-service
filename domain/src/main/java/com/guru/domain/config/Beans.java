package com.guru.domain.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.actor.RepositoryActor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class Beans {

    @Inject
    private ActorSystem actorSystem;

    @Bean
    public ActorRef repositoryActor() {
        return actorSystem.actorOf(Props.create(RepositoryActor.class));
    }
}
