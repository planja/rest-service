package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import com.guru.domain.model.Trip;
import com.guru.parser.impl.acparser.ACParser;
import com.guru.vo.transfer.RequestData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by Никита on 13.05.2016.
 */
public class ParserACDataThread implements Callable {

    ActorRef processingResultOfParserActor;
    private ACParser acParser;
    private RequestData requestData;
    private ParserAC parserAC;

    public ParserACDataThread(ACParser acParser, RequestData requestData, ActorRef processingResultOfParserActor, ParserAC parserAC) {

        this.acParser = acParser;
        this.requestData = requestData;
        this.processingResultOfParserActor = processingResultOfParserActor;
        this.parserAC = parserAC;
    }

    public ACParser getAcParser() {
        return acParser;
    }

    public void setAcParser(ACParser acParser) {
        this.acParser = acParser;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public ActorRef getProcessingResultOfParserActor() {
        return processingResultOfParserActor;
    }

    public void setProcessingResultOfParserActor(ActorRef processingResultOfParserActor) {
        this.processingResultOfParserActor = processingResultOfParserActor;
    }

    public ParserAC getParserAC() {
        return parserAC;
    }

    public void setParserAC(ParserAC parserAC) {
        this.parserAC = parserAC;
    }

    @Override
    public Object call() {
        Collection<Trip> flights;
        try {
            flights = acParser.parse(requestData);

        } catch (Exception e) {
            flights = new ArrayList<>();
        }

        if (flights.size() != 0)
            processingResultOfParserActor.tell(flights, parserAC.self());
        else
            processingResultOfParserActor.tell((long) requestData.getRequest_id(), parserAC.self());
        return null;
    }
}
