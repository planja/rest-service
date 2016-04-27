package com.guru.domain.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem applicationSystem() {
        return ActorSystem.create("ApplicationSystem", ConfigFactory.load().getConfig("applicationConfig"));
    }

    @Bean
    public ActorSystem domainSystem() {
        return ActorSystem.create("DomainSystem", ConfigFactory.load().getConfig("repositoryConfig"));
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load("akka");
    }
}
