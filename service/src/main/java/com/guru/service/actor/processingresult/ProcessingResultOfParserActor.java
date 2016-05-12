package com.guru.service.actor.processingresult;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import java.util.List;

public class ProcessingResultOfParserActor extends UntypedActor {

    private ActorSelection repositoryActor;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Long) {
            repositoryActor.tell(message, self());
        }
        if (message instanceof List<?>) {
            repositoryActor.tell(message, self());
        } else
            unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
        repositoryActor = context().system().actorSelection("akka.tcp://DomainSystem@127.0.0.1:1719/user/repositoryConfig");
    }

}
