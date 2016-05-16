package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import com.guru.domain.model.Trip;
import com.guru.parser.impl.qfparser.QFParser;
import com.guru.vo.transfer.RequestData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by Никита on 13.05.2016.
 */
public class ParserQFDataThread implements Callable {

    ActorRef processingResultOfParserActor;
    private QFParser qfParser;
    private RequestData requestData;
    private ParserQF parserQF;

    public ParserQFDataThread(QFParser qfParser, RequestData requestData, ActorRef processingResultOfParserActor, ParserQF parserQF) {

        this.qfParser = qfParser;
        this.requestData = requestData;
        this.processingResultOfParserActor = processingResultOfParserActor;
        this.parserQF = parserQF;
    }

    public QFParser getQfParser() {
        return qfParser;
    }

    public void setQfParser(QFParser qfParser) {
        this.qfParser = qfParser;
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

    public ParserQF getParserQF() {
        return parserQF;
    }

    public void setParserQF(ParserQF parserQF) {
        this.parserQF = parserQF;
    }

    @Override
    public Object call() {
        Collection<Trip> flights;
        try {
            flights = qfParser.parse(requestData);

        } catch (Exception e) {
            flights = new ArrayList<>();
        }

        if (flights.size() != 0)
            processingResultOfParserActor.tell(flights, parserQF.self());
        else
            processingResultOfParserActor.tell((long) requestData.getRequest_id(), parserQF.self());
        return null;
    }

}
