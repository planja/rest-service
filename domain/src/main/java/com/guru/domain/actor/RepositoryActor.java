package com.guru.domain.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.config.DataConfig;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.TripRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("i'm in");
        if (message instanceof HelloMessage) {
            HelloMessage recMsg = (HelloMessage) message;
            System.out.println("Received Message: " + recMsg.getText());
            System.out.println("***** Hello World! ******");
        }
        if (message instanceof String) {
            log.info("get String");
        }
        if (message instanceof List<?>) {
            List<Trip> trips = (List<Trip>) message;




            log.info("create");
            sender().tell("ok", self());
        }
        unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
