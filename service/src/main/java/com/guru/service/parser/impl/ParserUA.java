package com.guru.service.parser.impl;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.parser.interf.ParserActor;

public class ParserUA extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
     /*   if (message instanceof RequestData) {
            RequestData requestData = (RequestData) message;
            log.info("got it UA");
            String date = "05/26/2016";
            String origin = "SYD";
            String destination = "FRA";
            UANParser uaParser = new UANParser();
            List<Award> flights = uaParser.getUnited(date, origin, destination, 1, "E");
            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            processingResultOfParserActor.tell(flights, self());
        } else unhandled(message);*/
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
