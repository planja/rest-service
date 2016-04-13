package parser.impl;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import parser.interf.ParserActor;

public class ParserUA extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("got it UA");
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
