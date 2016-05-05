package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.parser.impl.qfparser.QFParser;
import com.guru.parser.interf.Parser;
import com.guru.parser.ke.KEParser;
import com.guru.service.actor.processingresult.ProcessingResultOfParserActor;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.transfer.RequestData;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Iterator;

public class ParserKE extends UntypedActor implements ParserActor {

    @Inject
    private KEParser keParser;

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            log.info("got it KE");
           Collection<Trip> trips = keParser.parse((RequestData) message);
            log.info("find " + trips.size() + " trips");
            Iterator it = trips.iterator();
            while(it.hasNext()){
                Trip trip =(Trip) it.next();
                System.out.println(trip);
                System.out.println(trip.getFlights());
            }
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
