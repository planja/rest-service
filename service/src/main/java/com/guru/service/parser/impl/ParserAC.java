package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.parser.impl.acparser.ACParser;
import com.guru.service.actor.processingresult.ProcessingResultOfParserActor;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.transfer.RequestData;
import com.guru.vo.utils.ProcessRequestHelperService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserAC extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private ACParser acParser;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {

            RequestData requestData = (RequestData) message;
            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            log.info("got it AC");


            if (Objects.equals(requestData.getType(), "ow")) {
                List<Date> owDates = requestData.getOwDates();
                for (Date date : owDates) {
                    requestData.setOw_end_date(date);
                    requestData.setOw_start_date(date);
                    Collection<Trip> flights = acParser.parse(requestData);
                    if (flights.size() != 0)
                        processingResultOfParserActor.tell(flights, self());
                    else
                        processingResultOfParserActor.tell((long) requestData.getRequest_id(), self());

                }
            } else {
                List<Date> owDates = ProcessRequestHelperService
                        .getDaysBetweenDates(requestData.getOw_start_date(), requestData.getOw_end_date());
                List<Date> rtDates = ProcessRequestHelperService
                        .getDaysBetweenDates(requestData.getRt_start_date(), requestData.getRt_end_date());
                for (Date date : owDates) {
                    requestData.setOw_end_date(date);
                    requestData.setOw_start_date(date);
                    Collection<Trip> flights = acParser.parse(requestData);
                    if (flights.size() != 0)
                        processingResultOfParserActor.tell(flights, self());
                    else
                        processingResultOfParserActor.tell((long) requestData.getRequest_id(), self());

                }
                String destination = requestData.getDestination();
                String origin = requestData.getOrigin();
                requestData.setOrigin(destination);
                requestData.setDestination(origin);
                for (Date date : rtDates) {
                    requestData.setOw_start_date(date);
                    requestData.setOw_end_date(date);
                    Collection<Trip> flights = acParser.parse(requestData);
                    if (flights.size() != 0)
                        processingResultOfParserActor.tell(flights, self());
                    else
                        processingResultOfParserActor.tell((long) requestData.getRequest_id(), self());


                }
            }


        }
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
