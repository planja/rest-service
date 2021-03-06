package com.guru.domain.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
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
