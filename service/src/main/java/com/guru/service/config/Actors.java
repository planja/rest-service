package com.guru.service.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.guru.service.actor.RequestActor;
import com.guru.service.parser.impl.ParserKE;
import com.guru.service.parser.impl.ParserQF;
import com.guru.service.parser.interf.ParserActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Actors {

    @Autowired
    private ActorSystem applicationSystem;

    @Bean
    public ActorRef requestActor() {
        return applicationSystem.actorOf(Props.create(RequestActor.class), "applicationConfig");
    }

    @Bean(name = "parserQF")
    @Scope("prototype")
    public ParserQF parserQF() {
        return new ParserQF();
    }
}
