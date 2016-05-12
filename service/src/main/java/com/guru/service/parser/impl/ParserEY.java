package com.guru.service.parser.impl;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.parser.interf.ParserActor;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserEY extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
/*
        if (message instanceof RequestData) {
            log.info("got it EY");
            RequestData requestData = (RequestData) message;
            EYParser eyParser = new EYParser();
            List flights = eyParser.getEtihad("04/25/2016", "LAX", "AUH", 1, "E");
           /* List flights = eyParser.getEtihad("04/25/2016", requestData.getOrigin(),
                    requestData.getDestination(), 1, "E");
            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            processingResultOfParserActor.tell(flights, self());
        } else unhandled(message);
*/
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
