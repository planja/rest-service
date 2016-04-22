package com.guru.service.parser.impl;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.parser.interf.ParserActor;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserLH extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("got it LH");
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
