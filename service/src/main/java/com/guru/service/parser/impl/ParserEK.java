package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.RequestData;
import com.guru.service.actor.processingresult.ProcessingResultOfParserActor;
import com.guru.service.parser.interf.ParserActor;
import parser.ek.EKParser;

import java.util.List;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserEK extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof RequestData) {
            RequestData requestData = (RequestData) message;
            log.info("got it EK");
            EKParser emParser = new EKParser();
            String username = "ruslan.nurtdinov@gmail.com";
            String password = "test1985";
            String from = "SYD";
            String to = "LHR";
            String date = "03/14/2016";
            EKParser.Client client = emParser.login("ruslan.nurtdinov@gmail.com", "test1985");
            emParser.getEmirates(client, from, to, date, 1, "E");
            /*List flights = emParser.getEmirates(client, requestData.getOrigin(),
                    requestData.getDestination(), date, 1, "E");*/
            List flights = emParser.getEmirates(client, from,
                    to, date, 1, "E");

            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            processingResultOfParserActor.tell(flights, self());
        } else unhandled(message);


    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}

