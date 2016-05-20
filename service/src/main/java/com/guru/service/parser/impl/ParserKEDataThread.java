package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.QueryRepository;
import com.guru.parser.ke.KEParser;
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
public class ParserKEDataThread implements Callable {
    ActorRef processingResultOfParserActor;
    private KEParser keParser;
    private RequestData requestData;
    private ParserKE parserKE;


    @Inject
    private QueryRepository queryRepository;

    public ParserKEDataThread(KEParser keParser, RequestData requestData, ActorRef processingResultOfParserActor, ParserKE parserKE) {

        this.keParser = keParser;
        this.requestData = requestData;
        this.processingResultOfParserActor = processingResultOfParserActor;
        this.parserKE = parserKE;
    }

    public KEParser getKeParser() {
        return keParser;
    }

    public void setKeParser(KEParser keParser) {
        this.keParser = keParser;
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

    public ParserKE getParserKE() {
        return parserKE;
    }

    public void setParserKE(ParserKE parserKE) {
        this.parserKE = parserKE;
    }

    @Override
    public Object call() {
        Collection<Trip> flights;
        try {
            flights = keParser.parse(requestData);
        } catch (Exception e) {
            flights = new ArrayList<>();
        }

        if (flights.size() != 0)
            processingResultOfParserActor.tell(flights, parserKE.self());
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

