package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.QueryRepository;
import com.guru.parser.impl.qfparser.QFParser;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.transfer.RequestData;
import com.guru.vo.transfer.Status;
import com.guru.vo.transfer.StatusCount;
import com.guru.vo.utils.ProcessRequestHelperService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

/**
 * Created by Никита on 18.04.2016.
 */

public class ParserQF extends UntypedActor implements ParserActor {

    @Inject
    private QFParser qfParser;

    @Inject
    private QueryRepository queryRepository;

    //для всех

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            log.info("got it QF");

            ActorRef processingResultOfParserActor = getContext().actorOf(SpringExtProvider.get(getContext().system())
                    .props("processingResultOfParserActor"));

            RequestData requestData = (RequestData) message;
            RequestData newRequestData = new RequestData(requestData);

            if (Objects.equals(newRequestData.getType(), "ow")) {
                List<Date> owDates = newRequestData.getOwDates();
                for (Date date : owDates) {
                    newRequestData.setOw_end_date(date);
                    newRequestData.setOw_start_date(date);
                    Collection<Trip> flights = qfParser.parse(newRequestData);
                    if (flights.size() != 0)
                        processingResultOfParserActor.tell(flights, self());
                    else {
                        Long queryId = (long) newRequestData.getRequest_id();
                        StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
                        Status.updateStatus(queryId);
                        float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
                        queryRepository.updateStatus(queryId, (int) status);
                        System.out.println(status + "%");
                        System.out.println(Status.count++);
                        //System.out.println("Count - " + count++);
                    }
                    ;

                }
            } else {
                List<Date> owDates = ProcessRequestHelperService
                        .getDaysBetweenDates(newRequestData.getOw_start_date(), newRequestData.getOw_end_date());
                List<Date> rtDates = ProcessRequestHelperService
                        .getDaysBetweenDates(newRequestData.getRt_start_date(), newRequestData.getRt_end_date());
                for (Date date : owDates) {
                    newRequestData.setOw_end_date(date);
                    newRequestData.setOw_start_date(date);
                    Collection<Trip> flights = qfParser.parse(newRequestData);
                    if (flights.size() != 0) {
                        processingResultOfParserActor.tell(flights, self());
                       // System.out.println(Status.count++);
                    }
                    else {
                        Long queryId = (long) newRequestData.getRequest_id();
                        StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
                        Status.updateStatus(queryId);
                        float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
                        queryRepository.updateStatus(queryId, (int) status);
                        System.out.println(status + "%");
                        System.out.println(Status.count++);
                        //System.out.println("Count - " + count++);
                    }

                }
                String destination = newRequestData.getDestination();
                String origin = newRequestData.getOrigin();
                newRequestData.setOrigin(destination);
                newRequestData.setDestination(origin);
                for (Date date : rtDates) {
                    newRequestData.setOw_start_date(date);
                    newRequestData.setOw_end_date(date);
                    Collection<Trip> flights = qfParser.parse(newRequestData);
                    if (flights.size() != 0) {
                        processingResultOfParserActor.tell(flights, self());

                    }
                    else {
                        Long queryId = (long) newRequestData.getRequest_id();
                        StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
                        Status.updateStatus(queryId);
                        float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
                        queryRepository.updateStatus(queryId, (int) status);
                        System.out.println(status + "%");
                        System.out.println("Count -"+Status.count++);
                        //System.out.println("Count - " + count++);
                    }


                }
            }
        } else unhandled(message);
    }


    @Override
    public void postStop() {
        context().system().shutdown();
    }
}