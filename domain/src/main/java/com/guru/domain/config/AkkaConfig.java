package com.guru.domain.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

@Configuration
@ComponentScan("com.guru.domain.actor")
public class AkkaConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem domainSystem() {
        ActorSystem system = ActorSystem.create("DomainSystem", ConfigFactory.load().getConfig("repositoryConfig"));
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public ActorSystem applicationSystem() {
        return ActorSystem.create("ApplicationSystem", ConfigFactory.load().getConfig("applicationConfig"));
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load("akka");
    }
}
