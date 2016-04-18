package com.guru.service.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.config.DataConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.guru.service.actor.RequestActor;

import javax.inject.Inject;

@Configuration
@ComponentScan( {"com.guru.service"} )
@Import( {DataConfig.class} )
public class ServiceConfig {

    @Inject
    private ActorSystem actorSystem;

    @Bean
    public ActorRef requestActor() {
        return actorSystem.actorOf(Props.create(RequestActor.class));
    }
}
