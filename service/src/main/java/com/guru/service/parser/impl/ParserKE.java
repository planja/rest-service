package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.parser.interf.Parser;
import com.guru.parser.ke.KEParser;
import com.guru.service.actor.processingresult.ProcessingResultOfParserActor;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.transfer.RequestData;

import java.util.Collection;

public class ParserKE extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            log.info("got it KE");
            Parser parser = new KEParser();
            Collection<Trip> trips = parser.parse((RequestData) message);
            log.info("find " + trips.size() + " trips");

            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            processingResultOfParserActor.tell(trips, self());
        }
    }

    @Override
    public void preStart() {
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
