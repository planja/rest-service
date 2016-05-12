package com.guru.domain.actor;


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
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ServiceRepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private TripRepository tripRepository;

    @Inject
    private TripCostRepository tripCostRepository;

    @Inject
    private QueryRepository queryRepository;


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Long) {
            Long queryId = (Long) message;
            StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
            Status.updateStatus(queryId);
            float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;
            queryRepository.updateStatus(queryId, (int)status);
            System.out.println(queryRepository.findOne(queryId).getStatus());
            if (statusCount.getCurrentStatus() == statusCount.getMaxStatus())
                Status.deleteFromStatusList(queryId);
        }
       else if (message instanceof List<?>) {
            List<Trip> trips = (List<Trip>) message;
            for (Trip trip : trips) {
                log.info(trip.getFlightNumbers() + " flights in this trip: " + trip.getFlights().get(0));
            }
            List<Trip> save = StreamSupport.stream(Spliterators.spliteratorUnknownSize(tripRepository.save(trips).iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));

            Long queryId = trips.get(0).getQueryId();
            StatusCount statusCount = Status.getStatusCountByQueryId(queryId);
            Status.updateStatus(queryId);

            float status = (float) statusCount.getCurrentStatus() / statusCount.getMaxStatus() * 100;

            queryRepository.updateStatus(queryId, (int)status);

            System.out.println(queryRepository.findOne(queryId).getStatus());

            if (statusCount.getCurrentStatus() == statusCount.getMaxStatus())
                Status.deleteFromStatusList(queryId);

            List<TripCost> tripCosts = CalculateCost.calc(save);
            tripCostRepository.save(tripCosts);


        } else {
            unhandled(message);
        }
    }

    @Override
    public void preStart() {
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
