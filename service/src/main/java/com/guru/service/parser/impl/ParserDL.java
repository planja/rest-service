package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.parser.dl.DLParser;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.transfer.RequestData;
import com.guru.vo.utils.ProcessRequestHelperService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserDL extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    @Inject
    private DLParser dlParser;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            log.info("got it DL");

            ActorRef processingResultOfParserActor = getContext().actorOf(SpringExtProvider.get(getContext().system())
                    .props("processingResultOfParserActor"));
            ExecutorService executor = Executors.newCachedThreadPool();
            RequestData requestData = (RequestData) message;
            Set<Callable<Object>> callables = new HashSet<Callable<Object>>();
            if (Objects.equals(requestData.getType(), "ow")) {
                List<Date> owDates = requestData.getOwDates();
                for (Date date : owDates) {
                    RequestData reqData = new RequestData(requestData);
                    reqData.setOw_end_date(date);
                    reqData.setOw_start_date(date);
                    System.out.println(date);
                    callables.add(new ParserDLDataThread(dlParser, reqData, processingResultOfParserActor, this));
                }
            } else {
                List<Date> owDates = ProcessRequestHelperService
                        .getDaysBetweenDates(requestData.getOw_start_date(), requestData.getOw_end_date());
                List<Date> rtDates = ProcessRequestHelperService
                        .getDaysBetweenDates(requestData.getRt_start_date(), requestData.getRt_end_date());
                for (Date date : owDates) {
                    RequestData reqData = new RequestData(requestData);
                    reqData.setOw_end_date(date);
                    reqData.setOw_start_date(date);
                    System.out.println(date);
                    callables.add(new ParserDLDataThread(dlParser, reqData, processingResultOfParserActor, this));

                }
                String destination = requestData.getDestination();
                String origin = requestData.getOrigin();
                requestData.setOrigin(destination);
                requestData.setDestination(origin);
                for (Date date : rtDates) {
                    RequestData reqData = new RequestData(requestData);
                    reqData.setOw_end_date(date);
                    reqData.setOw_start_date(date);
                    System.out.println(date);
                    callables.add(new ParserDLDataThread(dlParser, reqData, processingResultOfParserActor, this));
                }
            }
            List<Future<Object>> futureList = executor.invokeAll(callables);
            for (Future<Object> futureItem : futureList) {
                try {
                    futureItem.get();
                } catch (Exception ex) {
                }
            }
            executor.shutdown();
        }
    }


    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
