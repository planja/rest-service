package com.guru.service.parser.impl;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.parser.interf.ParserActor;

public class ParserAA extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
    /*    if (message instanceof RequestData) {
            RequestData requestData = (RequestData) message;
            log.info("got it AA");
            AAParser aaParser = new AAParser();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Account account = AccountUtils.getAccount("AA");
            DefaultHttpClient loggedInClient = aaParser.logIn(account.getLogin(), account.getPin(), account.getPassword(), account);
           List flights = aaParser.getAmericanAirlines(loggedInClient, account, requestData.getOrigin(), requestData.getDestination(),
                    sdf.parse("04/25/2016"), 1);
            List flights = aaParser.getAmericanAirlines(loggedInClient, account, "SDQ", "NYC", sdf.parse("04/26/2016"), 1);
            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            processingResultOfParserActor.tell(flights, self());
        } else unhandled(message); */
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
