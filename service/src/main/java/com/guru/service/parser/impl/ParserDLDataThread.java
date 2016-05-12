package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import com.guru.domain.model.Trip;
import com.guru.parser.dl.DLParser;
import com.guru.vo.transfer.RequestData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by Anton on 12.05.2016.
 */
public class ParserDLDataThread implements Callable {
    ActorRef processingResultOfParserActor;
    private DLParser dlParser;
    private RequestData requestData;
    private ParserDL parserDL;

    public ParserDLDataThread(DLParser dlParser, RequestData requestData, ActorRef processingResultOfParserActor, ParserDL parserDL) {

        this.dlParser = dlParser;
        this.requestData = requestData;
        this.processingResultOfParserActor = processingResultOfParserActor;
        this.parserDL = parserDL;
    }

    public DLParser getDlParser() {
        return dlParser;
    }

    public void setDlParser(DLParser dlParser) {
        this.dlParser = dlParser;
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

    public ParserDL getParserDL() {
        return parserDL;
    }

    public void setParserDL(ParserDL parserDL) {
        this.parserDL = parserDL;
    }

    @Override
    public Object call() {
        Collection<Trip> flights;
        try {
            flights = dlParser.parse(requestData);

        } catch (Exception e) {
            flights = new ArrayList<>();
        }

        if (flights.size() != 0)
            processingResultOfParserActor.tell(flights, parserDL.self());
        else
            processingResultOfParserActor.tell((long) requestData.getRequest_id(), parserDL.self());
        return null;
    }
}

