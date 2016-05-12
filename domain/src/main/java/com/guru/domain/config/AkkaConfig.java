package com.guru.domain.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.actor.RepositoryActor;
import com.guru.domain.actor.ServiceRepositoryActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

@Configuration
//@ComponentScan("com.guru.domain.actor")
public class AkkaConfig {

/*    @Autowired
    private ActorSystem domainSystem;*/


    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorRef repositoryActor() {
        return domainSystem().actorOf(Props.create(RepositoryActor.class), "repositoryConfig");
    }

    @Bean
    public ActorSystem domainSystem() {
        ActorSystem system = ActorSystem.create("DomainSystem", ConfigFactory.load().getConfig("repositoryConfig"));
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public ActorSystem applicationSystem() {
        ActorSystem system = ActorSystem.create("ApplicationSystem", ConfigFactory.load().getConfig("applicationConfig"));
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load("akka");
    }

    @Bean(name = "serviceRepositoryActor")
    @Scope("prototype")
    public ServiceRepositoryActor serviceRepositoryActor() {
        return new ServiceRepositoryActor();
    }
}
