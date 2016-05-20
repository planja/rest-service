package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.QueryRepository;
import com.guru.parser.dl.DLParser;
import com.guru.vo.transfer.RequestData;
import com.guru.vo.transfer.Status;
import com.guru.vo.transfer.StatusCount;

import javax.inject.Inject;
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

    @Inject
    private QueryRepository queryRepository;

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
        else {
            System.out.println("noFlights");
            Long queryId = (long) requestData.getRequest_id();
            System.out.println(queryId);
            StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
            Status.updateStatus(queryId);
            float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
            System.out.println(status);
            queryRepository.updateStatus(queryId, (int) status);
            System.out.println(status + "%");
            System.out.println(Status.count++);
            if (statusCount.getCurrentStatus() == statusCount.getMaxStatus())
                Status.deleteFromStatusList(queryId);
        }
        return null;
    }
}

