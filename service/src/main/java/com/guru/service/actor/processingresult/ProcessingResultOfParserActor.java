package com.guru.service.actor.processingresult;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.domain.model.TripCost;
import com.guru.domain.repository.QueryRepository;
import com.guru.domain.repository.TripCostRepository;
import com.guru.domain.repository.TripRepository;
import com.guru.domain.service.cost.CalculateCost;
import com.guru.vo.transfer.Status;
import com.guru.vo.transfer.StatusCount;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProcessingResultOfParserActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private TripRepository tripRepository;

    @Inject
    private TripCostRepository tripCostRepository;

    @Inject
    private QueryRepository queryRepository;


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof List<?>) {
            List<Trip> trips = (List<Trip>) message;
            System.out.println("Kol-vo poletov -  " + trips.size());
            System.out.println(trips.get(0).getFlights().get(0).getParser() + " " + trips.size());


            for (Trip trip : trips) {
                log.info(trip.getFlightNumbers() + " flights in this trip: " + trip.getFlights().get(0));
            }
            List<Trip> save = StreamSupport.stream(Spliterators.spliteratorUnknownSize(tripRepository.save(trips).iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));

            Long queryId = trips.get(0).getQueryId();
            StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
            Status.updateStatus(queryId);

            float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;

            queryRepository.updateStatus(queryId, (int) status);


            System.out.println(status + " %");
            System.out.println(status + "%");
            System.out.println("Count -" + Status.count++);

            if (statusCount.getCurrentStatus() == statusCount.getMaxStatus())
                Status.deleteFromStatusList(queryId);

            List<TripCost> tripCosts = CalculateCost.calc(save);
            tripCostRepository.save(tripCosts);


        } else {
            unhandled(message);
        }
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }

   /* private ActorSelection repositoryActor;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Long){
            repositoryActor.tell(message, self());
        }
        if (message instanceof List<?>) {
            repositoryActor.tell(message, self());
        } else
            unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
        repositoryActor = context().system().actorSelection("akka.tcp://DomainSystem@127.0.0.1:1719/user/repositoryConfig");
    }*/

}
