package com.guru.service.actor.processingresult;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import com.guru.vo.transfer.ParserResult;

import java.util.List;

public class ProcessingResultOfParserActor extends UntypedActor {

    private ActorSelection repositoryActor;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof List<?>) {
            List list = (List) message;
            repositoryActor.tell(list, self());
        } else
            unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
        repositoryActor = context().system().actorSelection("akka.tcp://DomainSystem@127.0.0.1:1719/user/repositoryConfig");
    }

}
