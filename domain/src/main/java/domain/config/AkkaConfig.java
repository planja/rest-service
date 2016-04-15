package domain.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;

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
