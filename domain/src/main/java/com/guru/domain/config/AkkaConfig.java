package com.guru.domain.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;

public class AkkaConfig {

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("ApplicationSystem", akkaConfiguration());
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load("akka");
    }
}
