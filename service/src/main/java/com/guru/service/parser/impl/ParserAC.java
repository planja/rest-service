package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.QueryRepository;
import com.guru.parser.impl.acparser.ACParser;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.temp.Account;
import com.guru.vo.transfer.RequestData;
import com.guru.vo.transfer.Status;
import com.guru.vo.transfer.StatusCount;
import com.guru.vo.utils.ProcessRequestHelperService;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.inject.Inject;
import java.util.*;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserAC extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private ACParser acParser;

    @Inject
    private QueryRepository queryRepository;


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {

            RequestData requestData = (RequestData) message;
            RequestData newRequestData = new RequestData(requestData);
            ActorRef processingResultOfParserActor = getContext().actorOf(SpringExtProvider.get(getContext().system())
                    .props("processingResultOfParserActor"));
            log.info("got it AC");
            Account account = com.guru.vo.temp.AccountUtils.getAccount("AC");
            DefaultHttpClient httpclient = ACParser.login("947", "826", "111", "test1985", account);
            if (httpclient == null) {
                return;
            } else {
                newRequestData.setAccount(account);
                newRequestData.setDefaultHttpClient(httpclient);
                if (Objects.equals(newRequestData.getType(), "ow")) {
                    List<Date> owDates = ProcessRequestHelperService
                            .getDaysBetweenDates(newRequestData.getOw_start_date(), newRequestData.getOw_end_date());
                    for (int i = 0; i < owDates.size(); i++) {
                        newRequestData.setOw_start_date(owDates.get(i));
                        newRequestData.setRt_start_date(owDates.get(i));
                        Collection<Trip> flights;
                        try {
                            flights = acParser.parse(newRequestData);
                        } catch (Exception e) {
                            System.out.println("EXEPTION");
                            flights = new ArrayList<>();
                        }                        if (flights.size() != 0) {
                            processingResultOfParserActor.tell(flights, self());
                        } else {
                            Long queryId = (long) newRequestData.getRequest_id();
                            StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
                            Status.updateStatus(queryId);
                            float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
                            queryRepository.updateStatus(queryId, (int) status);
                            System.out.println(status + "%");
                            System.out.println("Count -" + Status.count++);
                        }
                    }
                } else {
                    List<Date> owDates = ProcessRequestHelperService
                            .getDaysBetweenDates(newRequestData.getOw_start_date(), newRequestData.getOw_end_date());
                    List<Date> rtDates = ProcessRequestHelperService
                            .getDaysBetweenDates(newRequestData.getRt_start_date(), newRequestData.getRt_end_date());
                    for (int i = 0; i < owDates.size(); i++) {
                        newRequestData.setOw_start_date(owDates.get(i));
                        newRequestData.setRt_start_date(rtDates.get(i));
                        Collection<Trip> flights;
                        try {
                            flights = acParser.parse(newRequestData);
                        } catch (Exception e) {
                            flights = new ArrayList<>();
                        }                        if (flights.size() != 0) {
                            processingResultOfParserActor.tell(flights, self());
                        } else {
                            Long queryId = (long) newRequestData.getRequest_id();
                            StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
                            Status.updateStatus(queryId);
                            float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
                            queryRepository.updateStatus(queryId, (int) status);
                            System.out.println(status + "%");
                            System.out.println("Count -" + Status.count++);
                        }
                    }
                }
            }


        }
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}