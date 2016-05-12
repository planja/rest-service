package com.guru.service.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.service.actor.RequestActor;
import com.guru.service.parser.impl.ParserAC;
import com.guru.service.parser.impl.ParserDL;
import com.guru.service.parser.impl.ParserKE;
import com.guru.service.parser.impl.ParserQF;
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

    @Bean(name = "parserKE")
    @Scope("prototype")
    public ParserKE parserKE() {
        return new ParserKE();
    }

    @Bean(name = "parserDL")
    @Scope("prototype")
    public ParserDL parserDL() {
        return new ParserDL();
    }

    @Bean(name = "parserAC")
    @Scope("prototype")
    public ParserAC parserAC() {
        return new ParserAC();
    }

}
