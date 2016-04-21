package com.guru.service.parser.impl;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.parser.interf.ParserActor;

public class ParserAA extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("got it AA");
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}